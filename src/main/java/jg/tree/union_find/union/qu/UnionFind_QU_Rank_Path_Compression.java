package jg.tree.union_find.union.qu;


// quick_union,基于Rank优化,加上路径压缩
// 虽然有了基于rank的优化,树会相对平衡一点
// 但是随着union次数的增多,树的高度依然会越来越高
// -导致find操作变慢,尤其是底层节点(因为find操作是不断往上查找根节点的)

//  路径压缩使得路径上的所有节点都指向根节点,所以实现成本稍高

public class UnionFind_QU_Rank_Path_Compression extends UnionFind_QU_Rank {


    public UnionFind_QU_Rank_Path_Compression(int capacity) {
        super(capacity);
    }

    // 在find时使路径上的所有节点都指向根节点,从而降低树的高度
    @Override
    public int find(int v) { // v==1  parents[v]==2
       rangeCheck(v);
       if (parents[v]!=v){
           parents[v]=find(parents[v]);  // parents[v]==2 ->parents[v]==4
       }
       return parents[v];
    }
}
//    路径压缩使得路径上的所有节点都指向根节点,所以实现成本稍高
//    还有两种更优的做法,不但能降低树高,事先成本也比路径压缩嫡
//    -路径分裂(Path Spliting)
//    -路径减半(Path Halving)
//    路径分裂和路径减半的效率差不多,但都比路径压缩要好