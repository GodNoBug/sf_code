package jg.tree.binary;


import java.util.Comparator;


/**
 * 平衡二叉搜索树(Balanced Binary Search Tree)
 * - AVL树
 * - 红黑树
 * <p>
 * 1.平衡: 当节点数量固定时,左右子树的高度越接近,这颗二叉树就越平衡(高度越低) [满二叉树和完全二叉树这样是理想的平衡,高度是最小的]
 * 2.改进二叉搜索树:
 * - 首先,节点的添加、删除顺序是无法限制的,可以认为是随机的
 * - 所以改进方案是:在节点的添加、删除操作之后,想办法让二叉搜索树恢复平衡(减小树的高度)
 * - 如果接着继续调整节点的位置,完全可以达到理想平衡,但是付出的代价可能会比较大
 * -- 比如调整的次数会比较多,反而增加了时间复杂度
 * - 总结来说,比较合理的改进方案是: 用尽量少的调整次数达到适度平衡即可.
 * -
 * 3.平衡因子: 某结点的左右子树的高度差
 * <p>
 * AVL树的特点:
 * 1.每个结点的平衡因子只可能是1、0、-1(绝对值≤1,如果超过1,称之为"失衡")
 * 2.每个结点的左右子树高度差不超过1
 * 3.搜索、添加、删除的时间复杂度为O(logn)
 *
 * 重点在于对高度和旋转的理解
 *
 * TODO
 *  添加: 可能会导致所有祖先结点都失衡,但只要让高度最低的失衡结点恢复平衡,这棵树就恢复平衡[仅需O(1)次调整]
 *  删除: 只可能会导致父节点或者祖先结点失衡(总之只有1个节点失衡), 让父节点或者祖先结点恢复平衡后,可能会导致更高层的祖先结点失衡[最多需要O(logn)次调整]
 *  平均时间复杂度:
 *  1.搜索:O(logn)
 *  2.添加:O(logn),仅需O(1)次的旋转操作
 *  3.删除:O(logn),最多需要O(logn)次的旋转操作
 * @param <E>
 */
public class AVLTree<E> extends BBST<E> {

