package jg.tree.union_find.union.qu;

// quick_union
// 基于size的优化.也可能会存在树不平衡的问题
@SuppressWarnings("all")
public class UnionFind_QU_Size extends UnionFind_QU {
    private int[] sizes; // 以某结点为根节点的size,下标为根节点,元素为size

    public UnionFind_QU_Size(int capacity) {
        super(capacity);
        sizes = new int[capacity];
        for (int i = 0; i < sizes.length; i++) {
            // 一开始每个元素的父节点都是自己,也就是每个元素单独一个集合,以它为根节点的那棵树size最开始也是1
            sizes[i] = 1;
        }
    }

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