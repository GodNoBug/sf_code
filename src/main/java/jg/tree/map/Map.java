package jg.tree.map;

/**
 * Map(映射) 在有些编程也叫做字典
 * 类似Set,Map可以直接利用之前学习的链表/二叉搜索树(AVL,RBTree)等数据结构来实现
 * @param <K>
 * @param <V>
 */
public interface Map<K, V> {
    int size();

    boolean isEmpty();

    void clear();

    V put(K key, V value);

    V get(K key);

    V remove(K key);

    boolean containsKey(K key);

    boolean containsValue(V value);

    void traversal(Visitor<K, V> visitor);

    public static abstract class Visitor<K, V> {
        boolean stop;
        public abstract boolean visit(K key, V value);
    }
}
