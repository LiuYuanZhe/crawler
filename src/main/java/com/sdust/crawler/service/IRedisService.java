package com.sdust.crawler.service;

import com.alibaba.fastjson.JSON;

/**
 * Created by LiuYuanZhe on 18/5/4.
 */
public interface IRedisService {
    /**
     * put id to queue
     * @param jobid
     * @return
     */
    boolean putJobSet(String jobid);

    /**
     * return a jobid to crawler jobdetails
     * @return (string)id
     */
    String popTaskid();

    /**
     * query result for key
     * @param key p key
     * @return
     */
    String queryResult(String key);

    /**
     * put result to redis
     * @param obj json/string
     * @return
     */
    boolean putResultToQueue(Object obj);

    public boolean putExpireToRedis(Integer time);

    public long getJobSetSize();

    public boolean saveResult(JSON json);
}
