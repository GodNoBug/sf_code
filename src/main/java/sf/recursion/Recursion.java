package sf.recursion;

import jg.linear_list.AbstractList;
import org.junit.Test;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.Arrays;

public class Recursion {
    @Test
    public void justTest(){
        f(1);
    }
    // 递归
    //   函数调用其本身，这种调用过程被称为递归(recursion).
    // 递归基本原理:
    //1.每一级的函数调用都有它自己的变量。
    //2.每一次函数调用都会有一次返回，并且是某一级递归返回到调用它的那一级，而不是直接返回到main()函数中的初始调用部分(程序必须按顺序逐级返回递归)。
    //3.递归函数中，位于递归调用前的语句和各级被调函数具有相同的执行顺序。
    //4.递归函数中，位于递归调用后的语句的执行顺序和各个被调函数的顺序相反。递归调用的这种特性在解决涉及相反顺序的编程问题时很有用.
    //   位于递归调用后的语句体现了递归的根本：当某一级递归结束，则该级函数马上将程序的控制权交给该函数的调用函数。
    //5.虽然每一级递归都有自己的变量，但是函数代码不会复制。
    //6.递归函数中必须包含终止递归的语句。通常递归函数会使用一个if条件语句或其他类似语句一边当函数参数达到某个特定值时结束递归调用。
    public void f(int n){
        System.out.printf("Level %d\n",n);
        if (n<4){
            f(n+1);
        }
        System.out.printf("Level %d -\n",n);
    }





    // 递归和循环
    // 理论上,任何循环都可以重写为递归的形式.
    //  1. 有时候,为栈限制,需要"尾递归"
    //  2. Java不支持尾递归?
    // 有些语言没有循环语句,只能使用递归

    // 递归比作领导处理完一些事情然后交给下一级做,每一级做一点事情交给下一级,直到满足事情做完的条件位置(每个人并不是全权负责)


    // 方法参数:
    // 如果方法没有参数,那么递归调用产生类似死循环的效果.
    //    于是: 我们说:一个方法去调用自身,不能每次调用都要相同的环境,如果这样,
    // 执行永远不会结束,或者栈溢出被迫结束.必然要有些变化,这些变化是由参数引起的.
    // 每次调用的时候调整这个参数,使得每次调用条件产生变化,靠近递归出口,在适当的
    // 时候可以终止递归

    // 递归出口问题
    // 在某种条件满足情况下,
    //  1.选择不调用自身.
    //  2.或者直接返回 (无论如何都让它进入这层递归,在方法起始处做检查,发现不合理情况,终止)
    //  3.或者返回值


    public void f2(int begin, int end) {
        System.out.println(begin);
        if (begin < end)
            f2(begin + 1, end); // 不去调用递归了
    }

    public void f3(int begin, int end) {
        if (begin > end) return;
        System.out.println(begin);
        f3(begin + 1, end); // 无论如何都让它调用递归,在方法起始处做检查,发现不合理情况,终止
    }

