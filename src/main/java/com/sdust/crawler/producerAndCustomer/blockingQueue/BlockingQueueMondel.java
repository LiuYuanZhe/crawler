package com.sdust.crawler.producerAndCustomer.blockingQueue;

import com.sdust.crawler.producerAndCustomer.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LiuYuanZhe on 18/3/31.
 */
public class BlockingQueueMondel implements Model {
    private final BlockingQueue<Task> queue;
    private final AtomicInteger increTaskNo = new AtomicInteger(0);
    public BlockingQueueMondel(int cap) {
        this.queue = new LinkedBlockingDeque(cap);
    }

    @Override
    public Runnable newRunnableConsumer() {
        return new ConsumerImpl();
    }

    @Override
    public Runnable newRunnableProducer() {
        return new ProducerImpl();
    }
    private class ConsumerImpl extends AbstractConsumer implements Consumer,Runnable {
        @Override
        public void consume() throws InterruptedException {
            Task task = queue.take();
            Thread.sleep(500 + (long)(Math.random()*500));
            System.out.println("consume: " + task.no);
        }
    }

    private class ProducerImpl extends AbstractProducer implements Producer,Runnable {
        @Override
        public void produce() throws InterruptedException {
            Thread.sleep((long) (Math.random()*1000));
            Task task = new Task(increTaskNo.getAndIncrement());
            queue.put(task);
            System.out.println("produce :" + task.no);
        }
    }

    public static void main(String[] args) {
        Model model = new BlockingQueueMondel(3);
        for (int i = 0;i < 2;i++){
            new Thread(model.newRunnableConsumer()).start();
        }
        for (int i = 0;i < 5;i++){
            new Thread(model.newRunnableProducer()).start();
        }
    }

}
