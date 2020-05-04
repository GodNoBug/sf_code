package sf.sort.sort.cmp.bubble;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;
// 优化:
//    未排序的部分前段可能是有序的,那么到了有序的那一段,没必要再进行交换元素,应该提前结束
//       一趟遍历交换过程中,发现没有发生过交换,那么剩下的几轮培训就不必执行了,可以提前结束工作.
//    在外层循环内部设置一个标记默认为false(表示没有发生一次交换过),但凡内层循环执行过一次交换,说明交换还需要继续执行
//    若没有一次交换过,直接退出大循环.
// [注意,在提前有序的情况下,提前结束,量大的话,随机性大,很难达到提前有序提前结束,比没优化花费时间更长,效率更低]

public class BubbleSort2<T extends Comparable<T>> extends Sort<T> {

    @Override
    protected void sort() {
        for (int end = array.length - 1; end > 0; end--) {
            boolean sorted = true;   //因为不能知道哪一趟后,变得有序,于是每一趟扫描之前假定有序,在每一趟结束后判断是否交换过,可能不在第一趟扫描
            for (int begin = 1; begin <= end; begin++) {
                // 这一趟扫描没有进入到一下代码块内,没有进行交换,意味着完全有序
                if (cmp(begin, begin - 1) < 0) { // array[begin] < array[begin - 1] 左边比较大
                    swap(begin, begin - 1);
                    sorted = false;
                }
            }
            if (sorted) {
                break;
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

    public void sort2(int[] array) {
        for (int end = array.length - 1; end > 0; end--) {
            boolean exchanged = false;
            for (int begin = 1; begin <= end; begin++) {
                int tmp;
                if (array[begin] < array[begin - 1]) { // array[begin] < array[begin - 1] 左边比较大
                    tmp = array[begin];
                    array[begin] = array[begin - 1];
                    array[begin - 1] = tmp;
                    exchanged = true;
                }
            }
            // 如果在本轮排序中,元素有交换,则说明数列无序;如果没有元素交换.则说明数列已然有序， 然后直接跳出大循环。
            if (!exchanged) {
                break;
            }
        }
    }
}
