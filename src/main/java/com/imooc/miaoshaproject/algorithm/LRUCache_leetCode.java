package com.imooc.miaoshaproject.algorithm;

import java.util.Hashtable;

public class LRUCache_leetCode {
    int capacity;
    int count;
    Hashtable<Integer, LinkedNode> map = new Hashtable<Integer, LinkedNode>();
    LinkedNode head, tail;

    public LRUCache_leetCode(int capacity){
        this.capacity = capacity;
        this.count = 0;

        //创建头节点和尾节点
        head = new LinkedNode();
        tail = new LinkedNode();

        head.pre = null;
        head.next = tail;

        tail.pre = head;
        tail.next = null;
    }

    /**
     * 获取节点的值
     * @param key
     * @return
     */
    public int get(int key){
        LinkedNode node = map.get(key);
        if(node == null){
            return -1;
        }
        LinkedNode pre = node.pre;
        LinkedNode next = node.next;
        next.pre = pre;
        pre.next = next;
        System.out.println(node.value);
        return node.value;
    }

    /**
     * 存入节点的值
     * @param key
     * @param value
     */
    public void put(int key, int value){
        LinkedNode node = map.get(key);
        if(node == null){
            LinkedNode newNode = new LinkedNode();
            newNode.key = key;
            newNode.value = value;
            newNode.pre = head;
            newNode.next = head.next;

            head.next.pre = newNode;
            head.next = newNode;
            this.map.put(key, newNode);
            ++count;
            if(count > capacity){
                LinkedNode lastNode = tail.pre;
                System.out.println(lastNode);
                LinkedNode pre = lastNode.pre;
                LinkedNode next = lastNode.next;
                next.pre = pre;
                pre.next = next;
                this.map.remove(lastNode.key);
                --count;
            }
        }else{
            node.value = value;
            node.pre = head;
            node.next = head.next;
            head.next.pre = node;
            head.next = node;
            LinkedNode pre = node.pre;
            LinkedNode next = node.next;
            next.pre = pre;
            pre.next = next;
        }
    }

    class LinkedNode{
        int key;
        int value;
        LinkedNode pre;
        LinkedNode next;
    }
}


class LRUTest2{
    public static void main(String[] args) {
        LRUCache_leetCode cache = new LRUCache_leetCode(3);
//        cache.put(1,1);
////        cache.put(2,2);
////        cache.put(3,3);
////        cache.put(4,4);
////
////        System.out.println(cache.get(1));
////        System.out.println(cache.get(2));
////        System.out.println(cache.get(3));
////        System.out.println(cache.get(4));
        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);       // 返回  1
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        cache.get(2);       // 返回 -1 (未找到)
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        cache.get(1);       // 返回 -1 (未找到)
        cache.get(3);       // 返回  3
        cache.get(4);       // 返回  4
    }
}
