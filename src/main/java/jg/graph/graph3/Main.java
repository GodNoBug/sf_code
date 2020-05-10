package jg.graph.graph3;

import jg.graph.data.Person;
import org.junit.Test;

import java.util.ArrayList;

public class Main {
    @Test
    public void graph() {
        ArrayList<Person> people = new ArrayList<>();
        Person p1=new Person("张伟豪", "zwh");
        Person p2=new Person("吴彦祖", "wyz");
        Person p3=new Person("彭于晏", "pyy");
        Person p4=new Person("郭富城", "gfc");
        people.add(p1);
        people.add(p2);
        people.add(p3);
        people.add(p4);
        Graph<Person, Integer> graph = new Graph<>(people,4,4);

        graph.addEdge(p1, p2, 10);
        graph.addEdge(p3, p4, 100);
        //graph.addEdge(p3, p4, 100);
        graph.addEdge(p1, p3, 100);
        System.out.println(graph);
    }
}
