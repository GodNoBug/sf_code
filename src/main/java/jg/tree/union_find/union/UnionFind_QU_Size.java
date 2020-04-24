package jg.tree.union_find.union;

/**
 * quick_union 基于size的优化.也可能会存在树不平衡的问题
 * 查找的时间复杂度:O(logn),可以优化至O(α(n)),α(n)<5
 * 合并的时间复杂度:O(logn),可以优化至O(α(n)),α(n)<5
 * <p>
 * 合并的时候,让方法左边参数的根节点,指向右边参数的根节点.永远是拿根节点操作(推荐)
 */
public class UnionFind_QU_Size extends UnionFind_QU {
    private int[] sizes; // 以某结点为根节点的size,下标为根节点,元素为size

    public UnionFind_QU_Size(int capacity) {
        super(capacity);
        sizes = new int[capacity];
        for (int i = 0; i < sizes.length; i++) { // 一开始每个元素的父节点都是自己,也就是每个元素单独一个集合,以它为根节点的那棵树size最开始也是1
            sizes[i] = 1;
        }
    }


    // 将v1的根节点嫁接到v2的根节点上 时间复杂度O(logn)
    @Override
    public void union(int v1, int v2) {

        int p1 = find(v1);
        int p2 = find(v2);

        if (p1 == p2) return;
        if (sizes[p1] < sizes[p2]) {
            parents[p1] = p2;
            sizes[p2]+=sizes[p1]; // 小的树的元素数量添加到大的元素数量
        } else {
            parents[p2] = p1;
            sizes[p1]+=sizes[p2];
        }

    }
}
// 在Union的过程中,可能会出现树不平衡的状况.甚至退化成链表
// find操作从O(logn)逐步变成O(n)
// 有两种常见的优化方案
// 1.基于size的优化: 元素少的树 嫁接到 元素多的树
// 2.基于rank的优化: 矮的树 嫁接到 高的树