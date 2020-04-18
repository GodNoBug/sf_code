package jg.tree.binary.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person implements Comparable<Person> {
    private int age;
    private String username;

    public Person(int age, String username) {
        this.age = age;
        this.username = username;
    }

    public Person(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Person e) {
        return age - e.age;
    }
}
