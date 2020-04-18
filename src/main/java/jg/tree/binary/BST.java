package jg.tree.binary;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉搜索树(BinarySearchTree)
 * 1.是二叉树的一种,是应用非常广泛的一种二叉树,英文简称 BST(又被称为: 二叉初中生、二叉排序树)
 * - 任意节点的值都大于其左子树所有节点的值
 * - 任意节点的值都小于其右子树所有节点的值
 * - 它的左右子树也是一颗二叉搜索树
 * 2.二叉搜索树可以大大提高搜索数据的效率
 * 3.二叉搜索树存储的元素必须由可比较性: 如int、double等,如果是自定义类型,需要指定比较方式,不允许为null
 * <p>
 * 时间复杂度和树的高度有关 O(h) = O(logn) ,最坏的情况下是形成单链条,复杂度为 O(n)
 *
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class BST<E> extends BinaryTree<E> {

    private Comparator<E> comparator; // 比较器

    public BST() {
        this(null);
    }

    public BST(Comparator<E> comparator) {
        this.comparator = comparator;
    }


    /**
     * 添加元素
     * 情况1: 添加的是第一个结点 :  root = new Node<>(element, null);
     * 情况2: 添加的不是第一个结点
     * 一开始拿到的只有根节点,所以要顺着根节点找,找到合适的位置: 不断的拿结点和element比较,直到找到叶子节点为止
     * <p>
     * 添加步骤:
     * 1.找到要插入的节点的父节点 parent
     * 2.创建新结点node
     * 3.parent.left = node 或者 parent.right = node
     * 问:遇到值相等的元素如何处理? 覆盖或插入失败或直接返回不做任何操作?
     * - 此代码用覆盖
     *
     * @param element 元素
     */
    public void add(E element) {
        elementNotNullCheck(element);   // 判空,不能插入空元素

        // 情况1: 添加的是第一个结点
        if (root == null) {
            root = createNode(element, null);
            size++;
            afterAdd(root);
            return;
        }
        // 情况2: 添加的不是第一个结点
        Node<E> parent = root;  // 当前节点的父节点
        Node<E> node = root;    // 当前结点,从root节点开始找,于是设当前节点node=root
        int cmp = 0;            // 当前节点和要插入节点的比较

        // while用来确定添加的位置
        while (node != null) {
            cmp = compare(element, node.element); // 拿插入的元素和当前节点的元素比较
            parent = node;          // 在"向左"或"向右"之前,保存当前节点,作为下一次循环的当前节点的父节点.[待思考,语言描述要准确]
            if (cmp > 0) {          // 要传入的元素比当前节点大,应当要和当前节点的右子节点比较,如此循环下去,直到当前节点为空为止.
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;   // 要传入的元素比当前节点小,应当要和当前节点的左子节点比较,如此循环下去,直到当前节点为空为止.
            } else {
                node.element = element; // 如果相等值覆盖,有意义,因为考虑到自定义对象
                return;
            }
        }
        // 判断要插入到父节点的哪个位置,左还是右
        Node<E> newNode = createNode(element, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
        // 新添加结点之后的处理
        afterAdd(newNode);
    }


    /**
     * 添加node之后的相关调整
     * 默认什么都不做,子类实现定制自己的调整方案
     *
     * @param node 新添加结点
     */
    protected void afterAdd(Node<E> node) {

    }

    /*********************************************删除节点**************************************************************/

    /**
     * node为要删除的节点,删除得分情况讨论
     * <p>
     * <p>
     * 1. 删除叶子节点(直接删除,非常好删)
     * - node==node.parent.left 要删除的节点
     * - node.parent.left = null
     * <p>
     * - node==node.parent.right
     * - node.parent.right=null
     * <p>
     * - node.parent==null
     * - root = null
     * <p>
     * 2. 删除度为1的节点 (也简单)
     * 用当前要删除的节点的子节点替代原节点的位置
     * child 是node.left 或者 child 是node.right
     * 用child 替代 node的位置
     * - 如果node是左子节点,child.parent=node.parent; node.parent.left = child;
     * - 如果node是右子节点,child.parent=node.parent; node.parent.right= child;
     * - 删除node是根节点, child.parent=null; root = child;
     * <p>
     * 3. 删除度为2的节点 [真正删除的是取代要删除节点的前驱或后续]
     * 先用前驱或者后续节点的值[覆盖]原节点的值
     * 然后删除相应的前驱或者后续节点
     * [规律:如果一个节点的度为2,那么他的前驱/后续节点的度只可能是1和0(最右或者最左嘛,不可能度为2)] ↑
     *
     * @param element 对外界来说是元素,而对内来说是Node
     */
    public void remove(E element) {
        remove(node(element));
    }

    // 删除元素代表的节点
    private void remove(Node<E> node) {
        if (node == null) return; // 节点不存在
        size--;
        // 优先解决度为2的节点的情况
        if (node.hasTwoChildren()) {
            // 这里选择找到后继节点
            Node<E> s = successor(node);
            // 用后继节点的值覆盖度为2节点的值
            node.element = s.element;
            // 删除后继节点[node=s,node指向了s由后面的负责度为1和0的情况统一删除]
            node = s;
        }
        // 负责处理度为1的节点 代码执行到这里必然度为1或者0 要么左子节点要么右子节点
        Node<E> child = node.left != null ? node.left : node.right;
        if (child != null) { // 证明node是度为1的节点
            child.parent = node.parent;    // 更改parent
            if (node.parent == null) {
                root = child;
            } else if (node == node.parent.left) {
                node.parent.left = child;
            } else { // node == node.parent.right
                node.parent.right = child;
            }
            // 删除节点后的处理
            afterRemove(child);
        } else if (node.parent == null) { //证明node度为0,叶子节点并且是根节点
            root = null;
            // 删除节点后的处理
            afterRemove(node);
        } else { //证明node度为0,叶子节点,但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else {  //node==node.parent.right
                node.parent.right = null;
            }
            // 删除节点后的处理
            afterRemove(node);
        }
    }

    /**
     * 删除node之后的调整
     *
     * @param node 被删除的节点,或者用以取代被删除节点的子节点
     */
    protected void afterRemove(Node<E> node) {

    }

    // 是否包含某元素
    public boolean contains(E element) {
        return node(element) != null;
    }

    // 根据元素用来获取节点
    protected Node<E> node(E element) {
        Node<E> node = root; // 从根节点往下找
        while (node != null) {
            int cmp = compare(element, node.element);
            if (cmp == 0) { // 找到了
                return node;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return null;
    }


    /**
     * @return 返回值=0代表e1>e2  >0代表e1>e2    <0代表e1<e2
     */
    private int compare(E e1, E e2) {
        if (comparator != null) {
            return comparator.compare(e1, e2);
        }
        return ((Comparable<E>) e1).compareTo(e2); // 不写死,让不实现Comparable接口的对象也能比较.仅在comparator为空的时候强转为Comparable类型来比较
    }

    private void elementNotNullCheck(E element) {
        if (element == null) {
            throw new IllegalArgumentException("element must be null");
        }
    }


    /***********************************************树的遍历************************************************************/
    public void preOrder() {
        preOrder(root);
    }

    public void preOrder2() {
        preOrder2(root);
    }


    public void inOrder() {
        inOrder(root);
    }

    public void inOrder2() {
        inOrder2(root);
    }

    public void postOrder() {
        postOrder(root);
    }

    public void postOrder2() {
        postOrder2(root);
    }

    // 前序遍历方式1
    // 当前子树的根节点,前序遍历左子树,前序遍历右子树
    // 传入节点,告诉方法从哪个节点进行前序遍历
    private void preOrder(Node<E> node) {
        if (node == null) { // 不断递归的时候,总有一天node会空
            return;
        }
        System.out.println(node.element);
        preOrder(node.left);    //传入节点的左子节点,又从它开始进行前序遍历
        preOrder(node.right);   //传入节点的左子节点,又从它开始进行前序遍历
    }

    // 前序遍历方式2
    private void preOrder2(Node<E> node) {
        System.out.println(node.element);
        if (node.left != null) {
            preOrder2(node.left);
        }
        if (node.right != null) {
            preOrder2(node.right);
        }
    }

    // 中序遍历方式1
    // 中序遍历左子树,根节点,中序遍历右子树, 中序遍历访问二叉搜索树是从小到大的顺序,可以调整顺序至逆序
    // 搜索树的中序遍历数字是升序或者降序的
    private void inOrder(Node<E> node) {
        if (node == null) { // 不断递归的时候,总有一天node会空
            return;
        }
        inOrder(node.left);    //传入节点的左子节点,又从它开始进行中序遍历
        System.out.println(node.element);
        inOrder(node.right);   //传入节点的左子节点,又从它开始进行中序遍历
    }

    // 中序遍历方式2
    private void inOrder2(Node<E> node) {
        if (node.left != null) {
            inOrder2(node.left);
        }
        System.out.println(node.element);
        if (node.right != null) {
            inOrder2(node.right);
        }
    }


    // 后序遍历方式1
    // 后序遍历左子树,后序遍历右子树,根节点
    private void postOrder(Node<E> node) {
        if (node == null) { // 不断递归的时候,总有一天node会空
            return;
        }
        postOrder(node.left);    //传入节点的左子节点,又从它开始进行后序遍历
        postOrder(node.right);   //传入节点的左子节点,又从它开始进行后序遍历
        System.out.println(node.element);
    }

    // 后序遍历方式2
    private void postOrder2(Node<E> node) {
        if (node.left != null) {
            postOrder2(node.left);    //传入节点的左子节点,又从它开始进行后序遍历
        }
        if (node.right != null) {
            postOrder2(node.right);   //传入节点的左子节点,又从它开始进行后序遍历
        }
        System.out.println(node.element);
    }


    // 层序
    // 从上到下,从左至右访问每个节点
    // 思路: 队列,先进先出,前进入队列的节点先出来,按一下顺序入队,可以实现层序遍历
    // 1.将根节点入队
    // 2. 循环执行以下操作,直到队列为空
    //   1.将队列头节点A出队,进行访问 ↓
    //   2.将A的左子节点入队
    //   3.将A的右子节点入队  ↑
    // 特点: 访问当前节点,而后优先访问它的左右节点
    public void levelOrder() {
        if (root == null) return;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            System.out.println(node.element);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    /**********************************************翻转二叉树*************************************************************/

    public void invertTree() {
        invertTree(root);
    }

    public void invertTree2() {
        invertTree2(root);
    }

    public void invertTree3() {
        invertTree3(root);
    }

    public void invertTree4() {
        invertTree4(root);
    }

    /**
     * 翻转二叉树: 将所有结点的左右子树交换,前序和中序遍历都差不多
     * 前序式翻转
     *
     * @param node 当前节点
     * @return node
     */
    private Node<E> invertTree(Node<E> node) {
        if (node == null) return null;
        Node<E> tmp = root.left;
        root.left = root.right;
        root.right = tmp;

        invertTree(node.left);
        invertTree(node.right);
        return node;
    }

    // 后序式翻转
    private Node<E> invertTree2(Node<E> node) {
        if (node == null) return null;

        invertTree2(node.left);
        invertTree2(node.right);

        Node<E> tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        return node;
    }

    // 中序式翻转
    // 不加思考写代码,中序遍历会有问题
    private Node<E> invertTree3(Node<E> node) {
        if (node == null) return null;
        invertTree3(node.left);
        Node<E> tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        invertTree3(node.left); // 因为左右交换,如果node.right就是之前的left.所以node.left
        return node;
    }

    // 层序遍历翻转
    private Node<E> invertTree4(Node<E> root) {
        if (root == null) return null;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            // 每取出一个节点,其子树左右交换
            Node<E> tmp = node.left;
            node.left = node.right;
            node.right = tmp;

            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return root;
    }
    /*****************************重构二叉树********************************/
    // 前序遍历: 4 2 1 3 6 5 最左边是根节点
    // 中序遍历: 1 2 3 4 5 6
    // ↓

    // [4] 2 1 3 6 5 确定根节点
    // 1 2 3 [4] 5 6  "[]"左右是左子树和右子树
    // ↓

    // [4] [2 1 3] [6 5]   确定根节点
    // 1 [2] 3 [4] 5 [6]
    // ...


    // 以下结果可以保证重构出唯一的一颗二叉树
    // 前序遍历+中序遍历
    // 后序遍历+中序遍历

    // 前序遍历+后序遍历
    //  如果它是一颗真二叉树,结果是唯一的
    //  否则结果不唯一,因为左右子树不好分,不像中序遍历
    /*****************************概念********************************/
    // 前驱节点: 中序遍历时的前一个节点


    /************************************************toString递归*****************************************************/
    // 写的toString牵扯到递归与前序遍历相关
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(root, sb, "");
        return sb.toString();
    }

    // 前序遍历,树状结构显示
    // 中序遍历,二叉搜索树的中序遍历按升序或者降序处理节点
    // 后续遍历,使用于一些先子后父的操作
    // 层序遍历,计算二叉树的高度,判断一棵树是否为完全二叉树
    private void toString(Node<E> node, StringBuilder sb, String prefix) {
        if (node == null) return;
        sb.append(prefix).append(node.element).append("\n");
        toString(node.left, sb, prefix + "L---");
        toString(node.right, sb, prefix + "R---");
    }

}

// 二叉树的特点
//  1.每个结点的度最大为2(最多拥有2课子树)
//  2.左子树和右子树是由顺序的(有序树)
//  3.即使某结点只有一颗子树,也要区分左右子树

//  a.非空二叉树的第i层,最多有2^(i-1)个结点(i≥1)
//  b.在高度为h的二叉树上最多有2^h -1 个结点(h≥1)
//  c.对于任何一颗非空二叉树,如果叶子结点的个数为n0,度为2的结点个数为n2,则有n0=n2+1
//    - 假设度为1的结点个数为n1,那么二叉树的结点总数n = n0 + n1 + n2 (度为0,1,2三种情况的数量总和)
//    - 二叉树的边树 T = n1 + 2 * n2 (度为1的边数和度为2的边数) = n-1 (边=总结点数-1 : 唯独根节点没有边) = n0 + n1 + n2 -1
//    - 推出 n1 + 2*n2 = n0 + n1 + n2 -1 => n0=n2+1

// 真二叉树: 所有节点的度都要么为0,要么为2

// 满二叉树:
//  1.所有节点的度都要么为0,要么为2,且所有的叶子节点都在最后一层
//  2.在同样高度的二叉树中,满二叉树的叶子节点数量最多、总结点数量最多
//  3.满二叉树一定是真二叉树,真二叉树不一定是满二叉树
//  4.假设满二叉树的高度为h(h≥1),那么
//    - 第i层的节点数量: 2^(i-1)
//    - 叶子节点数量:   2^(h-1)
//    - 总结点数量:    n = (2^h)-1   ↓
//    - 树高度:        h = log2(n+1)

// 完全二叉树:
//    1.叶子节点只会出现最后2层,且最后1层的叶子结点都靠左对齐
//    2.从上往下,从左往右顺序,对应其满二叉树的顺序
//    3.完全二叉树从根节点至倒数第2层是一颗满二叉树
//    4.满二叉树因是完全二叉树,完全二叉树不一定是满二叉树
//  性质
//    1.度为1的节点只有左子树            [最后一个节点]
//    2.度为1的节点要么是1个,要么是0个
//    3.同样节点数量的二叉树,完全二叉树的高度最小
//    4.假设完全二叉树的高度为h(h≥1),那么
//      - 至少有2^(h-1)个节点 [取出最后一层,剩下一个节点,一个节点上面的二叉树是满二叉树]
//      - 最多有2^h -1 个节点 [最后一层满了,成为满二叉树]
//    5.总结点数量为n
//      - 则有h=floor(log2(n))+1   [2^(h-1)≤n<2^h =>  h-1≤log2(n)<h (h应该是整数) => h = log2(n) 向下取整 +1]
//    6.一颗有n个节点的完全二叉树(n>0),从上到下、从左至右对节点从1开始进行编号,对任意第i个结点
//       - 如果i=1,它是根节点
//       - 如果i>1,它的节点的编号为floor(i/2)
//       - 如果2i≤n,它的左子节点编号为2i
//       - 如果2i>n,它无左子节点
//       - 如果2i+1≤n,它的右子节点编号为2i+1
//       - 如果2i+1>n,它无有子结点
//    7.一颗有n个节点的完全二叉树(n>0),从上到下、从左至右对节点从0开始进行编号,对任意第i个结点
//       - 如果i=0,它是根节点
//       - 如果i>0,它的节点的编号为floor((i-1)/2)
//       - 如果2i+1≤n-1,它的左子节点编号为2i+1
//       - 如果2i+1>n-1,它无左子节点
//       - 如果2i+2≤n-1,它的右子节点编号为2i+2
//       - 如果2i+2>n-1,它无右子结点
//    8.总结点数量n,则有
//      - n如果是偶数,叶子节点数量n0 = n/2
//      - n如果是奇数,叶子节点数量n0 = (n+1)/2
//      - 代码公式: n0 =floor((n+1)/2)= (n+1)>>1
//      - 代码公式: n0 =ceiling(n/2)
//      - 非叶子节点数量 n1+n2 = ceiling(n/2) =floor((n+1)/2)

// TODO 面试题 : 如果一颗完全二叉树有768个节点,求叶子节点的个数
//   假设叶子节点个数为n0,度为1的节点个数为n1,度为2的节点个数为n2
//   ∵ 对于二叉树总结点个数n = n0 + n1 + n2 而且 n0 = n2 + 1
//   ∴ n =2n0 + n1 - 1
//   又∵ 完全二叉树中度为1的结点个数要么为0要么为1
//    当n1为1时,n=2n0,n必然是偶数
//     ∴ 叶子结点个数 n0 = n/2, 非叶子结点个数n1+n2 = n/2
//    当n1为0时,n=2n0-1,n必然是奇数
//      ∴ 叶子节点的个数n0=(n+1)/2,非叶子节点个数 n1 +n2 =(n-1)/2
//      ∴ 叶子节点个数为384




