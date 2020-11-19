package com.imooc.miaoshaproject.spring.spring01;

import com.imooc.miaoshaproject.spring.spring01.dao.UserDaoMysqlImpl;
import com.imooc.miaoshaproject.spring.spring01.service.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {

        /**
         * 一般的创建对象的方式
         */
//        UserServiceImpl userService = new UserServiceImpl();
//        userService.getUser();


        /**
         * 使用 setUserDao 的方式
         */
//        UserServiceImpl userService = new UserServiceImpl();
//        //想创建什么对象，由用户决定
//        userService.setUserDao(new UserDaoMysqlImpl());
//        userService.getUser();

        /**
         * 使用IOC容器创建对象
         * 创建方式：
         * 1.使用无参构造方法创建
         */
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserServiceImpl userServiceImpl = (UserServiceImpl) context.getBean("userServiceImpl");
        userServiceImpl.getUser();
    }
}
