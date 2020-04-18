package jg.linear_list.queue.circle;

/**
 * 双端循环队列
 * 底层用数组实现
 * 其实队列底层也可以使用动态数组实现,并且各项接口也可以优化到O(1)的时间复杂度,这个用数组实现并且优化之后的队列也叫做循环队列
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class CircleDeque<E> {
    private int front;   // 存储队头元素下标
    private E[] elements;// 环型队列
    private int size;    // 大小

    public static final int DEFAULT_CAPACITY = 10;

    public CircleDeque() {
        this(DEFAULT_CAPACITY);
    }

    public CircleDeque(int capacity) {
        elements = (E[]) new Object[capacity];
    }

    // 元素的数量
    public int size() {
        return size;
    }

    // 是否为空
    public boolean isEmpty() {
        return size == 0;
    }


    // 从队尾入队
    public void enQueueRear(E element) {
        ensureCapacity(size + 1);
        elements[index(size)] = element;
        size++;
    }

    // 从队头出队
    public E deQueueFront() {
        E frontElement = elements[front];
        elements[front] = null;
        front = index(1);// 移动front
        size--;
        return frontElement;
    }

    // 从队头入队
    public void enQueueFront(E element) {
        ensureCapacity(size + 1);
        front = index(-1); // front要向前移
        elements[front] = element; // 存放新的元素
        size++;
    }

    // 从队尾出队
    public E deQueueRear() {
        int rearIndex = index(size - 1);
        E rear = elements[rearIndex];
        elements[rearIndex] = null;
        size--;
        return rear;
    }

    // 获取队列的头元素
    public E front() {
        return elements[front];
    }

    // 获取队列的尾元素
    public E rear() {
        return elements[index(size - 1)];
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[index(i)] = null;
        }
        front = 0;
        size = 0;
    }

    /**
     * 保证要有capacity的容量
     * 队头元素放到新数组的[0],队尾元素放到[size-1],front重新置0
     *
     * @param capacity
     */
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length; // 旧容量

        if (oldCapacity >= capacity) return; // 如果size+1仍没有超过旧容量,那么不需要扩容

        // 新容量为旧容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[index(i)];
        }
        elements = newElements;
        front = 0; // 重置front
        System.out.println(oldCapacity + "扩容为" + newCapacity);
    }

    /**
     * 如果不是循环的情况下,这些索引定位操作都是对的
     * 公式(i + front) % elements.length 为了维护循环队列,本质上把之前的索引转换成循环队列上真实的索引
     * 完全可以封装成一个方法
     * % 优化 已知n%m等价于n-(m>n?0:m)的前提条件: n<2m
     * @param index 之前的索引
     * @return 循环队列上真实的索引
     */
    private int index(int index) {
        // 如果下标为负数,加上整个length  ==>  双端多出的问题
        // 如果是正数,与elements.length%
        index += front;
        if (index < 0) {
            return index + elements.length;
        }
        return index - (index >= elements.length ? elements.length : 0);
        // return index % elements.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("capacity=").append(elements.length)
                .append(" size=").append(size)
                .append(" front=").append(front)
                .append(" ,[");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(elements[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
