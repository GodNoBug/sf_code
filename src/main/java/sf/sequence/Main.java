package sf.sequence;

import org.junit.Test;

public class Main {
    //蛮力性能分析
    //n是文本串长度,m是模式串长度
    // 扫描模式串为1轮,那么一个格子代表一轮,最多n-m+1轮

    //最好情况:
    // 1.只需一轮比较久完全匹配成功,在第一个对齐位置只经过一轮对比之后就成功了,比较m次
    // 2.时间复杂度为O(m)

    // 最坏情况
    // 每轮都比对至模式串的末字符,且反复如此
    //   每轮循环: #比对= m-1(成功)+1(失败)=m
    //   循环次数: n-m+1 (尝试了所有对齐位置)
    // 一般地有 m << n (由于一般m远小于n)
    //   时间复杂度为O(m*(n-m+1)),,所以为O(nm)

    // (字符集越小,最坏的情况出现概率越高.因为字符集种类少,每轮的局部匹配出现概率更高)
    // (模式串长度越大,最坏情况的后果更加严重)
    @Test
    public void bruteForce1(){
        System.out.println(BruteForce1.indexOf("Hello World", "H")==0);
        System.out.println(BruteForce1.indexOf("Hello World", "d")==10);
        System.out.println(BruteForce1.indexOf("Hello World", "or")==7);
        System.out.println(BruteForce1.indexOf("Hello World", "abc")==-1);
    }
    @Test
    public void bruteForce2(){
        System.out.println(BruteForce2.indexOf("Hello World", "H")==0);
        System.out.println(BruteForce2.indexOf("Hello World", "d")==10);
        System.out.println(BruteForce2.indexOf("Hello World", "or")==7);
        System.out.println(BruteForce2.indexOf("Hello World", "abc")==-1);
    }
}
