package jg.tree.union_find.union.qu;

// quick_union,基于Rank优化
@SuppressWarnings("all")
public class UnionFind_QU_Rank extends UnionFind_QU {
    private int[] ranks; // 以某结点为根节点的树高度,下标为根节点,元素为ranks

    public UnionFind_QU_Rank(int capacity) {
        super(capacity);
        ranks = new int[capacity];
        for (int i = 0; i < ranks.length; i++) {  // 一开始每个元素的父节点都是自己,也就是每个元素单独一个集合,以它为根节点的那棵树高度最开始也是1
            ranks[i] = 1;
        }
    }

    // 将v1的根节点嫁接到v2的根节点上 时间复杂度O(logn)
    @Override
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;
        if (ranks[p1] < ranks[p2]) {
            parents[p1] = p2; // 矮的嫁接到高的,高度不会变
        } else if (ranks[p1] > ranks[p2]) {
            parents[p2] = p1; // 矮的嫁接到高的,高度不会变
        } else {
            // 两棵树的高度是一样的,由于QuickUnion的特性,把一个根节点合并到另一个根节点下面!注意是下面!那么高度就高出一个根节点
            // 树的高度会加一.谁嫁接谁都可以自己画图理解,
            parents[p1] = p2;
            ranks[p2] += 1;
        }
    }
}