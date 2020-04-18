package jg.linear_list.queue;

import jg.linear_list.list.LinkedList;

/**
 * 对了是一种特殊的线性表,只能在头尾两端进行操作
 * 队尾(rear): 只能从队尾添加元素,一般叫做enQueue,入队
 * 队头(front): 只能从队头一次元素,一般叫做deQueue,出队
 * 先进先出原则: First In First Out,FIFO
 * 左边添加右边提取
 *
 * @param <E>
 */
public class Queue<E> {
    // 优先使用双向链表,因为队列主要是往头尾操作元素
    private LinkedList<E> list = new LinkedList<>();

    // 元素的数量
    public int size() {
        return list.size();
    }

    // 是否为空
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // 入队
    public void offer(E element) {
        list.add(element);
    }

    // 出队
    public E poll() {
        return list.remove(0);
    }

    // 获取队列头的元素
    public E peek() {
        return list.get(0);
    }

    // 清空
    public void clear() {
        list.clear();
    }
}

