package sf.divide_and_conquer;

import org.junit.Test;

@SuppressWarnings("unused")
public class MaxSubarray {
    // 练习1
    // 最大连续子序列和[连续几个月业绩最大的]
    //  给定一长度为n的整数序列,求它最大连续子序列和?

    //  比如: [-2,1,-3,4,-1,2,1,-5,4],的最大连续子序列和是 4+(-1)+2+1=6
    //  解释: 子序列,从数组中挑出元素作为新序列,可不连续.
    //       连续子序列,从左到右连续取出,要求连续.子串/子区间/子数组必须连续
    //  这道题也属于最大切片问题(最大区段,Greatest Slice)

    // 解法1.暴力法
    // 穷举出所有可能的连续子序列,并计算它们的和,最后取它们中最大值
    // 如何穷举?
    // 双指针:begin end
    // 有问题
    // 1. begin指向0,end从0遍历到末尾
    // 2. begin+1,end从begin+1遍历到末尾,直到begin到末尾为止
    @Test
    public void solution() {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println(maxSubarray2(nums));
        System.out.println(maxSubarray(nums));
        System.out.println(maxSubarray3(nums));
    }

    // O(n^3)
    // 循环理解
    //  把外循环变量看做分针，内循环变量看做秒针。
    //  分针安静的看着秒针，等它走够一圈(内循环变量取了若干个值)，分针才走一步(外循环变量值才变一次)。如此重复。
    private int maxSubarray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int max = Integer.MIN_VALUE; //
        for (int begin = 0; begin < nums.length; begin++) {
            for (int end = begin; end < nums.length; end++) {
                //[begin,end]
                int sum = 0; // end每进一步,sum重新置0然后再重新加,效率不是太高
                for (int i = begin; i <= end; i++) {
                    sum += nums[i];
                }
                max = Math.max(max, sum);
            }
        }
        return max;
    }

    // 暴力解法基础上的优化
    private int maxSubarray2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int max = Integer.MIN_VALUE; //
        for (int begin = 0; begin < nums.length; begin++) {
            int sum = 0;
            for (int end = begin; end < nums.length; end++) {
                // sum是[begin end]的和
                sum += nums[end];
                max = Math.max(max, sum);
            }
        }
        return max;
    }


    // 解法2:分治
    // 将序列均匀地分割成2个子序列
    // [begin,end)= [begin,mid) + [mid,end),  mid =(begin+end) >> 1

    // 假设问题的解是 S[i,j),那么问题的解有3中可能分布
    // 1.[i,j)存在同于[begin,mid)中
    // 2.[i,j)存在同于[mid,end)中
    // 3.[i,j)一部分存在于[begin,mid)中,另一部分存在于[mid,end)中.比较复杂.从mid向左一个一个加,每加一个值看看是否是最大的.右边也一样
    //    - [i,j) = [i,mid) + [mid,j)
    //    - S[i,mid) = max{S[k,mid]} , begin ≤ k < mid


    public int maxSubarray3(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        return maxSubarray3(nums, 0, nums.length);
    }

    /**
     * 求解[begin,end)中最大连续子序列和
     */
    public int maxSubarray3(int[] nums, int begin, int end) {
        if (end - begin < 2) return nums[begin];  // 当减少到一个元素时,你的最大连续子序列和是该元素. return nums[begin] 或者 nums[end]都可以
        int mid = (begin + end) >> 1;

        // 中间往左
        int leftMax = Integer.MIN_VALUE;
        int leftSum = 0;
        for (int i = mid - 1; i >= begin; i--) {  // i=>k
            leftSum += nums[i];
            leftMax = Math.max(leftMax, leftSum);
        }
        // 中间往右
        int rightMax = Integer.MIN_VALUE;
        int rightSum = 0;
        for (int i = mid; i < end; i++) {
            rightSum += nums[i];
            rightMax = Math.max(rightMax, rightSum);
        }
        // 左右加起来就可以
        int max = rightMax + leftMax;


        // 1.解横跨了左右两边,处在中间的
        // 2.解分布在左边的
        // 3.解分布在右边的
        // 求三者情况取其中一个最大的
        return Math.max(
                max,    // 求解横跨左右的,返回的是范围内最大连续子序列和,左边
                Math.max(
                        maxSubarray3(nums, begin, mid),  // 求解 [begin,mid) 返回的是范围内最大连续子序列和,左边
                        maxSubarray3(nums, mid, end)     // 求解 [mid,end)   返回的是范围内最大连续子序列和,右边
                )
        );
    }
}
