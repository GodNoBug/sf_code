package sf.sort;

import org.junit.Test;
import sf.sort.sort.cmp.bubble.BubbleSort3;
import sf.sort.sort.cmp.heap.HeapSort;
import sf.sort.sort.cmp.select.SelectionSort;
import sf.sort.sort.cmp.insert.InsertionSort1;
import sf.sort.sort.cmp.insert.InsertionSort2;
import sf.sort.sort.cmp.insert.InsertionSort3;
import sf.sort.sort.cmp.shell.ShellSort;
import sf.sort.sort.no_cmp.radix.RadixSort;
import sf.sort.tools.Integers;


import java.util.Arrays;
@SuppressWarnings("all")
public class Main {
    @Test
    public void sort(){

        Integer[] array= Integers.random(10000,1,20000);


        sorts(array,
                new HeapSort<Integer>(),
                new SelectionSort<Integer>(),
                new BubbleSort3<Integer>(),
                new InsertionSort1<Integer>(),
                new InsertionSort2<Integer>(),
                new InsertionSort3<Integer>(),
                new ShellSort<Integer>(),
                new RadixSort()
        );


//        Integers.println(array1);
//        Asserts.test(Integers.isAscOrder(array1));
    }
    public void sorts(Integer[] array,Sort... sorts){
        for (Sort sort : sorts) {
            sort.sort(Integers.copy(array));
        }
        Arrays.sort(sorts);
        for (Sort sort : sorts) {
            System.out.println(sort);
        }
    }
}
