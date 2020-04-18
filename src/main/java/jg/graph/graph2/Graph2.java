package jg.graph.graph2;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

//图的深度优先搜索(Depth First Search) 。
// 1)深度优先遍历，从初始访问结点出发，初始访问结点可能有多个邻接结点，
//  深度优先遍历的策略就是首先访问第一个邻接结点，然后再以这个被访问的邻
//  接结点作为初始结点，访问它的第一个邻接结点， 可以这样理解：每次都在
//  访问完当前结点后首先访问当前结点的第一个邻接结点。
// 2)我们可以看到，这样的访问策略是优先往纵向挖掘深入，而不是对一个结点
//  的所有邻接结点进行横向访问。
// 3)显然，深度优先搜索是一个递归的过程


@Data
public class Graph2 {
    private ArrayList<String> vertexList;   // 存储顶点列表
    private int[][] edges;                  // 存储图对应的邻接矩阵,表示顶点之间相邻关系的矩阵
    private int numOfEdges;                 // 表示边的数目
    private boolean[] isVisited;            // 记录某个节点是否被访问

    public static void main(String[] args) {
        int n = 5; //节点的个数
        Graph2 graph = new Graph2(n);
        // 添加顶点
        String[] vertexes = {"A", "B", "C", "D", "E"};
        for (String vertex : vertexes) {
            graph.insertVertex(vertex);
        }
        // 添加边
        // A-B A-C B-C B-D B-E
        graph.insertEdge(0, 1, 1);
        graph.insertEdge(0, 2, 1);
        graph.insertEdge(1, 2, 1);
        graph.insertEdge(1, 3, 1);
        graph.insertEdge(1, 4, 1);

        graph.showGraph();
        graph.des();
    }

    // 深度优先遍历算法步骤
    // 1)访问初始结点v，并标记结点v为已访问。
    // 2)查找结点v的第一个邻接结点w。
    // 3)若w存在，则继续执行4，如果w不存在，则回到第1步，将从v的下一个结点继续。
    // 4)若w未被访问，对w进行深度优先遍历递归（即把w当做另一个v，然后进行步骤123）。
    // 5)查找结点v的w邻接结点的下一个邻接结点，转到步骤3。

    // 对dfs进行一个重载,遍历所有节点,
    public void des() {
        // 遍历所有的节点,进行dfs[回溯]
        for (int i = 0; i < getNumOfVertex(); i++) {
            if (!isVisited[i]) {
                dfs(isVisited, i);
            }
        }
    }

    // TODO 算法理解:
    //
    // 深度优先遍历算法
    // i 第一次是0
    private void dfs(boolean[] isVisited, int i) {
        // 首先我们先访问该节点,输出
        System.out.println(getValueByIndex(i) + "->");
        // 将节点设置为已经访问过
        isVisited[i] = true;
        // 查找节点v的第一个邻接节点w
        int w = getFirstNeighbor(i);
        while (w != -1) { // 说明有邻接节点
            if (!isVisited[w]) {
                dfs(isVisited, w);
            }
            // 如果w节点已经被访问过
            w = getNextNeighbor(i, w);
        }
    }

    //    A   B   C   D   E
    //A   0   1   1   0   0
    //B   1   0   1   1   1
    //C   1   1   0   0   0
    //D   0   1   0   0   0
    //E   0   1   0   0   0
    //得到第一个邻接节点的下标w index是
    private int getFirstNeighbor(int index) {
        for (int j = 0; j < vertexList.size(); j++) {
            if (edges[index][j] > 0) {
                return j;
            }
        }
        return -1;
    }

    //    A   B   C   D   E
    //A   0   1   1   0   0
    //B   1   0   1   1   1
    //C   1   1   0   0   0
    //D   0   1   0   0   0
    //E   0   1   0   0   0
    // 根据前一个邻接节点的下标来获取下一个邻接节点
    private int getNextNeighbor(int v1, int v2) {
        for (int j = v2 + 1; j < vertexList.size(); j++) {
            if (edges[v1][j] > 0) {
                return j;
            }
        }
        return -1;
    }


    //    A   B   C   D   E
    //A   0   1   1   0   0
    //B   1   0   1   1   1
    //C   1   1   0   0   0
    //D   0   1   0   0   0
    //E   0   1   0   0   0
    //
    ////说明
    ////(1) 1 表示能够直接连接
    ////(2) 0 表示不能直接连接


    public Graph2(int n) {
        this.edges = new int[n][n];             // 边的矩阵
        this.vertexList = new ArrayList<>(n);   // 顶点值
        this.numOfEdges = 0;                    // 多少条边
        this.isVisited = new boolean[n];          // 节点是否被访问
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
