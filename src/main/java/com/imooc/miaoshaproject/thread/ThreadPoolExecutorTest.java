package com.imooc.miaoshaproject.thread;

import java.util.concurrent.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

/**
 * 线程池 ThreadPoolExecutor
 * 四种拒绝策略
 * 1.ThreadPoolExecutor.AbortPolicy() 超出最大承载会抛出异常java.util.concurrent.RejectedExecutionException
 * 2.ThreadPoolExecutor.CallerRunsPolicy() 拒绝任务，不会抛出异常，让任务返回，（返回给main执行）
 * 3.ThreadPoolExecutor.DiscardOldestPolicy() 尝试与第一个竞争，如果第一个执行完了，则进入，没执行完，则拒绝
 * 4.ThreadPoolExecutor.DiscardPolicy() 拒绝任务，不会抛出异常
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) {

        //获取当前设备的CPU线程数
        System.out.println(Runtime.getRuntime().availableProcessors());

        /**
         * CPU密集型：设置最大线程数=当前设备的CPU线程数
         * IO密集型：假如有10个IO大型任务（耗费时间），则设置最大线程要大于10，如设置20
         */
        //自定义线程池
        ExecutorService pool = new ThreadPoolExecutor(
                2, //核心线程（最小开启线程）
                5,  //最大线程
                3,  //超时时间
                TimeUnit.SECONDS,  //超市单位
                new LinkedBlockingQueue<Runnable>(3),  //队列大小
                Executors.defaultThreadFactory(),  //默认线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy()); //四种拒绝策略

        //最大承载 = 队列大小 + 最大线程，超过会触发拒绝策略
        for (int i = 0; i < 9; i++) {
            pool.execute(()->{
                System.out.println(Thread.currentThread().getName()+"-->");
            });
        }
        pool.shutdown();
    }
}
