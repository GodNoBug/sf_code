package jg.linear_list.queue.priority;

import jg.tree.heap.BinaryHeap;

import java.util.Comparator;

/**
 * 普通的队列FIFO原则,也就是先进先出
 * 优先级队列则是按照优先级高低进行出队,比如将优先级最高的元素作为队头优先出队
 * 和医院的平常排队诊断与急诊的区别
 * 使用"堆"来实现
 * @param <E>
 */
public class PriorityQueue<E> {
    private int size;
    private BinaryHeap<E> heep;

    public PriorityQueue(Comparator<E> comparator) {
        heep=new BinaryHeap<>(comparator);
    }

    public PriorityQueue() {
        this(null);
    }

    public int size(){
        return heep.size();
    }
    public boolean isEmpty(){
        return heep.isEmpty();
    }
    public void clear(){
        heep.clear();
    }

    public void enQueue(E element){
        heep.add(element);
    }
    public E deQueue(){
        return heep.remove();
    }
    // 获取堆顶元素
    public E front(){
        return heep.get();
    }

}
