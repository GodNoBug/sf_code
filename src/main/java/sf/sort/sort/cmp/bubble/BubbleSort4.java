package sf.sort.sort.cmp.bubble;

import sf.sort.Sort;
@SuppressWarnings({"unused","all"})
public class BubbleSort4<T extends Comparable<T>> extends Sort<T> {

    @Override
    protected void sort() {
        while (!bubbleSort(0, array.length, array)) ;
    }


    public boolean bubbleSort(int begin, int end, T[] arr) {
        boolean sorted = true; // 整体有序标志
        while (++begin < end) {
            if (cmp(begin, begin - 1) > 0) { // arr[begin - 1] > arr[begin]
                sorted = false; // 意味着尚未整体有序,并需要交换
                swap(begin,begin-1);
            }
        }
        return sorted;
    }
}
