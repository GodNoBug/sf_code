package jg.tree.heap;


import jg.tree.heap.printer.BinaryTrees;
import org.junit.Test;

public class Main {
    @Test
    public void binaryHeap() {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(68);
        heap.add(72);
        heap.add(43);
        heap.add(50);
        heap.add(38);
        heap.add(10);
        heap.add(90);
        heap.add(65);
        BinaryTrees.println(heap);
        //heap.remove();
        //BinaryTrees.println(heap);
        System.out.println(heap.replace(70));
        BinaryTrees.println(heap);
    }

    @Test
    public void createHeap() {
        // 给定一个乱序数组,让它变成堆.for循环效率不高,因为批量上滤效率比下滤低
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        // 自上而下的上滤,本质(类似于添加,添加前,已经属于大顶堆性质,添加后,在大顶堆的基础上又添加符合大顶堆性质)
        // O(nlogn),所有节点深度之和
        // -- 仅仅是叶子节点,就有有近n/2个,而且每一个叶子节点的深度都是O(logn)级别的,因此在叶子节点这一块就达到了O(nlogn)级别
        // -- O(nlogn)的时间复杂度足以利用排序算法对所有子节点进行全排序   堆是偏序

        // 自上而下的下滤,本质在做让左右变成堆,然后合并成大堆(本质类似于堆的删除,删除前,根左右是符合大顶堆性质的) O(N),所有节点的高度之和
        // -- 假设是满树,节点总个数为n,树高为h,那么n = 2^h - 1 ↓
        // -- 所有节点的高度之和H(n) = 2^0 *(h-0)+2^1*(h-1)+2^2*(h-2)+...+2^(h-1)*[h-(h-1)] ...
        // -- H(h) = 2n-log2(n+1) = O(n)
        BinaryHeap<Integer> heap = new BinaryHeap<>(data);
        BinaryTrees.println(heap);
        data[0] = 10;
        data[1] = 20;
        BinaryTrees.println(heap);
    }

    @Test
    public void comparator() {
        // 最大堆把认为比较大的排到上面去,取决于Comparator的结果,
        // 参数
        // 大于0 认为左边的比较大
        // 小于0 认为右边的比较大
        // 大于0 认为左右相等
        // o2-o1最终导致数字比较小的反而让为它比较大,从而值比较小的会放在顶部,大顶堆变成了小顶堆
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        BinaryHeap<Integer> heap = new BinaryHeap<>(data, (o1, o2) -> o2 - o1);
        BinaryTrees.println(heap);
    }

    // 面试题 Top K 问题
    // 从n个整数中,找出最大的前k个数(k远远小于n)
    @Test
    public void topK() {
        // 如果使用排序算法进行全排序,需要O(nlogn)的时间复杂度
        // 如果使用二叉堆来解决,可以使用O(nlogk)的时间复杂度来解决.效率更高(k远远小于n)

        // 新建一个小顶堆 why are not 大顶堆,大顶堆搞完之后nlogn啊
        BinaryHeap<Integer> heap = new BinaryHeap<>((o1, o2) -> o2 - o1);

        // 找出最大的前k个数
        int k = 5;
        Integer[] data = {51, 30, 39, 92, 74, 25, 16, 93,
                91, 19, 54, 47, 73, 62, 76, 63, 35, 18,
                90, 6, 65, 49, 3, 26, 61, 21, 48};
        for (int i = 0; i < data.length; i++) {
            if (heap.size() < k) { // 前k个数添加到小顶堆
                heap.add(data[i]);
            } else { // 如果是第k+1个数,并且大于顶堆元素,replace意味着删除最小的,换一个比较大的,每一次都将较小的剔除掉,换上较大的,最终扫描完后但就是最终结果
                if (data[i] > heap.get()) {
                    heap.replace(data[i]);
                }
            }
        }
        // 如果是找出最小的前k个数呢.用大顶堆,如果小于堆顶元素,就使用replace操作
        BinaryTrees.println(heap);
    }
}
