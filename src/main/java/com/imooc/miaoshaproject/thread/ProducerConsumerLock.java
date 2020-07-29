package com.imooc.miaoshaproject.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者lock版
 */
public class ProducerConsumerLock {
    public static void main(String[] args) {
        Data2 data = new Data2();
        //生产者
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.increment();
            }
        },"A").start();

        //消费者
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.decrement();
            }
        },"B").start();

        //生产者
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.increment();
            }
        },"C").start();

        //消费者
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                data.decrement();
            }
        },"D").start();
    }
}

/**
 * 判断等待，执行业务，通知其他线程
 */
class Data2{
    private int num = 0;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    //生产一个
    public void increment(){
        lock.lock();//上锁
        try {
            while(num != 0){
                try {
                    condition.await(); //此线程等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            num++;
            System.out.println(Thread.currentThread().getName()+"-->"+num);
            condition.signal(); //唤醒其他的等待线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//解锁
        }
    }

    //消费一个
    public void decrement(){
        lock.lock();
        try {
            while(num == 0){
                try {
                    condition.await(); //此线程等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            num--;
            System.out.println(Thread.currentThread().getName()+"-->"+num);
            condition.signal(); //唤醒其他的等待线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//解锁
        }
    }
}

