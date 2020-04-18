package jg.linear_list.queue;

import jg.linear_list.stack.Stack;

/**
 * 使用栈来实现队列
 * 准备2个栈: inStack/outStack
 * 入队时,push到inStack
 * 出队时,
 * - 如果outStack为空,将inStack所有元素逐一弹出,push到outStack,outStack弹出栈顶元素
 * - 如果outStack不为空,outStack弹出栈顶元素
 *
 * @param <E>
 */
public class Queue2<E> {
    private Stack<E> inStack;
    private Stack<E> outStack;

    public Queue2() {
        this.inStack = new Stack<>();
        this.outStack = new Stack<>();
    }


    // 元素的数量
    public int size() {
        return inStack.size() + outStack.size();
    }

    // 是否为空
    public boolean isEmpty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    // 入队
    public void offer(E element) {
        inStack.push(element);
    }

    // 出队
    public E poll() {
        checkOutStack();
        return outStack.pop();
    }

    // 获取队列头的元素
    public E peek() {
        checkOutStack();
        return outStack.top();
    }

    private void checkOutStack() {
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    // 清空
    public void clear() {
        inStack.clear();
        outStack.clear();
    }
}
