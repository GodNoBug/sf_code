package sf.sort.sort.cmp.insert;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;

// 插入排序(扑克牌)

// 最好O(n) 最坏O(n^2) 平均O(n^2) 额外空间复杂度O(1) In-place √ 稳定性 √
// 因为相等就不交换,所以是稳定的.
@SuppressWarnings("unused")
public class InsertionSort1<T extends Comparable<T>> extends Sort<T> {

    //1. 在执行过程中，插入排序会将序列分为2部分
    //  ✓ 头部是已经排好序的(有序区域),尾部是待排序的[无序区域].
    //2. 从无序区域头开始扫描每一个元素
    //  ✓ 每当扫描到一个元素,就将它插入到头部合适的位置,然后有序区域的后半部分有序部分后移,使得头部数据依然保持有序.


    // 双层循环的理解: 外层for循环是从0到最后遍历每个元素,内层循环是对每个元素进行对应的操作
    @Override
    protected void sort() {
        // begin: 代表无序部分的当前元素,需要遍历每一个元素,对每一个元素进行排序操作(让这个元素移到前面合适的位置),需要从1开始.
        for (int begin = 1; begin < array.length; begin++) {
            int cur = begin; //
            while (cur > 0 && cmp(cur, cur - 1) < 0) {
                swap(cur, cur - 1);
                cur--;
            }
        }
    }

    /*测试插入排序及变体*/
    @Test
    public void test() {
        int[] arr = {6, 7, 8, 3, 1, 2, 4, 5, 9, 0};
        sort3(arr);
        System.out.println(Arrays.toString(arr));
    }

    //            ← cur
    //             begin →
    //               ↓
    // | 6 | 7 | 8 | 3 | 1 | 2 | 4 | 5 | 9 | 0 |
    //           ↑
    //          cur-1
    public void sort2(int[] array) {
        for (int begin = 1; begin < array.length; begin++) {
            int cur = begin; //
            while (cur > 0 && array[cur] < array[cur - 1]) {
                int tmp = array[cur];
                array[cur] = array[cur - 1];
                array[cur - 1] = tmp;
                cur--;
            }
        }
    }


    public void sort3(int[] array) {
        for (int begin = 1; begin < array.length; begin++) {
            for (int end = begin; end > 0 ; end--) {
                if (array[end] < array[end - 1]) {
                    int tmp = array[end];
                    array[end] = array[end - 1];
                    array[end - 1] = tmp;
                }
            }
        }
    }






}
// 扑克牌手法
// 基本思想:
//   每步将一个待排序的对象,按其关键码大小,插入到前面已经排好序的一组对象的适当位置上,直到对象全部插入位置
// 特点:
//   即边插入边排序,保证子序列中随时都是排好序的
// 基本操作: 有序插入
//   在有序序列中插入一个元素,保持序列有序,有序长度不断增加
//   - 起初,a[0]是长度为1的子序列.然后,逐一将a[1]至a[n-1]插入到有序子序列中
//   - 在插入a[i]前,数组a的前半段(a[0]~a[i-1])是有序段,后半段(a[i]~a[n-1])是停留于输入次序的"无序段".
//   - 插入a[i]使a[0]~a[i-1]有序,也就是要为a[i]找到有序位置j(0≤j≤i),将a[i]插入在a[j]的位置上.
//   找到插入位置和后移

// 找插入位置的方法
// - 顺序法定位插入位置(直接插入排序)
// - 二分法定位插入位置(二分插入排序)
// - 缩小增量多遍插入排序(希尔排序)

// 直接插入排序: 采用顺序查找法查找插入位置.
// 1.复制插入元素 x=a[i] [因为直接移会覆盖这个元素]
// 2.记录后移,查找插入位置
//    j指向排好序的序列当前比较位置.从最后(或最前)开始找.j=i-1(从最后往前找)
//    如果a[j]>x,j位置元素需要往后移.  a[j+1] = a[j]
//    如果a[j]<x,找到插入位置. a[j+1] = x
// 3.插入到正确位置
