package com.zhijia.crowd.api;

import com.zhijia.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

/**
 * @author zhijia
 * @create 2022-03-28 12:03
 */
@FeignClient("ATCROWFUNDING11-MEMBER-REDIS-PROVIDER")
public interface RedisRemoteService {

    //设置不带超时时间的
    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key,@RequestParam("value")String value);
    //设置带超时时间的
    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(@RequestParam("key") String key,
                                                           @RequestParam("value")String value,
                                                           @RequestParam("time")long time,
                                                           @RequestParam("timeUnit")TimeUnit timeUnit);
    //获取
    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueByKey(@RequestParam("key")String key);
    //删除
    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key")String key);
}
