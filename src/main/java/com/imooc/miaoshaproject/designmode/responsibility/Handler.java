package com.imooc.miaoshaproject.designmode.responsibility;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/28
 */
public abstract class Handler {
    protected Handler nextHander;

    // 普通方法，不是构造方法
    public void setNextHandler(Handler nextHander) {
        this.nextHander = nextHander;
    }

    public abstract void handleRequest(String request);
}
