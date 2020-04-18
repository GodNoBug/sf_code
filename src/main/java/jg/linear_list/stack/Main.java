package jg.linear_list.stack;

import org.junit.Test;

import java.util.HashMap;

public class Main {

    private static final String s = "(1){CHI[与|和] || CAT[J] LOGIC[G|D]}+(2){CAT[A] || OF_AMBI[A]}+(3){CHI[的]||CAT[N]}";

    @Test
    public void test1() {
        Stack<Integer> stack = new Stack<>();
        stack.push(11);
        stack.push(22);
        stack.push(33);
        stack.push(44);

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }

    // 有效的括,成对的括号
    // 1.遇见左字符,将左字符入栈
    // 2.遇见右字符
    // - 如果栈是空的,说明括号无效
    // - 如果栈不为空,将栈顶字符出栈,与右字符与之匹配
    //   --如果左右字符不匹配,说明括号无效
    //   --如果左右字符匹配,继续扫描下一个字符
    // 3.所有字符扫描完毕后
    // - 栈为空,说明括号有效
    // - 栈不为空,说明括号无效

    @Test
    public void test2() {
        System.out.println(isValid(s));
    }


    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.top() == '(') {
                    stack.pop();
                }
            } else if (c == ']') {
                if (stack.top() == '[') {
                    stack.pop();
                }
            } else if (c == '}') {
                if (stack.top() == '{') {
                    stack.pop();
                }
            }
        }
        if (stack.isEmpty()) {
            System.out.println("成对");
            return false;
        } else {
            System.out.println("不成对");
            return true;
        }
    }


}
