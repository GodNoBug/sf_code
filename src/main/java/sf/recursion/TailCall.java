package sf.recursion;

import org.junit.Test;

import java.util.Stack;

// 尾调用（Tail Call）
// 尾调用：一个函数的最后一个动作是调用函数
//    尾调用由于是函数的最后一步操作，所以不需要保留外层函数的调用记录，
//  因为调用位置、内部变量等信息都不会再用到了，只要直接用内层函数的调用
//  记录，取代外层函数的调用记录就可以了。
// 如果最后一个动作是调用自身,称为尾递归（Tail Recursion）,是尾调用的特殊情况
@SuppressWarnings("unused")
public class TailCall {
    @Test
    public void test1() {
        int a = 10;
        int b = a + 20;
        test2(b); // 尾调用
    }


    private void test2(int n) {
        if (n < 0) return;
        test2(n - 1); // 尾递归
    }
    // 一些编译器能对尾调用进行优化，以达到节省栈空间的目的

    // 下面代码不是尾调用,  因为它最后1个动作是乘法
    public int factorial(int n) {
        if (n <= 1) return n;
        return n * factorial(n - 1);
    }

    // 转成尾递归的形式
    public int factorial2(int n, int result) {
        if (n <= 1) return result;
        return factorial2(n, n * result);
    }

    // 尾调用优化也叫做尾调用消除(Tail Call Elimination)

    //  1.如果当前栈帧上的局部变量等内容都不需要用了,当前栈帧经过适当的改变后可以直接当作被尾调用的函数的栈帧
    //    使用,然后程序可以 jump 到被尾调用的函数代码
    //  2.生成栈帧改变代码与 jump 的过程称作尾调用消除或尾调用优化
    //  3.尾调用优化让位于尾位置的函数调用跟 goto 语句性能一样高

    //  消除尾递归里的尾调用比消除一般的尾调用容易很多
    //  1.比如Java虚拟机（JVM）会消除尾递归里的尾调用，但不会消除一般的尾调用（因为改变不了栈帧）
    //  2.因此尾递归优化相对比较普遍，平时的递归代码可以考虑尽量使用尾递归的形式

    public void test(int n) {
        if (n < 0) return;
        System.out.println("test-" + n);
        test(n - 2);
    }

    // 尾递归优化会变成类似以下的代码
    public void test3(int n) {
        if (n < 0) return;
        while (n >= 0) {
            System.out.println("test-" + n);
            n -= 2;
        }
    }

    // 斐波那契数列
    public int fib(int n) {
        if (n <= 2) return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public int fib2(int n){
        return fib3(n,1,1);
    }
    // 尾递归 6 1 1
    public int fib3(int n, int first, int second) {
        if (n <= 2) return first;
        return fib3(n - 1, second, first + second);
    }
}
