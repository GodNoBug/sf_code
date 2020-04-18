package jg.tree.set;

import jg.linear_list.List;
import jg.linear_list.list.LinkedList;

public class ListSet<E> implements Set<E> {
    private List<E> list = new LinkedList<>();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(E element) {
        return list.contains(element);
    }

    @Override
    public void add(E element) {
        // 得考虑不添加重复的元素,要么覆盖,要么直接返回,允许不允许为null自己可以决定
        int index = list.indexOf(element);
        if (index != List.ELEMENT_NOT_FOUND) { // 存在就覆盖
            list.set(index, element);
        }else { // 不存在就添加
            list.add(element);
        }
    }

    @Override
    public void remove(E element) {
        // 得考虑不添加重复的元素,要么覆盖,要么直接返回,允许不允许为null自己可以决定
        int index = list.indexOf(element);
        if (index != List.ELEMENT_NOT_FOUND) {
            list.remove(index);
        }
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        if (visitor == null) return;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            visitor.visit(list.get(i));
        }
    }
}
