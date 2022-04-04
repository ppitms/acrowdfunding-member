package com.zhijia.crowd.service.api;

import com.zhijia.crowd.entity.vo.AddressVO;
import com.zhijia.crowd.entity.vo.OrderProjectVO;
import com.zhijia.crowd.entity.vo.OrderVO;

import java.util.List;

/**
 * @author zhijia
 * @create 2022-04-02 21:36
 */
public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
