package jg.tree.union_find.union.qf;


import jg.tree.union_find.union.UnionFind;

// quick_find
//  union的时候,将v1所在集合的所有元素,都指向v2的根节点
//  find的时候,直接通过下标查询可得
//  查找的时间复杂度:O(1)
//  合并的时间复杂度:O(n)
public class UnionFind_QF extends UnionFind {
    public UnionFind_QF(int capacity) {
        super(capacity);
    }

    /*
     * 父节点就是根节点 时间复杂度O(1),因为合并的时候花了"心思",所以查的快,叫quick_find
     */
    public int find(int v) {
        rangeCheck(v);
        return parents[v];
    }

    /**
     * 将v1所在集合的所有元素,都指向v2的根节点 时间复杂度O(n)级别
     */
    public void union(int v1, int v2) {
        int p1 = find(v1);
        int p2 = find(v2);
        if (p1 == p2) return;  // 表明是同一个集合

        for (int i = 0; i < parents.length; i++) { // 遍历数组,发现属于同一个根节点的节点,把它们的根节点改成v2的根节点
            if (parents[i] == p1) {
                parents[i] = p2;
            }
        }
    }
}
