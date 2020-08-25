package com.imooc.miaoshaproject.designmode.prototype;

import java.util.Date;

public class Test {
    static int a = 10;
    public static void main(String[] args) throws CloneNotSupportedException {
        //原型
        Date date = new Date();
        Video video = new Video();
        video.setName("aaa");
        video.setCreateDate(date);

        //克隆
        Video video1 = (Video) video.clone();

        System.out.println(video);
        System.out.println(video1);

        //改变引用的数据
        date.setTime(111111111);

        System.out.println(video);
        System.out.println(video1);




    }
}
