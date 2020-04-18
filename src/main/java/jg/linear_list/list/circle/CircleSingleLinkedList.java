package jg.linear_list.list.circle;

import jg.linear_list.AbstractList;


/**
 * 环型单向链表
 * 链表的接口和动态数组大致一样
 */
public class CircleSingleLinkedList<E> extends AbstractList<E> {


    private Node<E> first;  // 指向第一个元素

    @Override
    public void clear() {
        first = null;// 断头线
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
        if (index == 0) {
            Node<E> newFirst = new Node<>(element, first);
            // 拿到最后一个节点
            Node<E> last = (size == 0) ? newFirst : node(size - 1);
            last.next = newFirst;
            first = newFirst;
        } else {
            Node<E> pre = node(index - 1);
            pre.next = new Node<>(element, pre.next);
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
        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
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
        Node<E> target = first;
        if (index == 0) {
            if (size == 1) {
                first = null;
            } else {
                Node<E> last = node(size - 1);
                first = first.next;
                last.next = first;
            }

        } else {
            Node<E> pre = node(index - 1);
            target = pre.next;
            pre.next = target.next;
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
        return 0;
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
        Node<E> next;

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(element).append("_").append(next.element);

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
