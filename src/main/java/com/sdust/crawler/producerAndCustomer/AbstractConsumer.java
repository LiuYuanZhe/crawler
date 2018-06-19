package com.sdust.crawler.producerAndCustomer;

/**
 * Created by LiuYuanZhe on 18/3/31.
 */
public class AbstractConsumer implements Consumer,Runnable {
    @Override
    public void consume() throws InterruptedException {

    }

    @Override
    public void run() {
        while (true){
            try {
                consume();
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
    }
}
