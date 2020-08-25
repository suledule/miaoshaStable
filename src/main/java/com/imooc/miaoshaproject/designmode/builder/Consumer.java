package com.imooc.miaoshaproject.designmode.builder;

public class Consumer {
    public static void main(String[] args) {
        Worker worker = new Worker();
        Product product = worker.getProduct();
        System.out.println(product);

        Product product1 = worker.builderA("test").getProduct();
        System.out.println(product1);
        new Object();
    }
}
