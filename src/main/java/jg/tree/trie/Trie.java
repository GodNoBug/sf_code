package jg.tree.trie;

import java.util.HashMap;
// 需求:
// 如何判断一堆不重复的字符串是否以某个前缀开头?
// 1.用Set/Map存储字符串
// 2.遍历所有字符串进行判断
// 3.时间复杂度 O(n) 和 元素数量有关

// 有没有更优的数据结构实现前缀搜索?
// Trie,也叫做字典树、前缀树(Prefix Tree)、单词查找树
// 优点:
//  Trie搜索字符串的效率主要跟字符串的长度有关
// 缺点:
//   需要耗费大量的内存,因此还有待改进
// 更多Trie
//  Double-array Trie、Suffix Tree、Patricia Tree、Crit-bit Tree、AC自动机


// 假设使用Tire存储 cat/dog/doggy/does/cast/add六个单词
//         [ ]
//     a/   |c  \d
//    [ ]  [ ]  [ ]
//    d|    |a   |o
//    [ ]  [ ]  [ ]
//    d|   s/ \t |g \e
//    { } [ ] { }[ ][ ]
//        t|      |g |s
//        { }    [ ]{ }
//                |y
//               { }
public class Trie<V> {
    private int size;
    private Node<V> root;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        root = null;
    }

    public V get(String key) {
        Node<V> node = node(key);
        return node != null && node.word ? node.value : null;
    }

    private Node<V> node(String key) {
        keyCheck(key);

        Node<V> node = this.root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            if (node == null || node.children == null || node.children.isEmpty()) return null;
            char c = key.charAt(i);
            node = node.children.get(c);
        }

        // 来到这里不一定是完整的单词
        return node;
    }

    private void keyCheck(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key must not be empty");
        }
    }

    public boolean contains(String key) {
        Node<V> node = node(key);
        return node != null && node.word;
    }

    // TODO
    public V add(String key, V value) {
        keyCheck(key);

        // 创建根节点
        if (root == null) {
            root = new Node<>(null);
        }

        Node<V> node = root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            char c = key.charAt(i);
            boolean emptyChildren = node.children == null;
            Node<V> childNode = emptyChildren ? null : node.children.get(c);
            if (childNode == null) {
                childNode = new Node<>(node);
                childNode.character = c;
                node.children = emptyChildren ? new HashMap<>() : node.children;
                node.children.put(c, childNode);
            }
            node = childNode;
        }
        if (node.word) { // 已存在单词
            // 覆盖
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        }
        // 新增一个单词
        node.word = true;
        node.value = value;
        size++;
        return null;
    }

    public V remove(String key) {
        keyCheck(key);

        // 找到最后一个节点
        Node<V> node = node(key);
        // 如果不是单词结尾,不用做任何处理
        if (node == null || !node.word) return null;
        size--;
        V oldValue = node.value;
        // 如果还有子节点
        if (node.children != null && !node.children.isEmpty()) {

            node.word = false;
            node.value = null;
            return oldValue;
        }
        // 没有子节点,往回删除,直到发现
        Node<V> parent = null;
        while ((parent = node.parent) != null) {
            parent.children.remove(node.character);
            if (!parent.children.isEmpty() || parent.word) break; // 删除后还有
            node = parent;
        }
        return oldValue;
    }

    public boolean startsWith(String prefix) {
        return node(prefix) != null;
    }

    private static class Node<V> {
        Node<V> parent;
        Character character;
        HashMap<Character, Node<V>> children;  // key 字符 , value为节点
        V value;
        boolean word; // 是否为单词的结尾(是否为一个完整的单词) value在word为true的节点中

        public Node(Node<V> parent) {
            this.parent = parent;
        }
    }
}
