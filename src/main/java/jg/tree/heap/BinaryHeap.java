package jg.tree.heap;


import jg.tree.heap.printer.BinaryTreeInfo;

import java.util.Comparator;


// 二叉堆的逻辑结构就是一颗完全二叉树,所以也叫完全二叉堆
// 1.鉴于完全二叉树的一些特性,二叉堆底层(物理结构)一般用数组实现即可,并不需要使用节点对象来表示.
// 2.索引i的规律(n为元素数量)
//  - i=0,它是根节点
//  - i>0,它的父节点索引为floor((i-1)/2)

//  - 2i+1<n-1,它的左子节点索引为2i+1
//  - 2i+1>n-1,它无左子节点

//  - 2i+2≤n-1,它的右子节点索引为2i+2
//  - 2i+2>n-1,它无右子节点

//  此代码默认大顶堆实现,指定Comparator策略即可切换大顶堆和小顶堆
//  可以用来解决[TOP K]问题: 从海量数据中找出前K个数据
@SuppressWarnings({"unchecked", "unused"})
public class BinaryHeap<E> extends AbstractHeap<E> implements BinaryTreeInfo {
    private E[] elements;   // 二叉堆的数组表示
    public static final int DEFAULT_CAPACITY = 10; // 默认容量


    // get是获取根元素,堆顶元素
    @Override
    public E get() {
        emptyCheck();
        return elements[0];
    }

    // 先添加到数组最后,也就是完全二叉堆的最后,但是新插入的并不一定让整个二叉堆满足应有的性质.想办法让这个元素往顶上窜
    // 不断和父节点比较,发现新增节点比较大.交换位置.直到不能再往上交换或者已经到了根节点,[此过程叫上滤]
    // 如果 node > 父节点 ,与父节点交换位置
    // 如果 node ≤ 父节点 ,或者node没有父节点,退出循环
    @Override
    public void add(E element) {
        elementNotNullCheck(element);
        ensureCapacity(size + 1);
        elements[size++] = element;  // size++ => size = size +1

        siftUp(size - 1);
    }

    // 让index位置上滤  [index 新增节点的数组下标]
    //  可以优化,一般交换位置需要三行代码,可
    //  E tmp = elements[index];
    //  elements[index] = elements[pIndex];
    //  elements[pIndex] = tmp;
    // 优化
    //  以进一步优化：将新添加的节点备份,比较与父节点的元素大小,
    //  如果父节点比新增节点小,父节点挪下来,如此循环, 确定最终位置才摆放上去
    private void siftUp(int index) {
        // 备份新增节点
        E element = elements[index];
        while (index > 0) { // 它应该有父节点,才能上滤
            int parentIndex = (index - 1) >> 1;  // 父节点的索引 相当于(index - 1) / 2
            E parent = elements[parentIndex];
            if (compare(element, parent) <= 0) break; // ↓
            // 交换index,pindex位置的内容
            elements[index] = parent;
            // 重新赋值index
            index = parentIndex;
        }
        elements[index] = element;
    }
//  优化前的代码
//        while (index > 0) { // 它应该有父节点,才能上滤
//            int pIndex = (index - 1) >> 1;  // 父节点的索引 相当于(index - 1) / 2
//
//            if (compare(elements[index], elements[pIndex]) <= 0) return;
//            // 交换index,pindex位置的内容
//            E tmp = elements[index];
//            elements[index] = elements[pIndex];
//            elements[pIndex] = tmp;
//            // 重新赋值index
//            index = pIndex;
//        }

    // 删除[大顶堆]顶元素
    // 正常思维是删除[0],所有需要元素往前移,这样的操作时间复杂度为O(n)
    // * 如何降低这样的复杂度
    //      二叉堆最后一个替换被删除的堆顶元素,也就是数组末尾元素替换最后一个元素,这样会导致不符合二叉堆的性质.那么得调整
    //      1. 用最后一个节点替换根节点,有可能不会满足堆性质.
    //      2. 删除最后一个节点
    //      3. 循环执行以下操作
    //       - 如果node<子节点,与最大的子节点交换位置
    //       - 如果node≥子节点,或者node没有子节点,退出循环
    //       [这个过程叫做下滤,时间复杂度O(logn)]
    //       同样的,交换位置的操作可以像添加那样进行优化
    @Override
    public E remove() {
        emptyCheck();
        int lastIndex = --size;
        E root = elements[0];
        elements[0] = elements[lastIndex];
        elements[lastIndex] = null;
        siftDown(0);
        return root;
    }

