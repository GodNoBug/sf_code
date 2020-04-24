package jg.tree.union_find.union;

/**
 * quick_union
 * 查找的时间复杂度:O(logn),可以优化至O(α(n)),α(n)<5
 * 合并的时间复杂度:O(logn),可以优化至O(α(n)),α(n)<5
 * <p>
 * 合并的时候,让方法左边参数的根节点,指向右边参数的根节点.永远是拿根节点操作(推荐)
 */
public class UnionFind_QU extends UnionFind {

    public UnionFind_QU(int capacity) {
        super(capacity);
    }

    // 通过parent链表不断向上找,直到找到根节点. 时间复杂度O(logn)
    @Override
    public int find(int v) {
        rangeCheck(v);
        while (v != parents[v]) { // v的值!+父节点的值.将父节点的值赋值给v,继续向上找
            v = parents[v];
        }
        return v;
    }

    // 将v1的根节点指向v2的根节点上 时间复杂度O(logn)
    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2= find(v2);
        if (p1==p2) return;

        parents[p1] = p2;
    }
}
// 在Union的过程中,可能会出现树不平衡的状况.甚至退化成链表
// find操作从O(logn)逐步变成O(n)
// 有两种常见的优化方案
// 1.基于size的优化: 元素少的树 嫁接到 元素多的树
// 2.基于rank的优化: 矮的树 嫁接到 高的树