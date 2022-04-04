package com.zhijia.crowd.handler;

import com.zhijia.crowd.api.MySQLRemoteService;
import com.zhijia.crowd.api.RedisRemoteService;
import com.zhijia.crowd.config.ShortMessageProperties;
import com.zhijia.crowd.constart.CrowdConstant;
import com.zhijia.crowd.entity.po.MemberPO;
import com.zhijia.crowd.entity.vo.MemberLoginVO;
import com.zhijia.crowd.entity.vo.MemberVO;
import com.zhijia.crowd.util.CrowdUtil;
import com.zhijia.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author zhijia
 * @create 2022-03-28 21:28
 */
@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties messageProperties;

    @Resource
    private RedisRemoteService redisRemoteService;
    @Resource
    private MySQLRemoteService mysqlRemoteService;

    //点击退出登录
    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:http://localhost/";
    }

    //点击登录
    @RequestMapping("/auth/member/do/login")
    public String doLogin(@RequestParam("loginacct")String loginacct,
                          @RequestParam("userpswd")String userpswd, ModelMap modelMap, HttpSession session){
        //查询是否有此账号
        ResultEntity<MemberPO> resultEntity = mysqlRemoteService.getMemberPOByloginAcctRemote(loginacct);
        if(ResultEntity.FAILED.equals(resultEntity)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,resultEntity.getMessage());
            return "member-login";
        }
        MemberPO memberPO = resultEntity.getData();
        if(memberPO==null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FATLED);
            return "member-login";
        }
        //比较密码
        boolean matchesResult = new BCryptPasswordEncoder().matches(userpswd, memberPO.getUserpswd());
        if(!matchesResult){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_FATLED);
            return "member-login";
        }
        //成功则存入session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);
        return "redirect:http://localhost/auth/member/to/center/page";
    }

    //点击注册
    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {

        //从redis中读取对应手机的value的
        String key = CrowdConstant.REDIS_CODE_PREFIX + memberVO.getPhoneNum();
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKey(key);
        //查询失败
        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }
        String redisCode = resultEntity.getData();
        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        //查询
        String fromCode = memberVO.getCode();
        //比对一致，
        if (!Objects.equals(redisCode, fromCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }
        //删除redis保存的信息
        redisRemoteService.removeRedisKeyRemote(key);
        //加密密码、
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(memberVO.getUserpswd());
        memberVO.setUserpswd(encode);

        MemberPO memberPO = new MemberPO();
        //复制属性
        BeanUtils.copyProperties(memberVO, memberPO);
        //保存数据
        ResultEntity<String> saveMemberResultEntity = mysqlRemoteService.saveMember(memberPO);
        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveMemberResultEntity.getMessage());
            return "member-reg";
        }
        return "redirect:http://localhost/auth/member/to/login/page";
    }


    //点击获取验证码
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {
        //发送验证码到手机
//        ResultEntity<String> stringResultEntity = CrowdUtil.sendShortMessage();
        ResultEntity<String> stringResultEntity = CrowdUtil.sendShortMessage(
                messageProperties.getHost(),
                messageProperties.getPath(),
                messageProperties.getMethod(), phoneNum,
                messageProperties.getAppCode(),
                messageProperties.getSkin());
        //判断结果
        if (stringResultEntity.getResult() == "SUCCESS") {
            //存入redis
            String code = stringResultEntity.getData();
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return stringResultEntity;
        }
    }
}
