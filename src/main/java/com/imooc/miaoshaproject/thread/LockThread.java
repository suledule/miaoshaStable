package com.imooc.miaoshaproject.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockThread {
    public static void main(String[] args) {
        Ticket2 ticket = new Ticket2();
        //lambda表达式 () -> {}，简写
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"A").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"B").start();

        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"C").start();
    }
}

/**
 * 1.new ReentrantLock();
 * 2.lock.lock();  加锁
 * 3.finally -> lock.unlock();  解锁
 */
class Ticket2{
    private int num = 50;

    Lock lock = new ReentrantLock();

    public void sale(){
        lock.lock();//加锁

        try {
            if(num > 0){
                System.out.println(Thread.currentThread().getName()+"卖出了"+num--+"票，剩余"+num+"张");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//解锁
        }


    }
}
