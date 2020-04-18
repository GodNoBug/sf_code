package jg.tree.binary;

import java.util.Comparator;

/**
 * 红黑树
 * 1.也是一种自平衡的二叉搜索树.
 * <p>
 * 红黑树必须满足一下5条性质
 * 1.节点是RED或者BLACK
 * 2.根节点是BLACK
 * 3.叶子节点(外部节点,也是空节点)都是BLACK [红黑树让原来度为0或度为1的节点最终都为2的节点--添加null节点,null节点是配合红黑树性质假想出来的,使得红黑树称为真二叉树,每个节点度要么为2要么为0]
 * 4.RED节点的子节点都是BLACK => RED节点的parent都是BLACK => RED节点的父节点和子节点都是BLACK/从根节点到叶子节点的所有路径上不能有2个连续的RED节点
 * 5.从任一节点到叶子节点(null的叶子节点)的所有路径都包含相同数目的BLACK
 *
 * @param <E>
 */
public class RBTree<E> extends BBST<E> {
    public static final boolean RED = false;        // 红
    public static final boolean BLACK = true;       // 黑

    public RBTree() {
        this(null);
    }

    public RBTree(Comparator<E> comparator) {
        super(comparator);
    }

    // parent: 父节点
    // sibling: 兄弟节点
    // uncle: 叔父节点(parent的兄弟节点)
    // grand: 祖父节点(parent的父节点)

    // 添加

    // 已知
    // B树中,新元素必定是添加到也子结点中
    // 4阶B树所有节点的元素个数x都符合1≤x≤3
    // 建议新添加的节点默认为RED,这样能够让红黑树的性质尽快满足(性质1/2/3/5都满足,性质4不一定)
    // 如果添加的是根节点,染成BLACK即可

    // 添加的所有情况
    // 叶子节点所有的可能的情况: 红黑红 黑红 红黑 黑
    // 1. 红黑红  添加两边红的左右子节点   4种情况
    // 2. 黑红    黑的左边,红的左右两边    3种情况
    // 3. 红黑    红的左右两边,黑的右边    3种情况
    // 4. 黑      黑的两边               2种情况
    // 归纳
    // - 有4种情况满足红黑树的性质4:   添加的节点的父节点parent为BLACK,同样也满足4阶B树的性质,因此不用做任何额外处理
    // - 有8种情况不满足红黑树的性质4: 添加的节点的父节点parent为RED(Double Red),其中前4种属于B树节点上溢的情况[添加到红黑红左右孩子的左右]
    // - 总共有12总情况

    // 红黑树无平衡因子的概念,不能用AVL树来衡量平衡
    // 只要在添加和移除后维护好红黑树的五条性质就能保证平衡

    // 根据以上违背性质4的情况进行修复

    // 一、叔父节点是红色
    // 修复性质4-LL\RR (插入导致双红情况)
    //      判定条件: uncle 不是 RED
    //      1. parent染成BLACK, grand 染成 RED
    //      2. grand进行单旋操作
    //         - LL : 右旋转
    //         - RR : 左旋转
    // 修复性质4-LR\RL (插入导致双红情况)
    //      判定条件: uncle 不是 RED
    //      1.把自己染成BLACK, grand 染成 RED
    //      2.进行双旋操作
    //       - LR: parent 左旋转,grand 右旋转
    //       - RL: parent 右旋转,grand 左旋转
    //[站在B树的角度,往里加入新节点,必然要成为B树的一个节点,应该要让中间变为黑色,两边变为红色.因为BLACK节点与它的RED子节点融合在一起,形成1个B树节点]


    // 二、叔父节点是红色
    // 修复性质4-上溢-LL/RR/RL/LR
    //      判定条件: uncle 是 RED
    //      1.parent、uncle 染成 BLACK
    //      2.grand向上合并
    //       - 染成RED,当做是新添加的节点进行处理
    //       - grand向上合并时可能继续发生上溢
    //       - 若上溢持续到根节点,只需将根节点染成BLACK (包含递归)



    /*------------------------------------------添加节点后调整----------------------------------------------------------*/

