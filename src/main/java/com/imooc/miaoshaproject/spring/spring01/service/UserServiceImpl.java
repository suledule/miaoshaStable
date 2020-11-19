package com.imooc.miaoshaproject.spring.spring01.service;

import com.imooc.miaoshaproject.spring.spring01.dao.UserDao;
import com.imooc.miaoshaproject.spring.spring01.dao.UserDaoImpl;
import com.imooc.miaoshaproject.spring.spring01.dao.UserDaoMysqlImpl;

import javax.annotation.Resource;

public class UserServiceImpl implements UserService {

    /**
     * 一般的创建对象的方式
     * 缺点：如果业务发生修改，必须修改该对象的创建
     */
    //private UserDao userDao = new UserDaoMysqlImpl();

    /**
     * 使用 setUserDao 的方式，
     * 优点：想创建什么对象，由用户决定
     */
    @Resource
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void getUser() {
        userDao.getUser();
    }
}
