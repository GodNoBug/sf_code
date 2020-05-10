package jg.graph.graph3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


// 链式存储结构: 很难用多重链表表示,转而使用邻接表,邻接多重表,十字链表
// 邻接表数组。我们可以使用一个以顶点为索引的列表数组，其中的每个元素都是和该顶点相邻的顶点列表
// 特点:
//  1.邻接表不唯一.当邻接表的存储结构形成后,图便唯一确定
//  2.若无向图有n个顶点,e条边,则其邻接表需n个头节点和2e个表结点.适宜存储稀疏图
//  3.有向图中,出度好求,入度难求(要么反过来);或者另存一个邻接表,存入度边
//     顶点Vi的出度为第i个单链表中的节点个数
//     顶点Vi的入度为整个单链表中邻接点阈值是i-1的节点个数
@SuppressWarnings({"unchecked", "unused"})
public class Graph<V, E> {
    private int vertexSize;                     // 顶点数目
    private int edgeSize;                       // 边的数目
    private Vertex<V, E>[] abj;                 // 邻接表,顶点按编号顺序将顶点数据存放在一维数组中;关联同一顶点的边用线性链表存储

    //private Vertex<V,E>[] abj2;                 // 逆邻接表,存有向图的入度边
    public Graph(ArrayList<V> arr, int vertexSize, int edgeSize) {
        init(arr, vertexSize, edgeSize);
    }

    public void init(ArrayList<V> arr, int vertexSize, int edgeSize) {
        this.vertexSize = vertexSize;
        this.edgeSize = edgeSize;
        this.abj = new Vertex[vertexSize];
        // 建立顶点表.异常输入点的信息,存入顶点表中,使每个表头结点的指针域初始化为null
        for (int i = 0; i < vertexSize; i++) {
            Vertex<V, E> vertex = new Vertex<>(arr.get(i), null);
            abj[i] = vertex;
        }
        // 建立邻接表,依次输入每条边所依附的两个顶点,确定两个顶点的序号i和j,建立边结点
        // 将此边结点分别插入到vi和vj对应的两个边链表的头部
        // 可以在此用输入流输入,但没必要,我选择手动调用方法区添加边
    }

    // 给定顶点,返回在邻接表中的下标
    private int vertexIndex(Vertex<V, E> vertex) {
        for (int i = 0; i < abj.length; i++) {
            if (vertex.value == abj[i].value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 向图中添加一条边 v-w,维护邻接表
     * 创建邻接表
     * @param from 顶点v
     * @param to   顶点w
     */
    public void addEdge(V from, V to, E weight) {
        // 依次输入每条边依附的两个顶点
        Vertex<V, E> v1 = new Vertex<>(from, null);
        Vertex<V, E> v2 = new Vertex<>(to, null);
        // 确定两个顶点的序号i和j,建立边结点
        int i = vertexIndex(v1); // from下标
        int j = vertexIndex(v2); // to下标
        // 头插法,将此边结点分别插入到Vi和Vj对应的两个边链表的头部.

        // 下标为i的顶点以from为起点.该边所指向的顶点的下标为j,
        abj[i].next = new Edge<>(j, abj[i].next, weight);
        // 下标为j的顶点to为起点.该边所指向的顶点的下标为i
        abj[j].next = new Edge<>(i, abj[j].next, weight);
        edgeSize++;
    }

    // 根据用户输入的value类型的顶点返回该顶点
    public Vertex<V, E> getVertex(V value) {
        for (int i = 0; i < vertexSize; i++) {
            if (abj[i].value.equals(value)) {
                return abj[i];
            }
        }
        return null;
    }

    public void print() {
        for (Vertex<V, E> vertex : abj) {
            System.out.println("===========from==========");
            System.out.println(vertex);
            Edge<E> edge = vertex.next;
            System.out.println("===========边==========");
            while (edge != null) {
                System.out.println(edge.weight);
                edge = edge.next;
            }
            List<Vertex<V, E>> adj = adj(vertex);
            System.out.println("===========to==========");
            System.out.println(adj);
            System.out.println("-----------------------");
        }
    }


    public int vertexSize() {
        return vertexSize;
    }

    public int edgeSize() {
        return edgeSize;
    }


    public List<Vertex<V, E>> adj(Vertex<V, E> v) {
        List<Vertex<V, E>> list = new LinkedList<>();
        int index = vertexIndex(v);
        Edge<E> edge = abj[index].next;
        while (edge != null) {
            list.add(abj[edge.adjvex]);
            edge = edge.next;
        }
        return list;
    }


    // 邻接表头结点
    private static class Vertex<V, E> {
        V value;       // 顶点内容
        Edge<E> next;  // 指向第一条依附该顶点的边的指针

        public Vertex(V value, Edge<E> next) {
            this.value = value;
            this.next = next;
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
        int adjvex;// AdjacencyVertex 邻接点域,存放于Vi邻接的顶点在表头数组中的位置(该边所指向的顶点的位置)
        Edge<E> next;// 链域,指示下一条边或弧
        E weight;  // 权值,和边相关的信息

        public Edge(int adjvex, Edge<E> next, E weight) {
            this.adjvex = adjvex;
            this.next = next;
            this.weight = weight;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("vertexSize=").append(vertexSize).append(", edgeSize=").append(edgeSize).append("\n");

        for (int i = 0; i < vertexSize; i++) {
            Vertex<V, E> vertex = abj[i];
            sb.append(vertex).append("的出度为:");
            for (Vertex<V, E> v : this.adj(vertex)) {
                sb.append(v).append(" \n");
            }
            sb.append("-----------------------------");
        }
        return sb.toString();
    }
}
// 邻接表
// 优点:
//  1.方便找任一顶点的所有"邻接点"
//  2.节约稀疏图的空间
//     需要N个头指针+2E个结点(每个结点至少2个域)
//  3.对于计算顶点的度,对于无向图说很方便,但是对有向图来说不方便(还要考虑出度入度问题.只能计算出度,需要构造逆邻接表[存指向自己的边]来计算入度)
//  4.不方便检查任意一对顶点之间是否存在边