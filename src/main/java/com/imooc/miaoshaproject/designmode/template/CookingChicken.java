package com.imooc.miaoshaproject.designmode.template;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/28
 */
public class CookingChicken extends CookingRecipe {
    @Override
    public void cookFood() {
        System.out.println("炒鸡肉的步骤");
    }

    @Override
    protected boolean addSauce() {
        return false;
    }
}
