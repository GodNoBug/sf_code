package jg.tree.map;



import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Map所有key组合在一起,其实就是一个Set
 * 添加/删除/搜索 O(logn)
 * 1.Key必须具备可比较性
 * 2.元素的分布是有顺序的
 * 在实际应用中,很多时候的需求
 * - Map中存储的元素不需要讲究顺序
 * - Map中的key不需要具备可比较性
 * - 不考虑顺序,不考虑Key的可比较性,Map有更好的实现方案,平均时间复杂度可以达到O(1)
 * - 那就是采用哈希表来实现Map
 * @param <K>
 * @param <V>
 */
@SuppressWarnings({"unchecked","unused"})
public class TreeMap<K, V> implements Map<K, V> {
    public static final boolean RED = false;        // 红
    public static final boolean BLACK = true;       // 黑
    private int size;                               // 树大小
    private Node<K, V> root;                        // 根节点


    private Comparator<K> comparator;               // 比较器

    public TreeMap() {
        this(null);
    }

    public TreeMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    // 元素数量
    public int size() {
        return size;
    }

    // 是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 清空所有元素
    public void clear() {
        root = null;
        size = 0;
    }


    /**
     * 要求key有比较性
     *
     * @param key
     * @param value
     * @return 返回被覆盖的key
     */
    @Override
    public V put(K key, V value) {
        elementNotNullCheck(key);   // key不能为空

        // 情况1: 添加的是第一个结点
        if (root == null) {
            root = new Node<>(key, value, null);
            size++;
            afterPut(root);
            return null;
        }
        // 情况2: 添加的不是第一个结点
        Node<K, V> parent = root;  // 当前节点的父节点
        Node<K, V> node = root;    // 当前结点,从root节点开始找,于是设当前节点node=root

        int cmp = 0;            // 当前节点和要插入节点的比较
        // while用来确定添加的位置
        while (node != null) {
            cmp = compare(key, node.key); // 拿插入的元素和当前节点的元素比较
            parent = node;          // 在"向左"或"向右"之前,保存当前节点,作为下一次循环的当前节点的父节点.[待思考,语言描述要准确]
            if (cmp > 0) {          // 要传入的元素比当前节点大,应当要和当前节点的右子节点比较,如此循环下去,直到当前节点为空为止.
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;   // 要传入的元素比当前节点小,应当要和当前节点的左子节点比较,如此循环下去,直到当前节点为空为止.
            } else {
                node.key = key; // 如果相等值覆盖,有意义,因为考虑到自定义对象
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        }
        // 判断要插入到父节点的哪个位置,左还是右
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
        // 新添加结点之后的处理
        afterPut(newNode);
        return null;
    }


    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    // value 不具备可比较性,只能遍历一个个比较
    // 建议层序遍历
    @Override
    public boolean containsValue(V value) {
        if (root == null) return false;
        Queue<Node<K, V>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<K, V> node = queue.poll();
            if (valEquals(value, node.value)) return true;
            if (node.left != null) {
                queue.offer(node.left);
            }

            if (node.right != null) {
                queue.offer(node.right);
            }
        }
        return false;
    }


    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (visitor == null) return;
        traversal(root, visitor);
    }

    private void traversal(Node<K, V> node, Visitor<K, V> visitor) {
        if (node==null||visitor.stop) return;
        traversal(node.left,visitor);
        if (visitor.stop) return;
        visitor.visit(node.key,node.value);
        traversal(node.right,visitor);
    }


    // 删除元素代表的节点
    private V remove(Node<K, V> node) {
        if (node == null) return null; // 节点不存在
        size--;

        V oldValue = node.value;
        // 优先解决度为2的节点的情况
        if (node.hasTwoChildren()) {
            // 这里选择找到后继节点
            Node<K, V> s = successor(node);
            // 用后继节点的值覆盖度为2节点的值
            node.key = s.key;
            node.value = s.value;
            // 删除后继节点[node=s,node指向了s由后面的负责度为1和0的情况统一删除]
            node = s;
        }
        // 负责处理度为1的节点 代码执行到这里必然度为1或者0 要么左子节点要么右子节点
        Node<K, V> child = node.left != null ? node.left : node.right;
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
        return oldValue;
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
    private void afterRemove(Node<K, V> node) {
        // 如果删除的节点的是红色 或者 用以取代删除节点 的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }
        Node<K, V> parent = node.parent;
        // 删除的是根节点
        if (parent == null) return;
        // 删除的是黑色的叶子节点,在B树中意味着"下溢"
        // 判断被删除的node是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<K, V> sibling = left ? parent.right : parent.left;
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
                    sibling = parent.right;
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
                    sibling = parent.left;
                }
                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }

    }


    // 根据key用来找到对应的节点
    protected Node<K, V> node(K key) {
        Node<K, V> node = root; // 从根节点往下找
        while (node != null) {
            int cmp = compare(key, node.key);
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

    /*******************************************求任意节点的前驱节点/后继节点***********************************************/


    private Node<K, V> predecessor(Node<K, V> node) {
        if (node == null) return null;
        Node<K, V> p = node.left; // 假定前驱就是左子树中(left.right.right.right.right.....)
        // 1. 左子树不为空,前驱节点就是左子树一直往右找,直到不能再右为止返回节点
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }

        // 能到这里左子树是空的
        // 从父节点,祖父节点中寻找前驱节点,父节点不为空,且是父节点的左子树,循环一直执行,直到当前节点是父节点的左子结点为止
        // 那么当前节点的父节点就是目标节点的前驱节点
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        // node.parent==null
        // node==node.parent.right
        return node.parent;
    }

    /**
     * 后续节点(successor):
     * 1.中序遍历的后一个节点
     * 2.如果是二叉搜索树,后续节点就是后一个比它大的节点
     * <p>
     * 对于一般的二叉树
     * 1.当前右子树不为空,左子树的最左元素即是前驱节点
     * - node.right !=null
     * - successor=right.left.left.left....
     * - 终止条件: left为null找到
     * 2.当前节点的右子树为空,且父亲不为空
     * - node.right==null && node.parent!=null
     * - predecessor=node.parent.parent.....
     * - 终止条件: node在parent的左子树中
     * 3.node.right==null && node.parent==null
     * - 那就没有前驱节点,返回的也是null
     */
    private Node<K, V> successor(Node<K, V> node) {
        if (node == null) return null;
        Node<K, V> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    private void elementNotNullCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must be null");
        }
    }

    /**
     * @return 返回值=0代表e1>e2  >0代表e1>e2    <0代表e1<e2
     */
    private int compare(K key1, K key2) {
        if (comparator != null) {
            return comparator.compare(key1, key2);
        }
        return ((Comparable<K>) key1).compareTo(key2); // 不写死,让不实现Comparable接口的对象也能比较.仅在comparator为空的时候强转为Comparable类型来比较
    }

    /**
     * 添加后修复红黑树性质
     *
     * @param node 新添加结点
     */
    private void afterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;
        if (parent == null) {         // 添加的是根节点,或者上溢到了根节点,将新添加的node染黑
            black(node);
            return;
        }
        if (isBlack(parent)) return;  // parent为BLACK不用做任何添加的后续处理

        // 能到达这里的parent为RED(Double Red)
        Node<K, V> uncle = parent.sibling();  // 叔父(uncle)节点
        Node<K, V> grand = parent.parent;     // 祖父(grand)节点,可以先染红,grand替代下面的red(grand)


        // 判断叔父节点是否为红色
        if (isRed(uncle)) {  // 叔父节点是红色属于上溢情况,parent和uncle染成黑色,grand染成红色作为新添加的节点并向上合并
            black(parent);
            black(uncle);
            // 祖父节点当做是新添加的节点
            afterPut(red(grand));
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


    /*------------------------------------------旋转相关---------------------------------------------------------------*/
    // 左旋 RR
    private void rotateLeft(Node<K, V> g) {
        // 旋转
        Node<K, V> p = g.right;
        Node<K, V> child = p.left;
        g.right = child;
        p.left = g;
        // 维护相关内容,更新相关parent和相关height
        // 让p成为子树的根节点
        afterRotate(g, p, child);
    }

    // 右旋 LL
    private void rotateRight(Node<K, V> g) {
        Node<K, V> p = g.left;
        Node<K, V> child = p.right;
        g.left = child;
        p.right = g;
        // 维护相关内容
        // 让p为子树的根节点
        afterRotate(g, p, child);
    }

    // 旋转后的维护
    private void afterRotate(Node<K, V> g, Node<K, V> p, Node<K, V> child) {
        p.parent = g.parent;     // 变换p的父节点=>变成根节点
        // 当前子树根节点的父节点指向p,把根节点可能性考虑进去
        if (g.isLeftChild()) {
            g.parent.left = p;
        } else if (g.isRightChild()) {
            g.parent.right = p;
        } else {
            root = p;
        }
        // 更改child的parent指向
        if (child != null) {
            child.parent = g;
        }
        // 更改g的parent指向
        g.parent = p;
        // 更新高度 g p
    }
    /*------------------------------------------辅助函数---------------------------------------------------------------*/

    // 染红
    private Node<K, V> red(Node<K, V> node) {
        return color(node, RED);
    }

    // 染黑
    private Node<K, V> black(Node<K, V> node) {
        return color(node, BLACK);
    }

    // 染色,并返回对应节点
    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node == null) return null;
        node.color = color;
        return node;
    }

    // 是黑色
    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    // 是红色
    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }

    // 判断每个节点的颜色
    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }


    // value不具备可比较性
    private boolean valEquals(V v1, V v2) {
        return v1 == null ? v2 == null : v1.equals(v2);
    }

    private static class Node<K, V> {
        K key;
        V value;
        boolean color = RED;    // 颜色
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        // 判断度为2
        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        // 当前节点是否是父节点的左孩子
        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        // 当前节点是否是父节点的右孩子
        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        // 返回兄弟节点
        public Node<K, V> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }
            if (isRightChild()) {
                return parent.left;
            }
            return null;//没有兄弟节点
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
