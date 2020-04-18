package jg.linear_list.list;

import jg.linear_list.AbstractList;


/**
 * 双向链表(非环)
 * 动态数组: 开辟/销毁内存空间次数相对较少,但可能造成内存空间浪费(可以通过缩容解决)
 * 双向链表: 开辟/销毁内存空间次数相对较多,但不会造成内存空间浪费
 * <p>
 * - 如果频繁在尾部进行添加进行添加/删除操作,动态数组和双向链表均可选择
 * - 如果频繁在头部进行添加/删除操作,建议双向链表
 * - 如果有频繁的(在任意位置)添加/删除操作,双向链表
 * - 如果有频繁的查询操作(随机访问操作),动态数组
 * <p>
 * 有了双向链表,单向链表是否就没有用处了?
 * <p>
 * 在JDK LinkedList中
 *
 * @param <E>
 */
public class LinkedList<E> extends AbstractList<E> {
    private Node<E> first;  // 指向第一个元素
    private Node<E> last;   // 指向最后一个元素

    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
    }


    @Override
    public E get(int index) {
        return node(index).element;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = node(index);
        E old = node.element;
        node.element = element;
        return old;
    }

    /**
     * 要想添加到index位置,首先得找到index前面的Node,新Node.next指向index原来的元素,index前面的Node指向新Node
     * 编写链表过程中,要注意边界测试,比如index为0,size-1,size时
     *
     * @param index   要插入的索引
     * @param element 元素
     */
    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        if (index == size) { // 往最后面添加元素
            Node<E> oldLast = last;
            last = new Node<>(element, oldLast, null); // pre指向以前的last,next为null,last指针指向新节点
            if (oldLast == null) { // 链表添加的第一个元素
                first = last;
            } else {
                oldLast.next = last;   // 老last指向新last
            }

        } else {
            Node<E> next = node(index);// 新添加结点的下一个
            Node<E> prev = next.prev; // 新添加结点的上一个
            Node<E> node = new Node<>(element, prev, next);
            next.prev = node;

            if (prev == null) {  // index == 0;
                first = node;
            } else {
                prev.next = node;
            }
        }
        size++;
    }

    /**
     * 获取index对应的结点对象
     *
     * @param index 位置
     * @return Node
     */
    private Node<E> node(int index) {
        rangeCheck(index);

        if (index < (size >> 1)) { // 如果索引小于size的一半
            Node<E> node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
            return node;
        } else {
            Node<E> node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
            return node;
        }

    }


    /**
     * 获取要删除的元素的前面元素和后面元素,前指后,即可断开要删除的元素
     * -如果只传递节点,要删除节点,可以考虑使用当前节点的后一节点,后面的值覆盖当前传递的值,然后当前节点指向下一个的下一个
     *
     * @param index 索引
     * @return 被删除的元素
     */
    @Override
    public E remove(int index) {
        rangeCheck(index);
        Node<E> target = node(index);
        Node<E> prev = target.prev;
        Node<E> next = target.next;

        if (prev == null) { // index==0
            first = next;
        } else {
            prev.next = next;
        }

        if (next == null) {  // index==size-1
            last = prev;
        } else {
            next.prev = prev;
        }
        size--;
        return target.element;
    }

    @Override
    public int indexOf(E element) {
        if (element == null) {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (node.element == null) return i;
                node = node.next;
            }
        } else {
            Node<E> node = first;
            for (int i = 0; i < size; i++) {
                if (element.equals(node.element)) return i;

                node = node.next;
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    /**
     * 翻转一个链表
     * 递归
     */
    public void reverse() {
        first = reverse(first);
    }

    public Node<E> reverse(Node<E> head) {
        if (head == null || head.next == null) return head;
        Node<E> newHead = reverse(head.next);
        head.next.next = head;  // 下一个节点的next属性指向当前节点,画个图,注意一下箭头就很好理解了
        head.next = null;
        return newHead;
    }

    /**
     * 翻转一个链表
     * 非递归
     */
    public void reverse2() {
        Node<E> newHead = null; // 新头结点
        Node<E> head = first; // 旧头结点
        while (head != null) {
            Node<E> tmp = head.next;// 防止断链保存一下head.next
            head.next = newHead;
            newHead = head;
            head = tmp;
        }
        first = newHead;
    }

    /**
     * 判断一个链表是否有环
     * 否决自己的一个思考: 如果一个环不是单纯圆环,两个人一个人站在原地,另一个人绕迷宫一圈发现另一个人站在原地怎么样? 环并不规则,单纯的组成一个圆形,绕死都有可能回不到起点,否决
     * 快慢指针法:类似环型操场跑步
     * 假设
     * 快指针: 走一步
     * 慢指针: 走两步
     * 如果是环的话,有走到一定程度,会相遇
     * 如果没有环,快指针先达到空
     */
    public boolean hasCycle() {
        if (first == null || first.next == null) return false;
        Node<E> slow = first;
        Node<E> fast = first.next;
        while (fast != null) {
            if (slow == fast) return true;
            slow = slow.next;
            fast = first.next.next;
        }
        return false;   // 非环
    }


    // 节点
    private static class Node<E> {
        E element;
        Node<E> prev;
        Node<E> next;

        public Node(E element, Node<E> prev, Node<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (prev != null) {
                sb.append(prev.element);
            } else {
                sb.append("null");
            }
            sb.append("_").append(element).append("_");
            if (next != null) {
                sb.append(next.element);
            } else {
                sb.append("null");
            }
            return sb.toString();
        }
    }


    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(", [");
        Node<E> node = first;
        for (int i = 0; i < size; i++) {
            if (i != 0) { // 只要不是第一个元素,前面先添加","
                string.append(", ");
            }
            string.append(node);
            node = node.next;
        }
        string.append("]");
        return string.toString();
    }
}
