package com.imooc.miaoshaproject.thread;

import java.util.concurrent.*;

/**
 * submit() 和 execute() 的区别
 */
public class SubmitAndExecute {
    public static void main(String[] args) {

        //submit  有返回值
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        Future future = executorService.submit(()->{
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int a = 10/0;
            System.out.println(Thread.currentThread().getName()+"==>");
        });

        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //execute 无返回值
//        executorService.execute(new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }));
    }
}