    /**
     * @param node 新添加结点
     */
    @Override
    protected void afterAdd(Node<E> node) {
        Node<E> parent = node.parent;
        if (parent == null) {         // 添加的是根节点,或者上溢到了根节点,将新添加的node染黑
            black(node);
            return;
        }
        if (isBlack(parent)) return;  // parent为BLACK不用做任何添加的后续处理

        // 能到达这里的parent为RED(Double Red)
        Node<E> uncle = parent.sibling();  // 叔父(uncle)节点
        Node<E> grand = parent.parent;     // 祖父(grand)节点,可以先染红,grand替代下面的red(grand)

        // 判断叔父节点是否为红色
        if (isRed(uncle)) {  // 叔父节点是红色属于上溢情况,parent和uncle染成黑色,grand染成红色作为新添加的节点并向上合并
            black(parent);
            black(uncle);
            // 祖父节点当做是新添加的节点
            afterAdd(red(grand));
            return;
        }
        // 能来到这里,叔父节点不是红色
        if (parent.isLeftChild()) {  // L
            red(grand);
            if (node.isLeftChild()) { // LL: parent染成BLACK,grand染成RED,grand右旋
                black(parent);
            } else {  // LR: 自己染成BLACK,grand染成RED,进行双旋操作:parent左旋转,grand右旋转
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else { //R
            red(grand);
            if (node.isLeftChild()) { // RL: 自己染成BLACK,grand染成RED,进行双旋操作:parent右旋转,grand左旋转
                black(node);
                rotateRight(parent);
            } else {  // RR: parent染成BLACK,grand染成RED,grand左旋
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    /*------------------------------------------删除节点后调整----------------------------------------------------------*/

    // B树中,最后真正被删除的元素都在叶子节点中
    // 删除的是
    // 1.RED节点,直接删除,不影响红黑树的性质,不用作任何调整
    // 2.BLACK节点,有3中情况
    //  - 删除拥有两个RED子节点的BLACK节点: 不可能被直接删除,因为会找它的子节点替代删除,因此不用考虑这种情况(前驱后继)
    //  - 删除拥有一个RED子节点的BLACK节点:
    //    -- 判定条件: 用以替代的子节点为RED
    //    -- 将替代的子节点染成BLACK即可保持红黑树性质
    //  -删除BLACK叶节子点
    //
    @Override
    protected void afterRemove(Node<E> node) {
        // 如果删除的节点的是红色 或者 用以取代删除节点 的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }
        Node<E> parent = node.parent;
        // 删除的是根节点
        if (parent == null) return;
        // 删除的是黑色的叶子节点,在B树中意味着"下溢"
        // 判断被删除的node是左还是右
        boolean left = parent.left == null||node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;
        if (left) {  // 被删除的节点在左边,兄弟节点在右边
            if (isRed(sibling)) {  // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }
            // 兄弟节点必然是黑色
            if (isBlack(sibling.right) && isBlack(sibling.left)) {
                // 兄弟节点没有一个红色子节点,父节点要向下更兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有一个红色子节点,向兄弟节点借元素
                // 兄弟节点的左边是黑色,兄弟要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling=parent.right;
                }
                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边,兄弟节点在左边
            if (isRed(sibling)) {  // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }
            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有一个红色子节点,父节点要向下更兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有一个红色子节点,向兄弟节点借元素

                // 兄弟节点的左边是黑色,兄弟要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling=parent.left;
                }
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }

    }
/*    @Override
    protected void afterRemove(Node<E> node, Node<E> replacement) {
        // 如果删除的节点的是红色
        if (isRed(node)) return;
        // 用以取代node的子节点是红色
        if (isRed(replacement)) {
            black(replacement);
            return;
        }
        Node<E> parent = node.parent;
        // 删除的是根节点
        if (parent == null) return;
        // 删除的是黑色的叶子节点,在B树中意味着"下溢"
        // 判断被删除的node是左还是右
        boolean left = parent.left == null||node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;
        if (left) {  // 被删除的节点在左边,兄弟节点在右边
            if (isRed(sibling)) {  // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }
            // 兄弟节点必然是黑色
            if (isBlack(sibling.right) && isBlack(sibling.left)) {
                // 兄弟节点没有一个红色子节点,父节点要向下更兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent, null);
                }
            } else { // 兄弟节点至少有一个红色子节点,向兄弟节点借元素
                // 兄弟节点的左边是黑色,兄弟要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling=parent.right;
                }
                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边,兄弟节点在左边
            if (isRed(sibling)) {  // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }
            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有一个红色子节点,父节点要向下更兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent, null);
                }
            } else { // 兄弟节点至少有一个红色子节点,向兄弟节点借元素

                // 兄弟节点的左边是黑色,兄弟要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling=parent.left;
                }
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }

    }*/



    /*------------------------------------------辅助函数---------------------------------------------------------------*/

    // 染红
    private Node<E> red(Node<E> node) {
        return color(node, RED);
    }

    // 染黑
    private Node<E> black(Node<E> node) {
        return color(node, BLACK);
    }

    // 染色,并返回对应节点
    private Node<E> color(Node<E> node, boolean color) {
        if (node == null) return null;
        ((RBNode<E>) node).color = color;
        return node;
    }

    // 是黑色
    private boolean isBlack(Node<E> node) {
        return colorOf(node) == BLACK;
    }

    // 是红色
    private boolean isRed(Node<E> node) {
        return colorOf(node) == RED;
    }

    // 判断每个节点的颜色
    private boolean colorOf(Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>) node).color;
    }

    /*------------------------------------------节点类-----------------------------------------------------------------*/
    // 节点
    private static class RBNode<E> extends Node<E> {
        boolean color = RED;    // 颜色


        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if (color == RED) {
                str = "R_";
            }
            return str + element.toString();
        }
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode(element, parent);
    }
}
