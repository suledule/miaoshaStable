package com.imooc.miaoshaproject.designmode;

/**
 * 枚举
 */
public enum SingleEnum {
    INSTANCE;

    private SingleEnum(){
        System.out.println(Thread.currentThread().getName()+"==>");
    }

    public static SingleEnum getInstance(){
        return INSTANCE;
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                SingleEnum.getInstance();
            }).start();
        }
        //Constructor<EnumSingle> constructor = EnumSingle.class.getDeclaredConstructor(null);
        //有参构造（使用反射破坏枚举会报错）
//        Constructor<EnumSingle> constructor = EnumSingle.class.getDeclaredConstructor(String.class,int.class);
//        constructor.setAccessible(true);
//        System.out.println(constructor.newInstance());
//        System.out.println(constructor.newInstance());
    }
}
