package com.imooc.miaoshaproject.thread;

import java.util.stream.LongStream;

/**
 * ForkJoin 大任务分解成多个小任务，提高执行效率
 */
public class ForkJoinTest {
    public static void main(String[] args) {
        test1();
    }
    public static void test1(){
        long sum = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i <10_0000_0000 ; i++) {
            sum += i;
        }
        long end = System.currentTimeMillis();
        System.out.println("sum==="+sum+"time==="+(end-start));
    }

    public static void test2(){

    }

    public static void test3(){
        long start = System.currentTimeMillis();
        long sum = LongStream.rangeClosed(0L,10_0000_0000L).parallel().reduce(0,Long::sum);
        long end = System.currentTimeMillis();
        System.out.println("sum==="+sum+"time==="+(end-start));
    }
}
