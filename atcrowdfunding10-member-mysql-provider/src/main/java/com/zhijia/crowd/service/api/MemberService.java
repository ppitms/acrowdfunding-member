package com.zhijia.crowd.service.api;

import com.zhijia.crowd.entity.po.MemberPO;

/**
 * @author zhijia
 * @create 2022-03-28 10:29
 */
public interface MemberService {


    MemberPO getMemberPOByLoginAcct(String loginacct);

    void saveMember(MemberPO memberPO);
}
