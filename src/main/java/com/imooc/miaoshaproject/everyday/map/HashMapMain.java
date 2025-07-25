//package com.imooc.miaoshaproject.everyday.map;
//
//import java.io.Serializable;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.AbstractMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//
///**
// * @Author wuqiusheng
// * @Version 1.0
// * @Date 2020/12/29 9:32
// */
//public class HashMap1<K,V> extends AbstractMap<K,V>
//        implements Map<K,V>, Cloneable, Serializable {
//    public HashMap1(float loadFactor) {
//        this.loadFactor = loadFactor;
//    }
//
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        return null;
//    }
//
//    private static final long serialVersionUID = 362498820763181265L;
//
//    /**
//     * 默认初始容量
//     * 必须是2的幂。
//     */
//    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // 1右移4位，值等于 16
//
//    /**
//     * 最大容量2^30
//     * 如果较大的值由两个带参数的构造函数隐式指定时使用
//     * 一定是2的幂，左移相当于乘与2的多少次方
//     */
//    static final int MAXIMUM_CAPACITY = 1 << 30;
//
//    /**
//     * 默认负载因子
//     * 在构造函数中没有指定时，使用的默认负载因子。
//     */
//    static final float DEFAULT_LOAD_FACTOR = 0.75f;
//
//    /**
//     * 一个桶的树化阈值
//     * 当桶中元素个数超过这个值时
//     * 需要使用红黑树节点替换链表节点
//     */
//    static final int TREEIFY_THRESHOLD = 8;
//
//    /**
//     * 一个树的链表还原阈值
//     * 当扩容时，桶中元素个数小于这个值
//     * 就会把树形的桶元素 还原（切分）为链表结构
//     */
//    static final int UNTREEIFY_THRESHOLD = 6;
//
//    /**
//     * 哈希表的最小树形化容量
//     * 当哈希表中的容量大于这个值时，表中的桶才能进行树形化
//     * 否则桶内元素太多时会扩容，而不是树形化
//     * 为了避免进行扩容、树形化选择的冲突，这个值不能小于 4 * TREEIFY_THRESHOLD
//     */
//    static final int MIN_TREEIFY_CAPACITY = 64;
//
//
//    /**
//     * 基本哈希bin节点，用于大多数条目。
//     * (参见下面的TreeNode子类，以及LinkedHashMap中的Entry子类。)
//     */
//    static class Node<K, V> implements Map.Entry<K, V> {
//        final int hash;
//        final K key;
//        V value;
//        HashMap.Node<K, V> next;
//
//        Node(int hash, K key, V value, HashMap.Node<K, V> next) {
//            this.hash = hash;
//            this.key = key;
//            this.value = value;
//            this.next = next;
//        }
//
//        public final K getKey() {
//            return key;
//        }
//
//        public final V getValue() {
//            return value;
//        }
//
//        public final String toString() {
//            return key + "=" + value;
//        }
//
//        public final int hashCode() {
//            return Objects.hashCode(key) ^ Objects.hashCode(value);
//        }
//
//        public final V setValue(V newValue) {
//            V oldValue = value;
//            value = newValue;
//            return oldValue;
//        }
//
//        public final boolean equals(Object o) {
//            if (o == this)
//                return true;
//            if (o instanceof Map.Entry) {
//                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
//                if (Objects.equals(key, e.getKey()) &&
//                        Objects.equals(value, e.getValue()))
//                    return true;
//            }
//            return false;
//        }
//    }
//
//    /* ---------------- Static utilities -------------- */
//
//    /**
//     * h = key.hashCode()得到key的哈希码
//     * h >>> 16 h向右位移16位
//     * ^ 异或运算，让hash值更加均匀
//     */
//    static final int hash(Object key) {
//        int h;
//        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
//    }
//    /* ---------------- Fields -------------- */
//
//    /**
//     * 表，在第一次使用时初始化，并根据需要调整大小。
//     * 当分配长度时，长度总是2的幂
//     * (我们也允许在某些操作中长度为零，以允许当前不需要的引导机制)。
//     */
//    transient HashMap.Node<K, V>[] table;
//
//    /**
//     * 保存缓存entrySet()。请注意，AbstractMap字段用于keyset()和values()。
//     */
//    transient Set<Entry<K, V>> entrySet;
//
//    /**
//     * 键-值对的数量
//     */
//    transient int size;
//
//    /**
//     * HashMap中映射的修改次数，
//     * 此字段用于使HashMap集合视图上的迭代器快速失效。(见ConcurrentModificationException)。
//     */
//    transient int modCount;
//
//    /**
//     * 扩容阈值，要调整大小的下一个大小值(容量*负载因子)。
//     * (javadoc描述在序列化时为真。此外，如果表数组没有被分配，这个字段保存初始数组容量，或者零表示默认的初始容量。)
//     *
//     * @serial
//     */
//    int threshold;
//
//    /**
//     * 哈希表的负载因子
//     *
//     * @serial
//     */
//    final float loadFactor;
//
//    /* ---------------- 公共业务 -------------- */
//
//    /**
//     * 构造一个空的HashMap，具有指定的初始容量和负载因子。
//     * <p>
//     * 初始容量为负或负载因子为非正数，报异常IllegalArgumentException
//     */
//    public HashMap1(int initialCapacity, float loadFactor) {
//        if (initialCapacity < 0)
//            throw new IllegalArgumentException("Illegal initial capacity: " +
//                    initialCapacity);
//        if (initialCapacity > MAXIMUM_CAPACITY)
//            initialCapacity = MAXIMUM_CAPACITY;
//        if (loadFactor <= 0 || Float.isNaN(loadFactor))
//            throw new IllegalArgumentException("Illegal load factor: " +
//                    loadFactor);
//        this.loadFactor = loadFactor;
//        this.threshold = tableSizeFor(initialCapacity);
//    }
//    /**
//     * 返回给定目标容量的2次幂。
//     */
//    static final int tableSizeFor(int cap) {
//        int n = cap - 1;
//        n |= n >>> 1;
//        n |= n >>> 2;
//        n |= n >>> 4;
//        n |= n >>> 8;
//        n |= n >>> 16;
//        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
//    }
//
//    /**
//     * 构造一个空的HashMap，具有指定的初始容量和默认的负载因子(0.75)。
//     */
//    public HashMap1(int initialCapacity) {
//        this(initialCapacity, DEFAULT_LOAD_FACTOR);
//    }
//
//    /**
//     * 构造一个空的HashMap，使用默认的初始容量(16)和默认的负载因子(0.75)。
//     */
//    public HashMap1() {
//        this.loadFactor = DEFAULT_LOAD_FACTOR; // 所有其他字段都默认
//    }
//
//    /* ----------------get方法---------------- */
//
//    /**
//     * get方法
//     * 返回指定键映射到的值，或者null
//     */
//    public V get(Object key) {
//        HashMap.Node<K,V> e;
//        return (e = getNode(hash(key), key)) == null ? null : e.value;
//    }
//    /**
//     * 实现get方法。
//     */
//    final HashMap.Node<K,V> getNode(int hash, Object key) {
//        HashMap.Node<K,V>[] tab;// 表table
//        HashMap.Node<K,V> first, e;
//        int n;// 表长度
//        K k;
//        /**
//         * 如果tab不是空，且n>0,且
//         * &两侧是int，按位与，tab[(n - 1) & hash]
//         */
//        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
//            //如果第一节点的hash=hash,且第一节点的key=key，返回第一节点
//            if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k)))){
//                return first;
//            }
//            //如果有next
//            if ((e = first.next) != null) {
//                //如果first是红黑树类型，使用getTreeNode获取节点
//                if (first instanceof HashMap.TreeNode) {
//                    return ((HashMap.TreeNode<K, V>) first).getTreeNode(hash, key);
//                }
//                do {
//                    //顺着next往下查询，查询到e的hash=hash,key==key,返回e节点
//                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
//                        return e;
//                } while ((e = e.next) != null);
//            }
//        }
//        return null;
//    }
//    /* ----------------put方法---------------- */
//    /**
//     * 将指定的值与映射中的指定键关联。
//     * 如果映射以前包含键的映射，则替换旧的值。
//     */
//    public V put(K key, V value) {
//        return putVal(hash(key), key, value, false, true);
//    }
//    /**
//     * 实现put方法
//     * onlyIfAbsent 如果为真，不要改变现有的值
//     * 如果false，则表处于创建模式
//     */
//    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
//        HashMap.Node<K,V>[] tab;//表table
//        HashMap.Node<K,V> p;
//        int n;//表长度
//        int i;//数组下标
//        /**
//         * 1.判断数组是否为空，为空则扩容数组
//         */
//        //如果表tab=null或表长n=0，
//        if ((tab = table) == null || (n = tab.length) == 0)
//            //表为空，则对表resize()，初始化大小
//            n = (tab = resize()).length;
//        /**
//         * 2.计算这个hash值在数组的下标位置
//         * 获取了当前table数组的最大下标与hash(key)进行按位与操作,
//         * 利用了按位与操作的一个特点,必须两个位都为1,才是1,那么也就是说,如果数组最大下标为15,
//         * 那么不管Hash(key)是多少都不会大于15，也就不会数组越界
//         */
//        p = tab[i = (n - 1) & hash];
//        //p表示在tab数组中存放的节点，（头节点）如果p为空,则创建新节点，
//        /**
//         * 3.如果这个数组的下标位置是空节点，创建新节点并赋值，
//         * 如果不是空，则
//         *      1.判断当前节点是不是要找的节点，如果是，则替换当前值
//         *      2.如果不是，则判断当前节点是不是红黑树节点，如果是，则用putTreeVal查找这个节点，并替换值
//         *      3.如果都不是，说明当前节点是链表节点，则顺着当前节点往下找，找到了，则替换值
//         *      4.如果找不到，则创建新节点并赋值，
//         *      5.如果创建新节点后，当前链表的长度大于树化阈值，则用treeifyBin将链表转化成红黑树
//         */
//        if (p == null)
//            //创建一个新节点
//            tab[i] = newNode(hash, key, value, null);
//        else {
//            //如果p不为空，则替换已有节点
//            HashMap.Node<K,V> e;
//            K k;
//            //如果p的hash=hash且p的key=key，
//            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
//                //把p赋给e
//                e = p;
//            //如果p的类型是红黑树
//            else if (p instanceof HashMap.TreeNode)
//                //则使用红黑树中的putTreeVal方法
//                e = ((HashMap.TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
//            //如果p找不到，则顺着p的next往下查找，
//            else {
//                for (int binCount = 0; ; ++binCount) {
//                    //如果p的next为空
//                    if ((e = p.next) == null) {
//                        //创建新节点
//                        p.next = newNode(hash, key, value, null);
//                        //如果binCount大于桶的树化阈值，使用treeifyBin方法转化成红黑树
//                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
//                            treeifyBin(tab, hash);
//                        //跳出循环
//                        break;
//                    }
//                    //如果找到了
//                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
//                        break;
//                    //p=e，e=原p的next，
//                    p = e;
//                }
//            }
//            //e是查找到的节点
//            if (e != null) {
//                //e的旧值
//                V oldValue = e.value;
//                if (!onlyIfAbsent || oldValue == null)
//                    e.value = value;
//                afterNodeAccess(e);
//                return oldValue;
//            }
//        }
//        //map的修改次数
//        ++modCount;
//        /**
//         * 4.如果map的大小大于扩容阈值，则用resize进行扩容
//         */
//        //map的大小>扩容阈值(容量*负载因子)
//        if (++size > threshold)
//            //进行扩容
//            resize();
//        afterNodeInsertion(evict);
//        return null;
//    }
//    HashMap.Node<K,V> newNode(int hash, K key, V value, HashMap.Node<K,V> next) {
//        return new HashMap.Node<>(hash, key, value, next);
//    }
//    // 允许LinkedHashMap后动作的回调
//    void afterNodeAccess(HashMap.Node<K,V> p) { }
//    void afterNodeInsertion(boolean evict) { }
//    /**
//     * 除非表太小，否则将替换指定哈希的bin索引中的alt链接节点，在这种情况下将调整大小。
//     */
//    final void treeifyBin(HashMap.Node<K,V>[] tab, int hash) {
//        int n, index; HashMap.Node<K,V> e;
//        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
//            resize();
//        else if ((e = tab[index = (n - 1) & hash]) != null) {
//            HashMap.TreeNode<K,V> hd = null, tl = null;
//            do {
//                HashMap.TreeNode<K,V> p = replacementTreeNode(e, null);
//                if (tl == null)
//                    hd = p;
//                else {
//                    p.prev = tl;
//                    tl.next = p;
//                }
//                tl = p;
//            } while ((e = e.next) != null);
//            if ((tab[index] = hd) != null)
//                hd.treeify(tab);
//        }
//    }
//    // For treeifyBin
//    HashMap.TreeNode<K,V> replacementTreeNode(HashMap.Node<K,V> p, HashMap.Node<K,V> next) {
//        return new HashMap.TreeNode<>(p.hash, p.key, p.value, next);
//    }
//    /**
//     * 对表大小进行初始化或双精度化。
//     * 如果为空，则根据字段阈值中持有的初始容量目标进行分配。
//     * 否则，由于使用的是2的幂展开，每个容器中的元素必须要么保持在相同的索引上，要么在新表中以2的幂偏移量移动。
//     */
//    final HashMap.Node<K,V>[] resize() {
//        //旧数组
//        HashMap.Node<K,V>[] oldTab = table;
//        //旧数组容量
//        int oldCap = (oldTab == null) ? 0 : oldTab.length;
//        //旧的扩容阈值，（容量*负载因子)
//        int oldThr = threshold;
//        //新的容量，新的扩容阈值
//        int newCap, newThr = 0;
//        /**
//         * 1.如果旧容量大于0，
//         *      1.如果旧容量大于最大容量2^30，则不进行扩容
//         *      2.将旧数组扩容为两倍，如果新容量扩容两倍后小于最大容量，且旧容量大于初始容量16，设置新扩容阈值为旧的两倍
//         */
//        //如果旧容量大于0
//        if (oldCap > 0) {
//            //如果旧容量大于最大容量2^30
//            if (oldCap >= MAXIMUM_CAPACITY) {
//                //扩容阈值为2^31-1（正负号占一位，0跟0重合，所以减一）
//                threshold = Integer.MAX_VALUE;
//                //返回旧数组，不进行扩容
//                return oldTab;
//            }
//            //新容量设置为旧容量的两倍，
//            //如果新容量<最大容量2^30，且旧容量>=默认初始容量16
//            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
//                //新扩容阈值设置为旧扩容阈值的两倍
//                newThr = oldThr << 1; // double threshold
//        }
//        /**
//         * 2.如果旧容量=0，且旧扩容阈值大于0，设置新容量=旧扩容阈值
//         */
//        //如果旧容量=0，且旧扩容阈值大于0
//        else if (oldThr > 0)
//            //设置新容量=旧扩容阈值
//            newCap = oldThr;
//        /**
//         * 3.如果旧容量=0，且旧扩容阈值=0，设置容量=16，阈值=12
//         */
//        //如果旧容量=0，且旧扩容阈值=0
//        else {
//            //新容量=默认初始容量16
//            newCap = DEFAULT_INITIAL_CAPACITY;
//            //新扩容阈值=默认负载因子*默认初始容量（0.75f*16)=12
//            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//        }
//        /**
//         * 4.如果新扩容阈值=0，设置新扩容阈值
//         */
//        //如果新扩容阈值=0
//        if (newThr == 0) {
//            //ft新容量的扩容阈值=新容量*负载因子
//            float ft = (float)newCap * loadFactor;
//            //如果新容量<最大容量2^30，且ft<最大容量，则新扩容阈值=ft，否则=2^31-1
//            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
//                    (int)ft : Integer.MAX_VALUE);
//        }
//        threshold = newThr;
//        //创建新数组，并设置新容量
//        @SuppressWarnings({"rawtypes","unchecked"})
//        HashMap.Node<K,V>[] newTab = (HashMap.Node<K,V>[])new HashMap.Node[newCap];
//        table = newTab;
//        /**
//         * 1.如果旧数组不为空，从数组下标0到最大下标，循环取出旧数组的节点，
//         *      1.如果当前节点不为空，判断下个节点是不是空
//         *      2.如果下个节点是空，则说明当前节点是最后一个节点，将旧节点赋给新数组
//         *      3.如果下个节点不是空，则判断当前节点是不是红黑树节点，如果是，则使用split方法
//         *      4.如果不是，则表示当前节点是链表节点，将旧链表插入到新链表
//         *
//         */
//        //如果旧数组不为空
//        if (oldTab != null) {
//            //将旧数组的节点取出
//            for (int j = 0; j < oldCap; ++j) {
//                HashMap.Node<K,V> e;
//                //如果当前节点不为空
//                if ((e = oldTab[j]) != null) {
//                    //旧数组置空
//                    oldTab[j] = null;
//                    //当前节点的next为空，说明当前节点是最后一个
//                    if (e.next == null)
//                        //将旧节点重新运算后设置进新数组
//                        newTab[e.hash & (newCap - 1)] = e;
//                    //有下个节点，判断当前节点是不是红黑树节点
//                    else if (e instanceof HashMap.TreeNode)
//                        ((HashMap.TreeNode<K,V>)e).split(this, newTab, j, oldCap);
//                    else { // preserve order
//                        HashMap.Node<K,V> loHead = null, loTail = null;
//                        HashMap.Node<K,V> hiHead = null, hiTail = null;
//                        HashMap.Node<K,V> next;
//                        do {
//                            next = e.next;
//                            // 旧数组利用hash值和oldCap进行与运算，很明显当结果大于0代表hash值大于oldCap时，
//                            // 下标位置变为旧数组的下标j + oldCap；若结果等于0代表小于oldCap,则下标位置不变。
//                            // 相比于JDK1.7重新计算每个元素的哈希值，通过高位运算（e.hash & oldCap）无疑效率更高
//                            if ((e.hash & oldCap) == 0) {
//                                if (loTail == null)
//                                    loHead = e;
//                                else
//                                    loTail.next = e;
//                                loTail = e;
//                            }
//                            else {
//                                if (hiTail == null)
//                                    hiHead = e;
//                                else
//                                    hiTail.next = e;
//                                hiTail = e;
//                            }
//                        } while ((e = next) != null);
//
//                        if (loTail != null) {
//                            loTail.next = null;
//                            newTab[j] = loHead;
//                        }
//                        if (hiTail != null) {
//                            hiTail.next = null;
//                            newTab[j + oldCap] = hiHead;
//                        }
//                    }
//                }
//            }
//        }
//        return newTab;
//    }
//
//
//    /**
//     * 如果x的类形式为"class C implements ComparablekC"，则返回x的类，否则为null。
//     */
//    static Class<?> comparableClassFor(Object x) {
//        if (x instanceof Comparable) {
//            Class<?> c; Type[] ts, as; Type t; ParameterizedType p;
//            if ((c = x.getClass()) == String.class) // bypass checks
//                return c;
//            if ((ts = c.getGenericInterfaces()) != null) {
//                for (int i = 0; i < ts.length; ++i) {
//                    if (((t = ts[i]) instanceof ParameterizedType) &&
//                            ((p = (ParameterizedType)t).getRawType() ==
//                                    Comparable.class) &&
//                            (as = p.getActualTypeArguments()) != null &&
//                            as.length == 1 && as[0] == c) // type arg is c
//                        return c;
//                }
//            }
//        }
//        return null;
//    }
//    /**
//     * 返回k. compareto (x)如果x匹配kc (k的筛选的可比类)，否则e。
//     */
//    @SuppressWarnings({"rawtypes","unchecked"}) // for cast to Comparable
//    static int compareComparables(Class<?> kc, Object k, Object x) {
//        return (x == null || x.getClass() != kc ? 0 :
//                ((Comparable)k).compareTo(x));
//    }
//    /**
//     * Tie-breaking utility for ordering insertions when equal
//     * hashCodes and non-comparable. We don't require a total
//     * order, just a consistent insertion rule to maintain
//     * equivalence across rebalancings. Tie-breaking further than
//     * necessary simplifies testing a bit.
//     */
//    static int tieBreakOrder(Object a, Object b) {
//        int d;
//        if (a == null || b == null ||
//                (d = a.getClass().getName().
//                        compareTo(b.getClass().getName())) == 0)
//            d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
//                    -1 : 1);
//        return d;
//    }
//
//
//
//
//
//    /* ----------------红黑树---------------- */
//
//
//
//    /**
//     * Entry for Tree bins. Extends LinkedHashMap.Entry (which in turn
//     * extends Node) so can be used as extension of either regular or
//     * linked node.
//     */
//    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
//        HashMap.TreeNode<K, V> parent;  // red-black tree links
//        HashMap.TreeNode<K, V> left;
//        HashMap.TreeNode<K, V> right;
//        HashMap.TreeNode<K, V> prev;    // needed to unlink next upon deletion
//        boolean red;
//        TreeNode(int hash, K key, V val, HashMap.Node<K,V> next) {
//            super(hash, key, val, next);
//        }
//        /**
//         * Calls find for root node.
//         */
//        final HashMap.TreeNode<K, V> getTreeNode(int h, Object k) {
//            return ((parent != null) ? root() : this).find(h, k, null);
//        }
//
//        /**
//         * Returns root of tree containing this node.
//         */
//        final HashMap.TreeNode<K, V> root() {
//            for (HashMap.TreeNode<K, V> r = this, p; ; ) {
//                if ((p = r.parent) == null)
//                    return r;
//                r = p;
//            }
//        }
//        /**
//         * Tree version of putVal.
//         */
//        final HashMap.TreeNode<K,V> putTreeVal(HashMap<K,V> map, HashMap.Node<K,V>[] tab,
//                                               int h, K k, V v) {
//            Class<?> kc = null;
//            boolean searched = false;
//            HashMap.TreeNode<K,V> root = (parent != null) ? root() : this;
//            for (HashMap.TreeNode<K,V> p = root;;) {
//                int dir, ph; K pk;
//                if ((ph = p.hash) > h)
//                    dir = -1;
//                else if (ph < h)
//                    dir = 1;
//                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
//                    return p;
//                else if ((kc == null &&
//                        (kc = comparableClassFor(k)) == null) ||
//                        (dir = compareComparables(kc, k, pk)) == 0) {
//                    if (!searched) {
//                        HashMap.TreeNode<K,V> q, ch;
//                        searched = true;
//                        if (((ch = p.left) != null &&
//                                (q = ch.find(h, k, kc)) != null) ||
//                                ((ch = p.right) != null &&
//                                        (q = ch.find(h, k, kc)) != null))
//                            return q;
//                    }
//                    dir = tieBreakOrder(k, pk);
//                }
//
//                HashMap.TreeNode<K,V> xp = p;
//                if ((p = (dir <= 0) ? p.left : p.right) == null) {
//                    HashMap.Node<K,V> xpn = xp.next;
//                    HashMap.TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
//                    if (dir <= 0)
//                        xp.left = x;
//                    else
//                        xp.right = x;
//                    xp.next = x;
//                    x.parent = x.prev = xp;
//                    if (xpn != null)
//                        ((HashMap.TreeNode<K,V>)xpn).prev = x;
//                    //moveRootToFront(tab, balanceInsertion(root, x));详细请看源码
//                    return null;
//                }
//            }
//        }
//        final void split(HashMap<K,V> map, HashMap.Node<K,V>[] tab, int index, int bit) {
//            HashMap.TreeNode<K,V> b = this;
//            // Relink into lo and hi lists, preserving order
//            HashMap.TreeNode<K,V> loHead = null, loTail = null;
//            HashMap.TreeNode<K,V> hiHead = null, hiTail = null;
//            int lc = 0, hc = 0;
//            for (HashMap.TreeNode<K,V> e = b, next; e != null; e = next) {
//                next = (HashMap.TreeNode<K,V>)e.next;
//                e.next = null;
//                if ((e.hash & bit) == 0) {
//                    if ((e.prev = loTail) == null)
//                        loHead = e;
//                    else
//                        loTail.next = e;
//                    loTail = e;
//                    ++lc;
//                }
//                else {
//                    if ((e.prev = hiTail) == null)
//                        hiHead = e;
//                    else
//                        hiTail.next = e;
//                    hiTail = e;
//                    ++hc;
//                }
//            }
//
//            if (loHead != null) {
//                if (lc <= UNTREEIFY_THRESHOLD)
//                    tab[index] = loHead.untreeify(map);
//                else {
//                    tab[index] = loHead;
//                    if (hiHead != null) // (else is already treeified)
//                        loHead.treeify(tab);
//                }
//            }
//            if (hiHead != null) {
//                if (hc <= UNTREEIFY_THRESHOLD)
//                    tab[index + bit] = hiHead.untreeify(map);
//                else {
//                    tab[index + bit] = hiHead;
//                    if (loHead != null)
//                        hiHead.treeify(tab);
//                }
//            }
//        }
//    }
//}
//
//
