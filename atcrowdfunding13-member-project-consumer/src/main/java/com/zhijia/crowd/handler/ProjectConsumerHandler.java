package com.zhijia.crowd.handler;

import com.zhijia.crowd.api.MySQLRemoteService;
import com.zhijia.crowd.config.OSSProperties;
import com.zhijia.crowd.constart.CrowdConstant;
import com.zhijia.crowd.entity.vo.*;
import com.zhijia.crowd.util.CrowdUtil;
import com.zhijia.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhijia
 * @create 2022-03-31 12:42
 */
@Controller
public class ProjectConsumerHandler {

    @Autowired
    private OSSProperties ossProperties;
    @Resource
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId")Integer projectId,ModelMap modelMap){
        ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemove(projectId);
        System.out.println(resultEntity.getResult());
        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            DetailProjectVO data = resultEntity.getData();
            modelMap.addAttribute("detailProjectVO",data);
            System.out.println("dffdfdf");
            System.out.println(data);
        }
        return "project-detail";
    }

    //最后一步提交保存
    @RequestMapping("/create/confirm")
    public String saveConfirm(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO,ModelMap modelMap) {
        ProjectVO projectVO = (ProjectVO)session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        if(projectVO==null){
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }
        //存入
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        //当前用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();
        //调用远程方法保存数据
        ResultEntity<String> saveResultEntity=mySQLRemoteService.saveProjectVORemote(projectVO,memberId);
        if(ResultEntity.FAILED.equals(saveResultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
            return "project-confirm";
        }
        //成功后将临时projectvo对象移除
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        return "redirect:http://localhost/project/create/success";
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO,HttpSession session) {
        try {
            ProjectVO projectVO = (ProjectVO)session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
            if(projectVO==null){
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            if(returnVOList==null||returnVOList.size()==0){
                returnVOList=new ArrayList<>();
                projectVO.setReturnVOList(returnVOList);
            }
            returnVOList.add(returnVO);
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(@RequestParam("returnPicture") MultipartFile returnPicture) throws IOException {

        ResultEntity<String> uploadReturnPicResultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret(),
                returnPicture.getInputStream(), ossProperties.getBucketName(),
                ossProperties.getBucketDomain(), returnPicture.getOriginalFilename());

        return uploadReturnPicResultEntity;
    }

    @RequestMapping("/create/project/information")//普通数据/头图/返回表单带的数据/详情图/将信息存入session域
    public String saveProjectBasicInfo(ProjectVO projectVO, MultipartFile headerPicture,ModelMap modelMap,
                                       List<MultipartFile> detailPictureList, HttpSession session) throws IOException {
        //头图上传
        boolean headerPictureIsEmpty = headerPicture.isEmpty();
        if(!headerPictureIsEmpty){
            ResultEntity<String> uploadHeaderPicResultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret(),
                    headerPicture.getInputStream(), ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(), headerPicture.getOriginalFilename());
            String result = uploadHeaderPicResultEntity.getResult();
            //判断是否上传成功
            if(ResultEntity.SUCCESS.equals(result)) {
                String headerPicturePath = uploadHeaderPicResultEntity.getData();
                projectVO.setHeaderPicturePath(headerPicturePath);
            }else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_ASILED);
                return "project-launch";
            }
        }else {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        //详情图
        ArrayList<String> detailPictureNameList = new ArrayList<>();
        if(detailPictureList.isEmpty()){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETATL_PIC_EMPTY);
            return "project-launch";
        }
        for (MultipartFile detailPicture : detailPictureList) {
            if(!detailPicture.isEmpty()){
                //不为空就上传
                ResultEntity<String> detailUploadResultEntity = CrowdUtil.uploadFileToOss(
                        ossProperties.getEndPoint(),
                        ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret(),
                        detailPicture.getInputStream(), ossProperties.getBucketName(),
                        ossProperties.getBucketDomain(), detailPicture.getOriginalFilename());
                String result = detailUploadResultEntity.getResult();
                if(ResultEntity.SUCCESS.equals(result)){
                    String headerPicturePath = detailUploadResultEntity.getData();
                    detailPictureNameList.add(headerPicturePath);
                }else {
                    //重试
                    CrowdUtil.uploadFileToOss(
                            ossProperties.getEndPoint(),
                            ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret(),
                            detailPicture.getInputStream(), ossProperties.getBucketName(),
                            ossProperties.getBucketDomain(), detailPicture.getOriginalFilename());
                }
            }else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETATL_PIC_EMPTY);
                return "project-launch";
            }
        }
        projectVO.setDetailPicturePathList(detailPictureNameList);
        //将对象存入session域
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
        return "redirect:http://localhost/project/return/info/page";
    }
}
