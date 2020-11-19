package com.imooc.miaoshaproject.spring.spring01.dao;

public class UserDaoMysqlImpl implements UserDao {

    public UserDaoMysqlImpl(){
        System.out.println("UserDaoMysqlImpl的无参构造");
    }

    @Override
    public void getUser() {
        System.out.println("mysql实现");
    }
}
