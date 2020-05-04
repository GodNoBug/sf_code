package sf.sort.sort.cmp.shell;

import org.junit.Test;
import sf.sort.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// 希尔排序
//
// 最好: O(n)  最坏: O(n^(4/3)) ~ O(n^2) 平均: 取决于步长序列  额外空间复杂度 O(1) In-place √ 稳定性 ×

// 思考:
// 1.插入排序通常是比较一次移动一步,可否比较一次移动一大步
// 2.直接插入排序在什么情况下效率比较高? 直接插入排序在基本有序时,效率较高;在待排序的记录个数比较少时效率比较高.
// 这就是希尔排序的思想的出发点

// 基本思想:
//   先将整个待排记录分割成若干子序列,分别进行直接插入排序,待整个序列中的记录"基本有序"时,
// 再对全体记录进行一次直接插入排序
// 特点:
//   1.缩小增量
//   2.多遍插入排序
//   - 一次移动,移动位置较大,跳跃式地接近排序后的最终位置
//   - 最后一次只需要少量移动
//   - 增量序列必须是递减的,最后一个必须是1
//   - 增量序列应该是互质的(互为质数)
@SuppressWarnings("all")
public class ShellSort<T extends Comparable<T>> extends Sort<T> {


    @Override
    protected void sort() {
        List<Integer> stepSequence = shellStepSequence(); // {8,4,2,1} 每个元素是分成多少列
        for (Integer step : stepSequence) { // 遍历步长数列
            shellSort(step);
        }
    }


    /**
     * 分成step列进行排序
     * ◼ 假设元素在第 col 列、第 row 行，步长（总列数）是 step
     *  那么这个元素在数组中的索引是 col + row * step
     *  比如 9 在排序前是第 2 列、第 0 行，那么它排序前的索引是 2 + 0 * 5 = 2
     *  比如 4 在排序前是第 2 列、第 1 行，那么它排序前的索引是 2 + 1 * 5 = 7
     *
     * @param step 步长
     */
    private void shellSort(int step) {
        // 对每step个数组元素进行排序
        // col: 第几列,column的简称
        for (int col = 0; col < step; col++) { // 对第col进行排序
            // col col+step col+2*step col+3*step
            // 传统的插入排序是0,1,2,3,4....array.length-1 步长为1,从1开始
            // 现在的插入排序是0 col+step col+2*step col+3*step,步长为step,从0+step开始,因为第1个已经默认排好序的
            for (int begin = col + step; begin < array.length; begin += step) {
                int cur = begin;
                T v = array[begin];
                while (cur > col && cmp(v, array[cur - step]) < 0) {
                    array[cur] = array[cur - step];
                    cur -= step;
                }
                array[cur] = v;
            }
        }
    }


    // 希尔本人给出的步长序列是(n/(2^k)),也就是数据规模不断的除于二,得出数列
    // 使得最坏情况实际复杂度O(n^2)
    // 明确已知的最好的步长序列,最坏情况时间复杂度是O(n^(4/3)
    private List<Integer> shellStepSequence() {
        List<Integer> stepSequence = new ArrayList<>();
        int step = array.length;
        while ((step >>= 1) > 0) {  // 1/2=0  // step = step >> 1 可以变成 step>>=1
            stepSequence.add(step);
        }
        return stepSequence;
    }


    private List<Integer> sedgewickStepSequence() {
        List<Integer> stepSequence = new LinkedList<>();
        int k = 0, step = 0;
        while (true) {
            if (k % 2 == 0) {
                int pow = (int) Math.pow(2, k >> 1);
                step = 1 + 9 * (pow * pow - pow);
            } else {
                int pow1 = (int) Math.pow(2, (k - 1) >> 1);
                int pow2 = (int) Math.pow(2, (k + 1) >> 1);
                step = 1 + 8 * pow1 * pow2 - 6 * pow2;
            }
            if (step >= array.length) break;
            stepSequence.add(0, step);
            k++;
        }
        return stepSequence;
    }

