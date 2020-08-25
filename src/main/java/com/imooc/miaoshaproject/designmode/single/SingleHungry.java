package com.imooc.miaoshaproject.designmode.single;

/**
 * 单例模式（单例对象的类必须保证只有一个实例存在）
 * 饿汉式单例
 * 1.占用内存
 * 2.线程安全
 */
public class SingleHungry {
    //构造方法：当类实例化一个对象时会自动调用构造方法，初始化对象
    private SingleHungry(){
        System.out.println(Thread.currentThread().getName()+"==>");
    }

    private final static SingleHungry HUNGRY = new SingleHungry();

    public static SingleHungry getInstance(){
        return HUNGRY;
    }

    public static void main(String[] args) {
//        for (int i = 0; i < 10; i++) {
//            new Thread(()->{
//                SingleHungry hungry = new SingleHungry();
//            }).start();
//        }
    }
}
