package sf.sequence;

public class BruteForce1 {

    // 蛮力算法1
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
        // pi 和 ti 是为了分别指示在模式串和文本串中当前的字符位置.
        // pi的数值就对应于在当前的对齐位置下,已经做过的成功比对次数.当pi<plen条件不满足,意味着匹配成功
        // ti文本串中当前的字符位置. ti<tlen不满足时,意味着ti=tlen退出循环,整个匹配是以失败告终的
        // 在后一版本中同样用到这两个整数但语义不尽相同.也是两种版本的本质区别
        int pi = 0, ti = 0;
        while (pi < plen && ti<tlen) { // 从左向右逐个比对字符
            if (textChars[ti] == patternChars[pi]) { // 若匹配,则转到下一对字符. 这里的if相当于在保持相对位置不变的情况下去比较每一对字符
                ti++;
                pi++;
            } else { // 否则意味着失配, 这里的else对应于模式串和文本串的相对滑动
                ti -= pi - 1;
                pi = 0;
            }

        }
        return (pi == plen) ? (ti - pi) : -1; //
    }
    // 蛮力算法1-优化
    public static int indexOf2(String text, String pattern) {
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
        // 这里特别分析ti越界的情况
        // 越界的时候必然ti=tlen,且pi<plen处于合法位置
        // 由此推导 ti-pi > tlen-plen
        int pi = 0, ti = 0,lenDelta=tlen-plen;
        while (pi < plen && (ti-pi<=lenDelta)) { // 优化 ti < tlen改为 ti-pi<=tlen-plen 文本串正在匹配的子串的开始索引(是指每一轮比较中Text首个比较字符的位置) tlen-plen为开始索引的临界值
            if (textChars[ti] == patternChars[pi]) {
                ti++;
                pi++;
            } else {
                ti -= pi - 1;
                pi = 0;
            }

        }
        return (pi == plen) ? (ti - pi) : -1;
    }

    // 蛮力(Brute Force)
    // 以字符为单位,从左到右移动模式串,直到匹配成功

    // =>
    // 1 [0] 0
    // | 1 0 [0]
    // | | [1] 0 0
    // | | | 1 0 0 √
    // | | | |
    // 1 1 0 1 0 0 1 0 1 0

    // 模式串第一个字符与文本串匹配成功,再匹配模式串的第二个字符,如果第二个字符不匹配,没必要匹配模式串后续的字符.模式串和 文本串的下一个字符再进行比较


    // 蛮力1

    // pi=0 [标记模式串中哪个字符正在参与比较] 取值范围[0,plen)
    // [1] 0 0 0
    //  1  0 0 1 0 0 0 0 1 0
    // ti=0 [标记文本串中哪个字符正在参与比较] 取值范围[0,tlen)

    // 匹配成功 pi++ ti++
    // pi=1
    // [1] [0] 0 0
    //  1  0 0 1 0 0 0 0 1 0
    // ti=1

    // pi=2
    // [1] [0] [0] 0
    //  1   0   0  1 0 0 0 0 1 0
    // ti=2

    // pi=3         ↓
    // [1] [0] [0] {0}
    //  1   0   0   1 0 0 0 0 1 0
    //              ↑
    // ti=3

    // 匹配失败,pi要归零,ti要回归最开始比较的字符+1

    // pi=0
    //   [1] 0 0 0
    //  1 0  0 1 0 0 0 0 1 0
    // ti=1

    // pi=0
    //     [1] 0 0 0
    //  1 0 0 1 0 0 0 0 1 0
    // ti-=pi-1 : ti-pi+1

    // pi = plen 代表匹配成功
    // 返回ti-pi即匹配到的位置

}
