package jg.linear_list.array;

import jg.linear_list.AbstractList;

import java.util.Objects;

/**
 * ArrayList与之类似
 * 添加缩小容量的操作
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class DynamicArray2<E> extends AbstractList<E> {
    private E[] elements;       // 所有的元素

    private static final int DEFAULT_CAPACITY = 10;     // 默认容量

    public DynamicArray2(int capacity) {
        capacity = (capacity < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY : capacity; // 如果传进来的容量小于默认容量,使用默认容量,否则使用传进来的容量
        elements = (E[]) new Object[capacity];                                  // 创建Object数组
    }

    public DynamicArray2() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * 清除所有元素
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;  // 如果是对象的话,这步是有必要的,利于释放对象,
        }

        size = 0;  // 如果是基本类型,size=0即可,因为get是按size大小取的
        // 仅供参考
        if (elements != null && elements.length > DEFAULT_CAPACITY) {
            elements = (E[]) new Object[DEFAULT_CAPACITY];
        }
    }


    /**
     * 获取index位置的元素
     */
    public E get(int index) {
        rangeCheck(index);
        return elements[index];
    }

    /**
     * 设置index位置的元素
     * 不会移动后面的数据
     *
     * @param index   索引
     * @param element 元素
     * @return 原来的元素ֵ
     */
    public E set(int index, E element) {
        rangeCheck(index);
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    /**
     * 在index位置插入一个元素
     * 从最后面开始遍历,遍历到index位置,每走一步,后面的元素覆盖前面的,空出来一个位置正好插入
     *
     * @param index   要添加到指定数组下标
     * @param element 新增元素
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);

        ensureCapacity(size + 1); // 试试size+1是否需要扩容
        // 删除remove代码是因为前面空出来正好后面的往前挪,
        // 而添加的话,应该是要后面的挪到腾出一个位置,有空间插入元素,应该是倒过来遍历
        // 前面的覆盖后面的
        // 从size开始,而非数组末尾
        /*for (int i = size - 1; i >= index; i--) {
            elements[i + 1] = elements[i];
        }*/
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    /**
     * 删除index位置的元素,考虑到删除的地方在中间,后面的元素往前面移动
     * 方法: 从index后一位开始,每走一步,把后面的元素和前面的左交换
     * 优化:
     *
     * @param index 删除的索引
     * @return 删除的元素
     */
    public E remove(int index) {

        rangeCheck(index);
        E old = elements[index];  // 备份被删除的,用于返回
        // 从index后一位开始,每走一步,把后面的元素覆盖前面的,index+1~size-1
        /*for (int i = index + 1; i <= size - 1; i++) {
            elements[i - 1] = elements[i];
        }*/
        for (int i = index + 1; i < size; i++) {
            elements[i - 1] = elements[i];
        }
        // 维护移动后的原来应该空出来的数据,把它置空,也可以不用处理,不过如果存储的是对象的话建议要清空,因为--size导致无法访问那个元素
        elements[--size] = null;                                  // 简化代码,容易看不懂,先--再使用size这个位置清空合并成了一句话

        // 缩小容量
        trim();
        return old;
    }

    private void trim() {
        // 剩余空间占总占总容量的一半时,就进行缩容.保证小于默认容量不缩容[没有完全的标准,自己定义规则]
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity >> 1;
        if (size >= (newCapacity) || oldCapacity <= DEFAULT_CAPACITY) return;

        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
        System.out.println(oldCapacity + "缩容为" + newCapacity);
    }

    public void remove(E element) {
        remove(indexOf(element));
    }

    /**
     * 查看元素的索引
     *
     * @param element 元素
     * @return 元素所在的数组索引
     */
    public int indexOf(E element) {
        if (element == null) {  // 1  遍历查找为null的元素索引,遍历size大小
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {  // 遍历查找与element相同的元素 ,找到并返回
                if (element.equals(elements[i])) return i; // n // 写成==就写死比较基本类型数值和引用类型的地址值了
            }
        }
        return ELEMENT_NOT_FOUND;       // 找不到返回-1
    }

//	public int indexOf2(E element) {
//		for (int i = 0; i < size; i++) {
//			if (valEquals(element, elements[i])) return i; // 2n
//		}
//		return ELEMENT_NOT_FOUND;
//	}
//
//	private boolean valEquals(Object v1, Object v2) {
//		return v1 == null ? v2 == null : v1.equals(v2);
//	}

    /**
     * 保证要有capacity的容量
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
            newElements[i] = elements[i];
        }
        elements = newElements;

        System.out.println(oldCapacity + "扩容为" + newCapacity);
    }


    @Override
    public String toString() {
        // size=3, [99, 88, 77]
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(", [");
        for (int i = 0; i < size; i++) {
            if (i != 0) { // 只要不是第一个元素,前面先添加","
                string.append(", ");
            }
            string.append(elements[i]);
//			if (i != size - 1) {
//				string.append(", ");
//			}
        }
        string.append("]");
        return string.toString();
    }
}
