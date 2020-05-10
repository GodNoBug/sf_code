package sf.greedy;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
@SuppressWarnings("all")
public class ZeroOneKnapsack {
    // 练习3 - 0-1 背包
    // 有n件物品和一个最大承重为W的背包,每件物品的重量是wi,价值是vi
    // 问: 在保证总重量不超过W的前提下,将哪几件物品装入背包,可以使得背包的总价值最大?
    // 注意: 每件物品只有1件,也就是每个物品只能选择0件或者1件,因此称为0-1背包问题.

    // 如果采取贪心策略,有3个方案
    // 1.价值主导,优先选择价值最高的物品放进背包
    // 2.质量主导,优先选择质量最轻的物品放进背包
    // 3.价值密度主导: 优先选择价值密度最高的物品放进背包(价值密度=价值/重量)

    //   实例: 假设背包最大承重150,7个物品如表格所示
    // | 编号   | 1  | 2  | 3  | 4  | 5  | 6  | 7  |
    // | 重量   | 35 | 30 | 60 | 50 | 40 | 10 | 25 |
    // | 价值   | 10 | 40 | 30 | 50 | 35 | 40 | 30 |
    // | 密度   |0.29|1.33| 0.5| 1.0|0.88| 4.0| 1.2|
    // 1.价值主导: 放入背包的物品编号是[4,2,6,5] 总质量130,总价值165
    // 1.质量主导: 放入背包的物品编号是[6,7,2,1,5] 总质量140,总价值155
    // 1.密度主导: 放入背包的物品编号是[6,2,7,4,1] 总质量150,总价值170


    @Test
    public void knapsack() {
        select("价值主导", (Article o1, Article o2) -> o2.value - o1.value);//价值大的在前
        select("质量主导", (Article o1, Article o2) -> o1.weight - o2.weight);//质量轻的在前
        select("密度主导", (Article o1, Article o2) -> Double.compare(o2.valueDensity, o1.valueDensity));
    }

    private void select(String title, Comparator<Article> cmp) {
        Article[] articles = new Article[]{ // 已经计算了各自的价值密度主导
                new Article(35, 10), new Article(30, 40),
                new Article(60, 30), new Article(50, 50),
                new Article(40, 35), new Article(10, 40),
                new Article(25, 30)
        };
        Arrays.sort(articles, cmp); // 根据不同排序


        //  最大承重,当前背包质量,背包内的物品总价值
        int capacity = 150, weight = 0, value = 0;
        //  已选择的物品
        List<Article> selectedArticles = new LinkedList<>();
        for (int i = 0; i < articles.length && weight < capacity; i++) {
            int newHeight = weight + articles[i].weight;
            if (newHeight <= capacity) {
                weight = newHeight;
                value += articles[i].value;
                selectedArticles.add(articles[i]);
            }
        }
        System.out.println("[" + title + "]");
        System.out.println("总价值: " + value);
        for (int i = 0; i < selectedArticles.size(); i++) {
            System.out.println(selectedArticles.get(i));
        }
        System.out.println("-----------------------------");
    }


    // 作业
    // 分发饼干: https://leetcode-cn.com/problems/assign-cookies/
    // 用最少数量的箭引爆气球: https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons/
    // 买卖股票的最佳时机: https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/
    // 种花问题: https://leetcode-cn.com/problems/can-place-flowers/
    // 分发糖果: https://leetcode-cn.com/problems/candy/
}
