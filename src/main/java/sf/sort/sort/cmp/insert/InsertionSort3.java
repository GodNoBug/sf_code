package sf.sort.sort.cmp.insert;


import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;


// 如何确定一个元素在数组中的位置？（假设数组里面全都是整数）
//  如果是无序数组，从第 0 个位置开始遍历搜索，平均时间复杂度： O(n)
//  如果是有序数组，可以使用二分搜索，最坏时间复杂度：O(logn)
// 需要注意的是，使用了二分搜索后，只是减少了比较次数，但插入排序的平均时间复杂度依然是 O(n^2)

// 假设在 [begin, end) 范围内搜索某个元素 v, mid == (begin + end) / 2
//◼ 如果 v < m,去 [begin, mid) 范围内二分搜索
//◼ 如果 v ≥ m,去 [mid + 1, end) 范围内二分搜索
@SuppressWarnings("all")
public class InsertionSort3<T extends Comparable<T>> extends Sort<T> {

    // 使用二分搜索查找排好序的部分,找出适合的插入位置,然后后面的元素挪动[挪动是都内挪的,不过减少了查找要比较次数]
    @Override
    protected void sort() {

        for (int begin = 1; begin < array.length; begin++) {
            T v = array[begin];  // begin数值意味着begin之前已经排好序了
            int insertIndex = search(begin);
            // 将[insertIndex,begin)范围内的元素往右挪动一位
            //for (int i = begin - 1; i >= insertIndex; i--) {
            //    array[i + 1] = array[i];
            //}
            for (int i = begin; i > insertIndex; i--) {
                array[i] = array[i - 1];
            }
            array[insertIndex] = v;
        }
    }


//    @Override
//    protected void sort() {
//        for (int begin = 0; begin < array.length; begin++) {
//            insert(begin, search(begin)); // 原来的位置和待插入的位置
//        }
//    }

    /**
     * 将source位置的元素插入到dest位置
     *
     * @param source 元素原来位置
     * @param dest   待插入位置
     */
    private void insert(int source, int dest) {
        T v = array[source];  // begin数值意味着begin之前已经排好序了
        for (int i = source; i > dest; i--) {
            array[i] = array[i - 1];
        }
        array[dest] = v;
    }

    /**
     * 利用二分搜索找到 index位置元素的待插入位置
     * 已经排好序的区间范围是[0,index)
     * 1.如果存在多个重复元素,返回的是哪个是不确定的(需要优化)
     * 2.需要插入的元素可能并不存在已经排序好的序列中
     *
     * @param index 待插入元素的索引
     * @return 需要往后挪的元素的索引(规定是第一个大于v的元素位置)
     */
    private int search(int index) {
        int begin = 0;
        int end = index;
        while (begin < end) {
            int mid = (begin + end) >> 1;
            if (cmp(index, mid) < 0) {
                end = mid;
            } else { //cmp(index, mid) >= 0 // 要找的是第一个大于v的元素位置,如果中间等于v,那么认为插入位置在后面
                begin = mid + 1;
            }
        }
        return begin; // 也可以返回end
    }

    @Test
    public void sortTest(){
        int[] arr = {6, 7, 8, 3, 1, 2, 4, 5, 9, 0};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public void sort(int[] arr) {

        for (int begin = 1; begin < arr.length; begin++) {
            int low = 0, high = begin - 1;
            int cur = arr[begin];
            while (low <= high) {
                int mid = (low + high) >> 1;
                if (cur < arr[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
            for (int j = begin - 1; j >= high + 1; j--) {
                arr[j + 1] = arr[j];
            }
            arr[high + 1] = cur;
        }
    }


}
