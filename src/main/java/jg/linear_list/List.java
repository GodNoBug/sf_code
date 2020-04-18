package jg.linear_list;

// 提供对外的接口
public interface List<E> {
    static final int ELEMENT_NOT_FOUND = -1;

    void clear();        //清除所有元素

    int size();        // 元素的数量

    boolean isEmpty();    // 是否为空

    boolean contains(E element);    // 是否包含某个元素

    void add(E element);            // 添加元素到尾部

    E get(int index);    // 获取index位置的元素

    E set(int index, E element);        // 设置index位置的元素,返回原来的元素

    void add(int index, E element);        // 在index位置插入一个元素

    E remove(int index);        // 删除index位置的元素

    int indexOf(E element);        // 查看元素的索引
}
