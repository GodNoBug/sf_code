package jg.linear_list.stack;

import jg.linear_list.array.DynamicArray;

/**
 * 栈是一种特殊的线性表,只能在一端进行操作
 * 往栈中添加元素操作,一般叫做push,入栈
 * 从栈中移除元素的操作,一般叫做pop,出栈(只能移除栈顶元素,也叫做:弹出栈顶元素)
 * 后进先出的原则,Last In First Out,LIFO
 * 注意: 这里说的"栈"与内存中的"栈空间"是两个不同的概念
 *
 * 浏览器的前进和后退/ 撤销和恢复功能
 * 输入网址-进入栈
 * 后退-出栈,为保证前进功能,弹出的元素存到另外一个栈中
 * 前进-将另外一个栈的栈顶元素弹出,放在原来的栈中
 */
public class Stack<E> {
    private DynamicArray<E> list = new DynamicArray<>();

    public void clear() {
        list.clear();
    }

    // 元素的数量
    public int size() {
        return list.size();
    }

    // 是否为空
    public boolean isEmpty() {
        return list.isEmpty();
    }

    // 入栈
    public void push(E element) {
        list.add(element);
    }

    // 出栈
    public E pop() {
        return list.remove(list.size() - 1);
    }

    // 获取栈顶元素
    public E top() {
        return list.get(list.size() - 1);
    }
}
