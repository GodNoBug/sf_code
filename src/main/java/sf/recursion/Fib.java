package sf.recursion;

import org.junit.Test;
import sf.sort.tools.Times;

@SuppressWarnings("all")
public class Fib {


    // 求第n个斐波那契数列
    // 1 1 2 3  5 8  13 21 34
    // f(n) = f(n-1) + f(n-2) [n>=3 条件才成立]   -- 明确原问题与子问题的关系
    // f(1)=1, f(2)=1 而n=1 n=2时               --  明确递归基(边界条件)
    @Test
    public void test2() {
        System.out.println(fib6(7));
        int n = 6;
        Times.test("fib", () -> {
            fib(n);
        });
        Times.test("fib2", () -> {
            fib2(n);
        });
    }

    /**
     * 根据递推式T(n) = T(n-1) + T(n-2) + O(1),可得知时间复杂度: O(2^n)
     * 空间复杂度: O(n)   O(n)*O(1)
     * 递归调用的空间复杂度 = 递归深度*每次调用所需的辅助空间
     * "自顶向下"的调用,先入栈一边,入栈完全并弹出后,再入栈另外一边,有点像二叉树的遍历
     */
    private int fib(int n) {
        if (n <= 2) return 1;
        // 图片
        // TODO 在左边递归调用结束之前,是不会调用右边递归调用的,注意,在每个递归调用中都成立  [若教过树的遍历的话,可以类比更形象]
        return fib(n - 1) + fib(n - 2);
    }


    /**
     * TODO  优化,fib具有特别多的相同重复运算,导致时间复杂度高
     * 用数组存放计算过程的结果,避免重复计算
     * 时间复杂度: O(n) 传n,算n次
     * 空间复杂度: O(n)
     */
    private int fib2(int n) {
        if (n <= 2) return 1;
        int[] arr = new int[n + 1]; // 舍弃arr[0] 让代码更简洁
        arr[1] = arr[2] = 1;  // 初始化.防止无限递归
        return fib2(n, arr);
    }

    // 如何存储中间结果?需要容器存储中间结果,避免重复计算[结合递归过程图片来思考]
    // 下标代表第n项,值为中间结果,数字连续.适宜用数组
    private int fib2(int n, int[] arr) {
        if (arr[n] == 0) { // 说明数组内没有被赋值,第n项没有被求过
            // 每计算得到的值,都存储在数组中
            arr[n] = fib2(n - 1, arr) + fib2(n - 2, arr);
        }
        // 如果不等于0的话,说明已经被求过了return
        return arr[n];
    }

    // 去除递归调用 ,时间复杂度O(n),空间复杂度O(n)
    // 这是一种"自底向上"的计算过程,由小推大
    private int fib3(int n) {
        if (n <= 2) return 1;
        int[] arr = new int[n + 1];
        arr[2] = arr[1] = 1;
        for (int i = 3; i <= n; i++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[n];
    }


    // 由于每次运算只需要用到数组中的两个元素,所以可以使用滚动数组来优化
    // 滚动数组: 就像保险箱滚动的锁.
    // 时间复杂度： O(n)，空间复杂度： O(1)

    //   ↓   ↓
    // | 1 | 1 | 2   3   5   8   13
    //   1 | 1 | 2 | 3   5   8   13
    //   1   1 | 2 | 3 | 5   8   13
    //   1   1   2 | 3 | 5 | 8   13
    //   1   1   2   3 | 5 | 8 | 13
    //   1   1   2   3   5 | 8 | 13 |
    // 每次只用到数组中的两个元素,那么就用两个数组,两个数字相加覆盖到其中一个值,再相加,再覆盖.就像滚动

    private int fib4(int n) {
        int[] arr = new int[2];
        if (n <= 2) return 1;
        arr[0] = arr[1] = 1;
        for (int i = 3; i <= n; i++) {
            int tmp = arr[0];
            arr[0] = arr[1];
            arr[1] = tmp + arr[1];
        }
        return arr[1];
    }

    private int fib5(int n) { // % 2变成 &1
        if (n <= 2) return 1;
        int[] arr = new int[2];
        arr[0] = arr[1] = 1;
        for (int i = 3; i <= n; i++) {
            arr[i & 1] = arr[(i - 1) & 1] + arr[(i - 2) & 1];
        }
        return arr[n & 1];
    }

    // 线性代数方程
    // 时间复杂度/空间复杂度取决于pow函数(至少可以低至O(logn))
    public int fib6(int n) {
        double c = Math.sqrt(5);
        return (int) ((Math.pow((1 + c) / 2, n) - Math.pow((1 - c) / 2, n)) / c);
    }
    // 1 1 2 3  5 8  13 21 34
    // 时间复杂度： O(n)，空间复杂度： O(1)
    public int fib7(int n) {
        if (n <= 2) return 1;
        int first = 1;
        int second = 1;
        for (int i = 3; i <= n; i++) {
            second = first + second;
            first = second - first;
        }
        return second;
    }
}
