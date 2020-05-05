package jg.tree.union_find.union.qu;


// quick_union,基于Rank优化,加上路径分裂(Path Spliting)
// 使路径上的每个节点都指向其祖父节点（parent的parent）
public class UnionFind_QU_Rank_Path_Spliting extends UnionFind_QU_Rank {


    public UnionFind_QU_Rank_Path_Spliting(int capacity) {
        super(capacity);
    }

    // TODO why?
    @Override
    public int find(int v) {
        rangeCheck(v);
        while (parents[v] != v) {
            int p = parents[v];
            parents[v] = parents[parents[v]];  // 找到v的祖父节点,让v的父节点指向祖父节点.原来的父节点需要保留.父节点的操作如该节点的操作一样v = p
            v = p;
        }
        return v;
    }
}