package com.imooc.miaoshaproject.designmode.template;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/28
 */
public abstract class CookingRecipe {

    // final，防止子类重写
    public final void cook() {
        start();
        cookFood();
        if (addSauce()) {
            addSalt();
        }
        end();
    }

    // 通用方法
    private void start() {
        System.out.println("加热锅");
    }
    // 通用方法
    private void end() {
        System.out.println("装盘");
    }

    // 必须子类实现
    public abstract void cookFood();

    // 勾子，子类可选择实现
    protected boolean addSauce() {
        return true;
    }

    protected void addSalt() {
        System.out.println("加盐");
    }
}
