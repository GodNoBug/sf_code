package jg.tree.union_find.union;

/**
 * quick_union
 * 查找的时间复杂度:O(logn),可以优化至O(α(n)),α(n)<5
 * 合并的时间复杂度:O(logn),可以优化至O(α(n)),α(n)<5
 *
 * 合并的时候,让方法左边参数的根节点,指向右边参数的根节点.永远是拿根节点操作
 */
public class UnionFind_QU extends UnionFind {

    public UnionFind_QU(int capacity) {
        super(capacity);
    }

    @Override
    public int find(int v) {
        return 0;
    }

    @Override
    public void union(int v1, int v2) {

    }
}
