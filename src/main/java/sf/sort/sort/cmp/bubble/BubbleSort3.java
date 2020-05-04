package sf.sort.sort.cmp.bubble;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;

// 其他变体但和优化无关的代码在BubbleSort4和BubbleSort5
@SuppressWarnings("unused")
public class BubbleSort3<T extends Comparable<T>> extends Sort<T> {
    // 优化2
    // 问题: {3,4,2,1,5,6,7,8},前半部分的元素无序,后半部分的元素按升序排列.即使后半部分有序,但是没有优化的代码仍是需要做没必要的比较
    // 发现:
    //    这个问题的关键点在于对数列有序区的界定。
    //    按照现有的逻辑,有序区的长度和排序的轮数是相等的。例如第1轮排序过后的有序区长度是1,第2轮排序过后的有序区长度是2 ……
    //    实际上,数列真正的有序区可能会大于这个长度,如上述例子中在第2轮排序时,后面的5个元素实际上都已经属于有序区了。因此后面的多次
    //  元素比较是没有意义的。
    //    那么,该如何避免这种情况呢? 我们可以在每一轮排序后,记录下来最后一次元素交换的位置,该位置即为无序数列的边界,再往后就是有序区了。

    // 最坏、平均时间复杂度O(n^2)
    // 最好时间复杂度O(n) [一趟扫描]
    // 空间复杂度O(1)

    @Override
    protected void sort() {
        for (int end = array.length - 1; end > 0; end--) {
            // sorted的初始值的选择: sorted的初始值在数据完全有序的时候有用,1就一轮扫描完全结束,压根就没有进入if
            int sortedIndex = 1;
            boolean sorted = true;
            for (int begin = 1; begin <= end; begin++) {
                if (cmp(begin, begin - 1) < 0) { // array[begin] < array[begin - 1] 左边比较大
                    swap(begin, begin - 1);
                    sortedIndex = begin;
                    sorted = false;
                }
            }
            end = sortedIndex;  // 记录最后一次交换
            if (sorted) {
                break;
            }
        }
    }

    /*测试与冒泡排序变体*/
    @Test
    public void sortTest() {
        int[] ints = {3, 4, 2, 1, 5, 6, 7, 8};
        sort2(ints);
        System.out.println(Arrays.toString(ints));
    }

    protected void sort2(int[] array) {
        for (int end = array.length - 1; end > 0; end--) {
            boolean sorted = true;
            int sortedIndex = 1;  // 本轮记录最后一次交换的位置,内循环是交换,内循环结束即最后一次交换的位置.
            for (int begin = 1; begin <= end; begin++) {
                int tmp;
                if (array[begin] < array[begin - 1]) {
                    tmp = array[begin];
                    array[begin] = array[begin - 1];
                    array[begin - 1] = tmp;
                    sortedIndex = begin;
                    sorted = false;
                }
            }
            end = sortedIndex;  // 记录最后一次交换
            if (sorted) {
                break;
            }
        }
    }

    public void sort3(int[] array) {
        int lastExchangeIndex = 1; // 记录最后一次交换的位置,内循环是交换内循环结束即最后一次交换的位置.
        int sortBorder = array.length - 1; // 无序数列的边界,每次比较只需要到这里位置

        for (int i = 0; i < array.length - 1; i++) {
            boolean isSorted = true;  // 有序标记,每一轮的初始值都是true
            for (int j = 0; j < sortBorder; j++) {
                int tmp;
                if (array[j] > array[j + 1]) {
                    tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                    // 因为有元素进行交换,所以不是有序的,标记为false
                    isSorted = false;
                    // 更新最后一次交换元素的位置
                    lastExchangeIndex = j;
                }
            }
            sortBorder = lastExchangeIndex;
            if (isSorted) {
                break;
            }
        }
    }
}
