package jg.other.skip_list;


import java.util.Comparator;

@SuppressWarnings("unchecked")
public class SkipList<K, V> {
    private int size;
    private Node<K, V> first;                   // 首节点,不存放任何K-V.思考到底nexts多少呢?
    private Comparator<K> comparator;           // 比较
    private static final int MAX_LEVEL = 32;    // 规定最高32层
    private int level;                          // 有效层数,搜索的时候要从有效层数的最高层开始,因此记录有效层数
    public static final double P = 0.25;         //

    public SkipList() {
        this(null);
    }

    public SkipList(Comparator<K> comparator) {
        this.comparator = comparator;
        // 跳表一般会限制最高层数
        // 链表节点层数达到最高也不能超过头结点的高度
        this.first = new Node<>(null, null, MAX_LEVEL);
        this.first.nexts = new Node[MAX_LEVEL];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // 添加,新增节点需要多少层?官方说法是经过统计可以是随机的
    public V put(K key, V value) {
        keyCheck(key);
        Node<K, V> node = this.first;
        Node<K, V>[] prevs = new Node[level]; //TODO 将成为新增节点的前驱
        for (int i = level - 1; i >= 0; i--) { // 遍历层数
            int cmp = -1;
            while (node.nexts[i] != null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            if (cmp == 0) {// 表明节点是存在的,覆盖
                V oldValue = node.nexts[i].value;
                node.nexts[i].value = value;
                return oldValue;
            }
            prevs[i] = node;  // 往下的时候
        }
        // 新节点的层数
        int newLevel = randomLevel();
        // 添加新节点
        Node<K, V> newNode = new Node<>(key, value, newLevel);

        // TODO 维护添加后的节点的链接,想办法拿到左边的节点,那么在插入的位置的左边自然通过next就有了
        for (int i = 0; i < newLevel; i++) {
            if (i >= level) {
                first.nexts[i] = newNode;
            } else {
                newNode.nexts[i] = prevs[i].nexts[i];
                prevs[i].nexts[i] = newNode;
            }
        }
        // 节点数量添加
        size++;
        // 计算跳表的最终层数
        level = Math.max(level, newLevel);

        return null;
    }

    private int randomLevel() {
        int level = 1;     // [0,0,1)
        while (Math.random() < P && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }

    // first.nexts[3]== 21节点
    // first.nexts[2]== 9节点
    // first.nexts[1]== 6节点
    // first.nexts[0]== 3节点
    // first.nexts[k]== 一直往右第一个遇到的节点
    // key = 19 level=3
    public V get(K key) {
        keyCheck(key);
        // 从首元素的最顶层首元素开始找
        // 从左往右一直找,直到找到大于等于key为止,往回退.对下一层循环执行前面的逻辑
        Node<K, V> node = this.first;
        for (int i = level - 1; i >= 0; i--) { // 遍历层数
            int cmp = -1;
            while (node.nexts[i] != null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            //  node.nexts[i].key >= key
            if (cmp == 0) return node.nexts[i].value;
        }
        return null;
    }

    public V remove(K key) {
        keyCheck(key);
        Node<K, V> node = this.first;
        Node<K, V>[] prevs = new Node[level];
        boolean exist = false;
        for (int i = level - 1; i >= 0; i--) { // 遍历层数
            int cmp = -1;
            while (node.nexts[i] != null && (cmp = compare(key, node.nexts[i].key)) > 0) {
                node = node.nexts[i];
            }
            prevs[i] = node;  // 往下的时候
            if (cmp == 0) exist = true;
        }
        if (!exist) return null;
        // 需要被删除的节点
        Node<K, V> removedNode = node.nexts[0];
        // 节点数量减少
        size--;

        // 设置后续
        for (int i = 0; i < removedNode.nexts.length; i++) {
            prevs[i].nexts[i] = removedNode.nexts[i];
        }
        // 更新跳表的层数
        int newLevel = level;
        while (--newLevel >= 0 && first.nexts[newLevel] == null) { // 从头节点从顶层出发,如果删除后的头节点next不为空,那么还是以前的高度,否则--再看看是否还满足条件
            level = newLevel;
        }
        return removedNode.value;
    }

    private int compare(K key1, K key2) {
        return comparator != null ? comparator.compare(key1, key2) : ((Comparable<K>) key1).compareTo(key2);
    }

    private void keyCheck(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null.");
        }
    }

    // 节点定义
    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V>[] nexts;

        public Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            nexts = new Node[level];
        }
        @Override
        public String toString() {
            return key + ":" + value + "_" + nexts.length;
        }
    }

    // 逐层拼接
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("一共" + level + "层").append("\n");
        for (int i = level - 1; i >= 0; i--) {
            Node<K, V> node = first;
            while (node.nexts[i] != null) {
                sb.append(node.nexts[i]);
                sb.append(" ");
                node = node.nexts[i];
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
// 一个有序链表搜索、添加、删除的平均时间复杂度是多少？ O(n)
// 能否利用二分搜索优化有序链表,将搜索、添加、删除的平均时间复杂度降低至 O(logn)?
//  - 链表没有像数组那样的高效随机访问(O(1) 时间复杂度)，所以不能像有序数组那样直接进行二分搜索优化
// 那有没有其他办法让有序链表搜索、添加、删除的平均时间复杂度降低至 O(logn)？
//  - 使用跳表（SkipList）

// 跳表，又叫做跳跃表、跳跃列表，在有序链表的基础上增加了“跳跃”的功能。由William Pugh于1990年发布，设计的初衷是为了取代平衡树（比如红黑树）
//
//- Redis中 的 SortedSet、 LevelDB 中的 MemTable 都用到了跳表
//  - Redis、 LevelDB 都是著名的 Key-Value 数据库
//- 对比平衡树
//  - 跳表的实现和维护会更加简单
//  - 跳表的搜索、删除、添加的平均时间复杂度是 O(logn)

// 跳表的层数:
// - 跳表是按层构造的,底层是一个普通的有序链表,高层相当于是低层的"快速通道"
// -- 在第i层中的元素按某个固定的概率P(通常为1/2,1/4)出现在第i+1层中,产生越高的层数,概率越低
// -- 元素层数恰好等于1的概率为1-p
// -- 元素层数大于等于 2 的概率为 p，而元素层数恰好等于 2 的概率为 p * (1 – p)
// -- 元素层数大于等于 3 的概率为 p^2，而元素层数恰好等于 3 的概率为 p^2 * (1 – p)
// -- 元素层数大于等于 4 的概率为 p^3，而元素层数恰好等于 4 的概率为 p^3 * (1 – p)
// -- ......
// -- 一个元素的平均层数是 1 / (1 – p)
// 当 p = ½ 时，每个元素所包含的平均指针数量是 2
// 当 p = ¼ 时，每个元素所包含的平均指针数量是 1.33 (比红黑树节省内存的)

// 跳表的复杂度分析:
// ◼ 每一层的元素数量
// 第 1 层链表固定有 n 个元素
// 第 2 层链表平均有 n * p 个元素
// 第 3 层链表平均有 n * p^2 个元素
// 第 k 层链表平均有 n * p^k 个元素
//  另外
// 最高层的层数是 log1/p(n)，平均有个 1/p 元素
// 在搜索时，每一层链表的预期查找步数最多是 1/p，所以总的查找步数是 –(logp(n /p))，时间复杂度是 O(logn)

// 其他跳表的可能实现
//  在Node类成员变量可能多了一下几种
//  Node<K,V> right;
//  Node<K,V> down;
//  Node<K,V> top;
//  Node<K,V> left;


