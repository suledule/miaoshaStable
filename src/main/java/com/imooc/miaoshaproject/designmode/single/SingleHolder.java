package com.imooc.miaoshaproject.designmode.single;

/**
 * 静态内部类
 * 内部类：一个类的内部又定义了一个类
 */
public class SingleHolder {
    private SingleHolder(){

    }

    public static SingleHolder getInstance(){
        return innerClass.HOLDER;
    }

    //静态内部类
    public static class innerClass{
        private static final SingleHolder HOLDER = new SingleHolder();
    }
}
