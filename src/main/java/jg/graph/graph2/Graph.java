package jg.graph.graph2;

import lombok.Data;


import java.util.ArrayList;
import java.util.Arrays;

// 图的概念
// 图(Graph) G =(V,E)
//  V: 顶点(Vertex/Vertices)的有穷非空集合
//  E: 边的有穷集合
// 有向图/无向图: 每条边都是有/无方向的
// 完全图: 任意两个点都有一条边相连.包含有向完全图和无向完全图
//          无向完全图: n个顶点,n(n-1)/2 条边
//          有向完全图: n个顶点,n(n-1)条边
// 顶点的"邻接": 如果两个点之间有一条边,那么说两个点是邻接的
//        有边/弧相连的两个顶点之间的关系
//        存在(Vi,Vj),则称Vi和Vj互为邻接点;
//        存在<Vi,Vj>,则称Vi邻接到Vj,Vj邻接与Vi
// 关联(依附):边/弧与顶点之间的关系.存在(Vi,Vj)或<Vi,Vj>,则称该边/弧关联与Vi和Vj
// 顶点的度: 与该顶点相关联的边的数目,记为TD(v)
//         在有向图中,顶点的度大于该顶点的入度和出度之和.
//         顶点 v的入度是以v为终点的有向边条数,记作ID(v)
//         顶点 v的出度是以v为终点的有向边条数,记作OD(v)
// 稀疏图: 有很少边或弧的图(e<n*logn)
// 浓密图: 有较多边或弧的图
// 网:    边/弧带权的图
// 问: 当有向图中仅1个顶点的入度都为0,其余顶点的入度都均为1,此时是何形状? 答案:树,而且是一颗有向树

// 图的表示方式
// 1.二维数组表示(邻接矩阵)
// 2.链表表示(邻接)

// 邻接矩阵:是标识图形中顶点相邻关系的矩阵,对于n个顶点的图而言,矩阵是row和col表示的是1...n个点.
//   对于每条边(u,v),设置arr[u][v]=权
// 链接表:
//   1) 链接矩阵需要为每一个顶点分配n个边的空间,其实有很多边都是不存在,会造成空间的一定损失
//   2) 链接的实现只关心存在的边,不关心不存在的边.因此没有空间浪费,邻接表由数组+链表组成
//      数组位:顶点
//      数组下挂链表:链表表示能连接的地方


// 定义无向图
@Data
public class Graph {
    private ArrayList<String> vertexList;   // 存储顶点列表
    private int[][] edges;                  // [表示点的下标即是第几个顶点][表示第二个顶点对应的下标]存储图对应的邻接矩阵,表示顶点之间相邻关系的矩阵
    private int numOfEdges;                 // 表示边的数目

    public static void main(String[] args) {
        // TODO 存边,存对应的点
        int n = 5; // 节点的个数
        Graph graph = new Graph(n);
        // 添加顶点
        String[] vertexes = {"A", "B", "C", "D", "E"};
        for (String vertex : vertexes) {
            graph.insertVertex(vertex);
        }
        // 添加边
        // A-B A-C B-C B-D B-E
        graph.insertEdge(0,1,1);
        graph.insertEdge(0,2,1);
        graph.insertEdge(1,2,1);
        graph.insertEdge(1,3,1);
        graph.insertEdge(1,4,1);

        graph.showGraph();

    }

    public Graph(int n) {
        this.edges = new int[n][n];             //边的矩阵
        this.vertexList = new ArrayList<>(n);   //顶点值
        this.numOfEdges = 0;                    //多少条边
    }

    // 插入节点
    public void insertVertex(String vertex) {
        vertexList.add(vertex);
    }

    /**
     * 添加边
     *
     * @param v1     表示点的下标即是第几个顶点
     * @param v2     表示第二个顶点对应的下标
     * @param weight 权值
     */
    public void insertEdge(int v1, int v2, int weight) { // 增加节点之间的关系,增加边
        edges[v1][v2] = weight;
        edges[v2][v1] = weight;     // 无向图的双向
        this.numOfEdges++;          // 边数++
    }

    //图中常用 的方法
    //返回节点的个数
    public int getNumOfVertex() {
        return this.vertexList.size();
    }

    //得到边的数目
    public int getNumOfEdges() {
        return this.numOfEdges;
    }

    //返回节点i下标对应的数据
    public String getValueByIndex(int i) {
        return this.vertexList.get(i);
    }

    //返回v1和v2的权值
    public int getWeight(int v1, int v2) {
        return this.edges[v1][v2];
    }

    //显示图对应的矩阵
    public void showGraph() {
        for (int[] link : this.edges) {
            System.err.println(Arrays.toString(link));
        }
    }
}
// 遍历定义:
//   从已给的连通图中某一顶点出发,沿着一些访问图中所有的顶点,
//且使每个顶点仅被访问一次,叫做图的遍历,它是图的基本运算.遍历
//的实质就是找到某个顶点的邻接点的过程.
// 图的特点:
//   图中可能存在回路,且图的任一顶点都可能与其它顶点相通,在访问
//完某个顶点之后可能会沿着某些边 又回到了曾经访问过的顶点.怎么样
//才能避免:设置辅助数组boolean[] isVisited,用来标记某个被访
//问过的顶点.(0或1,false或true)防止被多次访问

//深度优先搜索(DFS)
// 一条路走到黑,走过没有路往回退,退到有路继续走,直到走完所有节点.
//方法:
// 1.在访问图中某一起点V后,由V出发,访问它的任一邻接顶点W1;
// 2.再从W1出发,访问与W1邻接但还未被访问过的顶点W2
// 3.然后再从W2出发,进行类似的访问, ...
// 4.如此进行下去,直至到达所由的邻接节点都被访问过的顶点U为止.
// 5.接着,退回一步,退到前一次刚访问过的顶点,看是否还有其他没有被访问的邻接顶点.
//   如果有,则访问此顶点,之后再从此顶点出发,进行与前述类似的访问.
//   如果没有,就再退回一步进行搜索.重复上述过程,知道连通图中所有顶点被访问过为止.



//广度优先搜索(BFS)

