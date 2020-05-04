package sf.sort.sort.cmp.heap;

import sf.sort.Sort;

// 堆排序
// 最好O(nlogn) 最坏O(nlogn) 平均O(nlogn) 额外空间复杂度O(1) In-place √  稳定性 √
// 可以认为是对选择排序的一种优化,也是选择最大的元素放到最后.使用堆使时间复杂度变低
// 二叉堆的构建、删除、自我调整等基本操作,正是实现堆排序的基础

// 执行流程
// 1.对序列进行原地建堆(heapify)形成大顶堆 O(n)
// 2.重复执行以下操作,直到元素数量为1 O(logn)
//  ✓ 交换堆顶元素与尾元素[也就是首尾交换]
//  ✓ 堆的元素数量减 1 (排除一个已经排好序的元素,剩下的仍为堆)
//  ✓ 对0位置进行1次下滤 (siftDown) 操作
// 时间复杂度: O(nlogn)
public class HeapSort<T extends Comparable<T>> extends Sort<T> {
    private int heapSize;

    @Override
    protected void sort() {
        // 原地建堆  自下而上的下滤,从最后一个非叶子节点开始,因为叶子节点不能下滤了
        heapSize=array.length;
        for (int i = (heapSize >> 1) - 1; i >= 0; i--) {
            siftDown(i);
        }
        while (heapSize > 1) {
            // 交换堆顶和尾部元素
            swap(0, heapSize - 1);
            // 减小heapSize
            heapSize--;
            // 对0位置进行下滤操作(恢复堆的性质)
            siftDown(0);
        }
    }


    /**
     * 让index位置下滤
     * 完全二叉树性质
     * index有两种情况,只有左子节点/同时有左右子节点
     * index< 第一个叶子节点的索引 也就是小于非叶子节点的数量=floor(n/2)
     * 为什么不能 2*index+2 >size-1  2*index+1 >size-1
     * @param index 索引
     */
    private void siftDown(int index) {
        T element = array[index];
        int half = heapSize >> 1;   // 第一个叶子节点的下标
        while (index < half) {  // 保证index下面有子节点才能下滤,也就是满足非叶子节点的条件才能进入循环,index<第一个叶子节点的索引
            // index有两种情况,
            // 1.只有左子节点
            // 2,同时有左右子节点
            // 默认为左子节点跟它进行比较
            int childIndex = (index << 1) + 1;
            T child = array[childIndex];
            // 右子节点
            int rightIndex = childIndex + 1;
            // 选出左右子节点最大的一个
            if (rightIndex < heapSize && cmp(array[rightIndex], child) > 0) {
                child = array[childIndex = rightIndex];
            }

            if (cmp(element, child) >= 0) break;
            // 将子节点存放到index位置
            array[index] = child;
            // 重新设置index
            index = childIndex;
        }
        array[index] = element;
    }

}
