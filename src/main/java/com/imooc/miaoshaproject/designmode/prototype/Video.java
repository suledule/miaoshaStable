package com.imooc.miaoshaproject.designmode.prototype;

import java.util.Date;

/**
 * 原型模式
 * 浅克隆：操作内存克隆一个一样的对象，包括对象的引用（引用的数据是共用的）
 * 深克隆：将对象的引用也克隆一份，引用的数据不共用
 */
public class Video implements Cloneable {
    private String name;
    private Date createDate;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        //浅克隆
//        return super.clone();
        //深克隆
        Object object = super.clone();
        Video v = (Video) object;
        v.createDate = (Date) this.createDate.clone();
        return object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Video{" +
                "name='" + name + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
