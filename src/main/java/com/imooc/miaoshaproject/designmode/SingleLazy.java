package com.imooc.miaoshaproject.designmode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * 单例模式（单例对象的类必须保证只有一个实例存在）
 * 懒汉式单例
 * 1.并发下存在问题
 */
public class SingleLazy {

    private  static boolean flag = false;
    private SingleLazy(){
        synchronized (SingleLazy.class){
            if(flag == false){
                flag = true;
                System.out.println(Thread.currentThread().getName()+"==>");
            }else{
                throw new RuntimeException("不要试图用反射破坏");
            }
        }
    }

    private volatile static SingleLazy lazy;

    //DLC懒汉式，双重检测锁
    public static SingleLazy getInstance(){
        if(lazy == null){
            synchronized (SingleLazy.class){
                if(lazy == null){
                    /**
                     * 1.分配内存空间
                     * 2.执行构造方法，初始化对象
                     * 3.把对象指向这个空间
                     *
                     * 在CPU执行时，会指令重排(防止指令重排，加  volatile)
                     * 执行顺序可能是
                     * 123（正常执行）
                     * 132（此时lazy还没有完成构造，导致第二个线程发生错误）
                     */
                    lazy = new SingleLazy(); //不是一个原子性操作
                }
            }
        }
        return lazy;
    }

    public static void main(String[] args) throws Exception {
//        for (int i = 0; i < 10; i++) {
//            new Thread(()->{
//                Lazy.getInstance();
//            }).start();
//        }

        //反射破坏
        Field field = SingleLazy.class.getDeclaredField("flag");
        field.setAccessible(true);

        Constructor<SingleLazy> constructor = SingleLazy.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        System.out.println(constructor.newInstance());
        field.set(constructor,false);
        System.out.println(constructor.newInstance());
    }
}