    public AVLTree() {
        this(null);
    }

    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }


    // 失衡
    // 哪些会失衡哪些不会?
    // 最坏的情况: 插入的结点,可能会导致其所有祖先结点都失衡
    // 其父节点、非祖先结点,都不可能失衡 [重点]
    // 不会出现那种,底下不失衡,顶上失衡.一般是底下失衡,导致顶上一连串的失衡

    // 解决失衡

    //     最小失衡子树：在新插入的结点向上查找，以第一个平衡因子的绝对值超过 1 的结点为根的子树称为最小不平衡子树。
    // 也就是说，一棵失衡的树，是有可能有多棵子树同时失衡的。而这个时候，我们只要调整最小的不平衡子树，就能够将不平衡
    // 的树调整为平衡的树。
    //     平衡二叉树的失衡调整主要是通过旋转最小失衡子树来实现的。根据旋转的方向有两种处理方式，左旋 与 右旋 。
    //     旋转的目的就是减少高度，通过降低整棵树的高度来平衡。哪边的树高，就把那边的子树向上旋转。

    /*-------------------------------------------------一般法----------------------------------------------------------*/
    // 一般法!!!
    //  n(node) p(parent) g(grandparent) [node是其子树要被插入的结点]
    //         g              g                   g                         g
    //        / \            / \                 / \                       / \
    //       p   T3        T0   p               p   T3                    T0  p
    //      / \                / \             / \                           / \
    //     n   T2             T1  n          T0   n                         n  T3
    //    / \                    / \             / \                       / \
    //   T0 T1                  T2 T3           T1 T2                     T1 T2
    // LL-右旋转(单旋)      RR-左旋转(单旋)    LR-RR左旋转,LL旋转(双旋)  RL-LL右旋转,RR左旋转(双旋)

    //  LL-右旋转(单旋)
    //  失衡点的左边的左边的子树加入节点导致失衡  left-left

    //  1.g.left = p.right (失衡节点的左孩子替代此节点位置)
    //  2.p.right = g      (失衡节点的左孩子的右子树变为节点的左子树)
    //  3.让p成为这颗子树的根节点 (将此节点作为左孩子节点的右子树)
    //  变换后,仍然是一颗二叉搜索树: T0<n<T1<p<T2<g<T3
    //  整颗树都达到平衡,以失衡结点为根的整颗子树的高度是没有发生变化的,因此不会导致祖父结点失衡
    //  [还需要注意维护的内容,T2、p、g的parent属性,先后更新g、p的高度属性]


    // RR - 左旋转(单旋)
    // 失衡点的右边的右边的子树加入节点导致失衡  right-right

    //  1.g.right = p.left (失衡节点的右孩子替代此节点位置)
    //  2.p.left = g       (失衡节点的右孩子的左子树变为该节点的右子树)
    //  3.让p成为这颗子树的根节点 (将此节点作为左孩子节点的右子树)
    //  变换后,仍然是一颗二叉搜索树: T0<n<T1<p<T2<g<T3
    //  整颗树都达到平衡,以失衡结点为根的整颗子树的高度是没有发生变化的,因此不会导致祖父结点失衡
    //  [还需要注意维护的内容,T1、p、g的parent属性,先后更新g、p的高度属性]

    // LR:
    // 失衡点的左边的右边的子树加入节点导致失衡  left-right
    // 1.先对p进行左旋转 RR
    // 2.再对g进行右旋转 LL
    // 旋法总结: LR=> RR LL

    // RL
    // 失衡点的右边的左边的子树加入节点导致失衡  right-left
    // 1.先对p进行右旋转 LL
    // 2.再对g进行左旋转 RR
    // 旋法总结: RL=> LL RR

    // 旋转维护平衡在添加之后,删除之后
    // 添加和删除节点后也得更新失衡后的高度[不失衡不必更新],没必要设计使用递归非递归方法再设计出获取高度的方法,复杂度会增大.


    /*-----------------------------------------------统一处理法----------------------------------------------------------*/
    // 一般处理法是要经过判断再决定是左旋转还是右旋转
    // 不必在代码判断是否是LL,RR,LR,RL.可以用这一种方法统一处理所有情况.
    // 对失衡点为根的子树做编号,根据二叉搜索树的性质,从左到右,从小到大进行编号a,b,c,d,e,f,g "[]"代表子树
    // 通过以下变化的最终结果可知,都是一样的格式

    // LL
    //               f                               d
    //             /  \                            /    \
    //            d   [g]                         b      f
    //           / \                             /  \   /  \
    //          b  [e]                         [a]  [c] [e] [g]
    //         / \
    //        [a][c]

    // RR
    //               b                                 d
    //             /  \                               /  \
    //           [a]   d                             b    f
    //                / \                          /  \   /  \
    //              [c]  f                       [a]  [c] [e] [g]
    //                  / \
    //                 [e][g]

    // LR
    //               f                                 d
    //             /  \                               /  \
    //            b   [g]                            b    f
    //           / \                                / \   /  \
    //         [a]  d                             [a] [c] [e] [g]
    //             / \
    //            [c][e]


    // RL
    //               b                                 d
    //             /  \                               /  \
    //           [a]   f                             b    f
    //                / \                          /  \   /  \
    //               d   [g]                     [a]  [c] [e] [g]
    //              / \
    //             [c][e]

    // 1.让d成为子树的根节点
    // 2.让a成为b的left,让c成为b的right,ac可能为空
    // 3.让e成为f的left,让g成为f的right,eg可能为空
    // 4.让b成为d的left,让f成为d的right
    // 维护parent和height


    /**
     * 使用parent属性一直往上找,找到相关失衡结点
     * 新增节点,是从0开始增的,也可以想到,在新增的时候去考虑维护节点的高度
     * @param node 新添加结点
     */
    @Override
    protected void afterAdd(Node<E> node) {
        // 新增节点往上找,判断是否平衡,如果平衡更新高度,如果不平衡则恢复平衡[内部有更新高度的]
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                // 更新高度,
                updateHeight(node);
            } else {
                // 恢复平衡,能进来的必然不平衡,高度最低的不平衡结点g
                reBalance(node);
                // 这棵树恢复了平衡,退出
                break;
            }
        }
    }

    /**
     * 恢复平衡
     * @param g 高度最大的那个不平衡结点
     */
    private void reBalance2(Node<E> g) {
        // 备齐 g p n 三个元素
        Node<E> p = ((AVLNode<E>) g).tallerChild(); // p高度是g左右子树中高度比较高的
        Node<E> n = ((AVLNode<E>) p).tallerChild(); // n高度是p左右子树高度比较高的

        // 根据LL/RR/RL/LR三种情况区分
        if (p.isLeftChild()) {
            if (n.isLeftChild()) {  // LL
                rotateRight(g);
            } else {   //LR
                rotateLeft(p);
                rotateRight(g);
            }
        } else { // R
            if (n.isLeftChild()) {  // RL
                rotateRight(p);
                rotateLeft(g);
            } else {   //RR
                rotateLeft(g);
            }
        }
    }


    private void reBalance(Node<E> g) {
        Node<E> p = ((AVLNode<E>) g).tallerChild();
        Node<E> n = ((AVLNode<E>) p).tallerChild();
        if (p.isLeftChild()) {
            if (n.isLeftChild()) {  // LL
                rotate(n.left, n, n.right, p, p.right, g, g.right, g);
            } else {   //LR
                rotate(p.left, p, n.left, n, n.right, g, g.right, g);
            }
        } else { // R
            if (n.isLeftChild()) {  // RL
                rotate(g.left, g, n.left, n, n.right, p, p.right, g);
            } else {   //RR
                rotate(g.left, g, p.left, p, n.left, n, n.right, g);
            }
        }
    }


    // 删除子树中的结点,可能会导致父节点或祖先结点失衡(只有1个结点会失衡),其他结点,都不可能失衡
    // 判断节点是否平衡
    private boolean isBalanced(Node<E> node) { // 左右子树高度差绝对值≤1
        return Math.abs(((AVLNode<E>) node).balanceFactor()) <= 1;
    }

    private void updateHeight(Node<E> node) {
        ((AVLNode<E>) node).updateHeight();
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new AVLNode<>(element, parent);
    }

    private static class AVLNode<E> extends Node<E> {
        // 结点的深度也是从根结点开始数，是第几层也决定于根结点在第0层还是第1层。
        // 结点的高度则取决于它的子树，该节点子树中最远的那个叶子结点作为1开始数。
        int height = 1; // 维护了高度,默认为1

        public AVLNode(E element, Node<E> parent) {
            super(element, parent);
        }

        // 获取平衡因子
        public int balanceFactor() { // 左子树的高度减去右子树的高度
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            return leftHeight - rightHeight;
        }

        // 更新高度: 左右子树高度最大的值+1
        // 规定: TODO 不同教材有不同规定,待比较
        // 结点的深度也是从根结点开始数，是第几层也决定于根结点在第0层还是第1层。
        // 结点的高度则取决于它的子树，该节点子树中最远的那个叶子结点作为1开始数。
        public void updateHeight() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            height = 1 + Math.max(leftHeight, rightHeight);
        }


        // 高度比较高的子节点
        public Node<E> tallerChild() {
            int leftHeight = left == null ? 0 : ((AVLNode<E>) left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>) right).height;
            if (leftHeight > rightHeight) return left;
            if (leftHeight < rightHeight) return right;
            // 高度一样话.如果是父节点的左子树,返回left子节点,否则返回右
            return isLeftChild() ? left : right; // 高度一样话,返回同方向的[如节点是父节点的左子节点,返回左的]
        }

        @Override
        public String toString() {
            String parentString = "null";
            if (parent != null) {
                parentString = parent.element.toString();
            }
            return element + "_h(" + height + ")";
        }
    }


    @Override
    protected void rotate(Node<E> a, Node<E> b, Node<E> c, Node<E> d, Node<E> e, Node<E> f, Node<E> g, Node<E> r) {
        super.rotate(a, b, c, d, e, f, g, r);
        // 更新高度,因为AVL特有的,红黑树没有高度的考虑
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
    }

    @Override
    protected void afterRotate(Node<E> g, Node<E> p, Node<E> child) {
        super.afterRotate(g, p, child);
        // 更新高度
        updateHeight(g);
        updateHeight(p);
    }

    /**
     * 删除节点,可能会导致父节点或者祖先结点失衡(总之只有1个节点会失衡),其他结点,都不可能失衡
     *
     * @param node 被删除的节点
     */
    @Override
    protected void afterRemove(Node<E> node) {
        while ((node = node.parent) != null) {
            if (isBalanced(node)) {
                // 更新高度
                updateHeight(node);
            } else {
                // 恢复平衡,能进来的必然不平衡,高度最低的不平衡结点
                // 往上不断的平衡,直到所有祖父节点平衡位置
                reBalance(node);
            }
        }
    }
}