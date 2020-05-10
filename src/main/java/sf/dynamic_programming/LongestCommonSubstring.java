package sf.dynamic_programming;

import org.junit.Test;

// 最长公共子串（Longest Common Substring）
//  子串是连续的子序列
//求两个字符串的最长公共子串长度
// ABCBA 和 BABCA 的最长公共子串是 ABC，长度为 3
@SuppressWarnings("all")
public class LongestCommonSubstring {
    @Test
    public void test1() {
        System.out.println(lcs("ABDCBA", "ABCBA"));
        System.out.println(lcs2("ABDCBA", "ABCBA"));
    }

    //假设 2 个字符串分别是 str1、 str2
    // -i ∈ [1, str1.length]
    // -j ∈ [1, str2.length]
    //假设 dp(i, j) 是以 str1[i – 1]、 str2[j – 1] 结尾的最长公共子串长度
    // -dp(i, 0)、 dp(0, j) 初始值均为 0
    // -如果 str1[i – 1] = str2[j – 1]，那么 dp(i, j) = dp(i – 1, j – 1) + 1
    // -如果 str1[i – 1] ≠ str2[j – 1]，那么 dp(i, j) = 0
    //最长公共子串的长度是所有 dp(i, j) 中的最大值 max { dp(i, j) }
    public int lcs(String str1, String str2) {
        if (str1 == null || str2 == null) return 0;
        char[] chars1 = str1.toCharArray();
        if (chars1.length == 0) return 0;
        char[] chars2 = str2.toCharArray();
        if (chars2.length == 0) return 0;

        int[][] dp = new int[chars1.length + 1][chars2.length + 1];
        int max = 0;
        for (int i = 1; i <= chars1.length; i++) {
            for (int j = 1; j <= chars2.length; j++) {
                if (chars1[i - 1] == chars2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = 0;
                }
                max = Math.max(dp[i][j], max);
            }
        }
        return max;
    }


    public int lcs2(String str1, String str2) {
        if (str1 == null || str2 == null) return 0;
        char[] chars1 = str1.toCharArray();
        if (chars1.length == 0) return 0;
        char[] chars2 = str2.toCharArray();
        if (chars2.length == 0) return 0;
        char[] rowsChars = chars1, colsChars = chars2;
        if (chars1.length < chars2.length) {
            colsChars = chars1;
            rowsChars = chars2;
        }

        int[] dp = new int[colsChars.length + 1];
        int max = 0;
        for (int row = 1; row <= rowsChars.length; row++) {
            int cur = 0;
            for (int col = 1; col <= colsChars.length; col++) {
                int leftTop = cur;
                cur = dp[col];
                if (chars1[row - 1] == chars2[col - 1]) {
                    dp[col] = leftTop + 1;
                } else {
                    dp[col] = 0;
                }
                max = Math.max(dp[col], max);
            }
        }
        return max;
    }

}
