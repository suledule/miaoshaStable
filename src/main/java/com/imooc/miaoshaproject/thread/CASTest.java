package com.imooc.miaoshaproject.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS compare and set 比较并交换
 * ABA 问题 （使用版本号解决）
 */
public class CASTest {
    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(10);

        //比较原来的值跟except值，如果相同，则更新，不同则不更新（except值，更新值）
        System.out.println(atomicInteger.compareAndSet(10, 20));
        System.out.println(atomicInteger.compareAndSet(20, 10));

        //ABA问题，前两行代码10->20,然后20->10，虽然最后结果还是10，但已经不是原来的那个10了，
        System.out.println(atomicInteger.compareAndSet(10, 30));
        System.out.println(atomicInteger);
        System.out.println("================");

        //（初始值，初始版本号）
        //initalRef 范围在-127到128，在IntegerCache.cache产生，会复用此对象，超过范围，在堆中产生，不会复用此对象
        AtomicStampedReference<Integer> atomic = new AtomicStampedReference<>(1,1);

        new Thread(()->{
            System.out.println("AAA当前版本="+atomic.getStamp());//当前版本号

            atomic.compareAndSet(1,2,atomic.getStamp(),atomic.getStamp()+1);
            System.out.println("AAA第一次="+atomic.getStamp());

            atomic.compareAndSet(2,1,atomic.getStamp(),atomic.getStamp()+1);
            System.out.println("AAA第二次="+atomic.getStamp());
        }).start();

        new Thread(()->{
            System.out.println("BBB当前版本="+atomic.getStamp());//当前版本号

            atomic.compareAndSet(1,2,atomic.getStamp(),atomic.getStamp()+1);
            System.out.println("BBB第一次="+atomic.getStamp());

        }).start();


        while(Thread.activeCount() > 2){
            Thread.yield();
        }
        //initalRef 范围在-127到128，在IntegerCache.cache产生，会复用此对象，超过范围，在堆中产生，不会复用此对象
        Integer integer = 1000;
        Integer integer1 = 1000;
        // == 如果比较的对象是基本数据类型，则比较的是数值是否一致；如果比较的是引用数据类型，则比较的是对象的地址值是否一致
        System.out.println("result:"+(integer==integer1));
        System.out.println("equals:"+(integer.equals(integer1)));
    }
}
