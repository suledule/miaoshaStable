package com.imooc.miaoshaproject.thread;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 写入锁和读取锁
 */
public class ReadWriteLock {
    public static void main(String[] args) {
        Data1 data = new Data1();
        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(()->{
                data.put(temp+"",temp+"写入");
            },temp+"").start();
        }

        for (int i = 0; i < 5; i++) {
            final int temp = i;
            new Thread(()->{
                data.get(temp+"");
            },temp+"").start();
        }
    }
}

class Data1{
    private volatile HashMap hashMap = new HashMap();

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock lock = new ReentrantLock();

    //写入时只能一个线程写入
    public void put(String key,Object object){
        //readWriteLock.writeLock().lock(); //写入锁，比lock的粒度更细
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"->写入"+key);
            hashMap.put(key,object);
            System.out.println(Thread.currentThread().getName()+"->写入完成"+key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //readWriteLock.writeLock().unlock();//解锁
            lock.unlock();
        }
    }

    //读取时可以多个线程读取
    public void get(String key){
        //readWriteLock.readLock().lock(); //读锁
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"->读取"+key);
            hashMap.get(key);
            System.out.println(Thread.currentThread().getName()+"->读取完成"+key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //readWriteLock.readLock().unlock();
            lock.unlock();
        }
    }
}
