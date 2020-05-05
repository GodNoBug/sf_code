package sf.sort.sort.counting;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;


// 优化后的空间复杂度
//  最好/最坏/平均复杂度: O(n+k)
//  空间复杂度:  O(n+k)
//   - k是整数的取值范围
//  In-place ×
//  稳定性  √
@SuppressWarnings("all")
public class CountingSort2 extends Sort<Integer> {
    // [max,min] mix-min+1
    // [max,min) mix-min

    // 7, 3, 5, 8, 6, 7, 4, 5
    // 从索引0开始依次存放3~8出现的次数
    // | 元素 |  3 4 5 6 7 8 |
    // | 索引 |  0 1 2 3 4 5 |
    // | 次数 |  1 1 2 1 2 1 |

    // 每个次数累加上其前面的所有次数
    // 得到的就是元素再有序序列中的位置信息
    // | 元素 |  3 4 5 6 7 8 |
    // | 索引 |  0 1 2 3 4 5 |
    // | 位置 |  1 2 4 5 7 8 | -> 位置相关
    // 位置8 代表着 7+1, 1代表了出现了1次,7代表了元素8前面有7个元素[有序数组中下标为7].决定了再有序数组中放在哪里.(得结合上面的统计数组)
    // 位置4 代表着 2+2, 2代表了出现了2次,[有序数组中下标为4-2=2,4-1=3].倒数第一个元素值为5的位置是4-1,倒数第二个元素值为5的位置是4-2  -> 倒序遍历更方便
    @Override
    protected void sort() {
        sort(array);
    }


    // 优化1:
    // 不再以输入数列的最大值+1作为计算整数在统计数组中的下标.同时,数列的最小值作为一个偏移量,用于计算整数在统计数组的下标
    // 以刚才的数列为例,统计出数组的长度为99-90+1=10,偏移量等于数组数列的最小值90
    // 对于第1个整数95, 对应的统计数组下标是95-90 = 5.
    // 优化2:
    //   针对稳定性差,只是按照统计数组的下标输出元素值的,并没有真正给元素数组排序.需要稍微改变之前的逻辑,在填充完统计数组以后,
    // 对统计数组做一下变形.

    @Test
    public void test() {
        int[] arr = {7, 3, 5, 8, 6, 7, 4, 5};
        sort(arr);
         System.out.println(Arrays.toString(arr));
    }

    //假设array中的最小值是 min
    // 1.array中的元素 k 对应的统计数组索引是 k – min
    // 2.array中的元素 k 在有序序列中的索引
    //  - counts[k – min] – p  counts是变形后的统计数组
    //  - p代表着是倒数第几个 k
    public void sort(int[] array) {
        // 1.得到数列的最大值和最小值,并计算出差值d
        int max = array[0];
        int min = array[0];
        for (int i = 1; i < array.length; i++) { // O(n)
            if (array[i] > max) {
                max = array[i];
            }
            if (array[i] < min) {
                min = array[i];
            }
        }
        int d = max - min;
        // 2.创建统计数组并统计对应元素的个数.
        int[] counts = new int[d + 1];
        for (int i = 0; i < array.length; i++) {
            counts[array[i] - min]++;
        }
        System.out.println("变形前的统计数组:" + Arrays.toString(counts));
        // 3.统计数组做变形,后面的元素等于前面的元素之和
        for (int i = 1; i < counts.length; i++) { // 遍历原统计数组.遍历到当前位置,把当前位置的值加上前面一个数,得排除第一个元素.
            // counts[i]=counts[i]+counts[i+1];
            counts[i] += counts[i - 1];
        }
        System.out.println("变形后的统计数组:" + Arrays.toString(counts));
        // 4.倒序遍历原始数列,从统计数组找到正确的位置,输出到结果数组[从由往左,具备稳定性](画图理解)
        int[] sorted = new int[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            sorted[counts[array[i] - min] - 1] = array[i]; // 待排序的元素根据找到的索引,放到新数组中.
            counts[array[i] - min]--;
        }
        // 将有序数组覆盖无序数组
        for (int i = 0; i < sorted.length; i++) {
            array[i] = sorted[i];
        }
    }
}

// 分数
//  90 99 95 94 95
//  A  B  C  D   E
// [偏移量为90,数组大小99-90+1=10,95对应统计数组的下标为95-90=5]
//  统计数组
// | 1 | 0 | 0 | 0 | 1 | 2 | 0 | 0 | 0 | 1 |
//   0   1   2   3   4   5   6   7   8   9
//                   ↓
// | 1 | 1 | 1 | 1 | 2 | 4 | 4 | 4 | 4 | 5 |
//   0   1   2   3   4   5   6   7   8   9
// 从统计数组的第2个元素开始,每一个元素都加上前面所有元素之和
// 这样相加的目的,是让统计数组存储的元素值,等于相应整数的最终排序位置序号.
// 例如下标是9的元素值为5,代表原始数列的整数9,最终的排序在第5位

// 第1步,遍历成绩表最后一个的E同学的成绩,E为95分,找到统计数组下标是5的元素,值是4,代表E成绩排名为自己在第4位
// 同时给统计数组下标是5的元素值减1,从4变成3,代表下次再遇到95分的成绩时,最终排名是3.
// 第二步,遍历成绩表倒数第二个的D成绩,为94分,找到统计数组下标是4的元素,值是2,代表D排名位置在第2位
// 同时给统计数组下标是4的元素值减1,从2变成1,代表下次再遇到94分的成绩时(实际上已经遇不到了),最终排名是第1。
// .....
// 这样得出来的计数排序属于稳定排序



