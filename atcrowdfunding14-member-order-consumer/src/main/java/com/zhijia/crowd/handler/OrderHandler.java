package com.zhijia.crowd.handler;

import com.zhijia.crowd.api.MySQLRemoteService;
import com.zhijia.crowd.constart.CrowdConstant;
import com.zhijia.crowd.entity.po.OrderProjectPO;
import com.zhijia.crowd.entity.vo.AddressVO;
import com.zhijia.crowd.entity.vo.MemberLoginVO;
import com.zhijia.crowd.entity.vo.OrderProjectVO;
import com.zhijia.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author zhijia
 * @create 2022-04-02 18:55
 */
@Controller
public class OrderHandler {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO,HttpSession session){
        //保存
        ResultEntity<String> resultEntity=mySQLRemoteService.saveAddressRemote(addressVO);
        //取出returnCount
        OrderProjectVO orderProjectVO = (OrderProjectVO)session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();
        //重定向
        return "redirect:http://localhost/order/confirm/order/"+returnCount;
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount,HttpSession session){
        OrderProjectVO orderProjectVO = (OrderProjectVO)session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO",orderProjectVO);
        //目前收货地址数据
        MemberLoginVO memberLoginVO = (MemberLoginVO)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();
        ResultEntity<List<AddressVO>> resultEntity=mySQLRemoteService.getAddressVORemote(memberId);
        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            List<AddressVO> list = resultEntity.getData();
            session.setAttribute("addressVOList",list);
            System.out.println(list);
        }

        return "confirm_order";
    }

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(@PathVariable("projectId")Integer projectId,
                                        @PathVariable("returnId") Integer returnId, HttpSession session){
        ResultEntity<OrderProjectVO> resultEntity=mySQLRemoteService.getOrderProjectVORemote(projectId,returnId);
        System.out.println(resultEntity);
        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            OrderProjectVO orderProjectVO = resultEntity.getData();
            session.setAttribute("orderProjectVO",orderProjectVO);
        }
        return "confirm_return";
    }
}
