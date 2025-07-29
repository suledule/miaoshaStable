package com.imooc.miaoshaproject.designmode.template;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/28
 */
public class CookingTest {

    public static void main(String[] args) {
        CookingRecipe cookingRecipe1 = new CookingTomato();
        cookingRecipe1.cook();
        System.out.println("======");

        CookingRecipe cookingRecipe2 = new CookingChicken();
        cookingRecipe2.cook();
    }
}
