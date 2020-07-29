package com.imooc.miaoshaproject.thread;

/**
 * synchronized锁
 */
public class SynchronizedThread{
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
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

class Ticket{
    private int num = 50;

    public synchronized void sale(){
        if(num > 0){
            System.out.println(Thread.currentThread().getName()+"卖出了"+num--+"票，剩余"+num+"张");
        }
    }
}
