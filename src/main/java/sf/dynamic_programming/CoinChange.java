package sf.dynamic_programming;

import org.junit.Test;

import java.util.Arrays;

// 找零钱
// 假设有25分、 20分、 5分、 1分的硬币，现要找给客户41分的零钱，如何办到硬币个数最少？
// 当时贪心算法得到了并不是最优解,得到的是5枚硬币
@SuppressWarnings("all")
public class CoinChange {
    // 1.定义状态(状态是原问题、子问题的解)
    // 假设dp(n)是凑到n分需要的最少硬币个数             (如dp(41)代表抽到41分需要的最少硬币个数)
    // - 如果第1次选择了25分硬币,那么dp(n)=dp(n-25)+1  (dp(n-25)代表抽到n-25分需要的最少硬币个数,+1代表选择了一枚硬币)
    // - 如果第1次选择了20分硬币,那么dp(n)=dp(n-20)+1
    // - 如果第1次选择了5分硬币,那么dp(n)=dp(n-5)+1
    // - 如果第1次选择了1分硬币,那么dp(n)=dp(n-1)+1
    // 所以dp(n)=min{dp(n-25),dp(n-20),dp(n-5),dp(n-1)}+1   由于求最少硬币,最终求dp(n)是求四种所有情况的最小值(所有情况的最优解)+1
    // 而贪心算法只考虑眼前的利益

    @Test
    public void change() {
        System.out.println(coins6(41, new int[]{1, 5, 20, 25}));
        // dp(41) = 筹够41需要的最少硬币数量
        // dp(41-1) => dp(40) = 筹够40需要的最少硬币数量
        // dp(41-5) => dp(36) = 筹够36需要的最少硬币数量
        // dp(41-25) => dp(16) = 筹够16需要的最少硬币数量
        // dp(41-20) => dp(21) = 筹够21需要的最少硬币数量
        // min{dp(n-25),dp(n-20),dp(n-5),dp(n-1)}+1  // min子问题最优的解获得原问题的最优解
    }

    // 1.暴力递归.（自顶向下，出现了重叠子问题）初步的动态优化,有优化空间
    // 类似于斐波那契数列的递归版，会有大量的重复计算，时间复杂度较高
    public int coins(int n) {
        // 递归基
        if (n < 1) return Integer.MAX_VALUE;
        if (n == 25 || n == 20 || n == 5 || n == 1) { // 凑25分最少多少硬币,凑20分/5分/1分
            return 1;
        }

        int min1 = Math.min(coins(n - 25), coins(n - 20));
        int min2 = Math.min(coins(n - 5), coins(n - 1));
        return Math.min(min1, min2) + 1;
    }
    // coins(6) -> coins(1) coins(5)
    // coins(5) -> coins(4)
    // coins(4) -> coins(3)
    // coins(3) -> coins(2)
    // coins(2) -> coins(1)

    // 记忆化搜索,把曾经计算过的值存储起来
    public int coins2(int n) {
        if (n < 1) return -1; // 排除不合理条件
        int[] dp = new int[n + 1];  /// coins2(16) = 4 dp[n]=4
        int[] faces = {1, 5, 20, 25};
        // dp[1] = dp[5] = dp[20] = dp[25] = 1;
        for (int face : faces) {
            if (n < face) break;
            dp[face] = 1;
        }
        return coins2(n, dp);
    }

    public int coins2(int n, int[] dp) {
        if (n < 1) return Integer.MAX_VALUE; // 递归基
        if (dp[n] == 0) { // 没有求过,求出来存起来
            int min1 = Math.min(coins2(n - 25, dp), coins2(n - 20, dp));
            int min2 = Math.min(coins2(n - 5, dp), coins2(n - 1, dp));
            dp[n] = Math.min(min1, min2) + 1;
        }
        return dp[n];//以便曾经求过了直接返回
    }

    // 3. 递推(自底向上)
    // 时间复杂度、空间复杂度： O(n)
    public int coins3(int n) {
        if (n < 1) return -1;
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            //伪代码 dp[i] = min { dp[i-25],dp[i-20],dp[i-5],dp[i-1]}+1
            int min = Integer.MAX_VALUE;
            if (i >= 1) min = Math.min(dp[i - 1], min);
            if (i >= 5) min = Math.min(dp[i - 5], min);
            if (i >= 20) min = Math.min(dp[i - 20], min);
            if (i >= 25) min = Math.min(dp[i - 25], min);

            dp[i] = min + 1;
        }
        return dp[n];
    }

    public int coins4(int n) {
        if (n < 1) return -1;
        int[] dp = new int[n + 1];
        int[] faces = new int[dp.length]; // face[i]是筹够i分时最后选择的那枚硬币的面值
        for (int i = 1; i <= n; i++) {
            //伪代码 dp[i] = min { dp[i-25],dp[i-20],dp[i-5],dp[i-1]}+1
            int min = Integer.MAX_VALUE;
            if (i >= 1 && dp[i - 1] < min) {
                min = dp[i - 1];
                faces[i] = 1;
            }
            if (i >= 5 && dp[i - 5] < min) {
                min = dp[i - 5];
                faces[i] = 5;
            }
            ;
            if (i >= 20 && dp[i - 20] < min) {
                min = dp[i - 20];
                faces[i] = 20;
            }
            if (i >= 25 && dp[i - 25] < min) {
                min = dp[i - 25];
                faces[i] = 25;
            }
            dp[i] = min + 1;
            print(faces, i);
        }
        // print(faces, n);
        return dp[n];
    }

    // n =41
    private void print(int[] faces, int n) {
        System.out.println("[" + n + "]=");
        while (n > 0) {
            System.out.print(faces[n] + " ");
            n -= faces[n];
        }
        System.out.println();
    }


    // 通用实现1
    public int coins5(int n, int[] faces) {
        if (n < 1 || faces == null || faces.length == 0) return -1;
        int[] dp = new int[n + 1];
        int[] chooseed = new int[dp.length];
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int face : faces) {
                if (i < face) continue;
                if (i >= face && dp[i - face] < min) {
                    min = dp[i - face];
                    chooseed[i] = face;
                }
            }
            dp[i] = min + 1;
            print(chooseed, i);
        }
        //print(chooseed, n);
        return dp[n];
    }

    // 最终通用方案
    public int coins6(int n, int[] faces) {
        if (n < 1 || faces == null || faces.length == 0) return -1;
        int[] dp = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            for (int face : faces) {
                if (i < face) continue;
                if (dp[i - face] < 0 || dp[i - face] >= min) continue;
                min = dp[i - face];
            }
            if (min == Integer.MAX_VALUE) { //
                dp[i] = -1;
            } else {
                dp[i] = min + 1;
            }
        }
        return dp[n];
    }
}
