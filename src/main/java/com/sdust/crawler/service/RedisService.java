package com.sdust.crawler.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by LiuYuanZhe on 18/5/3.
 */
@Component
public class RedisService implements IRedisService{
    private Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    //job set名称
    public static final String JOBID = "jobid";

    public static final String Expire_Identification="expi:ti";


    @Override
    public boolean putJobSet(String jobid) {
        //jobid为set名
        try {
            redisTemplate.opsForSet().add(JOBID,jobid);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    @Override
    public String popTaskid() {
        String id = String.valueOf(redisTemplate.opsForSet().pop(JOBID));
        return id;
    }

    @Override
    public String queryResult(String key) {

        return null;
    }

    @Override
    public boolean putResultToQueue(Object obj) {
        return false;
    }

    public int getExpireNum(){
        Integer i = (Integer)redisTemplate.opsForValue().get(Expire_Identification);
        if(i==null){
            return 2880;//默认两天
        }else{
            return i.intValue();
        }
    }

    @Override
    public boolean putExpireToRedis(Integer time) {
        try{
            redisTemplate.opsForValue().set(Expire_Identification, time);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public long getJobSetSize(){
        long size;
        try {
            size = redisTemplate.opsForSet().size(JOBID);
            return size;
        }catch (Exception e){
            e.printStackTrace();
            logger.info("redis连接超时");
            return 0;
        }
    }

    @Override
    public boolean saveResult(JSON json) {
        try {
            String result = json.toString();
//            redisTemplate.opsForValue().se
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
