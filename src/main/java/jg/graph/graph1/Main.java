package jg.graph.graph1;

import jg.graph.Graph;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    @Test
    public void graph(){
        String[] vertexes = {"A", "B", "C", "D", "E"};
        ArrayList<String> list = new ArrayList<>(Arrays.asList(vertexes));
        Graph<String,Integer> graph = new AdjacencyMatrixGraph<>(list);
        createGraph1(graph);


        graph.dfs("A",null);
       // graph.showGraph();
    }

    private void createGraph1(Graph<String, Integer> graph){
        // 添加边
        // A-B A-C B-C B-D B-E
        // 因为用数组加标来添加不直观,可是用字母来操作是直观,但是indexOf复杂度就上来了
        graph.addEdge("A","B",1);
        graph.addEdge("A","C",1);
        graph.addEdge("B","C",1);
        graph.addEdge("B","D",1);
        graph.addEdge("B","E",1);
    }
    private  void createGraph2(Graph<String, Integer> graph){
        graph.addEdge("V1","V0",9);
        graph.addEdge("V1","V2",3);
        graph.addEdge("V2","V0",2);
        graph.addEdge("V2","V3",5);
        graph.addEdge("V3","V4",1);
        graph.addEdge("V0","V4",6);

    }
    @Test
    public void graph2(){
        int n = 5; //节点的个数
        Graph2<String> graph = new Graph2<>(n);
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


    @Test
    public void graph3(){
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
}
