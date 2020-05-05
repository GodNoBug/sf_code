package sf.sort.sort.cmp.quick;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;


// 快速排序:
//   最好: O(nlogn) 在轴点左右元素数量比较均匀的情况下,同时也是最好的情况 T(n)= 2∗T(n/2)+O(n) = O(nlogn)
//   最坏: O(n^2)   如果轴点左右元素数量极度不均匀,最坏情况   T(n)= T(n−1)+O(n) = O(n^2) 为了降低最坏情况的出现概率，一般采取的做法是:  随机选择轴点元素
//   平均: O(nlogn)
//   额外空间复杂度 O(logn)   由于递归调用的缘故
//   In-place √
//   稳定性 ×
//
//   是从冒泡排序演变而来的.之所以快,是因为它使用了分治法.
//   同冒泡排序一样, 快速排序也属于交换排序, 通过元素之间的比较和交换位置来达到排序的目的。
//   不同的是,冒泡排序在每一轮中只把1个元素冒泡到数列的一端,而快速排序则在每一轮挑选一个基准元素,
// 并让其他比它大的元素移动到数列一边, 比它小的元素移动到数列的另一边, 从而把数列拆解成两个部分。

//执行流程
// 1.从序列中选择一个轴点元素(pivot)
//   ✓ 假设每次选择0位置的元素为轴点元素
// 2.利用 pivot 将序列分割成 2 个子序列
//  ✓ 将小于 pivot 的元素放在pivot前面(左侧)
//  ✓ 将大于 pivot 的元素放在pivot后面(右侧)
//  ✓ 等于pivot的元素放哪边都可以
// 3. 对子序列进行1,2操作
//  ✓ 直到不能再分割(子序列中只剩下1个元素)
//
//快速排序的本质
// 逐渐将每一个元素都转换成轴点元素
// 基本思想:
//   通过一趟排序将要排序的数据分割成独立的两部分,其中一部分的所有数据都比另外一部分的所有数据要小,然后再按此方法对
// 这两部分数据分别进行快速排序,整个排序过程可以递归进行,以此达到整个数据变成有序序列

// 如何让左边所有数据都比右边的所有数据要小 ?

// 1. 双边循环法
// pivot:    选择一个排序的基准点,通常在数列中在一定范围内随机选择
// left标记:  将左标记向右移动,当左标记的值大于基准点的值,左标记停止移动,切换到右标记[作用,找到比基准点大的数字,扔到右侧]
// right标记: 将右标记向左移动,当右标记的值小于基准点的值,右标记停止移动,切换到左标记[作用,找到比基准点小的数字,扔到左侧]
// - 当左右标记都停止移动时,交换两标记的值
// - 当左右标记指向同一个元素时,将该元素值与基准点的值进行交换,完成第一次操作
// - 接下来就可以递归对这分成的两个部分,进行同样的操作,完成排序
// 两种方案的切换代码形如
// while(){
//    while(){
//      break;
//    }
//    while(){
//      break;
//    }
// }
// 双边循环法从数组的两边交替遍历元素， 虽然更加直观， 但是代码实现相对烦琐。

// 2.单边循环法
//

public class QuickSort<T extends Comparable<T>> extends Sort<T> {

    @Override
    protected void sort() {

    }

    /**
     * 对[begin,end) 范围内恶的元素进行快速排序,左闭右开end-begin即元素数量
     */
    private void sort(int begin, int end, int[] arr) {
        if (end - begin < 2) return;

        // 确定轴点位置
        int mid = pivotIndex(begin, end);  // O(n),数据扫描一遍
        // 对子序列进行快速排序
        sort(begin, mid, arr);
        sort(mid + 1, end, arr);
    }

