package sf.sort.sort.no_cmp.bucket;


import org.junit.Test;
import sf.sort.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
// 桶排序同样是一种线性时间的排序算法。
//   类似于计数排序所创建的统计数组,桶排序需要创建若干个桶来协助排序。
// 每一个桶(bucket)代表一个区间范围,里面可以承载一个或多个元素。

// 空间复杂度O(n+m),m是桶的数量
// 时间复杂度O(n)+m*O(n/m * log(n/m) = O(n+n*log(n/m) = O(n + n ∗ logn − n ∗ logm)
// 因此为 O(n+k), k 为 n∗logn−n∗logm
// 属于稳定排序
@SuppressWarnings("all")
public class BucketSort extends Sort<Integer> {
    // {4.5,0.84,3.25,2.18,0.5}
    // 1.创建这些桶,并确定每一个桶的区间范围
    //

    @Override
    protected void sort() {

    }

    //执行流程
    // 1.创建一定数量的桶(比如用数组、链表作为桶),有很多方式.这里有多少个数就创建多少个桶
    //   各个桶的区间跨度 =(最大值-最小值)/(桶数量-1)
    //   按照实例 桶区间跨度为 6.894736842105263
    // 2.按照一定的规则(不同类型的数据,规则不同),将序列中的元素均匀分配到对应的桶
    // 3.分别对每个桶进行单独排序
    // 4.将所有非空桶的元素合并成有序序列
    @Test
    public void sortTest() {
        double[] arr = {-7, 51, 3, 121, -3, 32, 21, 43, 4, 25, 56, 77, 16, 22, 87, 56, -10, 68, 99, 70};
        double[] doubles = bucketSort(arr);
        System.out.println(Arrays.toString(doubles));

    }

    public double[] bucketSort(double[] arr) {
        //1.得到数列的最大值和最小值， 并算出差值d
        double max = arr[0];
        double min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        double d = max - min;
        System.out.println(d/(arr.length-1));
        //2.初始化桶
        int bucketNum = arr.length;
        ArrayList<LinkedList<Double>> bucketList = new ArrayList<>(bucketNum);
        for (int i = 0; i < bucketNum; i++) {
            bucketList.add(new LinkedList<>());
        }

        //3.遍历原始数组,将每个元素放入桶中.
        for (int i = 0; i < arr.length; i++) {
            int num =(int) ((arr[i]-min)*(bucketNum-1)/d);//  映射函数
            System.out.println(num);
            bucketList.get(num).add(arr[i]);
        }
        //4,对每个桶内部进行排序
        for (int i = 0; i < bucketList.size(); i++) {
            //JDK 底层采用了归并排序或归并的优化版本
            Collections.sort(bucketList.get(i));
        }
        // 5.输出全部元素
        double[] sortedArray=new double[arr.length];
        int index =0;
        for (LinkedList<Double> list : bucketList) {
            for (Double element : list) {
                sortedArray[index]=element;
                index++;
            }
        }

        return sortedArray;
    }
}
