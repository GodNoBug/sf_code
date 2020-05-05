package sf.sort.sort.no_cmp.radix;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;

// 基数排序
//
// 最好 O(d ∗ (n + k))
// 最坏 O(d ∗ (n + k))
// 平均 O(d ∗ (n + k))
// 额外空间复杂度 O(n + k)
// In-place ×
// 稳定性 √
// - d是最大值的位数,k是进制(十进制取值范围0~9)
@SuppressWarnings("all")
public class RadixSort extends Sort<Integer> {
    // 基数排序非常适合用于整数排序(尤其是非负整数),因此只演示对非负整数进行基数排序
    // 执行流程：依次对个位数、十位数、百位数、千位数、万位数...进行排序（从低位到高位）
    @Override
    protected void sort() {
        // 要排序多少次?
        // 最大数有多少位,那么就要排序多少次
        int max = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        for (int divider = 1; divider <= max; divider *= 10) {
            countingSort(divider);  // 变的是1,10,100,1000 除数变化
        }
        // array[i]=593
        // 个位数: array[i] / 1 % 10 = 3
        // 十位数：array[i] / 10 % 10 = 9
        // 百位数：array[i] / 100 % 10 = 5
        // 千位数：array[i] / 1000 % 10 = ...
    }

    @Test
    public void sortTest() {
        int[] arr = {7, 3, 5, 8, 6, 7, 4, 5, 1};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public void sort(int[] array) {
        int maxMun = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] > maxMun) {
                maxMun = array[i];
            }
        }

        for (int divider = 1; divider <= maxMun; divider *= 10) {
            // 每位数取值范围都在0-9
            int max = 9;
            int min = 0;
            int d = max - min;
            // 2.创建统计数组并统计对应元素的个数.
            int[] counts = new int[d + 1];
            for (int i = 0; i < array.length; i++) {
                counts[(array[i] - min) / divider % 10]++;   // array[i]的基数 - min
            }
            // 3.统计数组做变形,后面的元素等于前面的元素之和
            for (int i = 1; i < counts.length; i++) {
                counts[i] += counts[i - 1];
            }
            // 4.倒序遍历原始数列,从统计数组找到正确的位置,输出到结果数组[从由往左,具备稳定性](画图理解)
            int[] sorted = new int[array.length];
            for (int i = array.length - 1; i >= 0; i--) {
                sorted[counts[(array[i] - min) / divider % 10] - 1] = array[i]; // 待排序的元素根据找到的索引,放到新数组中.
                counts[(array[i] - min) / divider % 10]--;
            }
            // 将有序数组覆盖无序数组
            for (int i = 0; i < sorted.length; i++) {
                array[i] = sorted[i];
            }
        }
    }

    public void countingSort(int divider) {
        // 每位数取值范围都在0-9
        int max = 9;
        int min = 0;
        int d = max - min;
        // 2.创建统计数组并统计对应元素的个数.
        int[] counts = new int[d + 1];
        for (int i = 0; i < array.length; i++) {
            counts[(array[i] - min) / divider % 10]++;   // array[i]的基数 - min
        }
        // 3.统计数组做变形,后面的元素等于前面的元素之和
        for (int i = 1; i < counts.length; i++) {
            counts[i] += counts[i - 1];
        }
        // 4.倒序遍历原始数列,从统计数组找到正确的位置,输出到结果数组[从由往左,具备稳定性](画图理解)
        int[] sorted = new int[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            sorted[counts[(array[i] - min) / divider % 10] - 1] = array[i]; // 待排序的元素根据找到的索引,放到新数组中.
            counts[(array[i] - min) / divider % 10]--;
        }
        // 将有序数组覆盖无序数组
        for (int i = 0; i < sorted.length; i++) {
            array[i] = sorted[i];
        }
    }
}
// | 126 | 69 | 593 | 23 | 6 | 89 | 54 | 8 |  先对个位数排序
// | 593 | 23 | 54  |126 | 6 | 8  | 69 |89 |  再对十位数排序
// | 6   | 8  | 23  |126 |54 | 69 | 89 |593|  再对百位数排序
// | 6   | 8  | 23  |54  |69 | 89 |126 |593|
// 个位数、十位数、百位数的取值范围都是固定的0~9,可以使用[计数排序]对它们进行排序
// 思考：如果先对高位排序，再对低位排序，是否可行？ 不行,可以试试.