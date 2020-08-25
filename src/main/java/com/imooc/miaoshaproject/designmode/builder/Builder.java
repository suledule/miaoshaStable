package com.imooc.miaoshaproject.designmode.builder;

/**
 * 建造者模式
 * 适合一个具有较多属性的对象的创建过程
 */
public abstract class Builder {
    abstract Builder builderA(String msg);
    abstract Builder builderB(String msg);
    abstract Builder builderC(String msg);
    abstract Builder builderD(String msg);

    abstract Product getProduct();
}
