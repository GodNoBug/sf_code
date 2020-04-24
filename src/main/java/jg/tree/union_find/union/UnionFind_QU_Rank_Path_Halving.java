package jg.tree.union_find.union;

/**
 * quick_union,基于Rank优化,加上路径减半(Path Spliting)
 * 路径减半：使路径上每隔一个节点就指向其祖父节点（parent的parent）
 */
public class UnionFind_QU_Rank_Path_Halving extends UnionFind_QU_Rank {


    public UnionFind_QU_Rank_Path_Halving(int capacity) {
        super(capacity);
    }

    // TODO why?
    @Override
    public int find(int v) {
        rangeCheck(v);
        while (parents[v] != v) {
            parents[v] = parents[parents[v]];
            v = parents[v];
        }
        return v;
    }
}
// 在Union的过程中,可能会出现树不平衡的状况.甚至退化成链表
// find操作从O(logn)逐步变成O(n)
// 有两种常见的优化方案
// 1.基于size的优化: 元素少的树 嫁接到 元素多的树
// 2.基于rank的优化: 矮的树 嫁接到 高的树