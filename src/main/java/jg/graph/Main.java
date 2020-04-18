package jg.graph;

import jg.graph.data.Data;
import org.junit.Test;

public class Main {
    @Test
    public void createUndirectedGraph(){
        // 有向图,优先实现优先图,因为向下兼容无向图:再原来边加个反方向既可以

        // 添加边.首先它前提是由顶点,得考虑在添加边的时候,如果顶点不存在,直接先新添顶点
        ListGraph<String,Integer> graph =new ListGraph<>();
        graph.addEdge("V1","V0",9);
        graph.addEdge("V1","V2",3);
        graph.addEdge("V2","V0",2);
        graph.addEdge("V2","V3",5);
        graph.addEdge("V3","V4",1);
        graph.addEdge("V0","V4",6);

        graph.removeVertex("V0");


        //graph.removeEdge("v0","v4");
        System.out.println("边的个数:"+graph.edgesSize());
        System.out.println("顶点的个数"+graph.verticesSize());
        graph.print();
    }
    @Test
    public void createDirectedGraph(){
        ListGraph<String,Integer> graph =new ListGraph<>();
        graph.addEdge("v0","v1");
        graph.addEdge("v1","v0");

        graph.addEdge("v0","v2");
        graph.addEdge("v2","v0");

        graph.addEdge("v2","v1");
        graph.addEdge("v1","v2");

        graph.addEdge("v2","v3");
        graph.addEdge("v3","v2");

        graph.print();
    }

    @Test
    public void bfs(){
        // 无向图
        Graph<Object, Double> graph = undirectedGraph(Data.BFS_01);
        graph.bfs("A");
        // 有向图
        graph = directedGraph(Data.BFS_02);
        graph.bfs(7);
    }

    @Test
    public void dfs(){
        Graph<Object, Double> graph = undirectedGraph(Data.DFS_01);
        graph.dfs(1);
        graph=directedGraph(Data.DFS_02);
        graph.dfs("a");
    }

    /**
     * 有向图
     */
    private static Graph<Object, Double> directedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph<>();
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
     * @param data
     * @return
     */
    private static Graph<Object, Double> undirectedGraph(Object[][] data) {
        Graph<Object, Double> graph = new ListGraph<>();
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
