package jg.graph;

import java.util.*;
import java.util.function.Consumer;

/**
 * 图
 * 1.邻接矩阵(适合稠密图,不然会比较浪费内存)
 * -  一维数组存放顶点信息
 * -  二维数组存放边信息  (顶点之间的关系,邻接矩阵本体)
 * 邻接矩阵是表示图形中顶点之间相邻关系的矩阵.二维数组表示.邻接矩阵需要为每个顶点都分配n个边的空间,其实有很多边是不存在,会造成空间的一定损失
 *
 * 2.邻接表
 * - 邻接表的实现只关心存在的边,不关心不存在的边.因此没有空间浪费,邻接表由数组链表组成
 * - 数组下标代表某个顶点,而数组下标下存储的链表代表相关联的顶点
 *
 * 和邻接表的折中方案
 * 更偏向与邻接表
 *
 * 顶点Vertex: 元素,以此为起点的边,以此起点为终点的边
 * 边Edge: 起点,终点,权值 [代表顶点的关系]
 *
 * @param <V> 顶点元素
 * @param <E> 权值
 */
@SuppressWarnings("unchecked")
public class ListGraph<V, E> implements Graph<V, E> {



    // 顶点集合 以"元素-顶点"形式 存储
    private Map<V, Vertex<V, E>> vertices = new HashMap<>();
    // 边集 顶点间的关系集合,类似邻接表和邻接矩阵
    private Set<Edge<V, E>> edges = new HashSet<>();

    // 一个顶点: 元素,以此为起点的边,以此起点为终点的边
    private static class Vertex<V, E> {
        V value;      // 顶点包含的元素
        Set<Edge<V, E>> inEdges = new HashSet<>();// 以该顶点为起点的边,之所以用HashSet.因为储存不考虑顺序性
        Set<Edge<V, E>> outEdges = new HashSet<>();// 以该顶点为终点的边

        public Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex<?, ?> vertex = (Vertex<?, ?>) o;
            return Objects.equals(value, ((Vertex<V, E>) o).value) &&
                    Objects.equals(inEdges, vertex.inEdges) &&
                    Objects.equals(outEdges, vertex.outEdges);
        }

        @Override
        public int hashCode() {
            return value == null ? 0 : value.hashCode();
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    // 一条边: 起点,终点,权值
    private static class Edge<V, E> {
        Vertex<V, E> from;  // 起点
        Vertex<V, E> to;    // 终点
        E weight;     // 权值

        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        public Edge(Vertex<V, E> from, Vertex<V, E> to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o) {
            Edge<V, E> edge = (Edge<V, E>) o;
            return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return from.hashCode() * 31 + to.hashCode();
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }

    /*----------------------------------------------------打印--------------------------------------------------------*/
    public void print() {
        System.out.println("===========顶点==========");
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            System.out.println(v);
            // System.out.println("in------------");
            System.out.println(vertex.inEdges);
            // System.out.println("on------------");
            System.out.println(vertex.outEdges);
        });
        System.out.println("===========边==========");
        edges.forEach(System.out::println);
    }
    /*----------------------------------------------------大小--------------------------------------------------------*/
    // 大小
    @Override
    public int verticesSize() {
        return vertices.size();
    }

    @Override
    public int edgesSize() {
        return edges.size();
    }

