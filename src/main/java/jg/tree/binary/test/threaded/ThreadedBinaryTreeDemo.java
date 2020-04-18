package jg.tree.binary.test.threaded;

import lombok.Getter;
import lombok.Setter;

/**
 * 思路
 * 1. 规则
 *     当以二叉链表作为存储结构时,只能找到结点的左右孩子信息,而不能直接得到结点任一序列中的前驱和后续信息,这种信息只有在遍历的动态过程中
 *   才能得到在有n个结点的二叉树链表中必定存在n+1个空链域. 由此设想能否利用这些空链域来存放结点的前驱和后续的信息.
 *     试作如下规定:
 *        1. 若当前结点有左子树,则其属性left指示左孩子,否则left指示其前驱
 *        2. 若当前结点由右子树,则其属下right指示右孩子,否则left指示其后续
 *     也就是说,left可能指向左子树也可能指向前驱节点.right可能指向右子树,也可能指向后续节点
 *
 * 2. 遍历
 *     在线索树上进行遍历,只要先找到序列中的第一个结点,然后依次找到结点后续直至后续为空时为止.
 * 3.找后续
 *     画图可知,树中所有叶子结点的右链是线索,则右链域直接指示了结点的后续.树中非终端结点的右链均为指针,
 *  则无法由此得到后续的信息.然而(在非叶子结点中)根据中序遍历的规律可知,结点的后续应该是遍历其右子树时
 *  访问的第一个结点,即右子树中最左下的结点.
 *     在[后序线索树]中找结点后续比较复杂,分三种情况:
 *     (1)若结点x是二叉树的根,则后续为空
 *     (2)若结点x是其双亲的右孩子或是 其双亲的左孩子且双亲没有右子树,则其后续即为双亲结点
 *     (3)若结点x是其双亲的左孩子,且其双亲有右子树,则其后续为双亲的右子树上按后续遍历列出的第一个结点
 * 4.找前驱
 *     反之,在中序线索树中找结点前驱的规律是: 若其左标志位"1",则左链为线索,指示其前驱,否则遍历左子树时
 *     最后访问的一个节点(左子树最右下的节点)为其前驱
 * 5.线索化
 *     由于线索化的实质是将二叉链表中的空指针改为指向前驱或后续的线索,而前驱或后续的信息只有在遍历时才
 *     能得到,因此线索化即为在遍历的过程中修改空指针的过程. 为了记下遍历过程总访问结点的先后关系,附设一
 *     个指针pre始终指向刚刚访问过的结点,若指针p指向当前访问的结点,则pre指向它的前驱.
 */

//
// 思路
// 若该节点有左子树,则其
public class ThreadedBinaryTreeDemo {
//    public static void main(String[] args) {
//        Node root = new Node(1, "tom");
//        Node node2 = new Node(3, "jack");
//        Node node3 = new Node(6, "smith");
//        Node node4 = new Node(8, "mary");
//        Node node5 = new Node(10, "king");
//        Node node6 = new Node(14, "dim");
//
//        root.setLeft(node2);
//        root.setRight(node3);
//        node2.setLeft(node4);
//        node2.setRight(node5);
//        node3.setLeft(node6);
//        ThreadedBinaryTree tree = new ThreadedBinaryTree(root);
//        tree.threadedNodes();
//
//
//        // 以10号节点作测试
//        Node left = node5.getLeft(); // 没有线索化的话是为null,线索化后指向node3
//        Node right = node5.getRight(); // 没有出错的话就是指向root
//        System.out.println("10号节点的前驱节点是=" + left);
//        System.out.println("10号节点的后续节点是=" + right);
//        System.out.println("使用线索化的方式遍历 线索化二叉树");
//        tree.threadedList(); // 8 3 10 1 14 6
//    }
}


// 定义实现线索化功能的二叉树   中序线索化

// 难点就在当前结点的后继结点是父节点的，而父节点无法直接通过当前结点获得，所以只能返回上层递归把它作为上层结点的前驱结点或者左子节点



