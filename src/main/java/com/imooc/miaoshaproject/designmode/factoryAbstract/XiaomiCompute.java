package com.imooc.miaoshaproject.designmode.factoryAbstract;

public class XiaomiCompute implements Compute {
    @Override
    public void start() {
        System.out.println("小米电脑开机");
    }

    @Override
    public void shutdown() {
        System.out.println("小米电脑关机");
    }
}
