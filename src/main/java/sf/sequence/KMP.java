package sf.sequence;
// KMP 是 Knuth-Morris-Pratt(3位发明人)的简称

// 蛮力算法是匹配失败往后挪一个单位,而KMP是匹配失败直接跳过没有必要比较的地方,来到有必要比较的地方
// KMP的精妙之处: 充分利用了此前比较过的内容,可以很聪明地跳过一些不必要的比较位置.
public class KMP {

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

        // next 表
        int[] next = next(pattern);
        int pi = 0, ti = 0,lenDelta=tlen-plen;
        while (pi < plen && (ti-pi<=lenDelta)) { // 优化 ti < tlen改为 ti-pi<=tlen-plen 文本串正在匹配的子串的开始索引(是指每一轮比较中Text首个比较字符的位置) tlen-plen为开始索引的临界值
            if (pi<0 && textChars[ti] == patternChars[pi]) {
                ti++;
                pi++;
            } else { // 失配了
                pi =next[pi];

            }

        }
        return (pi == plen) ? (ti - pi) : -1;
    }


    private static int[] next(String pattern){
        return null;
    }
    // KMP - next表的使用(先学会怎么用)
    // KMP 会预先根据模式串的内容生成一张next表(一般是数组)
    // 如 模式串"ABCDABCE" 的next表
    // 模式串字符 | A | B | C | D | A | B | C | E |
    //  索引     | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
    //  元素     |-1 | 0 | 0 | 0 | 0 | 1 | 2 | 3 |

    //                                  ti=8
    //                                   ↓
    // | D | A | B | C | C | B | B | C | F | A | C | B | A |
    //     | A | B | C | D | A | B | C |{E}|
    //    pi=next[pi] -> 4               ↑
    // |----向右移动的距离--|             pi=7 失配的位置去查询next表 让pi = next[pi] ->3
    //                     | A | B | C | D | A | B | C |{E}|
    //                                   ↑
    //                                   pi=3


    // KMP的原理
}
