package jg.tree.set;

import jg.tree.binary.BinaryTree;

/**
 * 集合(Set)
 * 集合的特点:
 * 1.不存在重复的元素
 * 2.常用于去重
 *  - 存放新增IP,统计新增IP量
 *  - 存放词汇,统计词汇量
 *
 * 集合的内部实现能 使用 动态数组,链表,和二叉搜索树(AVL、红黑树)
 *
 * 复杂度
 * TreeSet O(logn)
 * ListSet contains O(n) add O(n) remove O(n)
 * 红黑树优于链表实现,局限性是元素必须具备可比较性,如果不具备可比较性不能加入. 不需要可比较性的那么建议使用HashSet
 *
 * https://leetcode-cn.com/problems/intersection-of-two-arrays/ 两个数组的相交集合
 */
public interface Set<E> {
    int size();
    boolean isEmpty();
    void clear();
    boolean contains(E element);
    void add(E element);
    void remove(E element);
    // 之前所学的动态数组可以通过索引来访问,因此不需要通过提供接口来遍历
    void traversal(Visitor<E> visitor);

    public static abstract class Visitor<E> {
        boolean stop;
        public abstract boolean visit(E element);
    }
}