    @Test
    public void test1() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        System.out.println(addAll(0, arr));
        System.out.println(addAll2(arr.length - 1, arr));
    }

    // 构造相似性(需要大量练习找感觉)
    // 如果没有明显的相似性,需要主动构造
    // 不能相似的原因很可能是缺少参数
    // 递归与数学上的递推公式很类似
    public int addAll(int[] arr) {
        int sum = 0; // 累加器
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }


    // arr传递的是引用,不变的,
    // 递归实质上是一种踢皮球的游戏 [.[.[.[.[.[.[.]]]]]]]   [[[[[[[.].].].].].].]    [[.].[.]].[[.].[.]]   [[[.][.]][[.][.]]][[[.][.]][[.][.]]]
    // 当前栈空间(工作单位)保存的临时变量(负责一点)交给下一部门去负责,并等待下一部门的处理结果后一并给出结果返回上层部门(上一个栈空间)

    // 求a数组中,从begin到结束的元素和
    // 1. a[begin] +(begin+1 .... 结束)   [.[.[.[.[.[.[.]]]]]]]
    // 2. (a[0]...end-1) + a[end]        [[[[[[[.].].].].].].]
    // 3. 折半求和 mid=(begin+end)/2  [begin,mid),[mid,emd)   [[.].[.]].[[.].[.]]
    public int addAll(int begin, int[] arr) {
        if (begin == arr.length) return 0;
        return arr[begin] + addAll(begin + 1, arr); // [.[.[.[.[.[.[.]]]]]]]
    }

    public int addAll2(int end, int[] arr) {
        if (end < 0) return 0;
        return arr[end] + addAll2(end - 1, arr); // [.[.[.[.[.[.[.]]]]]]]
    }

    public int addAll3(int end, int[] arr) {
        if (end < 0) return 0;
        return arr[end] + addAll2(end - 1, arr); // [.[.[.[.[.[.[.]]]]]]]
    }

    public boolean isSameString(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        if (s1.charAt(0) != s2.charAt(0)) {
            return false;
        }
        return isSameString(s1.substring(1), s2.substring(1));
    }

    // 递归调用仅仅是被调函数恰巧为主调函数
    // 注意每次调用的层次不同
    // 注意每次分配形参并非同一个变量
    // 注意返回的次序
    // 从前有座山，山里有座庙，庙里有个老和尚和一个小和尚…… 直到讲到从前有座山，山里有座庙，庙里有个老和尚和一个小和尚玩火全烧死了


    // 减而治之: 求解一个大规模的问题，可以将其划分为两个子问题，其一是平凡问题，另一个规模缩减。由子问题的解，得到原问题的解。(治: 分别解决)
    // 总结: 递归每深入一层，待求解问题的规模都缩减一个常数，直至最终蜕化为平凡小的问题。
    // . 所谓“平凡的问题”，是指无需进行复杂运算，可以直接给出结果的问题。例如，“对n个数进行排序”是一个复杂的问题，但当n等于1时，问题便成为了一个平凡的问题，因为序列长度为1，则序列自然是有序的。
    //       |----------------------原问题----------------------|
    //     ↗                    ↙            ↘                 ↖
    //   合                   缩减               平凡                合
    //    ↖                ↙                     ↘               ↗
    //      ↖|-----------------------------------||------------|↗
    //                   子问题(治)                  子问题(治) == 比如单位长度位1、由一个元素组成的问题
    //                      ||
    //         没有彻底解决但是规模已经相应的缩减,更重要的是子问题的形式和原问题的形式是几乎一样的,那么在这个时候可以递归求解出这个问题
    public int sum(int[] arr, int n) {
        // n:规模分解为规模缩小了一个单元的问题和平凡问题(可以直接求解得到)
        // 我们所需做的事情只是递归地求解前者,并且将二者的解合并(在此只是做加法),当问题规模小到递归基,则返回0
        //    递归基          规模缩小   合并  平凡
        //      ↓               ↓       ↓    ↓
        return (n < 1) ? 0 : arr[n - 1] + sum(arr, n - 1);
    }

    public int sum2(int[] arr, int n) { // 计算任意n个整数之和,其中n为问题规模
        int sum = 0;
        for (int i = 0; i < n; i++) { // 每经过一次迭代,有一个数统计完毕,剩余的问题规模(相应的尚未参与运算的)递减一个元素
            sum += arr[i]; //
        }
        return sum;
    }

    @Test
    public void reverse() {
        int[] ints = {1, 2, 3, 4, 5, 6, 7};
        reverse3(ints, 0, ints.length - 1);
        System.out.println(Arrays.toString(ints));
    }

    public void reverse(int[] arr, int low, int high) { // 任意数组A[0,n),将其前后颠倒
        // 递归基
        if (low == high) return;
        // O(1) 平凡
        int tmp = arr[high];
        arr[high] = arr[low];
        arr[low] = tmp;
        // 递归求解规模更小的问题,两端共减少2个元素规模
        reverse(arr, low + 1, high - 1);
    }

    public void reverse2(int[] arr, int low, int high) { // 任意数组A[0,n),将其前后颠倒
        while (low < high) {
            int tmp = arr[high];
            arr[high] = arr[low];
            arr[low] = tmp;
            low++;
            high--;
        }
    }

    public void reverse3(int[] arr, int low, int high) { // 任意数组A[0,n),将其前后颠倒
        while (low < high) {
            int tmp = arr[high];
            arr[high--] = arr[low];
            arr[low++] = tmp;
        }
    }
    // 分而治之
    // 为求解一个大规模的问题,可以将其划分为若干(通常两个)子问题,规模大体相当,而后分别求解子问题.由子问题的解,得到原问题的解

    //       |----------------------原问题----------------------|
    //     ↗                    ↙            ↘                 ↖
    //   合                   分                 分                  合
    //    ↖                ↙                     ↘               ↗
    //      ↖|------------------------||-----------------------|↗
    //                   子问题(治)                  子问题(治)

    // 仍是数组就和
    public int sum(int[] arr, int lo, int hi) { // 期间范围A[lo,hi]
        // 递归基
        if (lo == hi) return arr[lo];
        int mi = (lo + hi) >> 1;
        // 对2个子问题进行递归的求解,直到最后成为递归基的形式,使用加法进行合并
        return sum(arr, lo, mi) + sum(arr, mi + 1, hi);
    }

    // TODO ???
    // 总是把整个序列每次都划分为左侧和右侧两部分,接下来可以递归求解子问题(左侧的最大和次大元素,右侧的最大和次大元素)
    // 全局的最大元素必然来自于左侧元素最大值和右侧最大值的更大值
    // 如果经过比较后,确认左侧的胜出,我们可以进而得出结论:全局次大元素必然来自于左侧次大元素和右侧败下来的最大元素
    public void max2(int[] arr, int low, int high, int[] result) { // 从给定数组区间A[lo,hi)中找出最大的两个整数,元素比较次数要求尽可能地少.
        if (low+2==high) return;
        if (low+3==high) return;
        int mid = (low + high) >> 1;
        max2(arr,low,mid,result);
        max2(arr,mid+1,high,result);
    }

    public void max2_2(int[] arr, int low, int high, int x1, int x2) { // 从给定数组区间A[lo,hi)中找出最大的两个整数A[x1]和A[x2],元素比较次数要求尽可能地少.
        // 扫描A[lo,hi)
        x1 = low;
        for (int i = low + 1; i < high; i++) {
            if (arr[x1] < arr[i]) x1 = i; //总是指向当前最大的元素
        }
        // 在x1前缀和后缀中,查询除x1以外的最大值,也就是全局的最大值
        x2 = low;
        for (int i = low + 1; i < x1; i++) { // 扫描A[lo,x1)
            if (arr[x2] < arr[i]) x2 = i;
        }
        for (int i = x1 + 1; i < high; i++) { // 在扫描A(x1,hi),找出x2
            if (arr[x2] < arr[i]) x2 = i;
        }
    }

    // 我们可以维护两个指针x1,x2,分别指向当前最大和次大的元素
    // 整个算法的过程只是一趟扫描,扫描到第i个元素,与x2比较,如果比x2大,再进而和x1比较,从而有机会使用i更新当前次大和最大元素
    public void max2_3(int[] arr, int low, int high, int x1, int x2) {
        x1 = low;
        x2 = low + 1;
        if (arr[x1] < arr[x2]) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        for (int i = low + 2; i < high; i++) {
            if (arr[x2] < arr[i]) {
                x2 = i;
                if (arr[x1] < arr[x2]) {
                    int tmp = x1;
                    x1 = x2;
                    x2 = tmp;
                }
            }
        }
    }

    // 动态规划
    // 从某种意义上讲:所谓的动态规划,也可以通过递归找出算法的本质,并给出初步的解之后,再将其等效地转化为迭代的形式
    public int fib(int n){
        return (2>n)?n:fib(n-1)+fib(n-2); //定义本身就是一种递归的形式
    }
}
