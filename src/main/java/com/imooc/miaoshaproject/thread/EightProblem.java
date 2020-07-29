package com.imooc.miaoshaproject.thread;

import java.util.concurrent.TimeUnit;

/**
 * 锁的八大问题
 */
public class EightProblem {
    public static void main(String[] args) {

        Phone iphone = new Phone();
        new Thread(()->{ iphone.sendSms(); },"A").start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{ iphone.call(); },"B").start();
    }
}

class Phone{
    //synchronized锁的是调用这个方法的对象--->iphone,  snedSms()和call()两个方法使用的是同一把锁
    //如果是静态方法static，则锁的是class类模板对象，一个模板类，只有一个class类模板对象
    public synchronized void sendSms(){
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("发短信");
    }

    public synchronized void call(){
        System.out.println("打电话");
    }
}
