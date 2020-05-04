package sf.sort.sort.cmp.insert;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;

// 插入排序-逆序对(Inversion)
// ◼ 什么是逆序对？
//  数组 [2,3,8,6,1] 的逆序对为： <2,1> <3,1> <8,1> <8,6> <6,1>，共5个逆序对
// ◼ 插入排序的时间复杂度与逆序对的数量成正比关系
// 逆序对的数量越多，插入排序的时间复杂度越高
// ◼ 最坏、平均时间复杂度： O(n^2)
// ◼ 最好时间复杂度： O(n),没有逆序对
// ◼ 空间复杂度： O(1)
// ◼ 属于稳定排序
// ◼ 当逆序对的数量极少时，插入排序的效率特别高
//   有时候甚至速度比 O(nlogn)级别的快速排序还要快
// ◼ 数据量不是特别大的时候，插入排序的效率也是非常好的
@SuppressWarnings("unused")
public class InsertionSort2<T extends Comparable<T>> extends Sort<T> {

    // 优化1
    // 原本是把需要交换的元素交换,现在是备份一个,挪动其他,然后插入适合位置
    // 优化思路是将[交换]转为[挪动]
    // 1.先将待插入的元素备份
    // 2.头部有序数据中比待插入元素大的，都朝尾部方向挪动1个位置
    // 3.将待插入元素放到最终的合适位置
    // 逆序对越多,进入while的次数就越多,[挪动]相对[交换]的优化效果越明显
    @Override
    protected void sort() {
        for (int begin = 1; begin < array.length; begin++) {
            int cur = begin;
            T v = array[begin];  // 元素备份
            while (cur > 0 && cmp(v, array[begin - 1]) < 0) { // 从后面遍历前面,因为从前向后遍历,不好挪动,后面的值需要等于前面的值,那么被覆盖的值怎么办.可以考虑暂时保存
                array[cur] = array[cur - 1];
                cur--;
            }
            array[cur] = v;
        }
    }

    @Test
    public void test() {
        int[] arr = {6, 7, 8, 3, 1, 2, 4, 5, 9, 0};
        sort3(arr);
        System.out.println(Arrays.toString(arr));
    }

    //                    ← cur
    //                     begin →
    //                       ↓   v=arr[begin]
    // | 0 | 1 | 2 | 5 | 6 | 4 |
    //                   ↑
    //                 cur-1
    public void sort2(int[] array) {
        for (int begin = 1; begin < array.length; begin++) {
            int cur = begin;
            int v = array[cur]; // 备份待插入元素
            while (cur > 0 && array[cur - 1] > v) { // 挪动元素
                array[cur] = array[cur - 1];
                cur--;
            }
            array[cur] = v; // 插入待插入的元素
        }
    }

    //            ← cur
    //             begin →
    //   ↓           ↓                              x=arr[begin]
    // | 6 | 7 | 8 | 3 | 1 | 2 | 4 | 5 | 9 | 0 |
    public void sort3(int[] array) {
        int begin, end, tmp;
        for (begin = 1; begin < array.length; begin++) {
            tmp = array[begin];
            for (end = begin - 1; end >= 0 ; end--) {
                if (tmp>array[end]) break;
                array[end + 1] = array[end];
            }
            array[end + 1] = tmp;
        }
    }

}
