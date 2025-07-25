package com.imooc.miaoshaproject.thread;

import java.util.Arrays;
import java.util.List;
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
    /**
     * CPU密集型：设置最大线程数=当前设备的CPU线程数
     * IO密集型：假如有10个IO大型任务（耗费时间），则设置最大线程要大于10，如设置20
     */
    //自定义线程池
    static ExecutorService pool = new ThreadPoolExecutor(
            2, //核心线程（最小开启线程）
            5,  //最大线程
            3,  //超时时间
            TimeUnit.SECONDS,  //超时单位
            new LinkedBlockingQueue<Runnable>(3),  //队列大小
            Executors.defaultThreadFactory(),  //默认线程工厂
            new ThreadPoolExecutor.CallerRunsPolicy()); //四种拒绝策略

    public static void main (String[] args) throws Exception {
        //获取当前设备的CPU线程数
        System.out.println(Runtime.getRuntime().availableProcessors());

        System.out.println(submitForm1());
        System.out.println(submitForm7());
        System.out.println(submitForm9());
        System.out.println(submitForm10());

    }

    public static String submitForm() {
        for (int i = 0; i < 9; i++) {
            pool.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"-->");
            });
        }
        return "123";
    }

    /**
     * submit提交任务，优先使用submit，execute比较基础，缺少返回值，
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static String submitForm1() throws Exception {
        Future<String> future = pool.submit(()->{
            System.out.println(Thread.currentThread().getName()+"-->");
            return "1";
        });
        System.out.println(future.get());
        return "123";
    }

    /**
     * 批量提交并等待完成
     * @return
     * @throws Exception
     */
    public static String submitForm2() throws Exception {
        List<Callable<String>> tasks = Arrays.asList(
                () -> download("url1"), () -> download("url2"));
        List<Future<String>> future = pool.invokeAll(tasks);
        System.out.println(future.get(0));
        return "123";
    }

    public static String download(String url) {
        return "download";
    }

    public static String saveToDB(String url) {
        return "saveToDB";
    }

    /**
     * 同步执行任务，立刻返回
     * @return
     * @throws Exception
     */
    public static String submitForm3() throws Exception {
        CompletableFuture<String> future = CompletableFuture.completedFuture("Hello");
        System.out.println(future.get());
        return "123";
    }

    /**
     * 异步执行任务，无返回值的
     * @return
     * @throws Exception
     */
    public static String submitForm4() throws Exception {
        // 默认线程池
        CompletableFuture.runAsync(() -> System.out.println("Task running"));
        return "123";
    }

    /**
     * 异步执行任务，有返回值的
     * @return
     * @throws Exception
     */
    public static String submitForm5() throws Exception {
        // 默认线程池
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> download(""));
        // 指定线程池
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> download(""), pool);
        System.out.println(future.get());
        System.out.println(future1.get());
        return "123";
    }

    /**
     * thenApply：对任务结果进行转换，返回新值
     * thenAccept:消费结果，无返回值
     * thenRun:无结果消费，仅执行操作
     * @return
     * @throws Exception
     */
    public static String submitForm6() throws Exception {
        // 对任务结果进行转换，返回新值
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> download(""))
                .thenApply(s -> s + " World");
        // 将结果存入数据库
        future.thenAccept(result -> saveToDB(result));
        // 记录日志
        future.thenRun(() -> System.out.println("Task completed"));
        return "123";
    }

    /**
     * thenCompose:串联多个异步任务（上一个结果作为下一个任务的输入）
     * @return
     * @throws Exception
     */
    public static String submitForm7() throws Exception {
        // 串联多个异步任务（上一个结果作为下一个任务的输入）
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> download(""))
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> saveToDB(s)));
        System.out.println(future.get());
        return "123";
    }

    /**
     * 独立执行两个任务，合并结果
     * @return
     * @throws Exception
     */
    public static String submitForm8() throws Exception {
        // 独立执行两个任务，合并结果：
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> download(""));
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> saveToDB(""));
        CompletableFuture<String> future = future1.thenCombine(future2, (s1, s2) -> s1 + " " + s2);
        System.out.println(future.get());
        return "123";
    }

    /**
     * 批量执行任务，全部完成后触发后续操作：
     * @return
     * @throws Exception
     */
    public static String submitForm9() throws Exception {
        // 批量执行任务，全部完成后触发后续操作：
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> download(""));
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> saveToDB(""));
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(future1, future2);
        // 所有任务完成后执行
        allTasks.thenRun(() -> System.out.println("All tasks completed"));
        return "123";
    }

    /**
     * 多个任务中任意一个完成即执行后续操作：
     * @return
     * @throws Exception
     */
    public static String submitForm10() throws Exception {
        // 多个任务中任意一个完成即执行后续操作：
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> download(""));
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> saveToDB(""));
        CompletableFuture<Object> anyTask = CompletableFuture.anyOf(future1, future2);
        anyTask.thenAccept(result -> System.out.println("First result: " + result));
        System.out.println(anyTask.get());
        return "123";
    }
}
