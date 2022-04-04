package com.zhijia.crowd.service.impl;

import com.zhijia.crowd.entity.po.AddressPO;
import com.zhijia.crowd.entity.po.AddressPOExample;
import com.zhijia.crowd.entity.po.OrderPO;
import com.zhijia.crowd.entity.po.OrderProjectPO;
import com.zhijia.crowd.entity.vo.AddressVO;
import com.zhijia.crowd.entity.vo.OrderProjectVO;
import com.zhijia.crowd.entity.vo.OrderVO;
import com.zhijia.crowd.mapper.AddressPOMapper;
import com.zhijia.crowd.mapper.OrderPOMapper;
import com.zhijia.crowd.mapper.OrderProjectPOMapper;
import com.zhijia.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhijia
 * @create 2022-04-02 21:36
 */
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;


    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        OrderProjectVO orderProjectVO=orderProjectPOMapper.selectOrderProjectVO(returnId);
        return orderProjectVO;
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {
        AddressPOExample example = new AddressPOExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(example);
        List<AddressVO> addressVOList=new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO,addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO,addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO,orderPO);
        OrderProjectVO orderProjectVO = orderVO.getOrderProjectVO();
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderProjectVO,orderProjectPO);
        orderProjectPO.setOrderId(orderPO.getId());
        orderPOMapper.insert(orderPO);
        orderProjectPOMapper.insert(orderProjectPO);
    }
}
