package jg.graph.graph2;

import jg.graph.data.Data;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    @Test
    public void createUndirectedGraph() {
        // 有向图,优先实现有向图,因为向下兼容无向图:再原来边加个反方向既可以

        // 添加边.首先它前提是由顶点,得考虑在添加边的时候,如果顶点不存在,直接先新添顶点
        ListGraph<String, Integer> graph = new ListGraph<>();
        graph.addEdge("V1", "V0", 9);
        graph.addEdge("V1", "V2", 3);
        graph.addEdge("V2", "V0", 2);
        graph.addEdge("V2", "V3", 5);
        graph.addEdge("V3", "V4", 1);
        graph.addEdge("V0", "V4", 6);

        graph.removeVertex("V0");


        //graph.removeEdge("v0","v4");
        System.out.println("边的个数:" + graph.edgesSize());
        System.out.println("顶点的个数" + graph.verticesSize());
        graph.print();
    }

    @Test
    public void createDirectedGraph() {
        ListGraph<String, Integer> graph = new ListGraph<>();
        graph.addEdge("v0", "v1");
        graph.addEdge("v1", "v0");

        graph.addEdge("v0", "v2");
        graph.addEdge("v2", "v0");

        graph.addEdge("v2", "v1");
        graph.addEdge("v1", "v2");

        graph.addEdge("v2", "v3");
        graph.addEdge("v3", "v2");

        graph.print();
    }

    @Test
    public void bfs() {
        // 无向图
        Graph<Object, Double> graph = undirectedGraph(Data.BFS_01);
        graph.bfs("A", (Object v) -> {
            System.out.println(v);
            return false;
        });
        // 有向图
        graph = directedGraph(Data.BFS_02);
        graph.bfs(0, (Object v) -> {
            System.out.println(v);
            return v.equals(2);
        }); // 7 进不去其他节点,所以只输出7
    }

    @Test
    public void dfs() {
        Graph<Object, Double> graph = undirectedGraph(Data.DFS_01);
        //graph.dfs(1);
        graph = directedGraph(Data.DFS_02);
        //graph.dfs("a");
    }

    @Test
    public void topologicalSort() {
        Graph<Object, Double> graph = directedGraph(Data.TOPO);
        List<Object> list = graph.topologicalSort();
        System.out.println(list);
    }

    public static final Graph.WeightManager<Double> weightManager = new Graph.WeightManager<Double>() {
        public int compare(Double w1, Double w2) {
            return w1.compareTo(w2);
        }

        public Double add(Double w1, Double w2) {
            return w1 + w2;
        }

    };

    @Test
    public void mst() {
        Graph<Object, Double> graph = undirectedGraph(Data.MST_01);
        Set<Graph.EdgeInfo<Object, Double>> infos = graph.mst();
        for (Graph.EdgeInfo<Object, Double> info : infos) {
            System.out.println(info);
        }
    }
    @Test
    public void sp(){
        Graph<Object, Double> graph = undirectedGraph(Data.SP);
        Map<Object, Double> sp = graph.shortestPath("A");

        System.out.println(sp);
    }

    /**
     * 有向图
     */
    private static Graph<Object, Double> directedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph<>(weightManager);
        for (Object[] edge : data) { // 二维数组的小数组 0号位置为起点,1号位置为终点,2号位置为权值
            if (edge.length == 1) {             //1.只有顶点没有边
                graph.addVertex(edge[0]);
            } else if (edge.length == 2) {      //2.没有权值
                graph.addEdge(edge[0], edge[1]);
            } else if (edge.length == 3) {      //3.全具备
                double weight = Double.parseDouble(edge[2].toString());
                graph.addEdge(edge[0], edge[1], weight);
            }
        }
        return graph;
    }

    /**
     * 无向图
     *
     * @param data
     * @return
     */
    private static Graph<Object, Double> undirectedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph<>(weightManager);
        for (Object[] edge : data) {
            if (edge.length == 1) {
                graph.addVertex(edge[0]);
            } else if (edge.length == 2) {
                graph.addEdge(edge[0], edge[1]);
                graph.addEdge(edge[1], edge[0]);
            } else if (edge.length == 3) {
                double weight = Double.parseDouble(edge[2].toString());
                graph.addEdge(edge[0], edge[1], weight);
                graph.addEdge(edge[1], edge[0], weight);
            }
        }
        return graph;
    }

}
