package com.imooc.miaoshaproject.designmode.factoryMethod;

public class Consumer {
    public static void main(String[] args) {
        Car wuling = new WulingFactory().getCar();
        System.out.println(wuling.name());
    }
}
