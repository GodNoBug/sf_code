package jg.tree.set;

import jg.tree.binary.BinaryTree;
import jg.tree.binary.RBTree;

import java.util.Comparator;

public class TreeSet<E> implements Set<E> {
    private RBTree<E> tree;

    public TreeSet() {
        this(null);
    }

    public TreeSet(Comparator<E> comparator) {
        tree =new RBTree<>(comparator);
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public boolean contains(E element) {
        return tree.contains(element);
    }

    @Override
    public void add(E element) {
        // 内部已经默认实现去重了
        tree.add(element);
    }

    @Override
    public void remove(E element) {
        tree.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        tree.inOrder(new BinaryTree.Visitor<E>() {
            @Override
            protected boolean visit(E element) {
                return visitor.visit(element);
            }
        });
    }
}
