package com.imooc.miaoshaproject.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Test {
    //获取ArrayList的容量
    public static int getArrayListCapacity(ArrayList<?> arrayList) {
        Class<ArrayList> arrayListClass = ArrayList.class;
        try {
            Field field = arrayListClass.getDeclaredField("elementData");
            field.setAccessible(true);
            Object[] objects = (Object[])field.get(arrayList);
            return objects.length;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void main(String[] args) {
        ArrayList a = new ArrayList();
        LinkedList c = new LinkedList();
        HashMap d = new HashMap();

        a.add("1");
        System.out.println("a="+getArrayListCapacity(a));

        ArrayList b = new ArrayList(0);
        b.add("1");
        System.out.println("b="+getArrayListCapacity(b));
        String key = "1";
        int h ;
        System.out.println(key.hashCode());
        h = (h = key.hashCode()) ^ (h >>> 16);
        System.out.println(h);
        int z;
        z=49^0;
        System.out.println(z);
    }
}
