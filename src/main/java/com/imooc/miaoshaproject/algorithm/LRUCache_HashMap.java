package com.imooc.miaoshaproject.algorithm;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class LRUCache_HashMap{
    int capacity;
    int count;
    Hashtable<Integer, LinkedNode> map = new Hashtable<Integer, LinkedNode>();
    LinkedNode head, tail;

    public LRUCache_HashMap(int capacity){
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
        // 获取map的value
        LinkedNode node = map.get(key);
        if(node == null){
            // 为空，返回-1
            return -1;
        }
        // 把当前节点移动到头部
        this.moveToHead(node);
        // 返回当前节点的值
        return node.value;
    }

    /**
     * 存入节点的值
     * @param key
     * @param value
     */
    public void put(int key, int value){
        // 获取key对应的节点
        LinkedNode node = map.get(key);
        // 不存在key
        if(node == null){
            // 新增节点
            LinkedNode newNode = new LinkedNode();
            newNode.key = key;
            newNode.value = value;

            //
            this.addNode(newNode);
            this.map.put(key, newNode);
            ++count;
            if(count > capacity){
                LinkedNode lastNode = tail.pre;
                removeNode(lastNode);
                this.map.remove(lastNode.key);
                --count;
            }
        // 存在key，直接修改，并将节点移动到头部
        }else{
            node.value = value;
            // 将节点移动到头部
            this.moveToHead(node);
        }
    }

    /**
     * 将节点移动到头部
     * @param node
     */
    public void moveToHead(LinkedNode node){
        // 把前后节点相互指向，移除当前节点
        this.removeNode(node);
        // 把当前节点添加到头部
        this.addNode(node);
    }

    /**
     * 添加节点
     * @param node
     */
    public void addNode(LinkedNode node){
        node.pre = head;
        node.next = head.next;

        head.next.pre = node;
        head.next = node;
    }

    /**
     * 删除节点
     * @param node
     */
    public void removeNode(LinkedNode node){
        // 前节点
        LinkedNode pre = node.pre;
        // 后节点
        LinkedNode next = node.next;
        // 前后节点相互指向
        next.pre = pre;
        pre.next = next;
    }
    /**
     * 节点类
     */
    class LinkedNode{
        int key;
        int value;
        LinkedNode pre;
        LinkedNode next;
    }
}


class LRUTest{
    public static void main(String[] args) {
        LRUCache_HashMap cache = new LRUCache_HashMap(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        System.out.println(cache.get(2));
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));



    }
}
