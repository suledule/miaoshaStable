package com.imooc.miaoshaproject.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁
 * 拿到一个方法的锁时，也会拿到这个方法中调用的方法的锁
 * synchronized 与 ReentrantLock() 的区别，synchronized算一把锁，lock是两把锁
 */
public class ReentrantLockTest {
    private static Lock lock = new ReentrantLock();
    public static void main(String[] args) {
        new Thread(()->{
            sms();
        }).start();

        new Thread(()->{
            call();
        }).start();

    }

    //synchronized算一把锁，lock是两把锁
    public static synchronized void sms(){
        System.out.println(Thread.currentThread().getName()+"sms");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        call();//这个方法的锁也会拿到
    }

    public static synchronized void call(){
        System.out.println(Thread.currentThread().getName()+"call");
    }

    public static void sms1(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"sms");
            TimeUnit.SECONDS.sleep(2);
            call();//这个方法的锁也会拿到
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void call1(){
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"call");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
