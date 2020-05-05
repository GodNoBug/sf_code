package sf.sort.sort.counting;

import org.junit.Test;
import sf.sort.Sort;

import java.util.Arrays;


// 如果自定义对象可以提供用以排序的整数类型,依然可以使用计数排序
@SuppressWarnings("all")
public class CountingSort3 extends Sort<Integer> {


    @Override
    protected void sort() {
        sort(array);
    }


    @Test
    public void test() {
        Person[] people = {
                new Person(20,"A"),
                new Person(-13,"B"),
                new Person(17,"C"),
                new Person(12,"D"),
                new Person(-13,"E"),
                new Person(20,"F"),
        };
        sort(people);
        System.out.println(Arrays.toString(people));
    }


    public void sort(Person[] array) {
        // 1.得到数列的最大值和最小值,并计算出差值d
        int max = array[0].age;
        int min = array[0].age;
        for (int i = 1; i < array.length; i++) { // O(n)
            if (array[i].age > max) {
                max = array[i].age;
            }
            if (array[i].age < min) {
                min = array[i].age;
            }
        }
        int d = max - min;
        // 2.创建统计数组并统计对应元素的个数.
        int[] counts = new int[d + 1];
        for (int i = 0; i < array.length; i++) {
            counts[array[i].age - min]++;
        }
        // 3.统计数组做变形,后面的元素等于前面的元素之和
        for (int i = 1; i < counts.length; i++) { // 遍历原统计数组.遍历到当前位置,把当前位置的值加上前面一个数,得排除第一个元素.
            // counts[i]=counts[i]+counts[i+1];
            counts[i] += counts[i - 1];
        }

        // 4.倒序遍历原始数列,从统计数组找到正确的位置,输出到结果数组[从由往左,具备稳定性](画图理解)
        Person[] sorted = new Person[array.length];
        for (int i = array.length - 1; i >= 0; i--) {
            sorted[counts[array[i].age - min] - 1] = array[i]; // 待排序的元素根据找到的索引,放到新数组中.
            counts[array[i].age - min]--;
        }
        // 将有序数组覆盖无序数组
        for (int i = 0; i < sorted.length; i++) {
            array[i] = sorted[i];
        }
    }


    private static class Person {
        int age;
        String name;

        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}



