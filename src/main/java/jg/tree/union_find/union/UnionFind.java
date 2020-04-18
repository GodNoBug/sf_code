package jg.tree.union_find.union;

/**
 * 并查集也叫不相交结婚(Disjoint Set)
 * 并查集有两个核心操作:
 *   - 查找:查找元素所在的集合(这里的集合不是特指Set这种数据结构,是指广义的数据集合)
 *   - 合并:将两个元素所在的集合合并为一个集合
 *
 */
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
