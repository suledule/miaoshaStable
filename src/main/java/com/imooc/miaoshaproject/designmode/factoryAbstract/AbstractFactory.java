package com.imooc.miaoshaproject.designmode.factoryAbstract;

/**
 * 抽象工厂方法
 * 特点：
 * 1.围绕一个超级工厂创建其他工厂，
 * 2.不可以创建产品，可以创建产品组
 */
public interface AbstractFactory {
    //手机组
    Phone phone();

    //电脑组
    Compute compute();
}
