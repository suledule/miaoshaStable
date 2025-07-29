package com.imooc.miaoshaproject.designmode.responsibility;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/28
 */
public class HandlerA extends Handler {

    @Override
    public void handleRequest(String request) {
        if ("HandlerA".equals(request)) {
            System.out.println("HandlerA");
        } else {
            nextHander.handleRequest(request);
        }
    }
}
