package com.imooc.miaoshaproject.thread;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**函数式接口都可以用lambda表达式简化
 * Function 函数式接口,有输入值和输出值
 * Predicate 断定型接口，有输入值，输出值为 boolean
 * Consumer 消费型接口
 * Supplier 供给型接口
 */
public class FunctionTest {
    public static void main(String[] args) {

        //函数式接口
        Function<String, String> function = new Function<String, String>() {
            @Override
            public String apply(String str) {
                return str;
            }
        };
        //lambda表达式（简化代码）
        Function<String, String> function1 = (str)->{
            return str;
        };
        System.out.println(function.apply("abc"));

        //断定型接口
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String str) {
                return str.isEmpty();
            }
        };
        //lambda表达式（简化代码）
        Predicate<String> predicate1 = (str)->{
            return str.isEmpty();
        };

        System.out.println(predicate.test("abc"));


        //消费型接口
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String str) {
                System.out.println(str);
            }
        };
        //简化
        Consumer<String> consumer1 = (str)->{
            System.out.println(str);
        };

        //供给型接口
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "YES";
            }
        };
        //简化
        Supplier<String> supplier1 = ()->{
            return "YES";
        };
    }
}
