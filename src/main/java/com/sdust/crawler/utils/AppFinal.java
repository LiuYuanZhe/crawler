package com.sdust.crawler.utils;

/**
 * Created by LiuYuanZhe on 18/5/23.
 */
public interface AppFinal {
    class ESFin{
        public static final String INDEX_NAME = "jobdetails";
        public static final String TYPE_NAME = "job";
        public static final String ANALYZER_NAME = "job";
        public static final String CLUSTERNAME = "elasticsearch";
        public static final int SHARDS = 5;
        public static final int REPLICAS =1;
    }
}
