package jg.graph.graph1;

import jg.graph.Graph;
import jg.graph.VertexVisitor;
import jg.linear_list.List;
import jg.linear_list.list.LinkedList;
import lombok.Data;


import java.util.ArrayList;
import java.util.Arrays;

// 数据对象集合+数据关系

// 图的逻辑结构是多对多的,图没有顺序存储结构,但是可以借助二维数组来表示元素间的关系
// 数组表示法:邻接矩阵
// 建立一个顶点表(记录各个顶点信息)和一个邻接矩阵(表示各个顶点之间的关系)

// 注: 在有向图的邻接矩阵中,
//    第i行含义: 以节点Vi为尾的弧(即出度边)
//    第i列含义: 以节点Vi为头的弧(即入度边)
//    相加就是顶点的度

// 邻接矩阵
// 优点
// 1.直观、简单、好理解
// 2.方便检查任一对顶点间是否存在边
// 3.方便找任一顶点的所有"邻接点"(有边直接相连的顶点)
// 4.方便计算任一顶点的"度"(从该点发出的边数为"出度",指向该点的边数为"入度")
//   无向图: 对应行(或列)非0元素的个数
//   有向图: 对应行非0元素的个数是"出度";对应列非0元素的个数是"入度"
// 缺点:
//   1.不便于增加和删除顶点
//   2.浪费空间-- 存稀疏图(点很多而边很少)有大量无效元素.对稠密图(特别是完全图)还是很合算的
//   3.浪费时间 -- 统计稀疏图中一共有多少条边
@Data
@SuppressWarnings({"unchecked", "unused"})
public class AdjacencyMatrixGraph<V, E> implements Graph<V, E> {
    // vertexList存储的顶点的下标,也是邻接矩阵二维数组的行列的下标;在构造器中初始化vertexList,在调用添加边时,先查询在vertexList中的下标然后再存储到邻接矩阵中
    private ArrayList<V> vertexList;        // 存储顶点列表
    private E[][] edges;                    // [弧尾][弧头]存储图对应的邻接矩阵,表示顶点之间相邻关系的矩阵(注意: 一般情况下默认行为弧尾,列为弧头,找到弧尾,交叉部分对应的列下标就是弧尾的下标)
    private int numOfEdges;                 // 表示边的数目,顶点数ArrayList已经有了
    public static final int NOT_FROUND = -1;


    public AdjacencyMatrixGraph(ArrayList<V> list) {
        int n = list.size();                    // 节点的个数
        this.vertexList = new ArrayList<>(n);   // 顶点值集合
        vertexList.addAll(list);                // 依次输入点的信息存入顶点表中
        this.edges = (E[][]) new Object[n][n];  // 邻接矩阵
        // 如果是数值应该初始化邻接矩阵,使每个权值初始化为最大值?如果使用包装类型默认为null也是不错的选择
        this.numOfEdges = 0;                    // 多少条边
        this.visited = new boolean[n];
    }

    // 邻接矩阵的遍历

    private boolean[] visited;   // 标记则能保证不会两次访问同一个顶点和边

    // 深度优先遍历
    // 理解:
    //    A   B   C   D   E
    //A   0   1   1   0   0
    //B   1   0   1   1   1
    //C   1   1   0   0   0
    //D   0   1   0   0   0
    //E   0   1   0   0   0
    // A - [0] B
    @Override
    public void dfs(V begin, VertexVisitor<V> visitor) {
        System.out.println(begin);
        int v = vertexIndex(begin);
        visited[v] = true;
        for (int w = 0; w < vertexList.size(); w++) {
            if (edges[v][w] != null && (!visited[w])) {
                dfs(vertexList.get(w), visitor);
            }
        }
    }

    @Override
    public void bfs(V begin, VertexVisitor<V> visitor) {

    }

    // 用邻接矩阵来表示图,遍历图中每一个顶点都要从头扫描该顶点所在的行,时间复杂度为O(n^2)
    // 用邻接表来表示图,虽然有2e个表姐的,但只需扫描e个节点即可完成遍历,加上访问n个头结点的时间,时间复杂度为(n+e)


    @Override
    public void addEdge(V from, V to) {
        this.addEdge(from, to, null);
    }

    /**
     * 添加边
     * 传入的顶点元素需要查询在vertexList列表中的下标位置,才能添加到邻接矩阵对应的下标位置
     * 优化: 如果发现顶点不存在的话,自动创建顶点,就无需手动再调用一次添加顶点的方法
     *
     * @param from   表示点的下标即是第几个顶点
     * @param to     表示第二个顶点对应的下标
     * @param weight 权值
     */
    @Override
    public void addEdge(V from, V to, E weight) {  // 增加节点之间的关系(增加边)

        // 判断from,to是否存在
        int f = vertexIndex(from);
        int t = vertexIndex(to);
        edges[f][t] = weight;
        edges[t][f] = weight;       // 无向图的双向
        this.numOfEdges++;          // 边数++
    }


    @Override
    public List<V> topologicalSort() {
        List<V> list = new LinkedList<>(); // 存放拓扑结果

        return list;
    }


    @Override
    public void removeEdge(V from, V to) {

    }

    @Override
    public void removeVertex(V v) {

    }

    // 边数量
    @Override
    public int edgesSize() {
        return 0;
    }

    @Override
    public int verticesSize() {
        return vertexList.size();
    }

    private int vertexIndex(V value) {
        return vertexList.indexOf(value); // 如何避免遍历找下标?提前存储在对象中
    }


    //返回节点的个数
    public int getNumOfVertex() {
        return this.vertexList.size();
    }

    //得到边的数目
    public int getNumOfEdges() {
        return this.numOfEdges;
    }

    //返回节点i下标对应的数据
    public V getValueByIndex(int i) {
        return this.vertexList.get(i);
    }

    //返回v1和v2的权值
    public E getWeight(int v1, int v2) {
        return this.edges[v1][v2];
    }

    //显示图对应的矩阵
    public void showGraph() {
        for (E[] link : this.edges) {
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

