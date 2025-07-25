package com.imooc.miaoshaproject.collection;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * 双向链表，适合高频插入/删除
 * 适合：消息队列（FIFO），栈（FILO），LRU缓存，数据量波动大（避免频繁ArrayList扩容）
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/6/27
 */
public class LinkedListCopy<E> {

    /**
     * 元素数量
     */
    transient int size = 0;

    /**
     * 修改次数
     */
    protected transient int modCount = 0;

    /**
     * 头节点
     */
    transient LinkedListCopy.Node<E> first;

    /**
     * 尾节点
     */
    transient LinkedListCopy.Node<E> last;

    /**
     * 无参构造
     */
    public LinkedListCopy() {
    }

    private static class Node<E> {
        // 保存的元素
        E item;
        // 指向下一个节点
        LinkedListCopy.Node<E> next;
        // 指向前一个节点
        LinkedListCopy.Node<E> prev;

        Node(LinkedListCopy.Node<E> prev, E element, LinkedListCopy.Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * NOTE 新增
     * @param e
     * @return
     */
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    /**
     * 从尾节点进行添加
     * @param e
     */
    void linkLast(E e) {
        final LinkedListCopy.Node<E> l = last;
        // 创建新节点，设置前置指针，值，后置指针为null
        final LinkedListCopy.Node<E> newNode = new LinkedListCopy.Node<>(l, e, null);
        // 设置新节点为尾节点
        last = newNode;
        // 为空，说明第一次新增，设置新节点是头节点
        if (l == null)
            first = newNode;
        else
            // 设置旧的尾节点的后置指针为新节点
            l.next = newNode;
        size++;
        modCount++;
    }

    /**
     * NOTE 删除
     * @return
     */
    public E remove() {
        return removeFirst();
    }

    /**
     * 删除头节点
     * @return
     */
    public E removeFirst() {
        // 获取头节点
        final LinkedListCopy.Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return unlinkFirst(f);
    }

    /**
     * 删除操尾节点，
     * 把旧的头节点的前后指针置空，值也置空
     * 设置下一节点为头节点
     * @param f 头节点
     * @return
     */
    private E unlinkFirst(LinkedListCopy.Node<E> f) {
        // assert f == first && f != null;
        // 头节点的元素
        final E element = f.item;
        // 头节点的下一节点
        final LinkedListCopy.Node<E> next = f.next;
        f.item = null;
        f.next = null; // help GC
        // 设置头节点为下一节点
        first = next;
        // 为空，说明没有元素了，设置尾节点也为空
        if (next == null)
            last = null;
        else
            next.prev = null;
        size--;
        modCount++;
        return element;
    }

    /**
     * NOTE 修改
     * @param index
     * @param element
     * @return
     */
    public E set(int index, E element) {
        // 下标越界检查
        checkElementIndex(index);
        // 根据下标位置，决定从头，还是尾部开始遍历
        LinkedListCopy.Node<E> x = node(index);
        E oldVal = x.item;
        x.item = element;
        return oldVal;
    }

    /**
     * NOTE 查询
     * 查询头节点的元素
     * 复杂度 O（1）
     * @return
     */
    public E getFirst() {
        final LinkedListCopy.Node<E> f = first;
        if (f == null)
            throw new NoSuchElementException();
        return f.item;
    }

    /**
     * 查询尾节点的元素
     * 复杂度 O（1）
     * @return
     */
    public E getLast() {
        final LinkedListCopy.Node<E> l = last;
        if (l == null)
            throw new NoSuchElementException();
        return l.item;
    }

    /**
     * 根据下标查询
     * 复杂度 O（n）
     * @param index
     * @return
     */
    public E get(int index) {
        // 越界校验
        checkElementIndex(index);
        // 根据下标位置，决定从头，还是尾部开始遍历查询
        return node(index).item;
    }

    /**
     * 下标越界校验
     * @param index
     */
    private void checkElementIndex(int index) {
        if (!isElementIndex(index))
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    private boolean isElementIndex(int index) {
        return index >= 0 && index < size;
    }
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    LinkedListCopy.Node<E> node(int index) {
        // 根据下标位置，决定从头，还是尾部开始遍历
        if (index < (size >> 1)) {
            // 头节点开始遍历
            LinkedListCopy.Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            // 尾节点开始遍历
            LinkedListCopy.Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
}
