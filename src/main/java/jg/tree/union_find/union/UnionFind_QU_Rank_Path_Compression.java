package jg.tree.union_find.union;

/**
 * quick_union,基于Rank优化,加上路径压缩
 * 路径压缩使得路径上的所有节点都指向根节点,所以实现成本稍高
 * 还有两种更优的做法,不但能降低树高,事先成本也比路径压缩嫡
 * -路径分裂(Path Spliting)
 * -路径减半(Path Halving)
 * 路径分裂和路径减半的效率差不多,但都比路径压缩要好
 */
public class UnionFind_QU_Rank_Path_Compression extends UnionFind_QU_Rank {


    public UnionFind_QU_Rank_Path_Compression(int capacity) {
        super(capacity);
    }

    // TODO why?
    @Override
    public int find(int v) { // v==1  parents[v]==2
       rangeCheck(v);
       if (parents[v]!=v){
           parents[v]=find(parents[v]);  // 结果4
       }
       return parents[v];
    }
}
// 在Union的过程中,可能会出现树不平衡的状况.甚至退化成链表
// find操作从O(logn)逐步变成O(n)
// 有两种常见的优化方案
// 1.基于size的优化: 元素少的树 嫁接到 元素多的树
// 2.基于rank的优化: 矮的树 嫁接到 高的树