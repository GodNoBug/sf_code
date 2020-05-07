package sf.greedy;

import org.junit.Test;

import java.util.Arrays;
// 练习2-零钱兑换
// 假设有25分,10分,5分,1分的硬币,现在要找给客户41分的零钱,如何办到硬币个数最少?
// [这些面值不是一次用完就没了的,可以重复选的]
// 贪心策略: 每一次都优先选择面值最大的硬币

// 每次优先选择能选的面值中尽可能大的
// 1. 选择 25 分硬币,剩余16分
// 2. 选择 10 分硬币,剩余6分
// 3. 选择 5 分硬币,剩余1分
// 4. 选择 1 分硬币,剩余0分
// 最终的解是共4枚硬币


public class CoinChange {


    @Test
    public void change(){
        int[] faces = {25, 10, 5, 1};
        int money = 41;// 还剩多少钱没找
        coinChange(faces,money);
    }

    /**
     *  @param faces 面值
     * @param money 需要找多少钱
     */
    public void coinChange(int[] faces, int money) {
        Arrays.sort(faces); //  默认从小到大排序
        int coins = 0; // 当前找硬币个数
//        int idx = faces.length - 1;
//        while (idx >= 0) {
//            while (money >= faces[idx]) {
//                money -= faces[idx];
//                coins++;
//            }
//            idx--;
//        }
        // 从后面最大开始.
        for (int i = faces.length - 1; i >= 0; i--) {
            if (money < faces[i]) continue; //这枚硬币没法找给人家,跳过这枚硬币
            // 硬币可以重复使用
            while (money >= faces[i]) {
                System.out.println(faces[i]);
                money -= faces[i];
                coins++;
            }
        }
        System.out.println(coins);
    }


    @Test
    public void coinChange2() {
        int[] faces = {25, 10, 5, 1};
        Arrays.sort(faces); //  默认从小到大排序
        int money = 41;// 还剩多少钱没找
        int coins = 0; // 当前找硬币个数

        // 从后面最大开始
        for (int i = faces.length - 1; i >= 0; i--) {
            if (money < faces[i]) {
                continue;
            }
            System.out.println(faces[i]);
            money -= faces[i];
            coins++;
            i = faces.length; // 仍然从最后一个开始,后面会执行i--,那么去掉faces.length-1的-1;
        }
        System.out.println(coins);
    }

    // 零钱兑换的另一个例子
    // 试试假设有25分,[20分],5分,1分的硬币,现在要找给客户41分的零钱
    // 发现本来2个20和1个1分就能解决的问题,结果反倒复杂化了.因为选择硬币时考虑当前最大的面额
    // ◼ 贪心策略：每一步都优先选择面值最大的硬币
    //  1.选择 25 分的硬币，剩 16 分
    //  2.选择 5 分的硬币，剩 11 分
    //  3.选择 5 分的硬币，剩 6 分
    //  4.选择 5 分的硬币，剩 1 分
    //  5.选择 1 分的硬币
    // 最终的解是 1 枚 25 分、 3 枚 5 分、 1 枚 1 分的硬币，共 5 枚硬币
    //◼ 实际上本题的最优解是： 2 枚 20 分、 1 枚 1 分的硬币，共 3 枚硬币


    // 注意: 贪心策略并不一定能得到全局最优解
    // - 因为一般没有测试所有可能的解,容易过早做决定,所以没法达到最佳解.
    // - 贪图眼前局部利益最大化,看不到长远未来,走一步看一步
    // - 优点: 简单、高效、不需要穷举所有可能,通常作为其他算法的辅助算法来使用
    // - 鼠目寸光, 从不从整体上考虑其他可能,每次采取局部最优解,不会再回溯,因此很少情况会得到最优解
    @Test
    public void coinChange3() {
        Integer[] faces = {25, 20, 5, 1};
        Arrays.sort(faces, (Integer f1, Integer f2) -> f2 - f1); //  默认从小到大排序
        int money = 41, coins = 0, i = 0;
        while (i < faces.length) {
            if (money < faces[i]) {
                i++;
                continue;
            }
            System.out.println(faces[i]);
            money -= faces[i];
            coins++;
            // i = 0; // 没必要再置0
        }
        System.out.println(coins);
    }
}
