package sf.sequence;

public class BruteForce2 {

    // 蛮力算法2
    public static int indexOf(String text, String pattern) {
        if (text == null || pattern == null) return -1;
        // 文本串
        char[] textChars = text.toCharArray();
        int tlen = textChars.length;
        if (tlen == 0) return -1;
        // 模式串
        char[] patternChars = pattern.toCharArray();
        int plen = patternChars.length;
        if (plen == 0) return -1;
        if (tlen < plen) return -1;
        // 模式串中的pi仍然指向当前的字符位置
        // 文本串中的pi+ti指向当前的字符位置
        int tiMax = tlen - plen;
        for (int ti = 0; ti <= tiMax; ti++) {
            int pi = 0; // 每当ti增长一次,pi置0
            for (; pi < plen; pi++) { // pi位置和ti+pi比对,比对成功for循环自然的++
                if (textChars[ti + pi] != patternChars[pi]) break;  // 若失配,ti++整体右移一个字符,重新比对
            }
            // 一种是自然退出,pi==plen
            if (pi==plen) return ti;
            // 另外一种就是break退出了
        }
        return -1;
    }

    // pi=0  [标记模式串中哪个字符正在参与比较]    pi的取值范围[0,plen)
    // |[1]| 0 | 0 | 0 |                       ti的取值范围[0,tlen-plen]
    // | 1 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 1 | 0 |
    // ti=0  [现在是指每一轮比较中Text首个比较字符的位置]

    //      pi=1
    // |[1]|[0]| 0 | 0 |
    // | 1 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 1 | 0 |
    // ti=0 ti+pi

    //          pi=2
    // |[1]|[0]|[0]| 0 |
    // | 1 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 1 | 0 |
    // ti=0     ti+pi 原本的ti保持不变

    //          pi=2
    // |[1]|[0]|[0]|{0}|
    // | 1 | 0 | 0 | 1 | 0 | 0 | 0 | 0 | 1 | 0 |
    // ti=0     ti+pi 原本的ti保持不变


    // 匹配成功
    // 模式串第一个字符与文本串匹配成功,再匹配模式串的第二个字符,如果第二个字符不匹配,没必要匹配模式串后续的字符.模式串和 文本串的下一个字符再进行比较
    // pi++

    // 匹配失败
    // pi=0 ti++

    // pi==plen 匹配成功
}
