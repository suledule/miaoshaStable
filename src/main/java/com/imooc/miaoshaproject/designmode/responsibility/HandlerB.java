package com.imooc.miaoshaproject.designmode.responsibility;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/28
 */
public class HandlerB extends Handler {

    @Override
    public void handleRequest(String request) {
        if ("HandlerB".equals(request)) {
            System.out.println("HandlerB");
        } else {
            nextHander.handleRequest(request);
        }
    }
}
