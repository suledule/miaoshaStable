package com.imooc.miaoshaproject.thread;

import java.util.concurrent.*;

/**
 * 异步回调方法
 */
public class CompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //无返回值
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName()+"-->");
        });

        //有返回值
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(()->{
            System.out.println(Thread.currentThread().getName()+"-->");
            //int i = 10/0;
            return "YES";
        });
        String result = completableFuture1.whenComplete((t,u)->{
            System.out.println("t=="+t); //正常的返回结果
            System.out.println("u=="+u); //错误信息
        }).exceptionally((e)->{
            System.out.println(e.getMessage());
            return "ERROR"; //可以获取错误的结果
        }).get();

        //结果
        System.out.println("result==="+result);
    }
}
