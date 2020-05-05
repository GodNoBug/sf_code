package sf.sort.sort.no_cmp.counting;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;

// 计数排序
// 特点:
//    空间换时间
// 适用范围:
//    适合对一定范围内的整数进行排序
// 思想:
//   统计每个整数在序列中出现的次数，进而推导出每个整数在有序序列中的索引

//  以计数排序来说,这种排序算法是利用数组下标来确定元素的正确位置的。
@SuppressWarnings("all")
public class CountingSort extends Sort<Integer> {
    // 问题:
    //  假设数组中有20个随机整数,取值范围为0～ 10,要求用最快的速度把这20个整数从小到大进行排序。

    //   考虑到这些整数只能够在0、 1、 2、 3、 4、 5、 6、 7、 8、 9、 10这11个数中取
    //值,取值范围有限。所以,可以根据这有限的范围,建立一个长度为11的数组。 数组下标从0到10,
    //元素初始值全为0。

    @Override
    protected void sort() {
        sort(array);
    }

    @Test
    public void test() {
        int[] arr = {9, 3, 5, 4, 9, 1, 2, 7, 8, 1, 3, 6, 5, 3, 4, 0, 10, 9, 7, 9};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    // counts数组中每一个下标位置的值代表数列中对应整数出现的次数
    // 有了这个统计结果,排序就很简单了.直接遍历数组,输出数组元素的下标值,
    //元素的值是几,就输出几次。
    public void sort(int[] array) {
        // 1.得到数列的最大值
        int max = array[0];
        for (int i = 1; i < array.length; i++) { // O(n)
            if (array[i] > max) {
                max = array[i];
            }
        }
        // 2.根据数列最大值确定统计数组的长度,存储每个整数出现的次数. 1 + max 确保数组的最后一个下标是max
        int[] counts = new int[1 + max];
        // 3.遍历数列, 填充统计数组
        for (int i = 0; i < array.length; i++) {  // O(n)
            counts[array[i]]++;   // counts的下标就是元素的值,
        }
        // 根据整数出现的次数,对整数进行排序
        int index = 0;
        for (int i = 0; i < counts.length; i++) {  // O(n) ,复制了一遍
            while (counts[i] > 0) {
                array[index] = i;  // 元素的值,就等于统计数组的下标
                index++;           // 填下一个值
                counts[i]--;       // 当前整数出现次数-1
            }
        }
    }
}
// 这个版本的实现存在以下问题
// 1.无法对负整数进行排序
// 2.极其浪费内存空间
// 3.是个不稳定的排序
// 4.只能整数排序,不能自定义对象排序
//   ......

//◼ 之前学习的冒泡、选择、插入、归并、快速、希尔、堆排序，都是基于比较的排序
//   平均时间复杂度目前最低是 O(nlogn)
// ◼ 计数排序、桶排序、基数排序，都不是基于比较的排序
//   它们是典型的用空间换时间，在某些时候，平均时间复杂度可以比 O nlogn 更低
//◼ 计数排序于1954年由Harold H. Seward提出，适合对一定范围内的整数进行排序
//◼ 计数排序的核心思想
//   统计每个整数在序列中出现的次数，进而推导出每个整数在有序序列中的索引