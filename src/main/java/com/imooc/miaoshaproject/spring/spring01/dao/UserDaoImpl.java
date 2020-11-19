package com.imooc.miaoshaproject.spring.spring01.dao;

public class UserDaoImpl implements UserDao {

    public UserDaoImpl(){
        System.out.println("UserDaoImpl的无参构造");
    }
    @Override
    public void getUser() {
        System.out.println("默认实现");
    }
}
