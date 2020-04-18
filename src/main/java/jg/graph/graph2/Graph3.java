package jg.graph.graph2;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
//广度优先遍历基本思想
// 图的广度优先搜索(Broad First Search) 。
//   类似于一个分层搜索的过程，广度优先遍历需要使用一个队列以保持访问过的结点的顺序，
// 以便按这个顺序来访问这些结点的邻接结点
//
//广度优先遍历算法步骤
// 1)访问初始结点v并标记结点v为已访问。
// 2)结点v入队列
// 3)当队列非空时，继续执行，否则算法结束。
// 4)出队列，取得队头结点u。
// 5)查找结点u的第一个邻接结点w。
// 6)若结点u的邻接结点w不存在，则转到步骤3；否则循环执行以下三个步骤：
//  6.1 若结点w尚未被访问，则访问结点w并标记为已访问。
//  6.2 结点w入队列
//  6.3 查找结点u的继w邻接结点后的下一个邻接结点w，转到步骤6。


@Data
public class Graph3 {
    private ArrayList<String> vertexList;   // 存储顶点列表
    private int[][] edges;                  // 存储图对应的邻接矩阵,表示顶点之间相邻关系的矩阵
    private int numOfEdges;                 // 表示边的数目
    private boolean[] isVisited;            // 记录某个节点是否被访问

    public static void main(String[] args) {
        int n = 8; //节点的个数
        Graph3 graph = new Graph3(n);
        // 添加顶点
        String[] vertexes = {"1", "2", "3", "4", "5","6","7","8"};
        for (String vertex : vertexes) {
            graph.insertVertex(vertex);
        }
        // 添加边
        graph.insertEdge(0, 1, 1);
        graph.insertEdge(0, 2, 1);
        graph.insertEdge(1, 3, 1);
        graph.insertEdge(1, 4, 1);
        graph.insertEdge(3, 7, 1);
        graph.insertEdge(4, 7, 1);
        graph.insertEdge(2, 5, 1);
        graph.insertEdge(2, 6, 1);
        graph.insertEdge(5, 6, 1);

        graph.showGraph();
        graph.bfs();

    }


    // 遍历所有节点,都进行广度优先搜索
    public void bfs() {
        //
        for (int i = 0; i < getNumOfVertex(); i++) {
            if (!isVisited[i]) {
                bfs(isVisited, i);
            }
        }
    }

    // 对一个节点进行广度优先遍历的方法
    private void bfs(boolean[] isVisited, int i) {
        int u; // 表示队列的头节点对应的下标
        int w; // 邻接节点w
        // 队列,节点访问顺序
        LinkedList<Integer> queue = new LinkedList<>();
        // 访问这个节点,输出节点信息
        System.out.print(getValueByIndex(i) + "=>");
        // 标记为已访问
        isVisited[i] = true;
        // 将节点加入队列
        queue.addLast(i);
        while (!queue.isEmpty()) {
            // 取出队列的头节点下标
            u = queue.removeFirst();
            // 得到第一个邻接节点的下标w
            w = getFirstNeighbor(u);
            while (w != -1) { // 找到
                // 是否访问过
                if (!isVisited[w]) {
                    System.out.print(getValueByIndex(w) + "=>");
                    // 标记已经访问
                    isVisited[w] = true;
                    // 入队
                    queue.addLast(w);
                }
                // 以u为前驱点,找w后面的下一个联结点
                w = getNextNeighbor(u, w); // 体现出我们的广度优先
            }
        }

    }

    private int getNextNeighbor(int v1, int v2) {
        for (int j = v2 + 1; j < vertexList.size(); j++) {
            if (edges[v1][j] > 0) {
                return j;
            }
        }
        return -1;
    }

    //得到第一个邻接节点的下标w index是
    private int getFirstNeighbor(int index) {
        for (int j = 0; j < vertexList.size(); j++) {
            if (edges[index][j] > 0) {
                return j;
            }
        }
        return -1;
    }


    public Graph3(int n) {
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
