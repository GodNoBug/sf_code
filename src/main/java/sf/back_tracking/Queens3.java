package sf.back_tracking;

import org.junit.Test;

// 位运算解法
// 可以利用位运算进一步压缩八皇后的空间复杂度
// 面前优化只针对不超过n皇后不超过8的,byte 8位,short 16位.可以试试8皇后以上的可行性
@SuppressWarnings("all")
public class Queens3 {


    @Test
    public void queue() {
        place8Queues();
    }


    public void place8Queues() {
        queens = new int[8];
        place(0);  // 从0开始摆放8皇后
        System.out.println("8皇后一共有: " + ways + "种方法");
    }

    // 为什么这样优化?
    // 1.布尔数组完全可以使用二进制替代
    // 2.布尔数组长度可以用现有的数据类型长度表达,当然也可以使用二维数组表示特长的二进制数据[请参考布隆过滤器实现,使用二维数组]
    byte cols;    // 标记着某一列是否有皇后了[下标为列号],如果判断这一列有没有皇后,直接获取判断O(1).不需要循环O(n)比较判断
    short leftTop;// 标记着某一对角线是否有皇后了(左上角->右下角,left top -> right bottom)
    short rightTop;// 标记着某一对角线是否有皇后了(右上角->左下角,right top -> left bottom)


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
        // 左上角 -> 右下角的对角线索引： row – col + (n-1)
        // 右上角 -> 左下角的对角线索引： row + col
        for (int col = 0; col < queens.length && row < queens.length; col++) {  // 当前行有多少个选择
            // col = 2
            // cols = 11001011
            int cv = 1 << col;
            if ((cols & cv) != 0) continue;  // 第col有皇后需要忽视
            int lv = 1 << (row - col + queens.length - 1);
            if ((leftTop & lv) != 0) continue;
            int rv = 1 << (row + col);
            if ((rightTop & rv) != 0) continue;

            queens[row] = col;   // 在第row行第col列摆放皇后
            // cols= (byte) (cols|(1<<col));
            cols |= cv;
            leftTop |= lv;
            rightTop |= rv;

            place(row + 1);     // 就紧接着摆放下一行

            // 逆操作,还原现场.思考未优化之前的代码为什么不用写?因为旧值自然覆盖了新值,重复利用,不必人为操作.
            // 别人的设置无法覆盖旧的设置.需要逆操作
            cols &= ~cv;
            leftTop &= ~lv;
            rightTop &= ~rv;

        }

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

    // 位运算测试
    @Test
    public void test1() {
        // 常见位运算
        // 01111101 n
        //&00000100 v
        //----------
        // 00000100  判断二进制位col位是1还是0,   1<<col和目标数字取&,结果非0代表目标数字这个位置是1,为0代表这个位置是0
        int n = 125;
        for (int i = 0; i < 8; i++) {
            int result = n & (1 << i);
            System.out.println(i + "_" + (result != 0));
        }
//        int col = 2;
//        int v = 1 << col;
//        int result = n & (1 << col);
//        System.out.println(result!=0);
//        System.out.println(Integer.toBinaryString(v));


        //  01101101 n
        //| 00010000 v
        //-------------
        //  01111101     按位或  把指定位置变为1


        //  01111101 n
        //& 11111011 ~v    v=00000100[取反再运算]
        //-------------
        //  01111001   按位与  把指定位置变为0
        // 异或?可以试试
    }
}
//◼ 全排列： https://leetcode-cn.com/problems/permutations
//◼ 全排列 II： https://leetcode-cn.com/problems/permutations-ii/
//◼ 组合总和： https://leetcode-cn.com/problems/combination-sum/
//◼ 组合总和 II： https://leetcode-cn.com/problems/combination-sum-ii/
//◼ 子集： https://leetcode-cn.com/problems/subsets/
//◼ 子集 II： https://leetcode-cn.com/problems/subsets-ii/