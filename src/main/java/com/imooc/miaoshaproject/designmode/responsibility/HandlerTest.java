package com.imooc.miaoshaproject.designmode.responsibility;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/28
 */
public class HandlerTest {

    public static void main(String[] args) {
        Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        handlerA.setNextHandler(handlerB);

        handlerA.handleRequest("HandlerA");
        handlerA.handleRequest("HandlerB");
    }
}
