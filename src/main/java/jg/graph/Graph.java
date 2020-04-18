package jg.graph;


/**
 * 邻接矩阵:(适合稠密图,边比较多才能把素组充分利用起来,)
 * -用一维数组存放顶点信息
 * -用二维数组存放边信息:边和边的排列组合,交叉点的值就是权值,无穷大代表没有边.无权值可以用1,0表示存在边与否
 * 邻接表:
 * - 顶点信息有next  TODO
 */
public interface  Graph<V,E> {
    // 获取顶点/边大小
    int verticesSize();
    int edgesSize();
    // 添加顶点/边
    void addVertex(V v);
    void addEdge(V from,V to);
    void addEdge(V from,V to,E weight);
    // 删除顶点/边
    void removeVertex(V v);
    void removeEdge(V from,V to);
    void bfs(V begin);
    void dfs(V begin);
}
