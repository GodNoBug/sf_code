package sf.greedy;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


// 贪心策略,也称为贪婪策略
//  1.每一步都采用当前状态下最优的选择(局部最优解),从而希望推导出全局最优解
//  2.贪心的应用
//  - (1) 赫夫曼树
//  - (2) 最小生成树算法: Prim、Kruskal
//  - (3) 最短路径算法: Dijkstra
public class Pirate {

    // 练习1
    // 最优装载问题(加勒比海盗)
    //   在北美洲东南部,有一片神秘的海域,是海盗最活跃的加勒比海.有一天,海盗们补货了一艘装满各种各样古
    // 董的货船,每一件古董都价值连城,一旦打碎就失去了它的价值.
    //   海盗船的载重量为W, 每件古董的重量为wi, 海盗们该如何把尽可能多数量的古董装上海盗船?
    // 比如 W 为 30 , wi分别为 3,5,4,10,7,14,2,11


    // 贪心策略: 每一次都优先选择重量最小的古董(每次都选尽可能小重量的古董)
    // 1.选择重量为2的古董,剩重量28
    // 2.选择重量为3的古董,剩重量25
    // 3.选择重量为4的古董,剩重量21
    // 4.选择重量为5的古董,剩重量16
    // 5.选择重量为7的古董,剩重量9
    // 最多能装载5个古董

    @Test
    public void pirate() {
        int[] weights = {3, 5, 4, 10, 7, 14, 2, 11};
        Arrays.sort(weights);
        int capacity = 30;  // 船能载的最大重量
        int weight = 0;     // 船现在装载的重量
        int count = 0;      // 能装多少个

        for (int i = 0; i < weights.length && weight < capacity; i++) {
            int newWeight = weight + weights[i];  // 试着加,如果满足条件,再赋值给真正的weight.不能直接赋值weight然后判断输出.
            if (newWeight <= capacity) {
                weight = newWeight;
                count++;
                System.out.println("选择: " + weights[i]);
            }
        }
        System.out.println("最大能装:" + count + "件古董");
        System.out.println("最大能装:" + weight + "重量");
    }




}
