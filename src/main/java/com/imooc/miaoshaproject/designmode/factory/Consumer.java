package com.imooc.miaoshaproject.designmode.factory;

public class Consumer {
    public static void main(String[] args) {

        Car wuling = CarFactory.getCar("五菱");
        Car wuling1 = CarFactory.getWuling();

        System.out.println(wuling.name());


    }
}
