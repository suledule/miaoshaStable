package com.imooc.miaoshaproject.designmode.factoryAbstract;

/**
 * 小米工厂
 */
public class XiaomiFactory implements AbstractFactory {
    @Override
    public Phone phone() {
        return new XiaomiPhone();
    }

    @Override
    public Compute compute() {
        return new XiaomiCompute();
    }
}
