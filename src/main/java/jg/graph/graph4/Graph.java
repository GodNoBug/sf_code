package jg.graph.graph4;

// 十字链表
// 邻接表缺陷改进
//       有向图缺点是求节点的度困难 -> 十字链表
//       无向图缺点是每条边都要存储 -> 邻接多重表
// 十字链表(Orthogonal List)是有向图的另一种链式存储结构.我们可以把它看成是将有向图的邻接表和逆邻接表结合起来形成的一种链表
// 有向图中的每一条弧对应十字链表中的一个弧节点,同时有向图的每个顶点在十字链表中对应有一个节点,叫做顶点节点
@SuppressWarnings({"unchecked", "unused"})
public class Graph {

    // 邻接表头结点
    private static class Vertex<V, E> {
        V value;          // 顶点内容
        Edge<E> firstIn;  // 入边表(顶点为弧尾):指向第一条依附该顶点的入度边的指针
        Edge<E> firstOut; // 出边表(顶点为弧头)指向第一条依附该顶点的出度边的指针

        public Vertex(V value, Edge<E> next) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "value=" + value +
                    '}';
        }
    }

    // 邻接表表结点,边结点
    private static class Edge<E> {
        E weight;  // 权值,和边相关的信息
        Edge<E> headLink;   // 指向下一个终点相同的邻接点
        Edge<E> adjLink;    // 指向下一个起点相同的邻接点
        int tailVex = -1;   // (弧头)弧的起点在顶点数组中的下标
        int headVex = -1;   // (弧尾)弧终点在顶点数组中的下标,adjvex改为弧尾位置
    }
}
