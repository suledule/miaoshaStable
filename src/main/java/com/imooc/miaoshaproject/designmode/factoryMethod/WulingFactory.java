package com.imooc.miaoshaproject.designmode.factoryMethod;

public class WulingFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new Wuling();
    }
}