    /*----------------------------------------------------添加--------------------------------------------------------*/
    // 添加边
    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v)) return;
        vertices.put(v, new Vertex<>(v));
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    /**
     * 添加一条边:
     * 1.要创建Edge维护起点,终点,权值三个.前提是这条边对应的两个端点要存在
     * 2.维护两个Vertex端点的[起点的]"出度"还有[终点的]"入度"
     *
     * @param from   起点元素,
     * @param to     终点元素
     * @param weight 权值
     */
    @Override
    public void addEdge(V from, V to, E weight) {
        // 判断from,to顶点是否存在?因为要在顶点存在的前提才能添加
        Vertex<V, E> fromVertex = vertices.get(from);
        // 根据元素从顶点集中取出对应的边,如果不存在则创建一个,并存入顶点集
        if (fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }

        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }
        // 到这里fromVertex/toVertex顶点都有值
        // 以fromVertex为起点的边是否包含,
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex, weight);

        // edges是HashSet不好去取出来更新权值,干脆删除再创建.
        // 删除正向的边,再删除逆向的边
        if (fromVertex.outEdges.remove(edge)) { // 尝试去删除,有则删除成功,成功再去删除另外的
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }

        if (fromVertex.outEdges.add(edge)) {
            toVertex.inEdges.add(edge);
            edges.add(edge);
        }
    }

    /*----------------------------------------------------删除--------------------------------------------------------*/
    /**
     * 根据元素删除顶点,将顶点关联的边也删除掉
     * @param v 元素
     */
    @Override
    public void removeVertex(V v) {

        // 删除顶点集中的边
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex == null) return;

        // 出边: 遍历这个顶点的"出边",删除这条边终点的"入边",在边集中删除这条边
        for (Iterator<Edge<V, E>> iterator = vertex.outEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            edge.to.inEdges.remove(edge);
            iterator.remove();// 将当前遍历到的元素从outEdges删除
            edges.remove(edge);
        }

        // 入边: 遍历这个顶点的"入边",删除这条边终点的"出边",在边集中删除这条边
        for (Iterator<Edge<V, E>> iterator = vertex.inEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            edge.from.outEdges.remove(edge);
            iterator.remove();// 将当前遍历到的元素从inEdges删除
            edges.remove(edge);
        }
    }

    /**
     * 删除边,需要按起点元素和终点来删除边. [保留顶点]
     *
     * 1.传进来的起点或终点不存在的话,那更谈不上有边了
     * 2.删除边集的edges
     * 3.删除两个顶点集包含相应的出边/入边
     * @param from 起点元素
     * @param to   终点元素
     */
    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) return;

        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) return;

        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);

        // 分别删除两个端点出度和入度,删除边
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
    }
    /*----------------------------------------------------遍历--------------------------------------------------------*/
    // 图
    // 图遍历需要起点,类比树,以起点为"根节点",离根节点"最近"[一根线能访问到,且未访问过的]为下一层
    // 1.应该会有标记,去标记已经访问过的
    //
    /**
     * 广度优先搜索
     * 类似二叉树的层序遍历,它就是一种广度优先搜索[多叉树也可以这样]
     *
     * 要素: Queue队列
     * @param begin 开始元素
     */
    public void bfs(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        // 用set存储访问过的顶点,只要进过队列,我们就应该添加到集合中.
        // 而不是在出队输出的时候,因为这样会导致还没有输出的重复入队
        Set<Vertex<V, E>> visited = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        // 直接将起点入队
        queue.offer(beginVertex);
        visited.add(beginVertex);

        while (!queue.isEmpty()) {
            // 从堆头中拿取一个顶点,打印输出值
            Vertex<V, E> vertex = queue.poll();
            System.out.println(vertex.value);
            for (Edge<V, E> edge : vertex.outEdges) {
                // 排除已经遍历的元素,获取你完全可以在Vertex类中添加一个visited变量来标明是否被访问过
                if (!visited.contains(edge.to)) {
                    queue.offer(edge.to);
                    visited.add(edge.to);
                }
            }
        }
    }

    /**
     * 深度优先搜索
     * 二叉树的前序遍历就是一种深度优先搜索,一路走到底,有多深走多深.发现已经不能再走,已经最深了.
     * 往回退发现有另外一个分支.顺着这个分支有多深走多深.以此类推,所有路径都尝试过
     *
     * @param begin 开始元素
     */
    public void dfs(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        Set<Vertex<V, E>> visited = new HashSet<>();
        if (beginVertex == null) return;
        dfs(beginVertex, visited);
    }

    /**
     * System.out.println(vertex.value);
     * for (Edge<V, E> edge : vertex.outEdges) {
     * dfs(edge.to);
     * }
     * 这样的大体结构是出来了,缺记录访问过的顶点,应该在递归中传递集合Set<Vertex<V,E>>
     *
     * @param vertex
     */
    private void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> visited) {
        // 递归自然会往回退
        System.out.println(vertex.value);
        visited.add(vertex);
        for (Edge<V, E> edge : vertex.outEdges) {
            if (!visited.contains(edge.to)) {
                dfs(edge.to, visited);
            }
        }
    }

    // dfs 非递归的

}