    // 希尔排序
    // 希尔排序把序列看作是一个矩阵,分成m列,逐列进行排序 [注意是逐列]
    // m从某个整数逐渐减为1
    // 当m为1时,整个序列将完全有序

    // 因此,希尔排序也被称为递减增量排序(Diminishing Increment Sort)
    //◼ 矩阵的列数取决于步长序列(step sequence)
    // ✓ 比如，如果步长序列为{1,5,19,41,109,...}，就代表依次分成109列、 41列、 19列、 5列、 1列进行排序
    // ✓ 不同的步长序列，执行效率也不同
    // 希尔本人给出的步长序列是(n/(2^k)),比如n为16时,步长序列是{1,2,4,8}
    @Test
    public void sortTest() {
        int[] array = {81, 94, 11, 96, 12, 35, 17, 95, 28, 58, 41, 75, 15};
        // 81, 94, 11, 96, 12, 35          15, 94, 11, 58, 12, 35
        // 17, 95, 28, 58, 41, 75,    =>   17, 95, 28, 96, 41, 75,
        // 15                              81
        // 15, 94, 11, 58, 12, 35, 17, 95, 28, 96, 41, 75, 81

        // 15, 94, 11,                    15, 12, 11,
        // 58, 12, 35,                    17, 41, 28,
        // 17, 95, 28,   =>               58, 94, 35,
        // 96, 41, 75,                    81, 95, 75,
        // 81                             96
        // 15, 12, 11, 17, 41, 28, 58, 94, 35, 81, 95, 75, 96

        List<Integer> stepSequence = shellStepSequence2(array); // {8,4,2,1} 每个元素是分成多少列
        for (Integer step : stepSequence) { // 遍历步长数列
            sort(step, array);
        }
        sort1(array);

        System.out.println(Arrays.toString(array));
    }



    /**
     * 希尔排序 针对有序序列在插入时采用移动法。
     *
     * @param arr
     */
    public void sort(int step,int[] arr){
        for (int i = step; i < arr.length; i++) {
            int j = i;
            int temp = arr[j];
            if (arr[j] < arr[j - step]) {
                while (j - step >= 0 && temp < arr[j - step]) {
                    //移动法
                    arr[j] = arr[j - step];
                    j -= step;
                }
                arr[j] = temp;
            }
        }

    }

    public static void sort1(int[] arr) {
        //增量gap，并逐步缩小增量
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            System.out.println(gap);
            //从第gap个元素，逐个对其所在组进行直接插入排序操作
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                int temp = arr[j];
                if (arr[j] < arr[j - gap]) {
                    while (j - gap >= 0 && temp < arr[j - gap]) {
                        //移动法
                        arr[j] = arr[j - gap];
                        j -= gap;
                    }
                    arr[j] = temp;
                }
            }
        }
    }

    private void sort2(int step, int[] array) {
        // 对每step个数组元素进行排序
        // col: 第几列,column的简称
        for (int col = 0; col < step; col++) { // 对第col进行排序
            // col col+step col+2*step col+3*step
            // 传统的插入排序是0,1,2,3,4....array.length-1 步长为1,从1开始
            // 现在的插入排序是0 col+step col+2*step col+3*step,步长为step,从0+step开始,因为第1个已经默认排好序的
            for (int begin = col + step; begin < array.length; begin += step) {
                int cur = begin;
                while (cur > col && array[cur] < array[cur - step]) {
                    int tmp = array[cur];
                    array[cur] = array[cur - step];
                    array[cur - step] = tmp;
                    cur -= step;
                }
            }
        }
    }


    private List<Integer> shellStepSequence2(int[] array) {
        List<Integer> stepSequence = new LinkedList<>();

        int step = array.length;
        while ((step >>= 1) > 0) {  // 1/2=0  // step = step >> 1 可以变成 step>>=1
            stepSequence.add(step);
        }
        System.out.println(stepSequence);
        return stepSequence;
    }

}


// 初始数据:
// | 81 | 94 | 11 | 96 | 12 | 35 | 17 | 95 | 28 | 58 | 41 | 75 | 15 |

