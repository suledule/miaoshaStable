package com.imooc.miaoshaproject.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stream 流计算（链式编程）
 * 1.ID是偶数
 * 2.年龄大于20
 * 3.用户名转成大写
 * 4.用户名字母倒叙
 * 5.只输出一个用户
 */
public class StreamTest {
    public static void main(String[] args) {
        User user1 = new User(1,"a",18);
        User user2 = new User(2,"b",21);
        User user3 = new User(3,"c",15);
        User user4 = new User(4,"d",26);
        User user5 = new User(5,"e",25);
        //集合用来存储
        List<User> list = Arrays.asList(user1,user2,user3,user4,user5);
        //流用来计算
        list.stream()
                .filter((u)-> {return u.getId()%2==0;})
                .filter((u)-> {return u.getAge() > 20;})
                .map((u)-> {return u.getName().toUpperCase();})
                .sorted((uu1,uu2)->{return uu2.compareTo(uu1);})
                .limit(1)
                .forEach(System.out::println);
    }
}

class User{
    private int id;
    private String name;
    private int age;
    public User(int id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
