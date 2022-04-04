package com.zhijia.crowd;

import com.zhijia.crowd.api.MySQLRemoteService;
import com.zhijia.crowd.entity.vo.OrderProjectVO;
import com.zhijia.crowd.util.ResultEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @author zhijia
 * @create 2022-04-02 22:53
 */
@SpringBootTest
public class tess {

    @Autowired
    private MySQLRemoteService mySQLRemoteService;
    @Test
    public void testlosd(){
//        ResultEntity<OrderProjectVO> orderProjectVORemote = mySQLRemoteService.getOrderProjectVORemote(3, 2);
//        System.out.println(orderProjectVORemote);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        System.out.println(uuid);
    }
}
