package com.zhijia.crowd;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.zhijia.crowd.api.MySQLRemoteService;
import com.zhijia.crowd.api.RedisRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class CrowdMainClassTests {

    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @Test
    void contextLoads() {
            String host = "https://gyytz.market.alicloudapi.com";
            String path = "/sms/smsSend";
            String method = "POST";
            String appcode = "b81a499b1f46480fa077f146f4ea43c9";
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("mobile", "15307620821");
            querys.put("param", "**code**:12345,**minute**:5");
            querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
            querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
            Map<String, String> bodys = new HashMap<String, String>();


            try {
                /**
                 * 重要提示如下:
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                 * 下载
                 *
                 * 相应的依赖请参照
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 */
                HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
                System.out.println(response.toString());
                //获取response的body
                log.info(EntityUtils.toString(response.getEntity()));
                //System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
        }
    }

    @Test
    void contextLoads2() {
//        redisRemoteService.setRedisKeyValueRemote("dffffffdf","dffdf");
        redisRemoteService.setRedisKeyValueRemoteWithTimeout("fdf","eee",15L, TimeUnit.MINUTES);
    }

//    @Test
//    void testSaveMember(){
//        mySQLRemoteService.getMemberPOByloginAcctRemote()
//    }

}
