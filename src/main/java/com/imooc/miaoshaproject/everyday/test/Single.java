//package com.imooc.miaoshaproject.everyday.test;
//
//import java.util.*;
//
///**
// * @Author wuqiusheng
// * @Version 1.0
// * @Date 2020/12/24 9:30
// */
//public class Single<E> {
//    public int[] advantageCount(int[] A, int[] B) {
//        int[] sortedA = A.clone();
//        Arrays.sort(sortedA);
//        int[] sortedB = B.clone();
//        Arrays.sort(sortedB);
//
//        // 分配的[b] =分配给击败b的a的列表
//        Map<Integer, Deque<Integer>> assigned = new HashMap();
//        for (int b: B)
//            assigned.put(b, new LinkedList());
//
//        // remaining =未分配给任何b的a的列表
//        Deque<Integer> remaining = new LinkedList();
//
//        // 适当地填充(分配，剩余)sortedB[j]总是E中最小的未赋值元素
//        int j = 0;
//        for (int a: sortedA) {
//            if (a > sortedB[j]) {
//                assigned.get(sortedB[j++]).add(a);
//            } else {
//                remaining.add(a);
//            }
//        }
//
//        // 从注释(已分配，剩余)重构答案
//        int[] ans = new int[B.length];
//        for (int i = 0; i < B.length; ++i) {
//            // 如果a被分配给b..
//            if (assigned.get(B[i]).size() > 0)
//                ans[i] = assigned.get(B[i]).pop();
//            else
//                ans[i] = remaining.pop();
//        }
//        return ans;
//    }
//}
