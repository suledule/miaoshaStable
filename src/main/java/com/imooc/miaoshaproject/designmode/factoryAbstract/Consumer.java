package com.imooc.miaoshaproject.designmode.factoryAbstract;

public class Consumer {
    public static void main(String[] args) {

        XiaomiFactory xiaomiFactory = new XiaomiFactory();
        xiaomiFactory.phone().call();
    }
}
