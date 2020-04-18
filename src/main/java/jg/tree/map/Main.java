package jg.tree.map;

import jg.tree.map.pojo.Person;
import org.junit.Test;

import javax.swing.*;

public class Main {
    @Test
    public void test1(){
        Person person =new Person(10,1.67f,"jack");
        System.out.println(person.hashCode());
    }

    @Test
    public void test2(){
        Map<String,Integer> map=new TreeMap<>();
        map.put("class",2);
        map.put("public",5);
        map.put("test",6);
        map.put("public",8);

        map.traversal(new Map.Visitor<String, Integer>() {
            @Override
            public boolean visit(String key, Integer value) {
                System.out.println("key:"+key+" value:"+value);
                return false;
            }
        });
    }
    @Test
    public void test3(){
        Person p1=new Person(10,1.67f,"jack");
        Person p2=new Person(10,1.67f,"jack");
        HashMap<Object,Integer> map =new HashMap<>();
        map.put(p1,1);
        map.put(p2,3);
        System.out.println(map.size());
        System.out.println(map.get(p2));
        map.traversal(new Map.Visitor<Object, Integer>() {
            @Override
            public boolean visit(Object key, Integer value) {
                System.out.println("key:"+key+" value:"+value);
                return false;
            }
        });
        map.print();
    }
}
