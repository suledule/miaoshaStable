package com.imooc.miaoshaproject.comment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 注解
 */

public class commentTest {

    //如果没有默认值，则必须加值
    @MyTest(age=10)
    public void Test(){

    }

}

@Target(value={ElementType.TYPE,ElementType.METHOD})//此注解可以作用的范围，TYPE表示类，METHOD表示方法
@Retention(RetentionPolicy.RUNTIME)//注解的生命周期，RUNTIME：不仅被保存到class文件中，jvm加载class文件之后，仍然存在
@interface MyTest{ //interface:自定义注解
    //注解参数  参数类型 +参数名 default 默认值
    String name() default "";
    int age();
    int mobile() default -1;//-1表示不存在
    String[] school() default {"北京","上海"};

}
