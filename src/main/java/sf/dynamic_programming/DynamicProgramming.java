package sf.dynamic_programming;

// 动态规划，简称DP
//   是求解最优化问题的一种常用策略

import org.junit.Test;

//通常的使用套路（一步一步优化-新手向）
//1.暴力递归（自顶向下，出现了重叠子问题）
//2.记忆化搜索（自顶向下）
//3.递推（自底向上）
public class DynamicProgramming {
    // 动态规划的常规步骤(老手向)
    // 动态规划中的"动态"可以理解为是"会变化的状态"
    //  1.定义状态(状态是原问题、子问题的解)
    //    比如定义dp(i)的含义
    //  2.设置初始状态(边界)
    //    比如设置dp(0)的值
    //  3.确定状态转移方程       (递推由小推导大?)
    //     比如确定dp(i)和dp(i-1)的关系

    // 动态规划的一些相关概念
    // ◼ 来自维基百科的解释
    //    Dynamic Programming is a method for solving a complex problem by breaking it down into a
    //  collection of simpler subproblems, solving each of those subproblems just once, and storing their solutions.

    // 1.将复杂的原问题拆解成若干个简单的子问题
    // 2.每个子问题仅仅解决1次，并保存它们的解
    // 3.最后推导出原问题的解
    //可以用动态规划来解决的问题，通常具备2个特点
    // - 最优子结构(最优化原理): 通过求解子问题的最优解，可以获得原问题的最优解
    // - 无后效性
    //  ✓ 某阶段的状态一旦确定，则此后过程的演变不再受此前各状态及决策的影响（未来与过去无关）
    //  ✓ 在推导后面阶段的状态时，只关心前面阶段的具体状态值，不关心这个状态是怎么一步步推导出来的

    // 斐波那契数列,广义上来讲是动态规划.也有人说是算动态规划
    // fib(40)
    // dp(i) 第i项斐波那契数
    // dp(i) = dp(i-1)+dp(i-2)

}
