package com.sdust.crawler.producerAndCustomer.notifyAndWait;

import com.sdust.crawler.producerAndCustomer.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LiuYuanZhe on 18/4/1.
 */
public class WaitNotifyModel implements Model {
    private final Object BUFFER_LOCK = new Object();
    private final Queue<Task> buffer = new LinkedList<Task>();
    private final int cap;
    private final AtomicInteger increTaskNo = new AtomicInteger(0);
    public WaitNotifyModel(int cap){
        this.cap = cap;
    }

    @Override
    public Runnable newRunnableConsumer() {
        return new ProducerImpl();
    }

    @Override
    public Runnable newRunnableProducer() {
        return new ConsumerImpl();
    }

    private class ProducerImpl extends AbstractProducer implements Producer,Runnable {
        @Override
        public void produce() throws InterruptedException {
            synchronized (BUFFER_LOCK){
                while (buffer.size() == cap){
                    System.out.println("产品已满,请消费者消费");
                    BUFFER_LOCK.wait();
                }
                Task task = new Task(increTaskNo.getAndIncrement());
                buffer.offer(task);
                System.out.println("produce: "+task.no);
                BUFFER_LOCK.notifyAll();
            }
        }
    }

    private class ConsumerImpl extends AbstractConsumer implements Consumer,Runnable {
        @Override
        public void consume() throws InterruptedException {
            synchronized (BUFFER_LOCK){
                while (buffer.size() == 0){
                    System.out.println("没有产品,不能消费");
                    BUFFER_LOCK.wait();
                }
                Task task = buffer.poll();
                assert task != null;
                Thread.sleep(500+(int)(Math.random()*500));
                System.out.println("consume: "+task.no);
                BUFFER_LOCK.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        Model model = new WaitNotifyModel(3);
        for (int i = 0;i < 2;i++){
            new Thread(model.newRunnableConsumer()).start();
        }
        for (int i = 0;i < 5;i++){
            new Thread(model.newRunnableProducer()).start();
        }
    }
}
