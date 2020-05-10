package sf.dynamic_programming;

import org.junit.Test;

// 最长公共子序列
// 一个字符串 s 被称作另一个字符串 S 的子串，表示 s 在 S 中出现了。
// 一个字符串 s 被称作另一个字符串 S 的子序列，说明从序列 S 通过去除某些元素但不破坏余下元素的相对位置（在前或在后）可得到序列 s 。
public class LongestCommonSubsequence {
    // ◼ 最长公共子序列（Longest Common Subsequence， LCS）
    //   https://leetcode-cn.com/problems/longest-common-subsequence/
    //◼ 求两个序列的最长公共子序列长度
    //   [1, 3, 5, 9, 10] 和 [1, 4, 9, 10] 的最长公共子序列是 [1, 9, 10]，长度为 3
    //   ABCBDAB 和 BDCABA 的最长公共子序列长度是 4，可能是
    //   ✓ ABCBDAB 和 BDCABA > BDAB
    //   ✓ ABCBDAB 和 BDCABA > BDAB
    //   ✓ ABCBDAB 和 BDCABA > BCAB
    //   ✓ ABCBDAB 和 BDCABA > BCBA

    // 假设2个序列分别是nums1/nums2
    //  i ∈[1,nums1.length]
    //  j ∈[1,nums2.length]
    // 假设dp(i,j)是{nums1前 i个元素}与{nums2前j个元素}的最长公共子序列长度
    //  - dp(i,0)、dp(0,j)初始值为0
    //  - 如果nums1[i-1] = nums2[j-1],那么dp(i,j)=dp(i-1,j-1)+1    注意下标
    //  - 如果 nums1[i – 1] ≠ nums2[j – 1]，那么 dp(i, j) = max { dp(i – 1, j), dp(i, j – 1) }

    // dp(nums1.length,nums2.length)

    @Test
    public void test() {
        int[] nums1 = {1, 3, 5, 9, 10};
        int[] nums2 = {1, 4, 9, 10};
        System.out.println(lcs(nums1, nums2));
        System.out.println(lcs2(nums1, nums2));
        System.out.println(lcs3(nums1, nums2));
        System.out.println(lcs4(nums1, nums2));
    }

    // 递归实现
    // 空间复杂度： O k , k = min{n, m}， n、 m 是 2 个序列的长度
    // 时间复杂度： O 2n ，当 n = m 时
    public int lcs(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;

        return lcs(nums1, nums1.length, nums2, nums2.length);
    }

    // 求nums1前i个元素和nums2前j个元素的最长公共子序列长度
    public int lcs(int[] nums1, int i, int[] nums2, int j) {
        if (i == 0 || j == 0) return 0;
        if (nums1[i - 1] == nums2[j - 1]) {
            return lcs(nums1, i - 1, nums2, j - 1) + 1;
        }
        return Math.max(lcs(nums1, i - 1, nums2, j), lcs(nums1, i, nums2, j - 1));
    }

    // 空间复杂度： O (n ∗ m)
    // 时间复杂度： O(n ∗ m)
    public int lcs2(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;
        int[][] dp = new int[nums1.length + 1][nums2.length + 1];

        for (int i = 1; i <= nums1.length; i++) {
            for (int j = 1; j <= nums2.length; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[nums1.length][nums2.length];
    }

    // 非递归 滚动数组
    // 可以使用滚动数组优化空间复杂度
    public int lcs3(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;
        int[][] dp = new int[2][nums2.length + 1];
        for (int i = 1; i <= nums1.length; i++) {
            int row = i & 1;
            int prevRow = (i - 1) & 1;
            for (int j = 1; j <= nums2.length; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    dp[row][j] = dp[prevRow][j - 1] + 1;
                } else {
                    dp[row][j] = Math.max(dp[prevRow][j], dp[row][j - 1]);
                }
            }
        }
        return dp[nums1.length & 1][nums2.length];
    }

    // 可以将 二维数组 优化成 一维数组，进一步降低空间复杂度
    public int lcs4(int[] nums1, int[] nums2) {
        if (nums1 == null || nums1.length == 0) return 0;
        if (nums2 == null || nums2.length == 0) return 0;
        int[] dp = new int[nums2.length + 1];
        for (int i = 1; i <= nums1.length; i++) {
            int cur = 0;
            for (int j = 1; j <= nums2.length; j++) {
                int leftTop = cur;
                cur = dp[j];
                if (nums1[i-1] == nums2[j - 1]) {
                    dp[j] =leftTop + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
            }
        }
        return dp[nums2.length];
    }

    // 可以空间复杂度优化至 O k , k = min{n, m}
}
