package jg.linear_list.hash;

import jg.linear_list.hash.pojo.Person;
import org.junit.Test;

public class Main {
    @Test
    public void test1(){
        Person person =new Person(10,1.67f,"jack");
        System.out.println(person.hashCode());
    }
}
