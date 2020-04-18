package jg.tree.heap;

import java.util.Comparator;

@SuppressWarnings({"unchecked","unused"})
public abstract class AbstractHeap<E> implements Heap<E> {
    protected int size;                         // 容量
    protected Comparator<E> comparator;         // 比较器

    public AbstractHeap() {
        this(null);
    }

    public AbstractHeap(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // 比较
    protected int compare(E e1, E e2) {
        return comparator != null ? comparator.compare(e1, e2) : ((Comparable<E>) e1).compareTo(e2);
    }

}


