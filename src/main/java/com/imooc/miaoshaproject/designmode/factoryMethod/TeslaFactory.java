package com.imooc.miaoshaproject.designmode.factoryMethod;

public class TeslaFactory implements CarFactory{

    @Override
    public Car getCar() {
        return new Tesla();
    }
}
