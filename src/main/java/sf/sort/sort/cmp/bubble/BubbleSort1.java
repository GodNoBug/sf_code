package sf.sort.sort.cmp.bubble;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;

// 冒泡排序
//    最好 O(n) 最坏O(n^2) 平均O(n^2) 额外空间复杂度O(1) In-place √ 稳定性 √
// 观察: 有序/无序序列中,任意/总有一对相邻元素顺序/逆序
// 扫描交换: 依次比较每一对相邻元素,如果有必要,交换之.若整趟扫描都没有进行交换,则排序完成;否则,再做一趟扫描交换;
@SuppressWarnings("unused")
public class BubbleSort1<T extends Comparable<T>> extends Sort<T> {
    // 问题: 无序序列{5,8,6,3,9,2,1,7},希望从小到大的顺序对齐进行排序
    //    按照冒泡排序的思想,我们要把相邻的元素两两比较,当一个元素大于右侧相邻元素时,
    // 交换它们的位置;当一个元素小于或等于右侧相邻元素时,位置不变。最终导致的结果就是
    // 每一轮结果,末尾有序区域增加一个,直到有序区域覆盖到(元素数量-1)大小为止,因为剩下
    // 最后一个自然有序.
    //    每一轮都要遍历无序区域,使得有序区域增加,共遍历(元素数量-1)论,所以时间复杂度
    // 是O(n^2)
    @Override
    protected void sort() {
        for (int end = array.length - 1; end > 0; end--) {         // 从1开始,到末尾,当写这个循环,仅仅进行了一轮步骤1,末尾只确定了一个最大的值,剩下的还是乱序的,应该执行array.length遍,每遍忽略曾经找到的最大值,才能
            for (int begin = 1; begin <= end; begin++) {
                if (cmp(begin, begin - 1) < 0) { // array[begin] < array[begin - 1] 比大小,发现情况不对,交换
                    swap(begin, begin - 1);
                }
            }
        }
    }


    /*测试与冒泡排序变体*/
    @Test
    public void sortTest() {
        int[] ints = {5, 8, 6, 3, 9, 2, 1, 7};
        sort2(ints);
        System.out.println(Arrays.toString(ints));
    }

    // 使用双循环进行排序,外部循环控制所有的回合,内部循环实现每一轮的冒泡出来,进行元素比较,再进行元素交换
    // 对于array.length是否-1,不等式是≥/≤还是>/<,通常先考虑边界情况,不需要死记硬背.
    public void sort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                int tmp;
                if (array[j] > array[j + 1]) {
                    tmp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = tmp;
                }
            }
        }
    }

    public void sort2(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 1; j < array.length - i; j++) {
                int tmp;
                if (array[j - 1] > array[j]) {
                    tmp = array[j - 1];
                    array[j - 1] = array[j];
                    array[j] = tmp;
                }
            }
        }
    }

    public void sort3(int[] array) {

        for (int end = array.length - 1; end > 0; end--) { // end应该从最后一个索引开始,不断的--,减到0[因为无法拿左边的来比了,没有元素了]
            for (int begin = 1; begin <= end; begin++) {  //  不能忽略最后一位和前一位的比较
                int tmp;
                if (array[begin - 1] > array[begin]) {
                    tmp = array[begin - 1];
                    array[begin - 1] = array[begin];
                    array[begin] = tmp;
                }
            }
        }
    }
}
// 原地算法(In-place Algorithm)
// 何为原地算法？
//  不依赖额外的资源或者依赖少数的额外资源，仅依靠输出来覆盖输入[取出来覆盖]
//  空间复杂度为 𝑂(1) 的都可以认为是原地算法
//非原地算法，称为 Not-in-place 或者 Out-of-place
//冒泡排序属于 In-place
