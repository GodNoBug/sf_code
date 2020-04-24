package jg.bloom_filter;

// 总体上是按位运算
public class BloomFilter<T> {
    private int bitSize;      // 二进制向量的长度(一共有多少个二进制位)
    private long[] bits;      // 二进制向量 8字节--64位
    private int hashSize;     // 哈希函数的个数

    /**
     * @param n 数据规模
     * @param p 误判率, 取值范围(0, 1)
     */
    public BloomFilter(int n, double p) {
        if (n <= 0 || p <= 0 || p >= 1) {
            throw new IllegalArgumentException("wrong n or p");
        }
        double ln2 = Math.log(2);

        // 求出二进制向量的长度
        bitSize = (int) (-(n * Math.log(p)) / (ln2 * ln2));
        // 求出哈希函数的个数
        hashSize = (int) (bitSize * ln2 / n);
        // bits数组的长度
        bits = new long[(bitSize + Long.SIZE - 1) / Long.SIZE];
        // 每一页显示100条数据, pageSize
        // 一共有999999条数据, n
        // 请问有多少页 pageCount = (n + pageSize - 1) / pageSize
    }

    /**
     * 添加元素
     *
     * @return true代表它发生了改变
     */
    public boolean put(T value) {
        nullCheck(value);
        // 利用value对象生成两个整数
        int hash1 = value.hashCode();
        int hash2 = hash1 >>> 16;
        boolean result = false;
        for (int i = 1; i <= hashSize; i++) {
            // 尽可能生成不同的索引
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) { // 产生的值保证非负数
                combinedHash = ~combinedHash;
            }
            // 生成一个二进位的索引
            int index = combinedHash % bitSize;
            // 设置index位置上的二进制位为1.
            //  101001010100101010 (long数组是连续的,算出long数组的位置,再算出单独在一个long中的bit位)
            //| 000000000100000000 (1>>index 产生的二进制数,数按位或)
            if (set(index)) result = true;
        }
        return result;
    }

    /**
     * 查询元素是否存在
     *
     * @return false代表一定不存在, true代表可能存在
     */
    public boolean contains(T value) {
        nullCheck(value);
        // 利用value对象生成两个整数
        int hash1 = value.hashCode();
        int hash2 = hash1 >>> 16;

        for (int i = 1; i <= hashSize; i++) {
            // 尽可能生成不同的索引
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) { // 产生的值保证非负数
                combinedHash = ~combinedHash;
            }
            // 生成一个二进位的索引
            int index = combinedHash % bitSize;
            // 查询index位置的二进制位是否为0
            if (!get(index)) return false;
        }
        return true;
    }

    /**
     * 查看index位置的二进位的值
     *
     * @return true代表1, false代表0
     */
    private boolean get(int index) {
        // 对应的long值
        long value = bits[index / Long.SIZE];
        int bitValue = 1 << (index % Long.SIZE);
        //   1000101010
        // & 0000100000
        return (value & bitValue) != 0;
    }

    /**
     * 设置index位置的二进位为1
     */
    private boolean set(int index) {
        // 设置index位置上的二进制位为1.
        //  101001010100101010 (long数组是连续的,算出long数组的位置,再算出单独在一个long中的bit位)
        //| 000000000100000000 (1<<相关index数值 产生的二进制数,数按位或)

        // 从左到右找到对应的long,在long内部找进制位是从右往左数的

        long value = bits[index / Long.SIZE];

        int bitValue = 1 << (index % Long.SIZE);
        bits[index / Long.SIZE] = value | bitValue;
        return (value & bitValue) == 0;  // 这个位置上有没有被设值过,此次是否发生改变
    }

    private void nullCheck(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null.");
        }
    }

}
//◼ 如果要经常判断1个元素是否存在,你会怎么做？
//  很容易想到使用哈希表(HashSet、 HashMap),将元素作为 key 去查找
//  ✓ 时间复杂度: O(1)，但是空间利用率不高，需要占用比较多的内存资源
//◼如果需要编写一个网络爬虫去爬10亿个网站数据,为了避免爬到重复的网站,如何判断某个网站是否爬过？
//  很显然， HashSet、 HashMap 并不是非常好的选择
//◼ 是否存在时间复杂度低、占用内存较少的方案？

// 需求:
// 1.经常要判断某个元素是否存在
// 2.元素数量巨大,希望用比较少的内存空间
// 3.允许有一定的误判率

//布隆过滤器(Bloom Filter)
//◼ 1970年由布隆提出
//  它是一个空间效率高的概率型数据结构，可以用来告诉你: 一个元素一定不存在或者可能存在
//◼ 优缺点
//  优点：空间效率和查询时间都远远超过一般的算法
//  缺点：有一定的误判率、删除困难
//◼ 它实质上是一个很长的二进制向量和一系列随机映射函数（Hash函数）
//◼ 常见应用
//    网页黑名单系统、垃圾邮件过滤系统、爬虫的网址判重系统、解决缓存穿透问题

// 布隆过滤器原理
//