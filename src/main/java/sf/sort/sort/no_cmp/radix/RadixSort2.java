package sf.sort.sort.no_cmp.radix;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;

// 基数排序的另一种思路
// 空间复杂度是 O(kn+k)
// 计算复杂度是 O(dn)
//  d 是最大值的位数， k 是进制
@SuppressWarnings("all")
public class RadixSort2 extends Sort<Integer> {


    @Override
    protected void sort() {
        sort(array);
    }
    // | 126 | 69 | 593 | 23 | 6 | 89 | 54 | 8 |  对个位数进行排序


    //   0   1   2   3   4   5   6   7   8   9
    // |   |   |   |593|54 |   |126|   | 8 |69 |  大小为10的数组,下标代表基数为0~9.每个下标的元素又代表有相同基数的数组,为确保足够用,"挂载"的数组,需要array.length个
    // |   |   |   |23 |   |   | 6 |   |   |89 |

    // | 593 | 23 | 54 | 126 | 6 | 8 | 69 | 89 | 遍历取出放入原数组中. 现在对十位数进行相同逻辑的排序,以此类推

    //   0   1   2   3   4   5   6   7   8   9
    // | 6 |   |23 |   |   |54 |69 |   |89 |593|
    // | 8 |   |126|   |   |   |   |   |   |   |

    // | 6 | 8 | 23 | 126 | 54 | 69 | 89 | 593 |  遍历取出放入原数组中. 现在对百位数进行相同逻辑的排序

    //   0   1   2   3   4   5   6   7   8   9
    // | 6 |126|   |   |   |593|   |   |   |   |
    // | 8 |   |   |   |   |   |   |   |   |   |
    // |23 |   |   |   |   |   |   |   |   |   |
    // |54 |   |   |   |   |   |   |   |   |   |
    // |69 |   |   |   |   |   |   |   |   |   |
    // |89 |   |   |   |   |   |   |   |   |   |
    // | 6 | 8 | 23 | 54 | 69 | 89 | 126 | 593 | 多少轮排序?仍是取决于最大的数有多少位
    @Test
    public void sortTest() {
        int[] arr = {7, 3, 5, 8, 6, 7, 4, 5, 1,2,3,4};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    private void sort(int[] array){
        // 求出数组中的最大值
        int max = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        // 桶数组 10个桶,每个桶长度确保够用,为array.length
        int[][] buckets = new int[10][array.length];
        // 统计每个桶元素数量,如果发现当前桶元素数量为0可以不去遍历当前桶
        int[] bucketSizes = new int[buckets.length];
        for (int divider = 1; divider <= max; divider *= 10) {
            for (int i = 0; i < array.length; i++) {
                int no = array[i] / divider % 10;
                buckets[no][bucketSizes[no]++] = array[i]; // 将数组元素放到对应的桶里面去
            }
            int index=0;
            for (int i = 0; i < buckets.length; i++) {
                for (int j = 0; j < bucketSizes[i]; j++) {
                    array[index++]=buckets[i][j];        // 从桶里面取出东西来,覆盖原来的数组中
                }
                bucketSizes[i]=0;   // 清空每个元素数量的统计
            }
        }
    }


}
