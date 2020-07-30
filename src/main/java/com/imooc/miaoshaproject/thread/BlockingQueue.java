package com.imooc.miaoshaproject.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 队列 BlockingQueue
 */
public class BlockingQueue {
    public static void main(String[] args){

        //设置队列大小
        ArrayBlockingQueue queue = new ArrayBlockingQueue<>(2);
        //同步队列，大小为一的队列
        SynchronousQueue queue1 = new SynchronousQueue<>();
        System.out.println(queue1.offer("1"));
        System.out.println(queue1.offer("1"));
        /**
         * 有异常
         */
        System.out.println("=============error");
        //add,超出队列大小会报错java.lang.IllegalStateException: Queue full
        System.out.println(queue.add("1"));
        //remove,移除一个队列数据，队列无数据会报错java.util.NoSuchElementException
        System.out.println(queue.remove());
        //输出队首元素，队列无数据会报错java.util.NoSuchElementException
        //System.out.println(queue.element());

        /**
         * 有返回值
         */
        System.out.println("=============return");
        //offer,入队，超出队列大小会输出false
        System.out.println(queue.offer("2"));
        //poll,出队，无数据会输出null
        System.out.println(queue.poll());
        //输出队首元素，无数据会输出null
        //System.out.println(queue.peek());

        /**
         * 阻塞
         */
        System.out.println("=============block");
        try {
            //入队，队列没位置了，会一直阻塞，直到有位置
            queue.put("2");
            //出队，队列没数据，会一直等待
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 超时等待
         */
        System.out.println("=============timeout");
        try {
            //入队，队列没位置会等待两秒，还没有位置会返回false
            System.out.println(queue.offer("1",2,TimeUnit.SECONDS));
            //出队，队列无数据会等待两秒，还没数据会返回null
            System.out.println(queue.poll(2, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