    /**
     * 构造出[begin,end)范围的轴点元素
     */
    private int pivotIndex(int begin, int end) {
        // 备份begin的位置的元素
        // 随机选择一个元素跟begin位置进行交换,而不改变之前以begin为轴点的代码,是一种修改优化代码省力的手段
        int random = (int) (begin + Math.random() * (end - begin)); // [begin,end)范围内的随机轴点下标  Math.random()生成[0,1)
        swap(begin, random);
        // 备份begin位置的元素
        T pivot = array[begin];
        end--;                  // 因为采取[begin,end),左闭右开.end指向最后一个元素
        while (begin < end) {
            while (begin < end) {
                // 从右往左
                if (cmp(pivot, array[end]) < 0) { // 右边元素>轴点元素,如果写等于,切割的子序列非常不均匀,会导致快速排序的最坏时间复杂度O(n^2)
                    end--;
                } else { // 右边元素≤轴点元素
                    array[begin] = array[end]; // 直接覆盖到begin,而此时end指向的数据等待被arr[begin]大于基准元素的时候覆盖
                    begin++;
                    break;
                }
            }
            while (begin < end) {
                // 从左往右扫描
                if (cmp(pivot, array[end]) > 0) { // 左边元素<轴点元素
                    begin++;
                } else {
                    array[end] = array[begin];// 直接覆盖到end,而此时begin指向的数据等待被arr[end]小于基准元素的时候覆盖
                    end--;
                    break;
                }
            }
        }
        // begin==end,将轴点元素放入最终位置,返回轴点元素
        array[begin] = pivot;
        return begin;
    }


