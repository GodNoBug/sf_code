package jg.tree.map;

import jg.tree.binary.BinaryTree;
import jg.tree.binary.printer.BinaryTreeInfo;
import jg.tree.binary.printer.BinaryTrees;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 抛砖引玉:
 * TreeMap平均时间复杂度:添加/删除/搜索: O(logn)
 * -特点: key必须具备可比较性,元素的分布是有顺序的
 * -在实际应用中,很多需求Map中存储的元素不需要讲究顺序,Map中的key不需要具备可比较性
 * 不考虑顺序、不考虑Key的可比较性,Map有更好的实现方案,平均时间复杂度可以达到O(1)
 * 那就是采取Hash表来实现Map,
 * <p>
 * 哈希表也叫做散列表(hash有"剁碎"的意思)
 * -给一个key,通过哈希函数(散列函数)生成对应数组的索引,进行定位,然后搜索/删除/添加
 * -是空间换时间的典型应用,哈希表内部的数组元素,很多地方也叫Bucket(桶),整个数组叫Buckets或者Bucket Array
 * -哈希表其实指的是数组
 * <p>
 * 哈希冲突也叫做哈希碰撞
 * - 2个不同的key,经过hash函数计算相同的结果
 * - 开放定值法: 按照一定规则向其他地址探测,直到遇到空桶
 * - 再哈希法: 设计多个hash函数
 * - 链地址法: 比如通过链表将同一index的元素串起来
 * <p>
 * JDK1.8的哈希冲突解决方案
 * - 默认使用单向链表将元素串起来
 * - 在添加元素时,可能会由单向链表转为红黑树来存储元素
 * -- 比如当hash表容量≥64 且单向链表的节点数量大于8时
 * - 当红黑树节点数量少到一定程度是,又会转为单向链表
 * - JDK1.8中的hash表是使用链表+红黑树解决哈希冲突.执意用单链表因为每次都是从头节点开始遍历,单链表比双向链表少一个指针,可以节省内存空间
 * <p>
 * 一般数组下标为key,数组元素为value存元素会让空间复杂度空前变大.哈希表是更加高效的数组设计
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class HashMap<K, V> implements Map<K, V> {
    // 哈希表元素数量,包括链表和红黑树
    private int size;
    // 红黑树节点的黑色和红色
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    // 哈希表,存的是红黑树的根节点或者链表的头结点
    private Node<K, V>[] table;

    private static final int DEFAULT_CAPACITY = 1 << 4; // 16

    public HashMap() {
        this.table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        if (size == 0) return;
        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    @Override
    public V put(K key, V value) {
        int index = index(key);
        Node<K, V> root = table[index];// 取出index位置的红黑树根节点
        if (root == null) {
            root = new Node<>(key, value, null);
            table[index] = root;
            size++;
            afterPut(root);  // 修复红黑树性质
            return value;
        }
        // 来到这里hash冲突了,意味着添加新的节点到红黑树上的
        Node<K, V> parent = root;
        Node<K, V> node = root;
        int cmp = 0;
        K k1 = key;
        int h1 = key == null ? 0 : key.hashCode();
        Node<K, V> result = null;
        boolean searched = false; // 是否已经搜索过这个key
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;

            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (Objects.equals(k1, k2)) {
                cmp = 0;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable) {
                cmp = ((Comparable) k1).compareTo(k2);
            } else if (searched) { // 已经扫描了
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);

            }else { // searched == false 没有扫描,然后根据内存地址决定左右
                if ((node.left != null && (result = node(node.left, k1)) != null)
                        || (node.right != null && (result = node(node.right, k1)) != null)) {
                    // 已经存在这个key,覆盖,套用后面的代码
                    node = result;
                    cmp = 0;
                } else { // 不存在这个key
                    searched = true;
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
                }
            }
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else { // 相等
                node.key = key;
                V oldValue = node.value;
                node.value = value;
                node.hash = h1;
                return oldValue;
            }
        } while (node != null);

        // 看看插入到父节点的哪个位置
        Node<K, V> newNode = new Node<>(key, value, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;

        // 新添加节点之后的处理
        afterPut(newNode);
        return null;
    }


    // 哈希表中哈希函数的实现步骤大概如下:
    //  1.先生成key的哈希值(必须是整数)
    //  2.再让key的哈希值跟数组的大小进行相关运算,生成一个索引值

    // 设计良好的hash函数
    // 让哈希值更加均匀分布->减少哈希冲突次数->提升哈希表的性能
    //    为了提高效率,可以使用&位运算取代%运算[前提:将数组的长度设计为2的幂]
    //    -- 2^n 的二进制特性是 10000.... n个0, (2^n)-1 得到的值 是以0开头n个1(table.length-1)
    //    -- "&"全是1的的结果是: 与1&运算,获得原来的数. 长的数和短的全是1的数(短,前面补0),结果长的数被全是1的数截取了,绝对是不会超过全是1的数值

    // 如何生成key的hash值
    //  key的常见类型:
    //       整数,浮点数,字符串,自定义对象;不同种类的key,哈希值的生成方式不一样,但目标是一致的: 尽量让每个key的哈希值是唯一的,尽量让key的所有信息参与运算.
    //     在Java中,HashMap的key必须实现hashCode、equals方法,也允许key为null.
    //     1.整数: 就拿整数但哈希值,因为hash值确定索引,而索引是int值
    //     2.浮点数: float先计算出浮点数的二进制,再转成整数作为哈希值. 如: Float.floatToIntBits(10.6f)
    //     3.long: Java的哈希值规定必须是hash值,也就是必须32位,而long是64位,为充分运算这64位
    //             这么做-> 无符号右移动32位">>>",获得的结果与原来的取"^",异或运算[异(值不同)为1,否则为0]
    //             若有一个long值,64位,前32得以和后32位进行运算,再强转为int值,只截取了结果的后32位.之所以用异或运算,
    //             因为其他运算"与"和"或"可能导致结果大部分是1或0,容易造成哈希冲突.唯有异或运算混合出均匀的东西
    //     4.double: 先把double的二进制当做整数进行值转换,无符号右移动32位">>>",获得的结果与原来的取"^"
    //             ">>>"和"^" 利用高32位和低32位混合计算出32位32位的哈希值,为的是充分利用key的所有信息计算出哈希值
    //     5.String: 字符串是若干个字符组成的,字符的本质就是一个整数.
    //       模仿整数计算方式."jack"字符串的哈希值可以表示为 j*n^3+a*n^2+c*n^1+k*n^0,等价于[(j*n+a)*n+c]*n+k
    //      (JDK中n=31,因为31是一个奇素数,JVM会将31*i优化成(i<<5)-i) 31不仅仅是符合2^n -1 ,它是个奇数,又是素数,也就是质数.
    //       素数和其他数相乘的结果比其他方式更容易产生唯一性,减少哈希冲突)
    //     6. 自定义对象: 先算出每个成员变量,然后按照公式算,见Person类. 不重写会导致调用Object的hashCode,导致根据地址值计算hashCode,
    //        相同内容的对象被视为不同的对象,同时存在了hash表中.

    /**
     * 先根据key计算索引[在桶数组中的位置],允许key为空.key为空则存在[0]位置
     * JDK中还做了一件事情 hash ^ (hash >>> 16) 高16位和低16位混合运算得出hash值
     */
    private int index(K key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        return (hash ^ (hash >>> 16)) & (table.length - 1);
    }

    private int index(Node<K, V> node) {
        return (node.hash ^ (node.hash >>> 16)) & (table.length - 1);
    }


    /**
     * @param node
     */
    private void afterRemove(Node<K, V> node) {
        // 如果删除的节点是红色
        // 或者 用以取代删除节点的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }

        Node<K, V> parent = node.parent;
        if (parent == null) return;

        // 删除的是黑色叶子节点【下溢】
        // 判断被删除的node是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<K, V> sibling = left ? parent.right : parent.left;
        if (left) { // 被删除的节点在左边，兄弟节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }

                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边，兄弟节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
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


    private void afterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;

        // 添加的是根节点 或者 上溢到达了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent)) return;

        // 叔父节点
        Node<K, V> uncle = parent.sibling();
        // 祖父节点
        Node<K, V> grand = red(parent.parent);
        if (isRed(uncle)) { // 叔父节点是红色【B树节点上溢】
            black(parent);
            black(uncle);
            // 把祖父节点当做是新添加的节点
            afterPut(grand);
            return;
        }

        // 叔父节点不是红色
        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // LL
                black(parent);
            } else { // LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else { // R
            if (node.isLeftChild()) { // RL
                black(node);
                rotateRight(parent);
            } else { // RR
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    private void rotateLeft(Node<K, V> grand) {
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;
        grand.right = child;
        parent.left = grand;
        afterRotate(grand, parent, child);
    }

    private void rotateRight(Node<K, V> grand) {
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;
        grand.left = child;
        parent.right = grand;
        afterRotate(grand, parent, child);
    }

    private void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
        // 让parent称为子树的根节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else { // grand是root节点
            table[index(grand)] = parent;
        }

        // 更新child的parent
        if (child != null) {
            child.parent = grand;
        }

        // 更新grand的parent
        grand.parent = parent;
    }


    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node == null) return node;
        node.color = color;
        return node;
    }

    private Node<K, V> red(Node<K, V> node) {
        return color(node, RED);
    }

    private Node<K, V> black(Node<K, V> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }


    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node != null ? node.value : null;
    }

    private Node<K, V> node(K key) {
        Node<K, V> root = table[index(key)];
        return root == null ? null : node(root, key);
    }

    /**
     * TODO
     *
     * @param node
     * @param k1
     * @return
     */
    private Node<K, V> node(Node<K, V> node, K k1) {
        int h1 = k1 == null ? 0 : k1.hashCode();
        // 存储查找结果
        Node<K, V> result = null;
        while (node != null) {
            K k2 = node.key;
            int h2 = node.hash;
            // 先比较hash值
            if (h1 > h2) {
                node = node.right;
            } else if (h1 < h2) {
                node = node.left;
            } else if (Objects.equals(k1, k2)) {
                return node;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable) {
                int cmp = ((Comparable) k1).compareTo(k2);
                if (cmp > 0) {
                    node = node.right;
                } else if (cmp < 0) {
                    node = node.left;
                } else {
                    return node;
                }
            } else if (node.right != null && (result = node(node.right, k1)) != null) {
                return result;
            } else { // 只能往左边找
                node=node.left;
            }

            /*if (node.left != null && (result = node(node.left, k1)) != null) {
                return result;
            } else {
                return null;
            }*/
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

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
        int index = index(node);
        if (child != null) { // 证明node是度为1的节点
            child.parent = node.parent;    // 更改parent
            if (node.parent == null) {
                table[index] = child;
            } else if (node == node.parent.left) {
                node.parent.left = child;
            } else { // node == node.parent.right
                node.parent.right = child;
            }
            // 删除节点后的处理
            afterRemove(child);
        } else if (node.parent == null) { //证明node度为0,叶子节点并且是根节点
            table[index] = null;
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

    // 遍历数组上所有的树和链表,建议层序遍历
    @Override
    public boolean containsValue(V value) {
        if (size == 0) return false;
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;
            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (Objects.equals(value, node.value)) {
                    return true;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null) return;
        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;
            queue.offer(table[i]);
            while (!queue.isEmpty()) {
                Node<K, V> node = queue.poll();
                if (visitor.visit(node.key, node.value)) {
                    return;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

        }
    }


    // 定义红黑树节点
    private static class Node<K, V> {
        int hash;
        K key;
        V value;
        boolean color = RED;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);
            this.value = value;
            this.parent = parent;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public Node<K, V> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
        }

        @Override
        public String toString() {
            return "Node_" + key + "_" + value;
        }
    }

    public void print() {
        if (size == 0) return;
        for (int i = 0; i < table.length; i++) {
            System.out.println("[index=" + i + "]");
            final Node<K, V> root = table[i];
            BinaryTrees.println(new BinaryTreeInfo() {
                @Override
                public Object root() {
                    return root;
                }

                @Override
                public Object left(Object node) {
                    return ((Node<K, V>) node).left;
                }

                @Override
                public Object right(Object node) {
                    return ((Node<K, V>) node).right;
                }

                @Override
                public Object string(Object node) {
                    return node;
                }
            });
            System.out.println("-----------------------------------------------------");
        }
    }
}
