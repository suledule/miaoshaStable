package com.imooc.miaoshaproject.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * submit() 和 execute() 的区别
 */
public class SubmitAndExecute {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        Future future = executorService.submit(()->{
            System.out.println(Thread.currentThread().getName()+"==>");
        });
    }
}
