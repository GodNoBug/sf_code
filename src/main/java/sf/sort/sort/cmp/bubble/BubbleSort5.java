package sf.sort.sort.cmp.bubble;

import sf.sort.Sort;
@SuppressWarnings("unused")
public class BubbleSort5<T extends Comparable<T>> extends Sort<T> {

    @Override
    protected void sort() {
        bubbleSort(array.length);
    }



    public void bubbleSort(int n) {
        for (boolean sorted = false; sorted = !sorted; n--) { // 逐趟扫描交换,直至完全有序
            for (int i = 1; i < n; i++) { // 自左向右,逐对检查arr[0,n)内各相邻元素
                if (cmp(i,i-1)<0) { // arr[i - 1] > arr[i] 若逆序,则令其互换,同时清除(全局)有序标志
                    swap(i,i-1);
                    sorted = false;
                }
            }
        }
    }
}
