//package com.imooc.miaoshaproject.everyday.map;
//
///**
// * @Author wuqiusheng
// * @Version 1.0
// * @Date 2020/12/23 11:46
// */
//
//import java.io.IOException;
//import java.io.InvalidObjectException;
//import java.io.Serializable;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.*;
//import java.util.function.BiConsumer;
//import java.util.function.BiFunction;
//import java.util.function.Consumer;
//import java.util.function.Function;
//import sun.misc.SharedSecrets;
//
//public class HashMap<K,V> extends AbstractMap<K,V>
//        implements Map<K,V>, Cloneable, Serializable {
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
//     * 最大容量
//     * 如果较大的值由两个带参数的构造函数隐式指定时使用
//     * 一定是2的幂
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
//    /**
//     * 基本哈希bin节点，用于大多数条目。
//     * (参见下面的TreeNode子类，以及LinkedHashMap中的Entry子类。)
//     */
//    static class Node<K,V> implements Map.Entry<K,V> {
//        final int hash;
//        final K key;
//        V value;
//        HashMap.Node<K,V> next;
//
//        Node(int hash, K key, V value, HashMap.Node<K,V> next) {
//            this.hash = hash;
//            this.key = key;
//            this.value = value;
//            this.next = next;
//        }
//
//        public final K getKey()        { return key; }
//        public final V getValue()      { return value; }
//        public final String toString() { return key + "=" + value; }
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
//                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
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
//
//    /**
//     * 返回k. compareto (x)如果x匹配kc (k的筛选的可比类)，否则e。
//     */
//    @SuppressWarnings({"rawtypes","unchecked"}) // for cast to Comparable
//    static int compareComparables(Class<?> kc, Object k, Object x) {
//        return (x == null || x.getClass() != kc ? 0 :
//                ((Comparable)k).compareTo(x));
//    }
//
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
//    /* ---------------- Fields -------------- */
//
//    /**
//     * 表，在第一次使用时初始化，并根据需要调整大小。
//     * 当分配长度时，长度总是2的幂
//     * (我们也允许在某些操作中长度为零，以允许当前不需要的引导机制)。
//     */
//    transient HashMap.Node<K,V>[] table;
//
//    /**
//     * 保存缓存entrySet()。请注意，AbstractMap字段用于keyset()和values()。
//     */
//    transient Set<Map.Entry<K,V>> entrySet;
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
//     * 要调整大小的下一个大小值(容量*负载因子)。
//     * (javadoc描述在序列化时为真。此外，如果表数组没有被分配，这个字段保存初始数组容量，或者零表示默认的初始容量。)
//     * @serial
//     */
//    int threshold;
//
//    /**
//     * 哈希表的负载因子
//     * @serial
//     */
//    final float loadFactor;
//
//    /* ---------------- 公共业务 -------------- */
//
//    /**
//     * 构造一个空的HashMap，具有指定的初始容量和负载因子。
//     *
//     * 初始容量为负或负载因子为非正数，报异常IllegalArgumentException
//     */
//    public HashMap(int initialCapacity, float loadFactor) {
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
//
//    /**
//     * 构造一个空的HashMap，具有指定的初始容量和默认的负载因子(0.75)。
//     */
//    public HashMap(int initialCapacity) {
//        this(initialCapacity, DEFAULT_LOAD_FACTOR);
//    }
//
//    /**
//     * 构造一个空的HashMap，使用默认的初始容量(16)和默认的负载因子(0.75)。
//     */
//    public HashMap() {
//        this.loadFactor = DEFAULT_LOAD_FACTOR; // 所有其他字段都默认
//    }
//
//    /**
//     * 构造一个新的HashMap，具有与指定的Map相同的映射。
//     * HashMap是用默认负载因子(0.75)和初始容量创建的，
//     * 初始容量足以容纳指定的映射中的映射。
//     */
//    public HashMap(Map<? extends K, ? extends V> m) {
//        this.loadFactor = DEFAULT_LOAD_FACTOR;
//        putMapEntries(m, false);
//    }
//
//    /**
//     * 实现Map。putALL和Map构造器。
//     */
//    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
//        int s = m.size();
//        if (s > 0) {
//            if (table == null) { // pre-size
//                float ft = ((float)s / loadFactor) + 1.0F;
//                int t = ((ft < (float)MAXIMUM_CAPACITY) ?
//                        (int)ft : MAXIMUM_CAPACITY);
//                if (t > threshold)
//                    threshold = tableSizeFor(t);
//            }
//            else if (s > threshold)
//                resize();
//            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
//                K key = e.getKey();
//                V value = e.getValue();
//                putVal(hash(key), key, value, false, evict);
//            }
//        }
//    }
//
//    /**
//     * 返回此映射中的键-值对的数量
//     */
//    public int size() {
//        return size;
//    }
//
//    /**
//     * 如果该映射键值对为0，则返回true
//     */
//    public boolean isEmpty() {
//        return size == 0;
//    }
//
//    /**
//     * 返回指定键映射到的值，或者null
//     */
//    public V get(Object key) {
//        HashMap.Node<K,V> e;
//        return (e = getNode(hash(key), key)) == null ? null : e.value;
//    }
//
//    /**
//     * 实现了Map,get及相关方法。
//     */
//    final HashMap.Node<K,V> getNode(int hash, Object key) {
//        HashMap.Node<K,V>[] tab;
//        HashMap.Node<K,V> first, e;
//        int n;
//        K k;
//        if ((tab = table) != null && (n = tab.length) > 0 &&
//                (first = tab[(n - 1) & hash]) != null) {
//            if (first.hash == hash && // always check first node
//                    ((k = first.key) == key || (key != null && key.equals(k))))
//                return first;
//            if ((e = first.next) != null) {
//                if (first instanceof HashMap.TreeNode)
//                    return ((HashMap.TreeNode<K,V>)first).getTreeNode(hash, key);
//                do {
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k))))
//                        return e;
//                } while ((e = e.next) != null);
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 如果map包含指定key的映射,返回true
//     */
//    public boolean containsKey(Object key) {
//        return getNode(hash(key), key) != null;
//    }
//
//    /**
//     * 将指定的值与映射中的指定键关联。
//     * 如果映射以前包含键的映射，则替换旧的值。
//     */
//    public V put(K key, V value) {
//        return putVal(hash(key), key, value, false, true);
//    }
//
//    /**
//     * 实现put方法
//     * onlyIfAbsent 如果为真，不要改变现有的值
//     * 如果false，则表处于创建模式
//     */
//    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
//                   boolean evict) {
//        HashMap.Node<K,V>[] tab;
//        HashMap.Node<K,V> p;
//        int n, i;
//        if ((tab = table) == null || (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        if ((p = tab[i = (n - 1) & hash]) == null)
//            tab[i] = newNode(hash, key, value, null);
//        else {
//            HashMap.Node<K,V> e; K k;
//            if (p.hash == hash &&
//                    ((k = p.key) == key || (key != null && key.equals(k))))
//                e = p;
//            else if (p instanceof HashMap.TreeNode)
//                e = ((HashMap.TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
//            else {
//                for (int binCount = 0; ; ++binCount) {
//                    if ((e = p.next) == null) {
//                        p.next = newNode(hash, key, value, null);
//                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
//                            treeifyBin(tab, hash);
//                        break;
//                    }
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k))))
//                        break;
//                    p = e;
//                }
//            }
//            if (e != null) { // existing mapping for key
//                V oldValue = e.value;
//                if (!onlyIfAbsent || oldValue == null)
//                    e.value = value;
//                afterNodeAccess(e);
//                return oldValue;
//            }
//        }
//        ++modCount;
//        if (++size > threshold)
//            resize();
//        afterNodeInsertion(evict);
//        return null;
//    }
//
//    /**
//     * 对表大小进行初始化或双精度化。
//     * 如果为空，则根据字段阈值中持有的初始容量目标进行分配。
//     * 否则，由于使用的是2的幂展开，每个容器中的元素必须要么保持在相同的索引上，要么在新表中以2的幂偏移量移动。
//     */
//    final HashMap.Node<K,V>[] resize() {
//        HashMap.Node<K,V>[] oldTab = table;
//        int oldCap = (oldTab == null) ? 0 : oldTab.length;
//        int oldThr = threshold;
//        int newCap, newThr = 0;
//        if (oldCap > 0) {
//            if (oldCap >= MAXIMUM_CAPACITY) {
//                threshold = Integer.MAX_VALUE;
//                return oldTab;
//            }
//            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
//                    oldCap >= DEFAULT_INITIAL_CAPACITY)
//                newThr = oldThr << 1; // double threshold
//        }
//        else if (oldThr > 0) // initial capacity was placed in threshold
//            newCap = oldThr;
//        else {               // zero initial threshold signifies using defaults
//            newCap = DEFAULT_INITIAL_CAPACITY;
//            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//        }
//        if (newThr == 0) {
//            float ft = (float)newCap * loadFactor;
//            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
//                    (int)ft : Integer.MAX_VALUE);
//        }
//        threshold = newThr;
//        @SuppressWarnings({"rawtypes","unchecked"})
//        HashMap.Node<K,V>[] newTab = (HashMap.Node<K,V>[])new HashMap.Node[newCap];
//        table = newTab;
//        if (oldTab != null) {
//            for (int j = 0; j < oldCap; ++j) {
//                HashMap.Node<K,V> e;
//                if ((e = oldTab[j]) != null) {
//                    oldTab[j] = null;
//                    if (e.next == null)
//                        newTab[e.hash & (newCap - 1)] = e;
//                    else if (e instanceof HashMap.TreeNode)
//                        ((HashMap.TreeNode<K,V>)e).split(this, newTab, j, oldCap);
//                    else { // preserve order
//                        HashMap.Node<K,V> loHead = null, loTail = null;
//                        HashMap.Node<K,V> hiHead = null, hiTail = null;
//                        HashMap.Node<K,V> next;
//                        do {
//                            next = e.next;
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
//    /**
//     * Replaces all linked nodes in bin at index for given hash unless
//     * table is too small, in which case resizes instead.
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
//
//    /**
//     * Copies all of the mappings from the specified map to this map.
//     * These mappings will replace any mappings that this map had for
//     * any of the keys currently in the specified map.
//     *
//     * @param m mappings to be stored in this map
//     * @throws NullPointerException if the specified map is null
//     */
//    public void putAll(Map<? extends K, ? extends V> m) {
//        putMapEntries(m, true);
//    }
//
//    /**
//     * Removes the mapping for the specified key from this map if present.
//     *
//     * @param  key key whose mapping is to be removed from the map
//     * @return the previous value associated with <tt>key</tt>, or
//     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
//     *         (A <tt>null</tt> return can also indicate that the map
//     *         previously associated <tt>null</tt> with <tt>key</tt>.)
//     */
//    public V remove(Object key) {
//        HashMap.Node<K,V> e;
//        return (e = removeNode(hash(key), key, null, false, true)) == null ?
//                null : e.value;
//    }
//
//    /**
//     * Implements Map.remove and related methods.
//     *
//     * @param hash hash for key
//     * @param key the key
//     * @param value the value to match if matchValue, else ignored
//     * @param matchValue if true only remove if value is equal
//     * @param movable if false do not move other nodes while removing
//     * @return the node, or null if none
//     */
//    final HashMap.Node<K,V> removeNode(int hash, Object key, Object value,
//                                                 boolean matchValue, boolean movable) {
//        HashMap.Node<K,V>[] tab; HashMap.Node<K,V> p; int n, index;
//        if ((tab = table) != null && (n = tab.length) > 0 &&
//                (p = tab[index = (n - 1) & hash]) != null) {
//            HashMap.Node<K,V> node = null, e; K k; V v;
//            if (p.hash == hash &&
//                    ((k = p.key) == key || (key != null && key.equals(k))))
//                node = p;
//            else if ((e = p.next) != null) {
//                if (p instanceof HashMap.TreeNode)
//                    node = ((HashMap.TreeNode<K,V>)p).getTreeNode(hash, key);
//                else {
//                    do {
//                        if (e.hash == hash &&
//                                ((k = e.key) == key ||
//                                        (key != null && key.equals(k)))) {
//                            node = e;
//                            break;
//                        }
//                        p = e;
//                    } while ((e = e.next) != null);
//                }
//            }
//            if (node != null && (!matchValue || (v = node.value) == value ||
//                    (value != null && value.equals(v)))) {
//                if (node instanceof HashMap.TreeNode)
//                    ((HashMap.TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
//                else if (node == p)
//                    tab[index] = node.next;
//                else
//                    p.next = node.next;
//                ++modCount;
//                --size;
//                afterNodeRemoval(node);
//                return node;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Removes all of the mappings from this map.
//     * The map will be empty after this call returns.
//     */
//    public void clear() {
//        HashMap.Node<K,V>[] tab;
//        modCount++;
//        if ((tab = table) != null && size > 0) {
//            size = 0;
//            for (int i = 0; i < tab.length; ++i)
//                tab[i] = null;
//        }
//    }
//
//    /**
//     * Returns <tt>true</tt> if this map maps one or more keys to the
//     * specified value.
//     *
//     * @param value value whose presence in this map is to be tested
//     * @return <tt>true</tt> if this map maps one or more keys to the
//     *         specified value
//     */
//    public boolean containsValue(Object value) {
//        HashMap.Node<K,V>[] tab; V v;
//        if ((tab = table) != null && size > 0) {
//            for (int i = 0; i < tab.length; ++i) {
//                for (HashMap.Node<K,V> e = tab[i]; e != null; e = e.next) {
//                    if ((v = e.value) == value ||
//                            (value != null && value.equals(v)))
//                        return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Returns a {@link Set} view of the keys contained in this map.
//     * The set is backed by the map, so changes to the map are
//     * reflected in the set, and vice-versa.  If the map is modified
//     * while an iteration over the set is in progress (except through
//     * the iterator's own <tt>remove</tt> operation), the results of
//     * the iteration are undefined.  The set supports element removal,
//     * which removes the corresponding mapping from the map, via the
//     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
//     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
//     * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
//     * operations.
//     *
//     * @return a set view of the keys contained in this map
//     */
//
//
//    final class KeySet extends AbstractSet<K> {
//        public final int size()                 { return size; }
//        public final void clear()               { HashMap.this.clear(); }
//        public final Iterator<K> iterator()     { return new HashMap.KeyIterator(); }
//        public final boolean contains(Object o) { return containsKey(o); }
//        public final boolean remove(Object key) {
//            return removeNode(hash(key), key, null, false, true) != null;
//        }
//        public final Spliterator<K> spliterator() {
//            return new HashMap.KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
//        }
//        public final void forEach(Consumer<? super K> action) {
//            HashMap.Node<K,V>[] tab;
//            if (action == null)
//                throw new NullPointerException();
//            if (size > 0 && (tab = table) != null) {
//                int mc = modCount;
//                for (int i = 0; i < tab.length; ++i) {
//                    for (HashMap.Node<K,V> e = tab[i]; e != null; e = e.next)
//                        action.accept(e.key);
//                }
//                if (modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//    }
//
//    /**
//     * Returns a {@link Collection} view of the values contained in this map.
//     * The collection is backed by the map, so changes to the map are
//     * reflected in the collection, and vice-versa.  If the map is
//     * modified while an iteration over the collection is in progress
//     * (except through the iterator's own <tt>remove</tt> operation),
//     * the results of the iteration are undefined.  The collection
//     * supports element removal, which removes the corresponding
//     * mapping from the map, via the <tt>Iterator.remove</tt>,
//     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
//     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
//     * support the <tt>add</tt> or <tt>addAll</tt> operations.
//     *
//     * @return a view of the values contained in this map
//     */
//
//
//    final class Values extends AbstractCollection<V> {
//        public final int size()                 { return size; }
//        public final void clear()               { HashMap.this.clear(); }
//        public final Iterator<V> iterator()     { return new HashMap.ValueIterator(); }
//        public final boolean contains(Object o) { return containsValue(o); }
//        public final Spliterator<V> spliterator() {
//            return new HashMap.ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
//        }
//        public final void forEach(Consumer<? super V> action) {
//            HashMap.Node<K,V>[] tab;
//            if (action == null)
//                throw new NullPointerException();
//            if (size > 0 && (tab = table) != null) {
//                int mc = modCount;
//                for (int i = 0; i < tab.length; ++i) {
//                    for (HashMap.Node<K,V> e = tab[i]; e != null; e = e.next)
//                        action.accept(e.value);
//                }
//                if (modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//    }
//
//    /**
//     * Returns a {@link Set} view of the mappings contained in this map.
//     * The set is backed by the map, so changes to the map are
//     * reflected in the set, and vice-versa.  If the map is modified
//     * while an iteration over the set is in progress (except through
//     * the iterator's own <tt>remove</tt> operation, or through the
//     * <tt>setValue</tt> operation on a map entry returned by the
//     * iterator) the results of the iteration are undefined.  The set
//     * supports element removal, which removes the corresponding
//     * mapping from the map, via the <tt>Iterator.remove</tt>,
//     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
//     * <tt>clear</tt> operations.  It does not support the
//     * <tt>add</tt> or <tt>addAll</tt> operations.
//     *
//     * @return a set view of the mappings contained in this map
//     */
//    public Set<Map.Entry<K,V>> entrySet() {
//        Set<Map.Entry<K,V>> es;
//        return (es = entrySet) == null ? (entrySet = new HashMap.EntrySet()) : es;
//    }
//
//    final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
//        public final int size()                 { return size; }
//        public final void clear()               { HashMap.this.clear(); }
//        public final Iterator<Map.Entry<K,V>> iterator() {
//            return new HashMap.EntryIterator();
//        }
//        public final boolean contains(Object o) {
//            if (!(o instanceof Map.Entry))
//                return false;
//            Map.Entry<?,?> e = (Map.Entry<?,?>) o;
//            Object key = e.getKey();
//            HashMap.Node<K,V> candidate = getNode(hash(key), key);
//            return candidate != null && candidate.equals(e);
//        }
//        public final boolean remove(Object o) {
//            if (o instanceof Map.Entry) {
//                Map.Entry<?,?> e = (Map.Entry<?,?>) o;
//                Object key = e.getKey();
//                Object value = e.getValue();
//                return removeNode(hash(key), key, value, true, true) != null;
//            }
//            return false;
//        }
//        public final Spliterator<Map.Entry<K,V>> spliterator() {
//            return new HashMap.EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
//        }
//        public final void forEach(Consumer<? super Map.Entry<K,V>> action) {
//            HashMap.Node<K,V>[] tab;
//            if (action == null)
//                throw new NullPointerException();
//            if (size > 0 && (tab = table) != null) {
//                int mc = modCount;
//                for (int i = 0; i < tab.length; ++i) {
//                    for (HashMap.Node<K,V> e = tab[i]; e != null; e = e.next)
//                        action.accept(e);
//                }
//                if (modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//    }
//
//    // Overrides of JDK8 Map extension methods
//
//    @Override
//    public V getOrDefault(Object key, V defaultValue) {
//        HashMap.Node<K,V> e;
//        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
//    }
//
//    @Override
//    public V putIfAbsent(K key, V value) {
//        return putVal(hash(key), key, value, true, true);
//    }
//
//    @Override
//    public boolean remove(Object key, Object value) {
//        return removeNode(hash(key), key, value, true, true) != null;
//    }
//
//    @Override
//    public boolean replace(K key, V oldValue, V newValue) {
//        HashMap.Node<K,V> e; V v;
//        if ((e = getNode(hash(key), key)) != null &&
//                ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
//            e.value = newValue;
//            afterNodeAccess(e);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public V replace(K key, V value) {
//        HashMap.Node<K,V> e;
//        if ((e = getNode(hash(key), key)) != null) {
//            V oldValue = e.value;
//            e.value = value;
//            afterNodeAccess(e);
//            return oldValue;
//        }
//        return null;
//    }
//
//    @Override
//    public V computeIfAbsent(K key,
//                             Function<? super K, ? extends V> mappingFunction) {
//        if (mappingFunction == null)
//            throw new NullPointerException();
//        int hash = hash(key);
//        HashMap.Node<K,V>[] tab; HashMap.Node<K,V> first; int n, i;
//        int binCount = 0;
//        HashMap.TreeNode<K,V> t = null;
//        HashMap.Node<K,V> old = null;
//        if (size > threshold || (tab = table) == null ||
//                (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        if ((first = tab[i = (n - 1) & hash]) != null) {
//            if (first instanceof HashMap.TreeNode)
//                old = (t = (HashMap.TreeNode<K,V>)first).getTreeNode(hash, key);
//            else {
//                HashMap.Node<K,V> e = first; K k;
//                do {
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k)))) {
//                        old = e;
//                        break;
//                    }
//                    ++binCount;
//                } while ((e = e.next) != null);
//            }
//            V oldValue;
//            if (old != null && (oldValue = old.value) != null) {
//                afterNodeAccess(old);
//                return oldValue;
//            }
//        }
//        V v = mappingFunction.apply(key);
//        if (v == null) {
//            return null;
//        } else if (old != null) {
//            old.value = v;
//            afterNodeAccess(old);
//            return v;
//        }
//        else if (t != null)
//            t.putTreeVal(this, tab, hash, key, v);
//        else {
//            tab[i] = newNode(hash, key, v, first);
//            if (binCount >= TREEIFY_THRESHOLD - 1)
//                treeifyBin(tab, hash);
//        }
//        ++modCount;
//        ++size;
//        afterNodeInsertion(true);
//        return v;
//    }
//
//    public V computeIfPresent(K key,
//                              BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//        if (remappingFunction == null)
//            throw new NullPointerException();
//        HashMap.Node<K,V> e; V oldValue;
//        int hash = hash(key);
//        if ((e = getNode(hash, key)) != null &&
//                (oldValue = e.value) != null) {
//            V v = remappingFunction.apply(key, oldValue);
//            if (v != null) {
//                e.value = v;
//                afterNodeAccess(e);
//                return v;
//            }
//            else
//                removeNode(hash, key, null, false, true);
//        }
//        return null;
//    }
//
//    @Override
//    public V compute(K key,
//                     BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
//        if (remappingFunction == null)
//            throw new NullPointerException();
//        int hash = hash(key);
//        HashMap.Node<K,V>[] tab; HashMap.Node<K,V> first; int n, i;
//        int binCount = 0;
//        HashMap.TreeNode<K,V> t = null;
//        HashMap.Node<K,V> old = null;
//        if (size > threshold || (tab = table) == null ||
//                (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        if ((first = tab[i = (n - 1) & hash]) != null) {
//            if (first instanceof HashMap.TreeNode)
//                old = (t = (HashMap.TreeNode<K,V>)first).getTreeNode(hash, key);
//            else {
//                HashMap.Node<K,V> e = first; K k;
//                do {
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k)))) {
//                        old = e;
//                        break;
//                    }
//                    ++binCount;
//                } while ((e = e.next) != null);
//            }
//        }
//        V oldValue = (old == null) ? null : old.value;
//        V v = remappingFunction.apply(key, oldValue);
//        if (old != null) {
//            if (v != null) {
//                old.value = v;
//                afterNodeAccess(old);
//            }
//            else
//                removeNode(hash, key, null, false, true);
//        }
//        else if (v != null) {
//            if (t != null)
//                t.putTreeVal(this, tab, hash, key, v);
//            else {
//                tab[i] = newNode(hash, key, v, first);
//                if (binCount >= TREEIFY_THRESHOLD - 1)
//                    treeifyBin(tab, hash);
//            }
//            ++modCount;
//            ++size;
//            afterNodeInsertion(true);
//        }
//        return v;
//    }
//
//    @Override
//    public V merge(K key, V value,
//                   BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
//        if (value == null)
//            throw new NullPointerException();
//        if (remappingFunction == null)
//            throw new NullPointerException();
//        int hash = hash(key);
//        HashMap.Node<K,V>[] tab; HashMap.Node<K,V> first; int n, i;
//        int binCount = 0;
//        HashMap.TreeNode<K,V> t = null;
//        HashMap.Node<K,V> old = null;
//        if (size > threshold || (tab = table) == null ||
//                (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        if ((first = tab[i = (n - 1) & hash]) != null) {
//            if (first instanceof HashMap.TreeNode)
//                old = (t = (HashMap.TreeNode<K,V>)first).getTreeNode(hash, key);
//            else {
//                HashMap.Node<K,V> e = first; K k;
//                do {
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k)))) {
//                        old = e;
//                        break;
//                    }
//                    ++binCount;
//                } while ((e = e.next) != null);
//            }
//        }
//        if (old != null) {
//            V v;
//            if (old.value != null)
//                v = remappingFunction.apply(old.value, value);
//            else
//                v = value;
//            if (v != null) {
//                old.value = v;
//                afterNodeAccess(old);
//            }
//            else
//                removeNode(hash, key, null, false, true);
//            return v;
//        }
//        if (value != null) {
//            if (t != null)
//                t.putTreeVal(this, tab, hash, key, value);
//            else {
//                tab[i] = newNode(hash, key, value, first);
//                if (binCount >= TREEIFY_THRESHOLD - 1)
//                    treeifyBin(tab, hash);
//            }
//            ++modCount;
//            ++size;
//            afterNodeInsertion(true);
//        }
//        return value;
//    }
//
//    @Override
//    public void forEach(BiConsumer<? super K, ? super V> action) {
//        HashMap.Node<K,V>[] tab;
//        if (action == null)
//            throw new NullPointerException();
//        if (size > 0 && (tab = table) != null) {
//            int mc = modCount;
//            for (int i = 0; i < tab.length; ++i) {
//                for (HashMap.Node<K,V> e = tab[i]; e != null; e = e.next)
//                    action.accept(e.key, e.value);
//            }
//            if (modCount != mc)
//                throw new ConcurrentModificationException();
//        }
//    }
//
//    @Override
//    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
//        HashMap.Node<K,V>[] tab;
//        if (function == null)
//            throw new NullPointerException();
//        if (size > 0 && (tab = table) != null) {
//            int mc = modCount;
//            for (int i = 0; i < tab.length; ++i) {
//                for (HashMap.Node<K,V> e = tab[i]; e != null; e = e.next) {
//                    e.value = function.apply(e.key, e.value);
//                }
//            }
//            if (modCount != mc)
//                throw new ConcurrentModificationException();
//        }
//    }
//
//    /* ------------------------------------------------------------ */
//    // Cloning and serialization
//
//    /**
//     * Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and
//     * values themselves are not cloned.
//     *
//     * @return a shallow copy of this map
//     */
//    @SuppressWarnings("unchecked")
//    @Override
//    public Object clone() {
//        HashMap<K,V> result;
//        try {
//            result = (HashMap<K,V>)super.clone();
//        } catch (CloneNotSupportedException e) {
//            // this shouldn't happen, since we are Cloneable
//            throw new InternalError(e);
//        }
//        //result.reinitialize();删除了，详细看源码
//        result.putMapEntries(this, false);
//        return result;
//    }
//
//    // These methods are also used when serializing HashSets
//    final float loadFactor() { return loadFactor; }
//    final int capacity() {
//        return (table != null) ? table.length :
//                (threshold > 0) ? threshold :
//                        DEFAULT_INITIAL_CAPACITY;
//    }
//
//    /**
//     * Save the state of the <tt>HashMap</tt> instance to a stream (i.e.,
//     * serialize it).
//     *
//     * @serialData The <i>capacity</i> of the HashMap (the length of the
//     *             bucket array) is emitted (int), followed by the
//     *             <i>size</i> (an int, the number of key-value
//     *             mappings), followed by the key (Object) and value (Object)
//     *             for each key-value mapping.  The key-value mappings are
//     *             emitted in no particular order.
//     */
//    private void writeObject(java.io.ObjectOutputStream s)
//            throws IOException {
//        int buckets = capacity();
//        // Write out the threshold, loadfactor, and any hidden stuff
//        s.defaultWriteObject();
//        s.writeInt(buckets);
//        s.writeInt(size);
//        internalWriteEntries(s);
//    }
//
//    /**
//     * Reconstitutes this map from a stream (that is, deserializes it).
//     * @param s the stream
//     * @throws ClassNotFoundException if the class of a serialized object
//     *         could not be found
//     * @throws IOException if an I/O error occurs
//     */
//    private void readObject(java.io.ObjectInputStream s)
//            throws IOException, ClassNotFoundException {
//        // Read in the threshold (ignored), loadfactor, and any hidden stuff
//        s.defaultReadObject();
//        //reinitialize();
//        if (loadFactor <= 0 || Float.isNaN(loadFactor))
//            throw new InvalidObjectException("Illegal load factor: " +
//                    loadFactor);
//        s.readInt();                // Read and ignore number of buckets
//        int mappings = s.readInt(); // Read number of mappings (size)
//        if (mappings < 0)
//            throw new InvalidObjectException("Illegal mappings count: " +
//                    mappings);
//        else if (mappings > 0) { // (if zero, use defaults)
//            // Size the table using given load factor only if within
//            // range of 0.25...4.0
//            float lf = Math.min(Math.max(0.25f, loadFactor), 4.0f);
//            float fc = (float)mappings / lf + 1.0f;
//            int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ?
//                    DEFAULT_INITIAL_CAPACITY :
//                    (fc >= MAXIMUM_CAPACITY) ?
//                            MAXIMUM_CAPACITY :
//                            tableSizeFor((int)fc));
//            float ft = (float)cap * lf;
//            threshold = ((cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ?
//                    (int)ft : Integer.MAX_VALUE);
//
//            // Check Map.Entry[].class since it's the nearest public type to
//            // what we're actually creating.
//            SharedSecrets.getJavaOISAccess().checkArray(s, Map.Entry[].class, cap);
//            @SuppressWarnings({"rawtypes","unchecked"})
//            HashMap.Node<K,V>[] tab = (HashMap.Node<K,V>[])new HashMap.Node[cap];
//            table = tab;
//
//            // Read the keys and values, and put the mappings in the HashMap
//            for (int i = 0; i < mappings; i++) {
//                @SuppressWarnings("unchecked")
//                K key = (K) s.readObject();
//                @SuppressWarnings("unchecked")
//                V value = (V) s.readObject();
//                putVal(hash(key), key, value, false, false);
//            }
//        }
//    }
//
//    /* ------------------------------------------------------------ */
//    // iterators
//
//    abstract class HashIterator {
//        HashMap.Node<K,V> next;        // next entry to return
//        HashMap.Node<K,V> current;     // current entry
//        int expectedModCount;  // for fast-fail
//        int index;             // current slot
//
//        HashIterator() {
//            expectedModCount = modCount;
//            HashMap.Node<K,V>[] t = table;
//            current = next = null;
//            index = 0;
//            if (t != null && size > 0) { // advance to first entry
//                do {} while (index < t.length && (next = t[index++]) == null);
//            }
//        }
//
//        public final boolean hasNext() {
//            return next != null;
//        }
//
//        final HashMap.Node<K,V> nextNode() {
//            HashMap.Node<K,V>[] t;
//            HashMap.Node<K,V> e = next;
//            if (modCount != expectedModCount)
//                throw new ConcurrentModificationException();
//            if (e == null)
//                throw new NoSuchElementException();
//            if ((next = (current = e).next) == null && (t = table) != null) {
//                do {} while (index < t.length && (next = t[index++]) == null);
//            }
//            return e;
//        }
//
//        public final void remove() {
//            HashMap.Node<K,V> p = current;
//            if (p == null)
//                throw new IllegalStateException();
//            if (modCount != expectedModCount)
//                throw new ConcurrentModificationException();
//            current = null;
//            K key = p.key;
//            removeNode(hash(key), key, null, false, false);
//            expectedModCount = modCount;
//        }
//    }
//
//    final class KeyIterator extends HashIterator
//            implements Iterator<K> {
//        public final K next() { return nextNode().key; }
//    }
//
//    final class ValueIterator extends HashIterator
//            implements Iterator<V> {
//        public final V next() { return nextNode().value; }
//    }
//
//    final class EntryIterator extends HashMap.HashIterator
//            implements Iterator<Map.Entry<K,V>> {
//        public final Map.Entry<K,V> next() { return nextNode(); }
//    }
//
//    /* ------------------------------------------------------------ */
//    // spliterators
//
//    static class HashMapSpliterator<K,V> {
//        final HashMap<K,V> map;
//        HashMap.Node<K,V> current;          // current node
//        int index;                  // current index, modified on advance/split
//        int fence;                  // one past last index
//        int est;                    // size estimate
//        int expectedModCount;       // for comodification checks
//
//        HashMapSpliterator(HashMap<K,V> m, int origin,
//                           int fence, int est,
//                           int expectedModCount) {
//            this.map = m;
//            this.index = origin;
//            this.fence = fence;
//            this.est = est;
//            this.expectedModCount = expectedModCount;
//        }
//
//        final int getFence() { // initialize fence and size on first use
//            int hi;
//            if ((hi = fence) < 0) {
//                HashMap<K,V> m = map;
//                est = m.size;
//                expectedModCount = m.modCount;
//                HashMap.Node<K,V>[] tab = m.table;
//                hi = fence = (tab == null) ? 0 : tab.length;
//            }
//            return hi;
//        }
//
//        public final long estimateSize() {
//            getFence(); // force init
//            return (long) est;
//        }
//    }
//
//    static final class KeySpliterator<K,V>
//            extends HashMap.HashMapSpliterator<K,V>
//            implements Spliterator<K> {
//        KeySpliterator(HashMap<K,V> m, int origin, int fence, int est,
//                       int expectedModCount) {
//            super(m, origin, fence, est, expectedModCount);
//        }
//
//        public HashMap.KeySpliterator<K,V> trySplit() {
//            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
//            return (lo >= mid || current != null) ? null :
//                    new HashMap.KeySpliterator<>(map, lo, index = mid, est >>>= 1,
//                            expectedModCount);
//        }
//
//        public void forEachRemaining(Consumer<? super K> action) {
//            int i, hi, mc;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap<K,V> m = map;
//            HashMap.Node<K,V>[] tab = m.table;
//            if ((hi = fence) < 0) {
//                mc = expectedModCount = m.modCount;
//                hi = fence = (tab == null) ? 0 : tab.length;
//            }
//            else
//                mc = expectedModCount;
//            if (tab != null && tab.length >= hi &&
//                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
//                HashMap.Node<K,V> p = current;
//                current = null;
//                do {
//                    if (p == null)
//                        p = tab[i++];
//                    else {
//                        action.accept(p.key);
//                        p = p.next;
//                    }
//                } while (p != null || i < hi);
//                if (m.modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//
//        public boolean tryAdvance(Consumer<? super K> action) {
//            int hi;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap.Node<K,V>[] tab = map.table;
//            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
//                while (current != null || index < hi) {
//                    if (current == null)
//                        current = tab[index++];
//                    else {
//                        K k = current.key;
//                        current = current.next;
//                        action.accept(k);
//                        if (map.modCount != expectedModCount)
//                            throw new ConcurrentModificationException();
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        public int characteristics() {
//            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
//                    Spliterator.DISTINCT;
//        }
//    }
//
//    static final class ValueSpliterator<K,V>
//            extends HashMap.HashMapSpliterator<K,V>
//            implements Spliterator<V> {
//        ValueSpliterator(HashMap<K,V> m, int origin, int fence, int est,
//                         int expectedModCount) {
//            super(m, origin, fence, est, expectedModCount);
//        }
//
//        public HashMap.ValueSpliterator<K,V> trySplit() {
//            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
//            return (lo >= mid || current != null) ? null :
//                    new HashMap.ValueSpliterator<>(map, lo, index = mid, est >>>= 1,
//                            expectedModCount);
//        }
//
//        public void forEachRemaining(Consumer<? super V> action) {
//            int i, hi, mc;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap<K,V> m = map;
//            HashMap.Node<K,V>[] tab = m.table;
//            if ((hi = fence) < 0) {
//                mc = expectedModCount = m.modCount;
//                hi = fence = (tab == null) ? 0 : tab.length;
//            }
//            else
//                mc = expectedModCount;
//            if (tab != null && tab.length >= hi &&
//                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
//                HashMap.Node<K,V> p = current;
//                current = null;
//                do {
//                    if (p == null)
//                        p = tab[i++];
//                    else {
//                        action.accept(p.value);
//                        p = p.next;
//                    }
//                } while (p != null || i < hi);
//                if (m.modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//
//        public boolean tryAdvance(Consumer<? super V> action) {
//            int hi;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap.Node<K,V>[] tab = map.table;
//            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
//                while (current != null || index < hi) {
//                    if (current == null)
//                        current = tab[index++];
//                    else {
//                        V v = current.value;
//                        current = current.next;
//                        action.accept(v);
//                        if (map.modCount != expectedModCount)
//                            throw new ConcurrentModificationException();
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        public int characteristics() {
//            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0);
//        }
//    }
//
//    static final class EntrySpliterator<K,V>
//            extends HashMap.HashMapSpliterator<K,V>
//            implements Spliterator<Map.Entry<K,V>> {
//        EntrySpliterator(HashMap<K,V> m, int origin, int fence, int est,
//                         int expectedModCount) {
//            super(m, origin, fence, est, expectedModCount);
//        }
//
//        public HashMap.EntrySpliterator<K,V> trySplit() {
//            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
//            return (lo >= mid || current != null) ? null :
//                    new HashMap.EntrySpliterator<>(map, lo, index = mid, est >>>= 1,
//                            expectedModCount);
//        }
//
//        public void forEachRemaining(Consumer<? super Map.Entry<K,V>> action) {
//            int i, hi, mc;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap<K,V> m = map;
//            HashMap.Node<K,V>[] tab = m.table;
//            if ((hi = fence) < 0) {
//                mc = expectedModCount = m.modCount;
//                hi = fence = (tab == null) ? 0 : tab.length;
//            }
//            else
//                mc = expectedModCount;
//            if (tab != null && tab.length >= hi &&
//                    (i = index) >= 0 && (i < (index = hi) || current != null)) {
//                HashMap.Node<K,V> p = current;
//                current = null;
//                do {
//                    if (p == null)
//                        p = tab[i++];
//                    else {
//                        action.accept(p);
//                        p = p.next;
//                    }
//                } while (p != null || i < hi);
//                if (m.modCount != mc)
//                    throw new ConcurrentModificationException();
//            }
//        }
//
//        public boolean tryAdvance(Consumer<? super Map.Entry<K,V>> action) {
//            int hi;
//            if (action == null)
//                throw new NullPointerException();
//            HashMap.Node<K,V>[] tab = map.table;
//            if (tab != null && tab.length >= (hi = getFence()) && index >= 0) {
//                while (current != null || index < hi) {
//                    if (current == null)
//                        current = tab[index++];
//                    else {
//                        HashMap.Node<K,V> e = current;
//                        current = current.next;
//                        action.accept(e);
//                        if (map.modCount != expectedModCount)
//                            throw new ConcurrentModificationException();
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        public int characteristics() {
//            return (fence < 0 || est == map.size ? Spliterator.SIZED : 0) |
//                    Spliterator.DISTINCT;
//        }
//    }
//
//    /* ------------------------------------------------------------ */
//    // LinkedHashMap support
//
//
//    /*
//     * The following package-protected methods are designed to be
//     * overridden by LinkedHashMap, but not by any other subclass.
//     * Nearly all other internal methods are also package-protected
//     * but are declared final, so can be used by LinkedHashMap, view
//     * classes, and HashSet.
//     */
//
//    // Create a regular (non-tree) node
//    HashMap.Node<K,V> newNode(int hash, K key, V value, HashMap.Node<K,V> next) {
//        return new HashMap.Node<>(hash, key, value, next);
//    }
//
//    // For conversion from TreeNodes to plain nodes
//    HashMap.Node<K,V> replacementNode(HashMap.Node<K,V> p, HashMap.Node<K,V> next) {
//        return new HashMap.Node<>(p.hash, p.key, p.value, next);
//    }
//
//    // Create a tree bin node
//    HashMap.TreeNode<K,V> newTreeNode(int hash, K key, V value, HashMap.Node<K,V> next) {
//        return new HashMap.TreeNode<>(hash, key, value, next);
//    }
//
//    // For treeifyBin
//    HashMap.TreeNode<K,V> replacementTreeNode(HashMap.Node<K,V> p, HashMap.Node<K,V> next) {
//        return new HashMap.TreeNode<>(p.hash, p.key, p.value, next);
//    }
//
//    /**
//     * Reset to initial default state.  Called by clone and readObject.
//     */
//
//
//    // Callbacks to allow LinkedHashMap post-actions
//    void afterNodeAccess(HashMap.Node<K,V> p) { }
//    void afterNodeInsertion(boolean evict) { }
//    void afterNodeRemoval(HashMap.Node<K,V> p) { }
//
//    // Called only from writeObject, to ensure compatible ordering.
//    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
//        HashMap.Node<K,V>[] tab;
//        if (size > 0 && (tab = table) != null) {
//            for (int i = 0; i < tab.length; ++i) {
//                for (HashMap.Node<K,V> e = tab[i]; e != null; e = e.next) {
//                    s.writeObject(e.key);
//                    s.writeObject(e.value);
//                }
//            }
//        }
//    }
//
//    /* ------------------------------------------------------------ */
//    // Tree bins
//
//    /**
//     * Entry for Tree bins. Extends LinkedHashMap.Entry (which in turn
//     * extends Node) so can be used as extension of either regular or
//     * linked node.
//     */
//    static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
//        HashMap.TreeNode<K,V> parent;  // red-black tree links
//        HashMap.TreeNode<K,V> left;
//        HashMap.TreeNode<K,V> right;
//        HashMap.TreeNode<K,V> prev;    // needed to unlink next upon deletion
//        boolean red;
//        TreeNode(int hash, K key, V val, HashMap.Node<K,V> next) {
//            super(hash, key, val, next);
//        }
//
//        /**
//         * Returns root of tree containing this node.
//         */
//        final HashMap.TreeNode<K,V> root() {
//            for (HashMap.TreeNode<K,V> r = this, p;;) {
//                if ((p = r.parent) == null)
//                    return r;
//                r = p;
//            }
//        }
//
//        /**
//         * Ensures that the given root is the first node of its bin.
//         */
//        static <K,V> void moveRootToFront(HashMap.Node<K,V>[] tab, HashMap.TreeNode<K,V> root) {
//            int n;
//            if (root != null && tab != null && (n = tab.length) > 0) {
//                int index = (n - 1) & root.hash;
//                HashMap.TreeNode<K,V> first = (HashMap.TreeNode<K,V>)tab[index];
//                if (root != first) {
//                    HashMap.Node<K,V> rn;
//                    tab[index] = root;
//                    HashMap.TreeNode<K,V> rp = root.prev;
//                    if ((rn = root.next) != null)
//                        ((HashMap.TreeNode<K,V>)rn).prev = rp;
//                    if (rp != null)
//                        rp.next = rn;
//                    if (first != null)
//                        first.prev = root;
//                    root.next = first;
//                    root.prev = null;
//                }
//                assert checkInvariants(root);
//            }
//        }
//
//        /**
//         * Finds the node starting at root p with the given hash and key.
//         * The kc argument caches comparableClassFor(key) upon first use
//         * comparing keys.
//         */
//        final HashMap.TreeNode<K,V> find(int h, Object k, Class<?> kc) {
//            HashMap.TreeNode<K,V> p = this;
//            do {
//                int ph, dir; K pk;
//                HashMap.TreeNode<K,V> pl = p.left, pr = p.right, q;
//                if ((ph = p.hash) > h)
//                    p = pl;
//                else if (ph < h)
//                    p = pr;
//                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
//                    return p;
//                else if (pl == null)
//                    p = pr;
//                else if (pr == null)
//                    p = pl;
//                else if ((kc != null ||
//                        (kc = comparableClassFor(k)) != null) &&
//                        (dir = compareComparables(kc, k, pk)) != 0)
//                    p = (dir < 0) ? pl : pr;
//                else if ((q = pr.find(h, k, kc)) != null)
//                    return q;
//                else
//                    p = pl;
//            } while (p != null);
//            return null;
//        }
//
//        /**
//         * Calls find for root node.
//         */
//        final HashMap.TreeNode<K,V> getTreeNode(int h, Object k) {
//            return ((parent != null) ? root() : this).find(h, k, null);
//        }
//
//        /**
//         * Tie-breaking utility for ordering insertions when equal
//         * hashCodes and non-comparable. We don't require a total
//         * order, just a consistent insertion rule to maintain
//         * equivalence across rebalancings. Tie-breaking further than
//         * necessary simplifies testing a bit.
//         */
//        static int tieBreakOrder(Object a, Object b) {
//            int d;
//            if (a == null || b == null ||
//                    (d = a.getClass().getName().
//                            compareTo(b.getClass().getName())) == 0)
//                d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
//                        -1 : 1);
//            return d;
//        }
//
//        /**
//         * Forms tree of the nodes linked from this node.
//         */
//        final void treeify(HashMap.Node<K,V>[] tab) {
//            HashMap.TreeNode<K,V> root = null;
//            for (HashMap.TreeNode<K,V> x = this, next; x != null; x = next) {
//                next = (HashMap.TreeNode<K,V>)x.next;
//                x.left = x.right = null;
//                if (root == null) {
//                    x.parent = null;
//                    x.red = false;
//                    root = x;
//                }
//                else {
//                    K k = x.key;
//                    int h = x.hash;
//                    Class<?> kc = null;
//                    for (HashMap.TreeNode<K,V> p = root;;) {
//                        int dir, ph;
//                        K pk = p.key;
//                        if ((ph = p.hash) > h)
//                            dir = -1;
//                        else if (ph < h)
//                            dir = 1;
//                        else if ((kc == null &&
//                                (kc = comparableClassFor(k)) == null) ||
//                                (dir = compareComparables(kc, k, pk)) == 0)
//                            dir = tieBreakOrder(k, pk);
//
//                        HashMap.TreeNode<K,V> xp = p;
//                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
//                            x.parent = xp;
//                            if (dir <= 0)
//                                xp.left = x;
//                            else
//                                xp.right = x;
//                            root = balanceInsertion(root, x);
//                            break;
//                        }
//                    }
//                }
//            }
//            moveRootToFront(tab, root);
//        }
//
//        /**
//         * Returns a list of non-TreeNodes replacing those linked from
//         * this node.
//         */
//        final HashMap.Node<K,V> untreeify(HashMap<K,V> map) {
//            HashMap.Node<K,V> hd = null, tl = null;
//            for (HashMap.Node<K,V> q = this; q != null; q = q.next) {
//                HashMap.Node<K,V> p = map.replacementNode(q, null);
//                if (tl == null)
//                    hd = p;
//                else
//                    tl.next = p;
//                tl = p;
//            }
//            return hd;
//        }
//
//        /**
//         * Tree version of putVal.
//         */
//        final HashMap.TreeNode<K,V> putTreeVal(HashMap<K,V> map, HashMap.Node<K,V>[] tab,
//                                                         int h, K k, V v) {
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
//                    moveRootToFront(tab, balanceInsertion(root, x));
//                    return null;
//                }
//            }
//        }
//
//        /**
//         * Removes the given node, that must be present before this call.
//         * This is messier than typical red-black deletion code because we
//         * cannot swap the contents of an interior node with a leaf
//         * successor that is pinned by "next" pointers that are accessible
//         * independently during traversal. So instead we swap the tree
//         * linkages. If the current tree appears to have too few nodes,
//         * the bin is converted back to a plain bin. (The test triggers
//         * somewhere between 2 and 6 nodes, depending on tree structure).
//         */
//        final void removeTreeNode(HashMap<K,V> map, HashMap.Node<K,V>[] tab,
//                                  boolean movable) {
//            int n;
//            if (tab == null || (n = tab.length) == 0)
//                return;
//            int index = (n - 1) & hash;
//            HashMap.TreeNode<K,V> first = (HashMap.TreeNode<K,V>)tab[index], root = first, rl;
//            HashMap.TreeNode<K,V> succ = (HashMap.TreeNode<K,V>)next, pred = prev;
//            if (pred == null)
//                tab[index] = first = succ;
//            else
//                pred.next = succ;
//            if (succ != null)
//                succ.prev = pred;
//            if (first == null)
//                return;
//            if (root.parent != null)
//                root = root.root();
//            if (root == null
//                    || (movable
//                    && (root.right == null
//                    || (rl = root.left) == null
//                    || rl.left == null))) {
//                tab[index] = first.untreeify(map);  // too small
//                return;
//            }
//            HashMap.TreeNode<K,V> p = this, pl = left, pr = right, replacement;
//            if (pl != null && pr != null) {
//                HashMap.TreeNode<K,V> s = pr, sl;
//                while ((sl = s.left) != null) // find successor
//                    s = sl;
//                boolean c = s.red; s.red = p.red; p.red = c; // swap colors
//                HashMap.TreeNode<K,V> sr = s.right;
//                HashMap.TreeNode<K,V> pp = p.parent;
//                if (s == pr) { // p was s's direct parent
//                    p.parent = s;
//                    s.right = p;
//                }
//                else {
//                    HashMap.TreeNode<K,V> sp = s.parent;
//                    if ((p.parent = sp) != null) {
//                        if (s == sp.left)
//                            sp.left = p;
//                        else
//                            sp.right = p;
//                    }
//                    if ((s.right = pr) != null)
//                        pr.parent = s;
//                }
//                p.left = null;
//                if ((p.right = sr) != null)
//                    sr.parent = p;
//                if ((s.left = pl) != null)
//                    pl.parent = s;
//                if ((s.parent = pp) == null)
//                    root = s;
//                else if (p == pp.left)
//                    pp.left = s;
//                else
//                    pp.right = s;
//                if (sr != null)
//                    replacement = sr;
//                else
//                    replacement = p;
//            }
//            else if (pl != null)
//                replacement = pl;
//            else if (pr != null)
//                replacement = pr;
//            else
//                replacement = p;
//            if (replacement != p) {
//                HashMap.TreeNode<K,V> pp = replacement.parent = p.parent;
//                if (pp == null)
//                    root = replacement;
//                else if (p == pp.left)
//                    pp.left = replacement;
//                else
//                    pp.right = replacement;
//                p.left = p.right = p.parent = null;
//            }
//
//            HashMap.TreeNode<K,V> r = p.red ? root : balanceDeletion(root, replacement);
//
//            if (replacement == p) {  // detach
//                HashMap.TreeNode<K,V> pp = p.parent;
//                p.parent = null;
//                if (pp != null) {
//                    if (p == pp.left)
//                        pp.left = null;
//                    else if (p == pp.right)
//                        pp.right = null;
//                }
//            }
//            if (movable)
//                moveRootToFront(tab, r);
//        }
//
//        /**
//         * Splits nodes in a tree bin into lower and upper tree bins,
//         * or untreeifies if now too small. Called only from resize;
//         * see above discussion about split bits and indices.
//         *
//         * @param map the map
//         * @param tab the table for recording bin heads
//         * @param index the index of the table being split
//         * @param bit the bit of hash to split on
//         */
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
//
//        /* ------------------------------------------------------------ */
//        // Red-black tree methods, all adapted from CLR
//
//        static <K,V> HashMap.TreeNode<K,V> rotateLeft(HashMap.TreeNode<K,V> root,
//                                                                HashMap.TreeNode<K,V> p) {
//            HashMap.TreeNode<K,V> r, pp, rl;
//            if (p != null && (r = p.right) != null) {
//                if ((rl = p.right = r.left) != null)
//                    rl.parent = p;
//                if ((pp = r.parent = p.parent) == null)
//                    (root = r).red = false;
//                else if (pp.left == p)
//                    pp.left = r;
//                else
//                    pp.right = r;
//                r.left = p;
//                p.parent = r;
//            }
//            return root;
//        }
//
//        static <K,V> HashMap.TreeNode<K,V> rotateRight(HashMap.TreeNode<K,V> root,
//                                                                 HashMap.TreeNode<K,V> p) {
//            HashMap.TreeNode<K,V> l, pp, lr;
//            if (p != null && (l = p.left) != null) {
//                if ((lr = p.left = l.right) != null)
//                    lr.parent = p;
//                if ((pp = l.parent = p.parent) == null)
//                    (root = l).red = false;
//                else if (pp.right == p)
//                    pp.right = l;
//                else
//                    pp.left = l;
//                l.right = p;
//                p.parent = l;
//            }
//            return root;
//        }
//
//        static <K,V> HashMap.TreeNode<K,V> balanceInsertion(HashMap.TreeNode<K,V> root,
//                                                                      HashMap.TreeNode<K,V> x) {
//            x.red = true;
//            for (HashMap.TreeNode<K,V> xp, xpp, xppl, xppr;;) {
//                if ((xp = x.parent) == null) {
//                    x.red = false;
//                    return x;
//                }
//                else if (!xp.red || (xpp = xp.parent) == null)
//                    return root;
//                if (xp == (xppl = xpp.left)) {
//                    if ((xppr = xpp.right) != null && xppr.red) {
//                        xppr.red = false;
//                        xp.red = false;
//                        xpp.red = true;
//                        x = xpp;
//                    }
//                    else {
//                        if (x == xp.right) {
//                            root = rotateLeft(root, x = xp);
//                            xpp = (xp = x.parent) == null ? null : xp.parent;
//                        }
//                        if (xp != null) {
//                            xp.red = false;
//                            if (xpp != null) {
//                                xpp.red = true;
//                                root = rotateRight(root, xpp);
//                            }
//                        }
//                    }
//                }
//                else {
//                    if (xppl != null && xppl.red) {
//                        xppl.red = false;
//                        xp.red = false;
//                        xpp.red = true;
//                        x = xpp;
//                    }
//                    else {
//                        if (x == xp.left) {
//                            root = rotateRight(root, x = xp);
//                            xpp = (xp = x.parent) == null ? null : xp.parent;
//                        }
//                        if (xp != null) {
//                            xp.red = false;
//                            if (xpp != null) {
//                                xpp.red = true;
//                                root = rotateLeft(root, xpp);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        static <K,V> HashMap.TreeNode<K,V> balanceDeletion(HashMap.TreeNode<K,V> root,
//                                                                     HashMap.TreeNode<K,V> x) {
//            for (HashMap.TreeNode<K,V> xp, xpl, xpr;;) {
//                if (x == null || x == root)
//                    return root;
//                else if ((xp = x.parent) == null) {
//                    x.red = false;
//                    return x;
//                }
//                else if (x.red) {
//                    x.red = false;
//                    return root;
//                }
//                else if ((xpl = xp.left) == x) {
//                    if ((xpr = xp.right) != null && xpr.red) {
//                        xpr.red = false;
//                        xp.red = true;
//                        root = rotateLeft(root, xp);
//                        xpr = (xp = x.parent) == null ? null : xp.right;
//                    }
//                    if (xpr == null)
//                        x = xp;
//                    else {
//                        HashMap.TreeNode<K,V> sl = xpr.left, sr = xpr.right;
//                        if ((sr == null || !sr.red) &&
//                                (sl == null || !sl.red)) {
//                            xpr.red = true;
//                            x = xp;
//                        }
//                        else {
//                            if (sr == null || !sr.red) {
//                                if (sl != null)
//                                    sl.red = false;
//                                xpr.red = true;
//                                root = rotateRight(root, xpr);
//                                xpr = (xp = x.parent) == null ?
//                                        null : xp.right;
//                            }
//                            if (xpr != null) {
//                                xpr.red = (xp == null) ? false : xp.red;
//                                if ((sr = xpr.right) != null)
//                                    sr.red = false;
//                            }
//                            if (xp != null) {
//                                xp.red = false;
//                                root = rotateLeft(root, xp);
//                            }
//                            x = root;
//                        }
//                    }
//                }
//                else { // symmetric
//                    if (xpl != null && xpl.red) {
//                        xpl.red = false;
//                        xp.red = true;
//                        root = rotateRight(root, xp);
//                        xpl = (xp = x.parent) == null ? null : xp.left;
//                    }
//                    if (xpl == null)
//                        x = xp;
//                    else {
//                        HashMap.TreeNode<K,V> sl = xpl.left, sr = xpl.right;
//                        if ((sl == null || !sl.red) &&
//                                (sr == null || !sr.red)) {
//                            xpl.red = true;
//                            x = xp;
//                        }
//                        else {
//                            if (sl == null || !sl.red) {
//                                if (sr != null)
//                                    sr.red = false;
//                                xpl.red = true;
//                                root = rotateLeft(root, xpl);
//                                xpl = (xp = x.parent) == null ?
//                                        null : xp.left;
//                            }
//                            if (xpl != null) {
//                                xpl.red = (xp == null) ? false : xp.red;
//                                if ((sl = xpl.left) != null)
//                                    sl.red = false;
//                            }
//                            if (xp != null) {
//                                xp.red = false;
//                                root = rotateRight(root, xp);
//                            }
//                            x = root;
//                        }
//                    }
//                }
//            }
//        }
//
//        /**
//         * Recursive invariant check
//         */
//        static <K,V> boolean checkInvariants(HashMap.TreeNode<K,V> t) {
//            HashMap.TreeNode<K,V> tp = t.parent, tl = t.left, tr = t.right,
//                    tb = t.prev, tn = (HashMap.TreeNode<K,V>)t.next;
//            if (tb != null && tb.next != t)
//                return false;
//            if (tn != null && tn.prev != t)
//                return false;
//            if (tp != null && t != tp.left && t != tp.right)
//                return false;
//            if (tl != null && (tl.parent != t || tl.hash > t.hash))
//                return false;
//            if (tr != null && (tr.parent != t || tr.hash < t.hash))
//                return false;
//            if (t.red && tl != null && tl.red && tr != null && tr.red)
//                return false;
//            if (tl != null && !checkInvariants(tl))
//                return false;
//            if (tr != null && !checkInvariants(tr))
//                return false;
//            return true;
//        }
//    }
//
//}
//
