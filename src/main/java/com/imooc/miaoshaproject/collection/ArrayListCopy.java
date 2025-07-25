package com.imooc.miaoshaproject.collection;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 动态数组，适合高频随机访问
 * 数据展示、搜索、按索引批量处理，数组连续内存布局，CPU缓存命中率高
 * 内存敏感：存储百万级数据时，比LinkedList节省约33%内存（无指针开销）
 * 日志追加、流式数据处理：尾部添加均摊O(1)
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/6/27
 */
public class ArrayListCopy<E> {

    /**
     * 空元素数组
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     * 默认容量的空元素数组，无参构造使用
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * 非私有以简化嵌套类访问
     */
    transient Object[] elementData; // non-private to simplify nested class access

    /**
     * 当前数组元素个数
     */
    private int size;

    /**
     * 默认容量
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 修改次数
     */
    protected transient int modCount = 0;

    /**
     * 要分配的最大数组大小，-8是因为一些虚拟机在数组中保留一些头字，尝试分配更大的数组可能会导致OutOfMemoryError
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 无参构造方法
     */
    public ArrayListCopy() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 有参构造方法
     * @param initialCapacity 初始容量
     */
    public ArrayListCopy(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    /**
     * NOTE 新增
     * 不扩容时：复杂度 O（1），按下标设置
     * 扩容时：复杂度 O（n），Arrays.copyOf()复制旧数组到新数组
     * @param e
     * @return
     */
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        // 设置值到对应下标
        elementData[size++] = e;
        return true;
    }

    /**
     * 确保容量足够
     * @param minCapacity 允许的最小容量
     */
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    /**
     * 计算容量
     * @param elementData
     * @param minCapacity
     * @return
     */
    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        // 无参构造的，会返回默认容量10
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            // 新增第一个元素，minCapacity=1，如果是无参构造，则返回10的容量
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        // 有参构造，返回允许的最小容量
        return minCapacity;
    }

    /**
     * 确保显式容量
     * @param minCapacity 允许的最小容量
     */
    private void ensureExplicitCapacity(int minCapacity) {
        // 修改次数+1
        modCount++;

        // 判断是否容量不够，容量不够会溢出，需要扩容
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * 扩容数组，扩容1.5倍
     * 边界处理：最小最大处理
     * @param minCapacity 允许的最小容量
     */
    private void grow(int minCapacity) {
        // 老数组容量
        int oldCapacity = elementData.length;
        // 新数组容量，扩容1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);

        // 边界处理
        // 例子：当1.5倍扩容仍不足时（如旧容量=1，计算新容量=1.5→1，但minCapacity=2）
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        // 若需求超出MAX_ARRAY_SIZE，直接扩容至Integer.MAX_VALUE（硬极限）
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);

        // minCapacity通常接近size，所以这是一个胜利
        // 基于System.arraycopy()的底层优化，提升效率
        // 旧数组失去引用，等待GC回收（可能引发GC压力）
        elementData = Arrays.copyOf(elementData, newCapacity);
    }



    /**
     * 大容量
     * 优先返回 MAX_ARRAY_SIZE = 2147483639
     * 容量还是不够则返回 MAX_VALUE = 2147483647
     * @param minCapacity
     * @return
     */
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        // 返回数组最大值，相差只是8，是因为一些虚拟机在数组中保留一些头字，尝试分配更大的数组可能会导致OutOfMemoryError
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    /**
     * 复制
     * @param original 原有数组
     * @param newLength 新的容量大小
     * @return
     * @param <T>
     */
    public static <T> T[] copyOf(T[] original, int newLength) {
        return (T[]) copyOf(original, newLength, original.getClass());
    }

    /**
     * <T, U>：声明两个独立的泛型类型，U 表示源数组元素类型，T 表示目标数组元素类型
     * 设计意图：支持类型转换（如父类数组复制为子类数组），同时保持类型安全
     * @param original
     * @param newLength
     * @param newType 新的类型
     * @return
     * @param <T>
     * @param <U>
     */
    public static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        // getComponentType:是Java反射机制中的一个方法，主要用于获取数组类型的组件类型
        // Array.newInstance:是Java反射API中用于动态创建数组的静态方法，支持一维或多维数组的创建，其返回类型为Object，需强制转换为目标数组类型
        //若目标类型为 Object[]，直接 new Object[newLength]，避免反射开销，提升性能
        //否则通过 Array.newInstance() 根据组件类型（getComponentType()）创建具体类型的数组（如 String[]）
        T[] copy = ((Object)newType == (Object)Object[].class)
                ? (T[]) new Object[newLength]
                : (T[]) Array.newInstance(newType.getComponentType(), newLength);

        // 调用 System.arraycopy() 本地方法（JVM 底层优化），比循环复制效率更高
        System.arraycopy(original, 0, copy, 0,
                Math.min(original.length, newLength));
        return copy;
    }

    /**
     * NOTE 删除
     * 根据下标删除
     * 删除最后一个元素，复杂度 O（1）
     * 不是删除最后一个元素，复杂度 O（n），删除需要移动后面的元素
     * @param index
     * @return
     */
    public E remove(int index) {
        // 越界检查
        rangeCheck(index);

        modCount++;
        // 下标查询旧的值
        E oldValue = elementData(index);

        // 元素个数 - 下标 - 1
        int numMoved = size - index - 1;
        // 判断是否是最后一个元素，是则不需要移动元素
        if (numMoved > 0)
            // 参数：src -源数组。srcPos -源数组的起始位置。Dest -目标数组。destPos -目标数据的起始位置。长度-要复制的数组元素的数量。
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        // 最后一个值设置为空，让收集器处理
        elementData[--size] = null; // clear to let GC do its work

        return oldValue;
    }

    /**
     * NOTE 修改
     * 根据下标修改
     * 复杂度 O（1）
     * @param index
     * @param element
     * @return
     */
    public E set(int index, E element) {
        // 越界检查
        rangeCheck(index);
        // 根据下标，查询旧的值
        E oldValue = elementData(index);
        // 设置新的值
        elementData[index] = element;
        return oldValue;
    }

    /**
     * NOTE 查询
     * 根据下标，查询元素
     * 复杂度 O（1）
     * 按值查询，则是 O（n），需要遍历整个数组
     * @param index
     * @return
     */
    public E get(int index) {
        // 判断数组越界
        rangeCheck(index);

        return elementData(index);
    }

    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    /**
     * 根据下标，查询元素
     * 复杂度 O（1）
     * @param index
     * @return
     */
    E elementData(int index) {
        return (E) elementData[index];
    }

    public static void main(String[] args) throws Exception {
        ArrayList arrayList = new ArrayList<>(0);
        arrayList.add("1");
        Field field = ArrayList.class.getDeclaredField("elementData");
        field.setAccessible(true);
        Object[] array = (Object[]) field.get(arrayList);

        System.out.println(array.length);
    }
}
