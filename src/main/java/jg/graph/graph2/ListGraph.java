package jg.graph.graph2;

import jg.graph.VertexVisitor;
import jg.tree.heap.MinHeap;
import jg.tree.union_find.union.qu.GenericUnionFind;

import java.util.*;

/**
 * 图
 * 1.邻接矩阵(适合稠密图,不然会比较浪费内存)
 * -  一维数组存放顶点信息
 * -  二维数组存放边信息  (顶点之间的关系,邻接矩阵本体)
 * 邻接矩阵是表示图形中顶点之间相邻关系的矩阵.二维数组表示.邻接矩阵需要为每个顶点都分配n个边的空间,其实有很多边是不存在,会造成空间的一定损失
 * <p>
 * 2.邻接表
 * - 邻接表的实现只关心存在的边,不关心不存在的边.因此没有空间浪费,邻接表由数组链表组成
 * - 数组下标代表某个顶点,而数组下标下存储的链表代表相关联的顶点
 * <p>
 * 和邻接表的折中方案
 * 更偏向与邻接表
 * <p>
 * 顶点Vertex: 元素,以此为起点的边,以此起点为终点的边
 * 边Edge: 起点,终点,权值 [代表顶点的关系]
 *
 * @param <V> 顶点元素
 * @param <E> 权值
 */
@SuppressWarnings({"unchecked", "unused"})
public class ListGraph<V, E> implements Graph<V, E> {
    // 对外开放的是V.而不是Vertex,所以使用V来获取顶点,很合适用Map,时间复杂度比起ArrayList更低一些
    private Map<V, Vertex<V, E>> vertices = new HashMap<>(); // 顶点集合 以"元素-顶点"形式存储
    private Set<Edge<V, E>> edges = new HashSet<>();         // 边集,若没有,需要遍历vertices进行较复杂的统计


    // Map结构
    //                        入度边 inEdges(HashSet) -> 起点+终点+权值
    //                    ↗
    // V1 -> Vertex1<V, E> -> value 本体
    //                    ↘
    //                        出度边 outEdges(HashSet) -> 起点+终点+权值
    // edges(HashSet)

    /*---------------------------------------------------构造器--------------------------------------------------------*/
    public ListGraph() {
    }

    public ListGraph(ArrayList<V> list) {
        this(null, list);
    }

    public ListGraph(WeightManager<E> manager) {
        this(manager, null);
    }

