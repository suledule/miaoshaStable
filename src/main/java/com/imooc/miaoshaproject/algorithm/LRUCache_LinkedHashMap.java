package com.imooc.miaoshaproject.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU算法：当缓存满了，最久没使用的数据会被淘汰
 */
public class LRUCache_LinkedHashMap {
    int capacity;
    Map<Integer ,Integer> map;

    public LRUCache_LinkedHashMap(int capacity){
        this.capacity = capacity;
        map = new LinkedHashMap<>();
    }

    public int get(int key){
        if(!map.containsKey(key)){
            return -1;
        }
        Integer value = map.remove(key);
        map.put(key, value);
        return value;
    }

    public void put(int key, int value){
        if(map.containsKey(key)){
            map.remove(key);
            map.put(key,value);
        }else {
            map.put(key,value);
            if(map.size() > capacity){
                map.remove(map.entrySet().iterator().next().getKey());
            }
        }
    }

    public static void main(String[] args) {
        LRUCache_LinkedHashMap lru = new LRUCache_LinkedHashMap(3);
        lru.put(1,1);
        lru.put(2,2);
        lru.put(3,3);
        lru.put(4,4);
        System.out.println(lru.get(1));
        System.out.println(lru.get(2));
        System.out.println(lru.get(3));
        System.out.println(lru.get(4));
    }
}
