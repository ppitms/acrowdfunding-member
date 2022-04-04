package com.zhijia.crowd.service.impl;

import com.zhijia.crowd.entity.po.MemberConfirmInfoPO;
import com.zhijia.crowd.entity.po.MemberLaunchInfoPO;
import com.zhijia.crowd.entity.po.ProjectPO;
import com.zhijia.crowd.entity.po.ReturnPO;
import com.zhijia.crowd.entity.vo.*;
import com.zhijia.crowd.mapper.*;
import com.zhijia.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhijia
 * @create 2022-03-30 21:39
 */
@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectPOMapper projectPOMapper;

    @Resource
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Resource
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Resource
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Resource
    private ReturnPOMapper returnPOMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(ProjectVO projectVO, Integer memberId) {
        //1 保存projectPO
        ProjectPO projectPO = new ProjectPO();
        BeanUtils.copyProperties(projectVO, projectPO);
        projectPO.setMemberid(memberId);
        //添加创建时间
        String createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        projectVO.setCreatedate(createDate);
        projectPO.setCreatedate(createDate);
        projectPO.setDeploydate(createDate);
        projectPO.setStatus(0);
        //有选择保存获取自增主键
        projectPOMapper.insertSelective(projectPO);
        Integer projectId = projectPO.getId();
        System.out.println(projectId);
        //2 保存项目分类的关联关系信息
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList, projectId);

        //3 保存项目标签的关联关系信息
        System.out.println(projectVO);
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList, projectId);

        //4 保存项目详情图片信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(detailPicturePathList, projectId);

        //5 保存项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        //6 保存项目回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        List<ReturnPO> returnPOList = new ArrayList<>();
        for (ReturnVO returnVO : returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO, returnPO);
            returnPOList.add(returnPO);
        }

        returnPOMapper.insertReturnPOBatch(returnPOList, projectId);

        //7 保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
        System.out.println(projectVO);
    }

    @Override
    public List<PortalTypeVO> getPortalProjectVO() {
        return projectPOMapper.selectPortalTypeOVList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }
        String deployDate = detailProjectVO.getDeployDate();
        System.out.println(deployDate);
        System.out.println("ddddddddddddddddddd");
//        deployDate="2022-01-01";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDate startDate = LocalDate.parse(deployDate,fmt);
        LocalDate now = LocalDate.now();
        long until = now.until(startDate, ChronoUnit.DAYS);
        Integer day = (int) (detailProjectVO.getDay() - until);
        detailProjectVO.setLastDay(day);
        return detailProjectVO;
    }
}