    public ListGraph(WeightManager<E> manager, ArrayList<V> list) {
        this.weightManager = manager;
        if (list != null && list.size() != 0) {
            for (V v : list) {
                this.addVertex(v);
            }
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

    /*----------------------------------------------------数量--------------------------------------------------------*/
    // 顶点数量
    @Override
    public int verticesSize() {
        return vertices.size();
    }

    // 边数量
    @Override
    public int edgesSize() {
        return edges.size();
    }

    /*----------------------------------------------------添加--------------------------------------------------------*/
    // 添加顶点,单纯的添加顶点,并没有维护顶点之间(也就是边)的关系
    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v)) return;
        vertices.put(v, new Vertex<>(v));
    }

    // 添加边,维护了顶点之间的关系
    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    /**
     * 添加一条边:
     * 1.要创建Edge维护起点,终点,权值三个.前提是这条边对应的两个端点要存在
     * 2.维护两个Vertex端点的[起点的]"出度"还有[终点的]"入度"
     * 边由顶点和权值构成
     *
     * @param from   起点元素,
     * @param to     终点元素
     * @param weight 权值
     */
    @Override
    public void addEdge(V from, V to, E weight) {
        // 判断from,to顶点是否存在?因为要在顶点存在的前提才能添加
        Vertex<V, E> fromVertex = vertices.get(from);
        Vertex<V, E> toVertex = vertices.get(to);

        // 如果不存在则创建一个,并存入顶点集
        if (fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }
        if (toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }
        // 到这里fromVertex/toVertex顶点都有值
        // 以fromVertex为起点的边是否包含,
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex, weight);

        // 可能添加的边已经存在,只是改权值,但edges是HashSet不好去取出来更新权值,干脆删除再创建.
        // 删除 [起点: 删除对应的出度边 终点: 再删除对应的入度边]
        if (fromVertex.outEdges.remove(edge)) { // 尝试去删除,有则删除成功,成功再去删除另外的
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }
        // 添加 [起点: 添加对应的出度边, 终点: 再添加对应的入度边]
        if (fromVertex.outEdges.add(edge)) {
            toVertex.inEdges.add(edge);
            edges.add(edge);
        }
    }

    /*----------------------------------------------------删除--------------------------------------------------------*/

    /**
     * 根据元素删除顶点,将顶点关联的边也删除掉
     *
     * @param v 元素
     */
    @Override
    public void removeVertex(V v) {
        // 删除顶点集中的边
        Vertex<V, E> vertex = vertices.remove(v);
        if (vertex == null) return;

        // 出度边: 删除所有出度边之前,先把所有出度边指向的终点包含的这个边删除掉,然后再删除这个入度边,再从边集合中剔除出去
        for (Iterator<Edge<V, E>> iterator = vertex.outEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            edge.to.inEdges.remove(edge);
            iterator.remove();// 将当前遍历到的元素从outEdges删除
            edges.remove(edge);
        }

        // 入度边: 删除所有入度边之前,先把所有入度边指向的起点包含的这个边删除掉,然后再删除这个入度边,在从边集合中剔除出去
        for (Iterator<Edge<V, E>> iterator = vertex.inEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            edge.from.outEdges.remove(edge);
            iterator.remove();// 将当前遍历到的元素从inEdges删除
            edges.remove(edge);
        }
    }

    /**
     * 删除边,需要按起点元素和终点来删除边. [保留顶点]
     * <p>
     * 1.传进来的起点或终点不存在的话,那更谈不上有边了
     * 2.删除边集的edges
     * 3.删除两个顶点集包含相应的出边/入边
     *
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
    // 图遍历需要起点,类比树,以起点为"根节点",离根节点"最近"[一根线能访问到,且未访问过的]为下一层 . 之所以是未被访问过的为加一层,因为图下一层可以走回上一层

    // 广度优先搜索
    //    类似二叉树的层序遍历,它就是一种广度优先搜索[多叉树也可以这样]
    //    要素: Queue队列
    // 思路
    //  每个顶点都应该会有标记,去标记已经访问过的
    //  1.将当前节点放入队列中.
    //  2.当前节点出队并打印,标记为已访问,紧跟将当前所能直接抵达的节点"入队"
    @Override
    public void bfs(V begin, VertexVisitor<V> visitor) {
        if (visitor == null) return;

        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;

        Set<Vertex<V, E>> visited = new HashSet<>(); // 用set存储访问过的顶点,只要进过队列,我们就应该添加到集合中.而不是在出队打印输出的时候,因为这样会导致还没有输出的重复入队
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        // 直接将起点入队
        queue.offer(beginVertex);
        visited.add(beginVertex);

        // 只要队列不为空,就不断地去队列中取
        while (!queue.isEmpty()) {
            // 从队头中拿取一个顶点,打印输出值
            Vertex<V, E> vertex = queue.poll();
            //  System.out.println(vertex.value);
            if (visitor.visit(vertex.value)) {
                return;
            }


            for (Edge<V, E> edge : vertex.outEdges) {
                if (!visited.contains(edge.to)) { // 排除已经遍历的顶点[或许你完全可以在Vertex类中添加一个visited变量来标明是否被访问过]
                    queue.offer(edge.to);
                    visited.add(edge.to);
                }
            }
        }
    }

    /**
     * 深度优先搜索[递归式]
     * 二叉树的前序遍历就是一种深度优先搜索,[一定要联系图的递归代码调用时机(在遍历子节点循环内调用)和二叉树前序遍历的递归代码的调用时机(递归调用左和右,也相当于遍历)]
     * <p>
     * 一路走到底,有多深走多深.发现已经不能再走,已经最深了.
     * 往回退发现有另外一个分支.顺着这个分支有多深走多深.以此类推,所有路径都尝试过
     *
     * @param begin 开始元素
     */
    @Override
    public void dfs(V begin, VertexVisitor<V> visitor) {
        if (visitor == null) return;
        // 取开始节点,设标记集合,开始调用递归函数
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;
        Set<Vertex<V, E>> visited = new HashSet<>();
        dfs(beginVertex, visited, visitor);
    }

    /**
     * System.out.println(vertex.value);
     * for (Edge<V, E> edge : vertex.outEdges) {
     * dfs(edge.to);
     * }
     * 这样的大体结构是出来了,缺记录访问过的顶点,应该在递归中传递集合Set<Vertex<V,E>>
     *
     * @param vertex 顶点
     */
    // 流程,思路,与代码的联系
    // 顺着一条岔路一路往下走
    // 走到下一条路要走不通了[节点已经访问过了]
    // 退回来,尝试其他岔路[意味着当前递归调用结束了]
    // 不行再退回一步,尝试其他岔路
    private void dfs(Vertex<V, E> vertex, Set<Vertex<V, E>> visited, VertexVisitor<V> visitor) {
        // 递归自然会往回退
        // System.out.println(vertex.value);
        if (visitor.visit(vertex.value)) {
            return;
        }
        visited.add(vertex);
        for (Edge<V, E> edge : vertex.outEdges) {
            if (!visited.contains(edge.to)) {
                dfs(edge.to, visited, visitor);
            }
        }
    }


    // 深度优先遍历,非递归的实现[递归函数调用过程很像平时数据结构"栈",可以使用这样的数据结构模拟递归]
    @Override
    public void dfs2(V begin, VertexVisitor<V> visitor) {
        if (visitor == null) return;
        // 取开始节点,设标记集合,开始调用递归函数
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return;

        Set<Vertex<V, E>> visited = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();

        // 访问起点
        stack.push(beginVertex);
        visited.add(beginVertex);
        //System.out.println(beginVertex.value);
        if (visitor.visit(beginVertex.value)) {
            return;
        }
        while (!stack.isEmpty()) {
            Vertex<V, E> v = stack.pop();
            for (Edge<V, E> edge : v.outEdges) {
                if (visited.contains(edge.to)) continue;
                stack.push(edge.from);
                stack.push(edge.to);
                visited.add(edge.to);
                //System.out.println(edge.to.value);
                if (visitor.visit(edge.to.value)) {
                    return;
                }
                break;  //
            }
        }
    }


    // 拓扑排序-思路
    //◼ 可以使用卡恩算法(Kahn于1962年提出)完成拓扑排序
    //假设 L 是存放拓扑排序结果的列表
    //  1.把所有入度为0的顶点放入L中，然后把这些顶点从图中去掉(不可持续,需要变通)
    //  2.重复操作1，直到找不到入度为0的顶点
    //   - 如果此时L中的元素个数和顶点总数相同，说明拓扑排序完成
    //   - 如果此时L中的元素个数少于顶点总数，说明原图中存在环，无法进行拓扑排序
    // 变通:
    //  1.维护一张表(顶点-入度表) 在Vertex表中设置inDegree属性 vertex-inDegree inEdges.size()是不会变的,未来是需要代表入度表的
    //  2.顶点-入度表一对一的关系,可以考虑使用Map
    @Override
    public List<V> topologicalSort() {
        List<V> list = new LinkedList<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        Map<Vertex<V, E>, Integer> ins = new HashMap<>();
        // 初始化(将度为0的节点都放入队列)
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            int in = vertex.inEdges.size();
            if (in == 0) {
                queue.offer(vertex);
            } else {
                //vertex.inDegree = in
                ins.put(vertex, in);
            }
        });
        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            list.add(vertex.value); // 放入返回结果中
            for (Edge<V, E> edge : vertex.outEdges) {
                int toIn = ins.get(edge.to) - 1;
                if (toIn == 0) {
                    queue.offer(edge.to);
                } else {
                    ins.put(edge.to, toIn);
                }
            }
        }
        return list;
    }

    //
    @Override
    public Set<EdgeInfo<V, E>> mst() {
        return Math.random() > 0.5 ? prim() : kruskal();
    }


    private WeightManager<E> weightManager;
    private Comparator<Edge<V, E>> edgeComparator = (Edge<V, E> e1, Edge<V, E> e2) -> {
        return weightManager.compare(e1.weight, e2.weight);
    };


    //◼ 假设 G = (V, E) 是有权的连通图(无向), A 是 G 中最小生成树的边集
    // 算法从 S = {u0}(u0 ∈ V), A = { } 开始，重复执行下述操作, 直到 S = V 为止
    //✓ 找到切分 C = (S, V–S) 的最小横切边 (u0， v0) 并入集合A, 同时将v0并入集合S
    private Set<EdgeInfo<V, E>> prim() {
        Iterator<Vertex<V, E>> it = vertices.values().iterator();
        if (!it.hasNext()) return null;

        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        Set<Vertex<V, E>> addedVertices = new HashSet<>();     // 表示已经添加过的节点

        Vertex<V, E> vertex = it.next();    // 取出第一个元素
        addedVertices.add(vertex);
        MinHeap<Edge<V, E>> heap = new MinHeap<>(vertex.outEdges, edgeComparator); // 创建最小堆.将当前顶点元素的出度全部添加到最小堆中

        int verticesSize = vertices.size();  //  edgeInfos.size() <vertices.size() - 1 与此等价
        while (!heap.isEmpty() && addedVertices.size() < verticesSize) {
            Edge<V, E> edge = heap.remove(); // 取堆顶元素,也就是当前元素权值最小的出度边
            if (addedVertices.contains(edge.to)) continue;  // 去重,查看边的终点是已经添加过的,就没必要继续执行下一步操作

            edgeInfos.add(edge.info());      // 边存入集合
            addedVertices.add(edge.to);      // 表示已经添加的顶点
            heap.addAll(edge.to.outEdges);   // 把当前权值最小的出度边的终点的所有出度边存入最小堆中,注意边添加的重复性(新+旧)
        }
        return edgeInfos;
    }

    //◼按照边的权重顺序（从小到大）将边加入生成树中,直到生成树中含有 V – 1 条边为止(V是顶点数量)
    //  若加入该边会与生成树形成环,则不加入该边
    //  从第3条边开始,可能会与生成树形成环
    //  [prim算法每次切分都选取最小的.....,]
    // 为什么这里用并查集而prim用hashSet

    private Set<EdgeInfo<V, E>> kruskal() {
        int edgesSize = vertices.size() - 1;
        if (edgesSize == -1) return null;

        Set<EdgeInfo<V, E>> edgeInfos = new HashSet<>();
        MinHeap<Edge<V, E>> heap = new MinHeap<>(edges, edgeComparator);
        GenericUnionFind<Vertex<V, E>> uf = new GenericUnionFind<>();
        vertices.forEach((v, vertex) -> {
            uf.makeSet(vertex);
        });

        while (!heap.isEmpty() && edgeInfos.size() < edgesSize) {
            Edge<V, E> edge = heap.remove();
            if (uf.isSame(edge.from, edge.to)) continue;  // 不让其有满足成环条件的机会[若取出来的边的两个顶点本来就属于同一个并查集,再次合并的话,会成环]

            edgeInfos.add(edge.info());
            uf.union(edge.from, edge.to);   // 边的两个点,合并成一个集合
        }

        return edgeInfos;
    }

    /*----------------------------------------------------最短路径-----------------------------------------------------*/
    @Override
    public Map<V, E> shortestPath(V begin) {
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (beginVertex == null) return null;
        Map<V, E> selectPaths = new HashMap<>();      //
        Map<Vertex<V, E>, E> paths = new HashMap<>(); //

        // 初始化paths
        for (Edge<V, E> outEdge : beginVertex.outEdges) {
            paths.put(outEdge.to, outEdge.weight);
        }

        while (!paths.isEmpty()) {
            Map.Entry<Vertex<V, E>, E> minEntry = getMinPath(paths);
            // minVertex离开桌面
            Vertex<V, E> minVertex = minEntry.getKey();
            selectPaths.put(minVertex.value, minEntry.getValue());
            paths.remove(minVertex);
            // 对他的 minVertex的outEdges进行松弛操作
            for (Edge<V, E> outEdge : minVertex.outEdges) {
                // 如果edge.to已经离开桌面,就没必要进行松弛操作
                if (selectPaths.containsKey(outEdge.to.value)||outEdge.to.equals(beginVertex)) continue;
                // 新的可选的最短路径 beginVertex到edge.from的最短路径 + edge.weight
                E newWeight = weightManager.add(minEntry.getValue(), outEdge.weight);
                // 以前的最短路径: beginVertex到edge.to的最短路径
                E oldWeight = paths.get(outEdge.to);
                if (oldWeight == null || weightManager.compare(newWeight, oldWeight) < 0) {
                    paths.put(outEdge.to, newWeight);
                }
            }
        }
        return selectPaths;
    }

    /**
     * 从paths中挑一个最小的路径出来
     *
     * @param paths
     * @return
     */
    private Map.Entry<Vertex<V, E>, E> getMinPath(Map<Vertex<V, E>, E> paths) {
        Iterator<Map.Entry<Vertex<V, E>, E>> it = paths.entrySet().iterator();
        Map.Entry<Vertex<V, E>, E> minEntry = it.next();
        while (it.hasNext()) {
            Map.Entry<Vertex<V, E>, E> entry = it.next();
            if (weightManager.compare(entry.getValue(), minEntry.getValue()) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;
//        Vertex<V, E> minVertex = null;
//        E minWeight = null;
//        for (Map.Entry<Vertex<V, E>, E> entry : paths.entrySet()) {
//            E weight = entry.getValue();
//            if (minVertex == null || weightManager.compare(weight, minWeight) < 0) {
//                minVertex = entry.getKey();
//                minWeight = weight;
//            }
//        }
//        return minVertex;
    }

    private void relax() {

    }

    private void dijkstra() {

    }


    // 一个顶点: 元素,以此为起点的边,以此起点为终点的边
    private static class Vertex<V, E> {
        V value;                                    // 顶点包含的元素
        Set<Edge<V, E>> inEdges = new HashSet<>();  // 入度边 之所以用HashSet.因为储存不考虑顺序性
        Set<Edge<V, E>> outEdges = new HashSet<>(); // 出度边
        // int inDegree;

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
        E weight;           // 权值


        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        public Edge(Vertex<V, E> from, Vertex<V, E> to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        EdgeInfo<V, E> info() {
            return new EdgeInfo<>(from.value, to.value, weight);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
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

}
