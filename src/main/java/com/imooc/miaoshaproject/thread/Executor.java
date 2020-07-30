package com.imooc.miaoshaproject.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 三大线程，七个参数，四种策略
 * Executor 线程池，不建议使用,建议使用 ThreadPoolExecutor（方便深入理解）
 */
public class Executor {
    public static void main(String[] args) {
        //创建有单个线程的线程池
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //创建有5个线程的线程池
        ExecutorService executorService1 = Executors.newFixedThreadPool(5);

        //可伸缩的，动态调整线程
        ExecutorService executorService2 = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            executorService.execute(()->{
                System.out.println(Thread.currentThread().getName()+"-->");
            });
        }
        System.out.println("=============");
        for (int i = 0; i < 10; i++) {
            executorService1.execute(()->{
                System.out.println(Thread.currentThread().getName()+"-->");
            });
        }
        System.out.println("=============");
        for (int i = 0; i < 10; i++) {
            executorService2.execute(()->{
                System.out.println(Thread.currentThread().getName()+"-->");
            });
        }
        //关闭线程池
        executorService.shutdown();
    }
}
