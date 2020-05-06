package sf.back_tracking;

import org.junit.Test;

@SuppressWarnings("all")
public class Queens2 {


    @Test
    public void queue() {
        placeQueues(4);
    }

    // 摆放皇后
    // n 摆放多少个皇后
    public void placeQueues(int n) {
        if (n < 1) return;
        queens = new int[n];
        cols = new boolean[n];
        leftTop = new boolean[(n << 1) - 1]; // 2 * n - 1
        rightTop = new boolean[leftTop.length];
        place(0);  // 从0开始摆放8皇后
        System.out.println(n + "皇后一共有: " + ways + "种方法");
    }


    boolean[] cols;     // 标记着某一列是否有皇后了[下标为列号],如果判断这一列有没有皇后,直接获取判断O(1).不需要循环O(n)比较判断
    boolean[] leftTop;  // 标记着某一对角线是否有皇后了(左上角->右下角,left top -> right bottom)
    boolean[] rightTop;  // 标记着某一对角线是否有皇后了(右上角->左下角,right top -> left bottom)

    // 由于回溯的缘故程序执行完毕cols,leftTop,rightTop全部为false,没办法确定摆法的位置
    private int[] queens;  // 使用一维数组摆放8个皇后位置,下标代表行号,元素值代表列号

    private int ways;    // 统计多少最种摆法

    /**
     * 递归 以"行"为单位
     *
     * @param row 从第row行开始摆放皇后
     */
    private void place(int row) {
        if (row == cols.length) { // 直到最后一行摆放成功,则是一种成功的摆法
            ways++;  // 摆法+1
            show();  // 打印摆法
            return;  // 不需要往下走了,回溯?
        }
        // 左上角 -> 右下角的对角线索引： row – col + (n-1)
        // 右上角 -> 左下角的对角线索引： row + col
        for (int col = 0; col < cols.length && row < cols.length; col++) {  // 当前行有多少个选择
            if (cols[col]) continue;  // 第col有皇后需要忽视
            int ltIndex = row - col + cols.length - 1;
            if (leftTop[ltIndex]) continue;
            int rtIndex = row + col;
            if (rightTop[rtIndex]) continue;

            queens[row] = col;   // 在第row行第col列摆放皇后
            cols[col] = true;
            leftTop[ltIndex] = true;
            rightTop[rtIndex] = true;

            place(row + 1);     // 就紧接着摆放下一行

            // 逆操作,还原现场.思考未优化之前的代码为什么不用写?因为旧值自然覆盖了新值,重复利用,不必人为操作.
            // 别人的设置无法覆盖旧的设置.需要逆操作
            cols[col] = false;
            leftTop[ltIndex] = false;
            rightTop[rtIndex] = false;

        }

    }


    // 打印摆法
    private void show() {
        for (int row = 0; row < cols.length; row++) {
            for (int col = 0; col < cols.length; col++) {
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