// 间隔(增量)为5,分成若干个子序列
// | 81 | 94 | 11 | 96 | 12 |           | 35 | 17 | 11 | 28 | 12 |
// | 35 | 17 | 95 | 28 | 58 |     -->   | 41 | 75 | 15 | 96 | 58 |
// | 41 | 75 | 15 |                     | 81 | 94 | 95 |
//   ↑
//  对这逐列进行插入排序,进行比较交换位置[为一组,做完该组相对有序]
//  比较完三者,各个离最终的位置近了
// | 35 | 17 | 11 | 28 | 12 | 41 | 75 | 15 | 96 | 58 | 81 | 94 | 95 |

// 接下来3间隔的元素为1组,进行插入排序
// | 35 | 17 | 11 |                      | 28 | 12 | 11 |
// | 28 | 12 | 41 |                      | 35 | 15 | 41 |
// | 75 | 15 | 96 |               -->    | 58 | 17 | 94 |
// | 58 | 81 | 94 |                      | 75 | 81 | 96 |
// | 95 |                                | 95 |
//   ↑
// 此次3间隔插入排序结果为:
// | 28 | 12 | 11 | 35 | 15 | 41 | 58 | 17 | 94 | 75 | 81 | 96 | 95 |

// 接下来1间隔的元素为1组,进行插入排序,排序完成
// | 11 | 12 | 15 | 17 | 28 | 35 | 41 | 58 | 75 | 81 | 94 | 95 | 96 |


// 总变换过程:
// 初始数据:
// | 81 | 94 | 11 | 96 | 12 | 35 | 17 | 95 | 28 | 58 | 41 | 75 | 15 |
// | 35 | 17 | 11 | 28 | 12 | 41 | 75 | 15 | 96 | 58 | 81 | 94 | 95 |
// | 28 | 12 | 11 | 35 | 15 | 41 | 58 | 17 | 94 | 75 | 81 | 96 | 95 |
// | 11 | 12 | 15 | 17 | 28 | 35 | 41 | 58 | 75 | 81 | 94 | 95 | 96 |
// 每排完一次序,逆序对的数量在逐渐减少,是有意义的过程
// 因此希尔排序底层一般使用插入排序对每一列进行排序，也很多资料认为希尔排序是插入排序的改进版
// 为什么使用插入排序.最好O(n),大致有序情况下效率高,逆序对数量越多插入排序的时间复杂度越高,逆序对越少,效率越高.而希尔排序的过程是让逆序对越来越少.适合用插入排序

// 再来一个实例
// 假设有11个元素,步长序列是{1,2,5}
// | 11 | 10 | 9 | 8 | 7 | 6 | 5 | 4 | 3 | 2 | 1 |

// | 11 | 10 | 9 | 8 | 7 |         | 1  | 5  | 4 | 3 | 2 |
// | 6  | 5  | 4 | 3 | 2 |   -->   | 11 | 10 | 9 | 8 | 7 |
// | 1  |                          | 6  |

// | 1 | 5 | 4 | 3 | 2 | 11 | 10 | 9 | 8 | 7 | 6 |

// | 1 | 5 |                          | 1 | 3 |
// | 4 | 3 |                          | 2 | 5 |
// | 2 | 11|                -->       | 4 | 7 |
// |10 | 9 |                          | 6 | 9 |
// | 8 | 7 |                          | 8 | 11|
// | 6 |                              | 10|

// | 1 | 3 | 2 | 5 | 4 | 7 | 6 | 9 | 8 | 11 | 10 |
//                           ↓
// | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 |

// 变换过程
// 初始值:
// | 11 | 10 | 9 | 8 | 7 | 6 | 5 | 4 | 3 | 2 | 1 |
// | 1 | 5 | 4 | 3 | 2 | 11 | 10 | 9 | 8 | 7 | 6 |
// | 1 | 3 | 2 | 5 | 4 | 7 | 6 | 9 | 8 | 11 | 10 |
// | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 |