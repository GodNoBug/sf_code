package sf.recursion;

import org.junit.Test;

public class ClimbStairs {
    @Test
    public void test1() {
        System.out.println(climbStairs(3));
    }
    @Test
    public void test2(){
        System.out.println(climbStairs2(3));
    }

    //

    // 题目描述：一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种方法？

    // 1.如果只有1级台阶，那显然只有1种跳法
    // 2.如果有2级台阶，那么就有2种跳法，一种是分2次跳。每次跳1级，另一种就是一次跳2级
    // 3.如果台阶级数大于2，设为n的话，这时我们把n级台阶时的跳法看成n的函数，记为f(n),
    //  - 第一次跳的时候有2种不同的选择：
    //  -- 一是第一次跳一级，此时跳法的数目等于后面剩下的n-1级台阶的跳法数目，即为,f(n-1)
    //  -- 二是第一次跳二级，此时跳法的数目等于后面剩下的n-2级台阶的跳法数目，即为,f(n-2)
    //  -- 因此n级台阶的不同跳法的总数为f(n)=f(n-1)+f(n-2)，不难看出和斐波那契数列相似,但初始条件不同


    /**
     * 递归
     * @param n n个台阶
     * @return 走法
     */
    public int climbStairs(int n) {
        if (n <= 2) return n; // n=2 n=1
        return climbStairs(n - 1) + climbStairs(n - 2);
    }
    /**
     * 迭代
     * @param n n个台阶
     * @return 走法
     */
    public int climbStairs2(int n) {
        if (n <= 2) return n;
        int first = 1;   // 上一个台阶的走法
        int second = 2;  // 上两个台阶的走法
        for (int i = 3; i < n; i++) {
            second = first + second; // 前两个走法之和
            first = second - first;
        }
        return second;
    }
    // 上4台阶的走法呢?

}
