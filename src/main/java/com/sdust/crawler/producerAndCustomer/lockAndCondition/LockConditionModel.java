package com.sdust.crawler.producerAndCustomer.lockAndCondition;

import com.sdust.crawler.producerAndCustomer.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by LiuYuanZhe on 18/4/1.
 */
public class LockConditionModel implements Model {
    private final Lock BUFFER_LOCK = new ReentrantLock();
    private final Condition BUFFER_COND = BUFFER_LOCK.newCondition();
    private final Queue<Task> buffer = new LinkedList<Task>();
    private final int cap;
    private final AtomicInteger increTaskNo = new AtomicInteger(0);
    public LockConditionModel(int cap){
        this.cap = cap;
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
            BUFFER_LOCK.lockInterruptibly();
            try {
                while (buffer.size() == 0){
                    BUFFER_COND.await();
                }
                Task task = buffer.poll();
                assert task != null;
                Thread.sleep(500+(long)(Math.random()*500));
                System.out.println("consume: "+task.no);
                BUFFER_COND.signalAll();
            }finally {
                BUFFER_LOCK.unlock();
            }
        }
    }

    private class ProducerImpl extends AbstractProducer implements Producer,Runnable {
        @Override
        public void produce() throws InterruptedException {
            Thread.sleep((long)(Math.random()*1000));
            BUFFER_LOCK.lockInterruptibly();
            try{
                while (buffer.size() == cap){
                    System.out.println("产品已满,请消费者消费");
                    BUFFER_COND.await();
                }
                Task task = new Task(increTaskNo.getAndIncrement());
                buffer.offer(task);
                System.out.println("produce: "+task.no);
                BUFFER_COND.signalAll();
            }finally {
                BUFFER_LOCK.unlock();
            }
        }
    }
    public static void main(String[] args) {
        Model model = new LockConditionModel(3);
        for (int i = 0;i < 2;i++){
            new Thread(model.newRunnableConsumer()).start();
        }
        for (int i = 0;i < 5;i++){
            new Thread(model.newRunnableProducer()).start();
        }
    }
}
