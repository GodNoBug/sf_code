package jg.tree.union_find.union;

/**
 * 并查集也叫不相交集合(Disjoint Set)
 * 并查集是一种树型的数据结构，用于处理一些不相交集合（Disjoint Sets）的合并及查询问题
 * 我们通常是在开始时让每个元素构成一个单元素的集合，然后按一定顺序将属于同一组的元素所在的集合合并，其间要反复查找一个元素在哪个集合中
 * 并查集有两个核心操作:
 * - 查找:查找元素所在的集合(这里的集合不是特指Set这种数据结构,是指广义的数据集合)
 * - 合并:将两个元素所在的集合合并为一个集合
 */

// 在Union的过程中,可能会出现树不平衡的状况.甚至退化成链表
// find操作从O(logn)逐步变成O(n)
// 有两种常见的优化方案
// 1.基于size的优化: 元素少的树 嫁接到 元素多的树
// 2.基于rank的优化: 矮的树 嫁接到 高的树

// 建议
// 使用路径压缩、分裂或减半 + 基于rank或者size的优化
// 可以确保每个操作的均摊时间复杂度为 O(𝛼(𝑛)) , α(𝑛) < 5
// 个人建议的搭配
// ✓ Quick Union
// ✓ 基于 rank 的优化
// ✓ Path Halving 或 Path Spliting


@SuppressWarnings("all")
public abstract class UnionFind {
    protected int[] parents;

    public UnionFind(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity must be >= 1");
        }

        parents = new int[capacity];
        for (int i = 0; i < parents.length; i++) {
            parents[i] = i;
        }
    }

    /**
     * 查找v所属的集合（根节点）
     *
     * @param v
     * @return
     */
    public abstract int find(int v);

    /**
     * 合并v1、v2所在的集合
     */
    public abstract void union(int v1, int v2);

    /**
     * 检查v1、v2是否属于同一个集合
     */
    public boolean isSame(int v1, int v2) {
        return find(v1) == find(v2);
    }

    protected void rangeCheck(int v) {
        if (v < 0 || v >= parents.length) {
            throw new IllegalArgumentException("v is out of bounds");
        }
    }
}
