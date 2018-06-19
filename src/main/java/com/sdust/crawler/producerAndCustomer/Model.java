package com.sdust.crawler.producerAndCustomer;

/**
 * Created by LiuYuanZhe on 18/3/31.
 */
public interface Model {
    Runnable newRunnableConsumer();
    Runnable newRunnableProducer();
}
