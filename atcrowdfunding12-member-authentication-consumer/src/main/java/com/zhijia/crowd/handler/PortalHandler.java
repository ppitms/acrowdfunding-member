package com.zhijia.crowd.handler;

import com.zhijia.crowd.api.MySQLRemoteService;
import com.zhijia.crowd.constart.CrowdConstant;
import com.zhijia.crowd.entity.vo.PortalTypeVO;
import com.zhijia.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author zhijia
 * @create 2022-03-28 12:58
 */
@Controller
public class PortalHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/")
    public String showPortalPage(ModelMap modelMap){
        //查询首页显示的数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();
        System.out.println(resultEntity);
        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            List<PortalTypeVO> list = resultEntity.getData();
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,list);
        }
        return "portal";
    }
}
