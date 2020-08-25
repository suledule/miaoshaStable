package com.imooc.miaoshaproject.designmode.factoryAbstract;

public class XiaomiPhone implements Phone {
    @Override
    public void call() {
        System.out.println("小米打电话");
    }

    @Override
    public void send() {
        System.out.println("小米发短信");
    }
}
