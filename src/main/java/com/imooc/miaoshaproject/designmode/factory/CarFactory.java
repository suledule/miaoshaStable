package com.imooc.miaoshaproject.designmode.factory;

/**
 * 工厂模式分为：1.简单工厂模式，2.工厂方法模式
 * 简单工厂模式（静态工厂模式）
 * 特点：生产同一等级结构的任意产品，对于新增的产品，需要修改已有代码
 * 缺点：
 * 1.如果新增一个汽车大众，必须要修改工厂代码。
 * 2.不满足开闭原则（规定软件中的对象（类，模块，函数等等）应该对于扩展是开放的，但是对于修改是封闭的）
 */
public class CarFactory {

    //方法一
    public static Car getCar(String name){
        if("五菱".equals(name)){
            return new Wuling();
        }else if("特斯拉".equals(name)){
            return new Tesla();
        }else {
            return null;
        }
    }

    //方法二
    public static Car getWuling(){
        return new Wuling();
    }

    public static Car getTesla(){
        return new Tesla();
    }
}
