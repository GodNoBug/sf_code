package sf.dynamic_programming;

import org.junit.Test;

//◼ 有 n 件物品和一个最大承重为 W 的背包，每件物品的重量是 𝑤i、价值是 𝑣i
//  在保证总重量不超过 W 的前提下，选择某些物品装入背包，背包的最大总价值是多少？
//  注意：每个物品只有 1 件，也就是每个物品只能选择 0 件或者 1 件
public class ZeroOneKnapsack {
    @Test
    public void test() {
        int[] values = {6, 3, 5, 4, 6};
        int[] weights = {2, 2, 6, 5, 4};
        int capacity = 10; // 7
        // dp(3,7)是最大承重为7,有前3件物品可以选时的最大价值
        // dp(4,8)是最大承重为8,有前1件物品可以选时的最大价值

        System.out.println(maxValue(values, weights, capacity));
        System.out.println(maxValue2(values, weights, capacity));
    }

    private int maxValue(int[] values, int[] weights, int capacity) {
        if (values == null || values.length == 0) return 0;
        if (weights == null || weights.length == 0) return 0;
        if (weights.length != values.length || capacity <= 0) return 0;

        int[][] dp = new int[values.length + 1][capacity + 1];
        for (int i = 1; i <= values.length; i++) {
            for (int j = 1; j <= capacity; j++) {
                if (j < weights[i - 1]) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - weights[i - 1]] + values[i - 1]);
                }
            }
        }
        return dp[values.length][capacity];
    }

    //假设 values 是价值数组， weights 是重量数组
    //   编号为 k 的物品，价值是 values[k]，重量是 weights[k]， k ∈ [0, n)
    //假设 dp(i, j) 是 最大承重为 j、 有前 i 件物品可选 时的最大总价值， i ∈ [1, n]， j ∈ [1, W]
    //  - dp(i, 0)、 dp(0, j) 初始值均为 0
    //  - 如果 j < weights[i – 1]，那么 dp(i, j) = dp(i – 1, j)  如果不选第i个物品
    //  - 如果 j ≥ weights[i – 1]，那么 dp(i, j) = max { dp(i – 1, j), dp(i – 1, j – weights[i – 1]) + values[i – 1] } 如果选择第i个物品
    //


    // ◼ dp(i, j) 都是由 dp(i – 1, k) 推导出来的，也就是说，第 i 行的数据是由它的上一行第 i – 1 行推导出来的
    //    因此，可以使用一维数组来优化
    //    另外，由于 k ≤ j ，所以 j 的遍历应该由大到小，否则导致数据错乱
    private int maxValue2(int[] values, int[] weights, int capacity) {
        if (values == null || values.length == 0) return 0;
        if (weights == null || weights.length == 0) return 0;
        if (weights.length != values.length || capacity <= 0) return 0;

        int[] dp = new int[capacity + 1];
        for (int i = 1; i <= values.length; i++) {
            for (int j = capacity; j >= 1; j--) {
                if (j < weights[i - 1]) continue;

                dp[j] = Math.max(dp[j], dp[j - weights[i - 1]] + values[i - 1]);

            }
        }
        return dp[capacity];
    }
}
