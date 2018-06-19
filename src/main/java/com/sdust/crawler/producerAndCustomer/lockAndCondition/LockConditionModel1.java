package com.sdust.crawler.producerAndCustomer.lockAndCondition;

import com.sdust.crawler.producerAndCustomer.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by LiuYuanZhe on 18/4/1.
 */
public class LockConditionModel1 implements Model {
    private final Lock CONSUME_LOCK = new ReentrantLock();
    private final Condition NOT_EMPTY = CONSUME_LOCK.newCondition();
    private final Lock PRODUCE_LOCK = new ReentrantLock();
    private final Condition NOT_FULL = PRODUCE_LOCK.newCondition();
    private final Buffer<Task> buffer = new Buffer();
    private AtomicInteger bufLen = new AtomicInteger(0);
    private final int cap;
    private final AtomicInteger increTaskNo = new AtomicInteger(0);
    public LockConditionModel1(int cap){
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

    /**
     * 自己实现一个buffer,
     * 在头部出队,尾部入队
     * poll()方法只操作head
     * offer()方法中国年只操作tail
     * @param <T>
     */
    private class Buffer<T> {
        private Node head;
        private Node tail;
        Buffer(){
            head = tail = new Node(null);
        }
        public void offer(T t){
            tail.next = new Node(t);
            tail = tail.next;
        }

        public T poll(){
            head = head.next;
            T t = head.item;
            head.item = null;
            return t;
        }

        private class Node {
            T item;
            Node next;
            Node(T item){
                this.item = item;
            }
        }
    }

    private class ProducerImpl extends AbstractProducer implements Producer,Runnable {
        @Override
        public void produce() throws InterruptedException {
            Thread.sleep((long)(Math.random()*1000));
            int newBufSize = -1;
            PRODUCE_LOCK.lockInterruptibly();
            try {
                while (bufLen.get() == cap){
                    System.out.println("buffer is full...");
                    NOT_FULL.await();
                }
                Task task = new Task(increTaskNo.getAndIncrement());
                buffer.offer(task);
                newBufSize = bufLen.incrementAndGet();
                System.out.println("produce: " + task.no);
                if (newBufSize < cap){
                    NOT_FULL.signalAll();
                }
            }finally {
                PRODUCE_LOCK.unlock();
            }
            if (newBufSize > 0){
                CONSUME_LOCK.lockInterruptibly();
                try {
                    NOT_EMPTY.signalAll();
                }finally {
                    CONSUME_LOCK.unlock();
                }
            }
        }

    }

    private class ConsumerImpl extends AbstractConsumer implements Consumer, Runnable {
        @Override
        public void consume() throws InterruptedException {
            int newBufSize = -1;
            CONSUME_LOCK.lockInterruptibly();
            try {
                while (bufLen.get() == 0){
                    System.out.println("buffer is empty...");
                    NOT_EMPTY.await();
                }
                Task task = buffer.poll();
                newBufSize = bufLen.decrementAndGet();
                assert task != null;
                Thread.sleep(500+(long)(Math.random()*500));
                System.out.println("consume: " + task.no);
                if (newBufSize > 0){
                    NOT_EMPTY.signalAll();
                }
            }finally {
                CONSUME_LOCK.unlock();
            }
            if (newBufSize < cap){
                PRODUCE_LOCK.lockInterruptibly();;
                try {
                    NOT_FULL.signalAll();
                }finally {
                    PRODUCE_LOCK.unlock();
                }
            }
        }
    }
    public static void main(String[] args) {
        Model model = new LockConditionModel1(3);
        for (int i = 0;i < 2;i++){
            new Thread(model.newRunnableConsumer()).start();
        }
        for (int i = 0;i < 5;i++){
            new Thread(model.newRunnableProducer()).start();
        }
    }
}
