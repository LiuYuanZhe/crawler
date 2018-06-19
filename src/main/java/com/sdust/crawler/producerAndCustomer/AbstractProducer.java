package com.sdust.crawler.producerAndCustomer;

/**
 * Created by LiuYuanZhe on 18/3/31.
 */
public class AbstractProducer implements Producer,Runnable {

    @Override
    public void produce() throws InterruptedException {

    }

    @Override
    public void run() {
        while (true){
            try {
                produce();
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
    }
}
