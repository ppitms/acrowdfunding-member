package com.zhijia.crowd.service.impl;

import com.zhijia.crowd.entity.po.MemberPO;
import com.zhijia.crowd.entity.po.MemberPOExample;
import com.zhijia.crowd.mapper.MemberPOMapper;
import com.zhijia.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhijia
 * @create 2022-03-28 10:30
 */
@Transactional(readOnly = true)//只读
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {
        MemberPOExample memberPOExample = new MemberPOExample();
        memberPOExample.createCriteria().andLoginacctEqualTo(loginacct);
        List<MemberPO> list = memberPOMapper.selectByExample(memberPOExample);
        if (list.size()==0||list==null){
            return null;
        }

        return list.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class,readOnly = false)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
