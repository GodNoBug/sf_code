package sf.sort;

import sf.sort.sort.cmp.select.SelectionSort;
import sf.sort.sort.cmp.shell.ShellSort;
import sf.sort.sort.no_cmp.counting.CountingSort;
import sf.sort.sort.no_cmp.radix.RadixSort;
import sf.sort.tools.Student;

import java.text.DecimalFormat;

/**
 * @param <T>
 */
// 排序: 将一组杂乱无章的数据按一定规矩顺次排列起来.即将无序序列排成一个有序序列(由小到大或由大到小)的运算.
// - 如果参加排序的数据节点包含多个数据域,那么排序往往是针对某一个数据域而言
// 按储存介质可分为
// - 内部排序: 数据量不大、数据在内存,无需内外存交换数据
// - 外部排序: 数据量较大、数据在外存(文件排序)
//	 外部排序时,要将数据分批调入内存来排序,中间结果还要及时放入外存,显然外部排序要复杂的多
// 按比较器个数可分为:
// - 串行排序: 单处理机(同一时刻比较一对元素)
// - 串行排序: 多处理机(同一时刻比较多对元素)
// 按主要操作可分为:
// - 比较排序: 用比较的方法 插入排序、交换排序、选择排序、归并排序
// - 基数排序: 不比较元素的大小,仅仅根据元素本身的取值确定其有序位置.
// 按辅助空间可分为:
//	- 原地排序: 辅助空间用来为O(1)的排序方法(所占的辅助存储空间与参加排序的数据量大小无关)
//	- 非原地排序: 辅助空间用量超过O(1)的排序方法
// 按稳定性可分为:
//	- 稳定排序: 能够使任何数值相等的元素,排序以后相对次序不变.
//	- 非稳定性排序: 不是稳定排序的方法
//	(排序稳定性的意义: 只对结构类型数据排序有意义[结构类型数据包含了多个数据项]. 排序算法是否稳定,并不能衡量一个排序算法的优劣)
@SuppressWarnings("unchecked")
public abstract class Sort<T extends Comparable<T>> implements Comparable<Sort<T>> {
    protected T[] array;                        // 要排序的数组
    private int cmpCount;                        // 比较次数
    private int swapCount;                        // 交换次数
    private long time;                            // 所花时间
    private DecimalFormat fmt = new DecimalFormat("#.00");

    // 排序
    public void sort(T[] array) {
        if (array == null || array.length < 2) return;

        this.array = array;

        long begin = System.currentTimeMillis();
        sort();  // 真正交给子类实现的
        time = System.currentTimeMillis() - begin;
    }

    // 排序经常需要比较和交换
    // 按耗时比较输出
    @Override
    public int compareTo(Sort<T> o) {
        int result = (int) (time - o.time);
        if (result != 0) return result;
        // 如果时间相等,拿比较次数来比
        result = cmpCount - o.cmpCount;
        if (result != 0) return result;
        // 比较次数相等,拿交换次数来比
        return swapCount - o.swapCount;
    }

    protected abstract void sort();

    /* 比较1
     * 返回值等于0，代表 array[i1] == array[i2]
     * 返回值小于0，代表 array[i1] < array[i2]
     * 返回值大于0，代表 array[i1] > array[i2]
     */
    protected int cmp(int i1, int i2) {
        cmpCount++;
        return array[i1].compareTo(array[i2]);
    }

    // 比较2
    protected int cmp(T v1, T v2) {
        cmpCount++;
        return v1.compareTo(v2);
    }

    protected void swap(int i1, int i2) {
        swapCount++;
        T tmp = array[i1];
        array[i1] = array[i2];
        array[i2] = tmp;
    }

    @Override
    public String toString() {
        String timeStr = "耗时：" + (time / 1000.0) + "s(" + time + "ms)";
        String compareCountStr = "比较：" + numberString(cmpCount);
        String swapCountStr = "交换：" + numberString(swapCount);
        String stableStr = "稳定性：" + isStable();
        return "【" + getClass().getSimpleName() + "】\n"
                + stableStr + " \t"
                + timeStr + " \t"
                + compareCountStr + "\t "
                + swapCountStr + "\n"
                + "------------------------------------------------------------------";

    }

    private String numberString(int number) {
        if (number < 10000) return "" + number;

        if (number < 100000000) return fmt.format(number / 10000.0) + "万";
        return fmt.format(number / 100000000.0) + "亿";
    }

    private boolean isStable() {
        if (this instanceof RadixSort) return true;
        if (this instanceof CountingSort) return true;
        if (this instanceof ShellSort) return false;
        if (this instanceof SelectionSort) return false;
        Student[] students = new Student[20];
        for (int i = 0; i < students.length; i++) {
            students[i] = new Student(i * 10, 10);
        }
        sort((T[]) students);
        // 比较排序前和排序后是否一样,来判断是否排序稳定
        for (int i = 1; i < students.length; i++) {
            int score = students[i].score;
            int prevScore = students[i - 1].score;
            if (score != prevScore + 10) return false;
        }
        return true;
    }
}