    /**
     * 让index位置下滤
     * 完全二叉树性质
     * index有两种情况,只有左子节点/同时有左右子节点
     * index< 第一个叶子节点的索引 也就是小于非叶子节点的数量=floor(n/2)
     * TODO 为什么不能 2*index+2 >size-1  2*index+1 >size-1
     *
     * @param index 索引
     */
    private void siftDown(int index) {
        E element = elements[index];
        int half = size >> 1;   // 第一个叶子节点的下标
        while (index < half) {  // 保证index下面有子节点才能下滤,也就是满足非叶子节点的条件才能进入循环,index<第一个叶子节点的索引
            // index有两种情况,
            // 1.只有左子节点
            // 2,同时有左右子节点
            // 默认为左子节点跟它进行比较
            int childIndex = (index << 1) + 1;
            E child = elements[childIndex];
            // 右子节点
            int rightIndex = childIndex + 1;
            // 选出左右子节点最大的一个
            if (rightIndex < size && compare(elements[rightIndex], child) > 0) {
                child = elements[childIndex = rightIndex];
            }

            if (compare(element, child) >= 0) break;
            // 将子节点存放到index位置
            elements[index] = child;
            // 重新设置index
            index = childIndex;
        }
        elements[index] = element;
    }


    /**
     * 删除堆顶元素的同时插入一个新元素
     * 如果是平常的调用remove和add,做了上滤和下滤两个logn的操作
     * 不如替换顶堆,做下滤操作
     *
     * @param element 添加的元素
     * @return 被删除的元素
     */
    @Override
    public E replace(E element) {
        elementNotNullCheck(element);
        E root = null;
        if (size == 0) { // 堆是空的
            elements[0] = element;
            size++;
        } else {
            root = elements[0];
            elements[0] = element;
            siftDown(0);
        }
        return root;
    }
    /**
     * 批量建堆,给一个随机数组,已通过构造器赋值数组
     */
    private void heapify() {
        // 自上而下的上滤,从根节点开始
        /*for (int i = 1; i < size; i++) {
            siftUp(i);
        }*/

        // 自下而上的下滤,从最后一个非叶子节点开始,因为叶子节点不能下滤了
        for (int i = (size >> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
    }


    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    // 添加必须符合完全二叉堆的性质,但不加考虑的添加,可能不符合二叉堆的性质
    // 出现不好的情况: 添加元素为空/容量不够/无法满足二叉堆[大顶堆]的性质







    /*--------------------------------------------------扩容-------------------------------------------------------------*/
    // 扩容
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;

        // 新容量为旧容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    /*--------------------------------------------------构造器-----------------------------------------------------------*/
    public BinaryHeap(E[] elements) {
        this(elements, null);
    }

    public BinaryHeap(E[] elements, Comparator<E> comparator) {
        super(comparator);
        if (elements == null || elements.length == 0) {
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        } else {
            size = elements.length;
            int capacity = Math.max(elements.length, DEFAULT_CAPACITY);   //太少不好
            // 对外界传进来的数组进行深拷贝
            this.elements = (E[]) new Object[capacity];
            for (int i = 0; i < elements.length; i++) {
                this.elements[i] = elements[i];
            }
            heapify();
        }
    }

    public BinaryHeap() {
        this(null, null);
    }

    public BinaryHeap(Comparator<E> comparator) {
        this(null, comparator);
    }

    /*--------------------------------------------------校验-------------------------------------------------------------*/
    private void emptyCheck() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Heap is empty");
        }
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must not be null");
        }
    }


    /*--------------------------------------------------打印-------------------------------------------------------------*/
    @Override
    public Object root() {
        return 0; // 索引
    }

    @Override
    public Object left(Object node) {
        int index = ((int) node << 1) + 1;
        return index >= size ? null : index;
    }

    @Override
    public Object right(Object node) {
        int index = ((int) node << 1) + 2;
        return index >= size ? null : index;
    }

    @Override
    public Object string(Object node) {
        return elements[(int) node];
    }
}