    /*测试快速排序与变体*/
    @Test
    public void sortTest() {
        int[] arr = new int[]{4, 4, 6, 5, 3, 2, 8, 1};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public void quickSort(int[] arr, int startIndex, int endIndex) {
        // 递归结束条件: startIndex大于或等于endIndex时
        if (startIndex >= endIndex) {
            return;
        }
        // 得到放好的[整理好的]基准元素位置,是分治算法的分割点
        int pivotIndex = partition(arr, startIndex, endIndex);
        // 根据基准元素,分成两部分进行递归排序
        quickSort(arr, startIndex, pivotIndex - 1);
        quickSort(arr, pivotIndex + 1, endIndex);
    }

    /**
     * 分治,双边循环法
     *
     * @param arr        待交换的数组
     * @param startIndex 起始下标
     * @param endIndex   结束下标
     * @return 返回轴点元素下标, 是整理好后的分割点.分为左右两边序列
     */
    private int partition(int[] arr, int startIndex, int endIndex) {
        // 取第1个位置(也可以选择随机位置)的元素作为基准元素
        int pivot = arr[startIndex];
        int left = startIndex;
        int right = endIndex;
        while (left != right) {
            // 控制right指正比较并左移
            while (left < right && arr[right] > pivot) {
                right--;
            }
            // 控制left指正比较并右移
            while (left < right && arr[left] <= pivot) {
                left++;
            }
            // 交换left和right指针所指向的元素
            if (left < right) {
                int tmp = arr[left];
                arr[left] = arr[right];
                arr[right] = tmp;
            }
        }
        // 当左右标记指向同一元素时,基准点元素和当前指向元素交换,
        arr[startIndex] = arr[left];
        arr[left] = pivot;
        return left; // right和left都可以
    }


    //  单边循环法和双边循环法的不同体现在partition函数内部。
    //    单边循环法在开始时和双边循环法类似,都是首先选定基准元素pivot.同时设置一个mark指针指向数列起始位置
    //,这个mark指针代表小于基准元素的区域边界接下来,从基准元素的下一个位置开始遍历数组: 若遍历到的元素大于基
    //准元素, 就继续往后遍历,若遍历到的元素小于基准元素，
    //    则需要做两件事：
    //      第一。mark指针右移1位, 因为小于pivot的区域边界增大了1; 第二,让最新遍历到的元素和mark
    //指针所在位置的元素交换位置,因为最新遍历的元素归属于小于pivot的区域。partition函数实现如下：
    //分治(单边循环法)
    // 单边循环法使得小于基准元素的区域增大了.从而达到大于基准元素也增大了

    private int partition2(int[] arr, int startIndex, int endIndex) {
        // 取第1位置(也可以随机选择位置)的元素作为基准元素
        int pivot = arr[startIndex];
        int mark = startIndex;
        for (int i = startIndex - 1; i <= endIndex; i++) {
            if (arr[i] < pivot) {
                mark++;
                int p = arr[mark];
                arr[mark] = arr[i];
                arr[i] = p;
            }
        }
        arr[startIndex] = arr[mark];
        arr[mark] = pivot;
        return mark;
    }
    // TODO 非递归实现

}

// 双边循环法
// | 4 | 7 | 6 | 5 | 3 | 2 | 8 | 1 |
//   首先选定基准元素pivot,并且设置两个指针left和right,指向数列的最左和最右两个元素
// | 4 | 7 | 6 | 5 | 3 | 2 | 8 | 1 |   pivot=4
//   ↑                           ↑
//  left                       right
//   接下来进行第1次循环,从right指针开始,让指针所指向的元素和基准元素做比较。[如果大
// 于或等于pivot],则指针向左移动;如果小于pivot,则right指针停止移动,切换到left指针
//   在当前数列中.1<4,所以right直接停止移动,换到left指针,进行下一步行动.
//   轮到left指针行动,让指针所指向的元素和基准元素做比较.[如果小于或等于pivot],则指针向右
// 移动;若大于pivot,则left指针停止移动.由于left开始指向的是基准元素,判断肯定相等,所以
// left右移1位。
// | 4 | 7 | 6 | 5 | 3 | 2 | 8 | 1 |   pivot=4
//       ↑                       ↑
//      left                   right
//   由于7>4, left指针在元素7的位置停下。这时, 让left和right指针所指向的元素进行交换。
// | 4 | 1 | 6 | 5 | 3 | 2 | 8 | 7 |   pivot=4
//       ↑                       ↑
//      left                   right
//   接下来, 进入第2次循环, 重新切换到right指针, 向左移动。 right指针先移动到8,  8>4,
// 继续左移。 由于2<4， 停止在2的位置。按照这个思路, 后续步骤如图所示。
// | 4 | 1 | 6 | 5 | 3 | 2 | 8 | 7 |   pivot=4 [第二次循环,right指针停在2的位置,left指针停在6的位置]
//           ↑           ↑
//          left       right
// | 4 | 1 | 2 | 5 | 3 | 6 | 8 | 7 |   pivot=4 [元素2和元素6交换]
//           ↑           ↑
//          left       right
// | 4 | 1 | 2 | 5 | 3 | 6 | 8 | 7 |   pivot=4 [第三次循环,left指针停在3的位置,right指针停在5的位置]
//               ↑   ↑
//             left right
// | 4 | 1 | 2 | 3 | 5 | 6 | 8 | 7 |   pivot=4 [元素3和元素5交换]
//               ↑   ↑
//             left right
// | 4 | 1 | 2 | 3 | 5 | 6 | 8 | 7 |   pivot=4 [第四次循环,left指针停在3的位置,和right指针重合]
//               ↑
//             left(right)
// | 3 | 1 | 2 | 4 | 5 | 6 | 8 | 7 |   pivot=4 [最后把pivot元素也就是4与重合点的元素3交换,这一轮宣告结束]
//               ↑
//             left(right)
// 接下来就是递归起作用了


// 单边循环法
// 只从数组的一边对元素进行遍历和交换
// | 4 | 7 | 6 | 5 | 3 | 2 | 8 | 1 |
//   开始和双边循环法相似,首先选定基准元素pivot. 同时, 设置一个mark指针指向数列起始位置,
// 这个mark指针代表小于基准元素的区域边界。
// | 4 | 7 | 6 | 5 | 3 | 2 | 8 | 1 |  pivot=4
//  mark