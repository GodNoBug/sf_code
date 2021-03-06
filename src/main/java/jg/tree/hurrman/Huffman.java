package jg.tree.hurrman;

/**
 * 哈夫曼编码,又称为赫夫曼编码,它是现代的压缩算法的基础
 * 假设要把字符串[ABBCCCCCCCCDDDDDDEE] 转成二进制编码进行传输
 * 可以把ASCⅡ(65~69,1000001~1000101),但是有点冗长,如果希望编码更短呢?
 *
 * 可以先约定5个字母对应的二进制.但是解码的时候容易产生歧义,有很多重解读方法.[原因: 其中一个字母的编码是另外一个编码的前缀]
 * 解决: 规定大家都是3位,取三位,读也是三位.达到缩短的目的
 * | A  |  B |  C |  D   E
 * |000 | 001| 010 011 100
 *
 * 如果使用赫夫曼编码,可以压缩至41个二进制位,约为原来长度的68.3%
 */
public class Huffman {

    // 赫夫曼树
    // 先计算出每个字母的出现频率(权值,这里直接用出现次数),[ABBCCCCCCCCDDDDDDEE]
    // 利用这些权值,构建一颗赫夫曼树(最优二叉树)
    // 1.以权值作为根节点构建n颗二叉树,组成森林
    // 2.在森林中选出2个根节点最小的树合并,作为一颗新树的左右子树,且新树的根节点为其左右子树根节点之和
    // 3.从森林中删除刚才选取的2颗树,并将新树加入森林
    // 4.重复2,3步骤,直到森林只剩一颗为止,该树即为赫夫曼树

    // 构建赫夫曼编码

    //   20
    //  /  \
    // C(8) 12
    //     /  \
    //   D(6)  6
    //        /  \
    //      B(3)  3
    //           / \
    //          A(1) E(2)

    // left 为0,right 为1,可以得出5个字母对应的赫夫曼编码 . 如
    // A : 1110
    // B : 110
    // C : 0
    // D : 10
    // E : 1111

    // 总结
    // n个权值构建出来的赫夫曼树拥有n个叶子节点
    // 每个赫夫曼编码都不是另一个赫夫曼编码的前缀
    // 赫夫曼树是带权路径长度最短的树,权值较大的结点离根较近
    // 带权路径长度 树中所有的叶子节点的权值乘上其到根节点的路径长度.与最终的赫夫曼编码总长度成正比关系.(也是编码长度?出现的次数*编码长度)
}
