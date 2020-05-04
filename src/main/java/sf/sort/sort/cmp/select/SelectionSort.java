package sf.sort.sort.cmp.select;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;

// 选择排序
//  最好O(n^2) 最好O(n^2) 平均O(n^2) [无论有序无序,都执行找最大值放到最后的操作] 额外空间复杂度O(1) In-place √  稳定性 ×
// 1.从序列中找出最大的那个元素,然后与末尾的元素交换位置.
//  ✓ 执行完一轮后，最末尾的那个元素就是最大的元素
// 2. 忽略1中曾经找到的最大元素,有序区域扩大,重复执行步骤
//    每一轮都要遍历无序区域,找到最大的,使得有序区域增加
// ◼ 选择排序的交换次数要远远少于冒泡排序,平均性能优于冒泡排序
//  ✓ 冒泡排序每比一次交换一次,而冒泡排序找到最大才执行一次交换

// 优化的地方考虑 如何更优地选最值=>大顶堆/小顶堆
public class SelectionSort<T extends Comparable<T>> extends Sort<T> {


    @Override
    protected void sort() {
        for (int end = array.length - 1; end > 0; end--) {
            int maxIndex = 0;
            for (int begin = 1; begin <= end; begin++) { // 每一轮选最值=>优化: 选最值交给堆来做
                if (cmp(maxIndex, begin) <= 0) { // array[maxIndex] <= array[begin]
                    maxIndex = begin;
                }
            }
            swap(maxIndex, end);
        }
    }

    /*测试与选择排序变体*/
    @Test
    public void sortTest() {
        int[] ints = {5, 8, 6, 3, 9, 2, 1, 7};
        sort2(ints);
        System.out.println(Arrays.toString(ints));
    }

    public void sort2(int[] arr) {
        for (int end = arr.length - 1; end > 0; end--) {
            int maxIndex = 0;
            for (int begin = 1; begin <= end; begin++) { // 每一轮选最值=>优化: 选最值交给堆来做
                if (arr[maxIndex]<=arr[begin]) { // array[maxIndex] <= array[begin]
                    maxIndex = begin;
                }
            }
            int tmp = arr[end];
            arr[end] = arr[maxIndex];
            arr[maxIndex] = tmp;
        }
    }

    public void sort3(int[] arr) {
        int sortBorder = arr.length - 1;
        for (int i = 0; i < arr.length - 1; i++) {
            int maxIndex = 0;
            for (int begin = 1; begin < sortBorder; begin++) {
                if (arr[maxIndex] <= arr[begin]) {
                    maxIndex = begin;
                }
            }
            int tmp = arr[sortBorder];
            arr[sortBorder] = arr[maxIndex];
            arr[maxIndex] = tmp;
            sortBorder--;
        }

    }

}
