package sf.back_tracking;

import org.junit.Test;

// 回溯可以理解为: 通过选择不同的岔路口来通往目的地
// 1.每一步都选择一条路出发,能进则进,不能进则退回上一步(回溯),换一条路再试.
// 2.树、图的深度优先搜索(DFS)就是典型的回溯应用.
// 3.不难看出来,回溯很适合使用递归

@SuppressWarnings("all")
public class Queens {
    // 八皇后问题
    // 在8*8格的国际象棋上摆放八个皇后,使其不能互相攻击: 任意两个皇后都不能处在同一行、同一列、同一斜线上.
    // 1.请问有多少摆法?分别打印出来
    // 2.请打印出来所有摆法


    // 思路1: 暴力出奇迹
    // 1.从64个格子中选出任意8个格子摆放皇后,检查每一种摆法的可行性
    // 2.一共C(8,64)种摆法(大概是4.4* 10^9种摆法)[排列组合的组合]

    // 思路2:根据题意减少暴力程度
    // 很显然,每一行只能放一个皇后,所以共有8^8种摆法(1677216种),检查每一种摆法的可行性

    // 思路3: 回溯法

    // 在解决八皇后问题之前,可以先缩小数据规模,看看如何解决四皇后问题

    // 1.一上来面临有四个位置的选择,这个时候需要选择其中一个摆法出发,轮到第二行摆
    // 2.一来到第二行,发现仍有四个位置提供选择,选择一个位置出发,轮到第三行摆
    // 3.一来到第三行,发现仍有四个位置提供选择,选择一个位置出发,轮到第四行摆
    // 4.走到第四行发现没有可以摆放的地方了,回到第三行(前一行),选择其他未摆过的位置继续走.

    // 和代码的联系

    // 剪枝处理(Pruning)
    // 按照默认的回溯算法,四皇后,每选1摆放位置个又面临4个选择....不过大可不必每种情况都考虑进去
    // 1.略过下一步不满足条件的
    // 2.略过同一行同一列同一斜线的
    // 省去了不满足条件的分叉,会省很多步骤,直接只判断满足条件的分叉

    // 直到最后一行摆放成功,则是一种成功的摆法


    @Test
    public void queue() {
        placeQueues(4);
    }

    // 摆放皇后
    // n 摆放多少个皇后
    public void placeQueues(int n) {
        if (n < 1) return;
        queens = new int[n];
        place(0);  // 从0开始摆放8皇后
        System.out.println(n + "皇后一共有: " + ways + "种方法");
    }


    private int[] queens;  // 使用一维数组摆放8个皇后位置,下标代表行号,元素值代表列号
    private int ways;    // 统计多少最种摆法

    /**
     * 递归 以"行"为单位
     *
     * @param row 从第row行开始摆放皇后
     */
    private void place(int row) {
        if (row == queens.length) { // 直到最后一行摆放成功,则是一种成功的摆法
            ways++;  // 摆法+1
            show();  // 打印摆法
            return;  // 不需要往下走了,回溯?
        }
        for (int col = 0; col < queens.length && row < queens.length; col++) {  // 当前行有多少个选择
            if (isValid(row, col)) {     // 剪枝处理,在多个选择里看那些列是需要剪枝的.
                queens[row] = col;         // 在第row行第col列摆放皇后
                place(row + 1);     // 就紧接着摆放下一行
                // 回溯时机:place()代码能够执行完的时候
                // 下一行全部分支都需要剪枝处理[返回false],
                // 每个分支都不往下递归执行下一行了.那么for循环结束,意味着place要返回了,该层递归就会结束
                // 返回到上一行(上一层递归),进行下一个选择摆放下一行
            } else {
                // 需要剪枝处理,什么都不做
            }
        }

    }
    // 判断第row行第col列(将要摆放)是否可以摆放(皇后剪枝处理)
    // 剪枝,参考前面已经摆好的情况进行过滤,过滤一些不必要的
    // 返回false 什么也不干,true表示不需要剪枝处理,可以摆放
    public boolean isValid(int row, int col) {
        // 遍历之前已经摆放好的皇后,看将要摆放的皇后是否满足条件
        for (int i = 0; i < row; i++) {
            // 第col列已经有皇后,第i行皇后跟row行第col列格子处在一个斜线上
            // 同一斜线: cols[i] == col
            // 同一列: (row - i == Math.abs(col - cols[i]))
            if (queens[i] == col || (row - i == Math.abs(col - queens[i]))) {
                System.out.println("["+row+"]["+col+"]=false");  // 为了更好的调试回溯过程
                return false;
            }
        }
        System.out.println("["+row+"]["+col+"]=true"); // 为了更好的调试回溯过程
        return true;
    }

    // 打印摆法
    private void show() {
        for (int row = 0; row < queens.length; row++) {
            for (int col = 0; col < queens.length; col++) {
                if (queens[row] == col) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
        System.out.println("--------------------");
    }




}
