package com.imooc.miaoshaproject.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile
 * 1.保证可见性 （能够感知堆内存的数据发生了变化）
 * 2.不保证原子性 （原子性：整个程序中的所有操作,要么全部完成,要么全部不完成）
 * 3.禁止指令重排 （指令重排：程序可能不会按顺序执行）volatile可以禁止指令重排，因为cpu内存屏障
 */
public class VolatileTest {

    private volatile static  int num = 0;

    private volatile static int count = 0;
    //AtomicInteger 原子类的integer
    private volatile static AtomicInteger atomicCount = new AtomicInteger();


    public static void main(String[] args) {
        /**
         * 保证可见性
         */
        new Thread(()->{
            while(num == 0){

            }
        }).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        num = 1;
        System.out.println("num=="+num);
        System.out.println("=============");

        /**
         * 不保证原子性
         */
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int i1 = 0; i1 < 1000; i1++) {
                    add();
                    addAtomic();
                }
            }).start();
        }

        while(Thread.activeCount()>2){ //最少有两个线程，main，gc
            Thread.yield(); //线程礼让,main线程礼让，使其他线程能执行完
            System.out.println("礼让线程是："+Thread.currentThread().getName());
        }

        System.out.println("count=="+count);
        System.out.println("atomicCount=="+atomicCount);
        System.out.println(Thread.activeCount());
    }

    public static void add(){
        count++;//不是原子性操作（底层是有多个步骤操作的）；
    }

    public static  void addAtomic(){
        atomicCount.getAndIncrement();// 加一的方法
    }
}
