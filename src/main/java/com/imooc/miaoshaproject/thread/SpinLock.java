package com.imooc.miaoshaproject.thread;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 自旋锁
 */
public class SpinLock {

    public static void main(String[] args) {

        MyLock lock = new MyLock();

        new Thread(()->{
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("A业务");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }).start();



        new Thread(()->{
            lock.myLock();
            try {
                System.out.println("B业务");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }).start();


    }
}

/**
 * 自己实现一个锁
 */
class MyLock{
    //null为默认值
    private AtomicReference<Thread> atomic = new AtomicReference<>(null);

    public void myLock(){
        Thread thread = Thread.currentThread();
        //System.out.println(Thread.currentThread().getName()+"===>上锁");

        //自旋锁 使用CAS,一直循环，直到当前线程调用了myUnLock(),才会跳出循环
        //(期望值，更新值）
        while(!atomic.compareAndSet(null,thread)){
//            System.out.println("=========");
        }
    }

    public void myUnLock(){
        Thread thread = Thread.currentThread();
        //System.out.println(Thread.currentThread().getName()+"===>解锁");
        atomic.compareAndSet(thread,null);
        ArrayList list = new ArrayList();
    }
}
