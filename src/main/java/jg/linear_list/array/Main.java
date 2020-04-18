package jg.linear_list.array;

import jg.linear_list.array.asserts.Asserts;
import jg.linear_list.array.pojo.Person;
import org.junit.Test;

import java.util.ArrayList;

public class Main {
    @Test
    public void test() {
        DynamicArray<Integer> list = new DynamicArray<>();
        list.add(99);
        list.add(88);
        list.add(77);
        list.add(66);
        list.add(55);
        list.set(3, 80);


        Asserts.test(list.get(3) == 80);


        list.remove(2);
        System.out.println(list);

    }

    @Test
    public void test1() {
        DynamicArray<Integer> list = new DynamicArray<>();
        for (int i = 0; i < 30; i++) {
            list.add(i);
        }

    }
    @Test
    public void test2() {
        DynamicArray<Person> list = new DynamicArray<>();
        list.add(new Person(10,"张伟豪"));
        list.add(new Person(12,"张三丰"));
        list.add(new Person(15,"张三"));
        System.out.println(list);
    }

    @Test
    public void indexOf(){
        DynamicArray<Person> list = new DynamicArray<>();
        list.add(new Person(10,"张伟豪"));
        list.add(null);

        System.out.println(list.indexOf(null));
    }
    @Test
    public void trim(){
        DynamicArray2<Integer> list=new DynamicArray2<>();
        for (int i = 0; i < 50; i++) {
            list.add(i);
        }
        for (int i = 0; i < 50; i++) {
            list.remove(0);
        }
        System.out.println(list);
    }
}
