package jg.linear_list.queue.priority;

import org.junit.Test;

public class Main {
    @Test
    public void test1(){
        PriorityQueue<Person> queue=new PriorityQueue<>();
        queue.enQueue(new Person("Jack",2));
        queue.enQueue(new Person("Rose",10));
        queue.enQueue(new Person("Jake",5));
        queue.enQueue(new Person("James",15));

        while (!queue.isEmpty()){
            System.out.println(queue.deQueue());
        }
    }
}
