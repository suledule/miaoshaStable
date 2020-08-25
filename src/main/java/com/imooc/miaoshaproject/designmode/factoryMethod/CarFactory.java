package com.imooc.miaoshaproject.designmode.factoryMethod;

/**
 * 工厂模式分为：1.简单工厂模式，2.工厂方法模式
 * 工厂方法模式
 * 优点：遵循开闭原则，
 * 缺点：代码变多，管理复杂，不如简单工厂模式方便
 */
public interface CarFactory {
    public Car getCar();
}
