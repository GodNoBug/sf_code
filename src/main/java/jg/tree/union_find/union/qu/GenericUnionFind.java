package jg.tree.union_find.union.qu;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
// 对外暴露的不是Node
public class GenericUnionFind<V> {
    private Map<V, Node<V>> nodes = new HashMap<>();

    //   Map<V, Node<V>>
    //   V-Node<V>  ->  Node<V>  ->   Node<V>  ->  Node<V>

    public void makeSet(V v) {
        if (nodes.containsKey(v)) return;
        nodes.put(v, new Node<>(v));
    }


    public V find(V v) {
        Node<V> node = findNode(v);
        return node == null ? null : node.value;
    }

    // 找出v的根节点,路径减半法缩小树的高度.通过Node的parent属性找根节点
    private Node<V> findNode(V v) {
        Node<V> node = nodes.get(v);
        if (node == null) return null;

        while (!Objects.equals(node.value, node.parent.value)) { // 当前节点的父节点是自己的时候,那么就是根节点.
            node.parent = node.parent.parent;
            node = node.parent;
        }
        return node;
    }


    public void union(V v1, V v2) {
        Node<V> p1 = findNode(v1);
        Node<V> p2 = findNode(v2);
        if (p1==null||p2==null) return;

        if (Objects.equals(p1.value,p2.value)) return;

        if (p1.rank < p2.rank) {
            p1.parent = p2;// 矮的嫁接到高的,高度不会变
        } else if (p1.rank > p2.rank) {
            p2.parent = p1; // 矮的嫁接到高的,高度不会变
        } else { // 两棵树的高度是一样的,树的高度会加一.谁嫁接谁都可以自己画图理解,
            p1.parent = p2;
            p2.rank += 1;
        }
    }

    public boolean isSame(V v1, V v2) {
        return Objects.equals(find(v1), find(v2));
    }

    private static class Node<V> {
        V value;
        Node<V> parent=this;
        int rank = 1;

        public Node(V value) {
            this.value = value;
        }
    }
}
