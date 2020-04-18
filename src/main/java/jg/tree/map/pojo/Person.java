package jg.tree.map.pojo;

import java.util.Objects;

/**
 * 重写HashCode和equals
 * hashCode定位到数组下标,hashCode相同(冲突)
 * 能放到同一数组索引下的元素,其hash值不一定是一样的,而由hash算出来的索引下标是一样的.
 * 不同的值计算出来的hash值可能会一样,不能进一步的检测两个key是一样的
 * 则用equals判断是否是同一个key,如果相同可以覆盖
 */
public class Person {
    private int age;
    private float height;
    private String name;

    public Person(int age, float height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }

    /**
     *  自定义对象作为key,最好同时重写hashCode、equals方法,
     *  equals用以判断2个key是否为同一个key
     *  -- 自反性: 对于任何非null的x,x.equals(x)必须返回true
     *  -- 对称性: 对于任何非null的x、y,如果y.equals(x)返回true,x.equals(y)必须返回true
     *  -- 传递性: 对于任何非null的x、y、z,如果x.equals(y)、y.equals(z)返回true,那么x.equals(z)必须返回true
     *  -- 一致性: 对于任何非null的x、y,只要equals的比较操作在对象中所用的信息没有被修改,多次调用x.equals(y)就会一致地返回true,或者一致地返回false.
     *  -- 对于任何非null的x,x.equals(null)必须返回false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 内存地址一样,铁定相等,就直接返回true
        if (o == null || getClass() != o.getClass()) return false; // 如果不是同一类型或者传进来的是null,返回false
        Person person = (Person) o;
        return age == person.age &&
                Float.compare(person.height, height) == 0 &&
                Objects.equals(person.name, name);
    }



    /**
     *不重写,仍是调用父类的hashCode,是对象地址值相关'
     * 应该充分利用成员变量信息,分别算出成员变量的hash
     * hashCode: 必须保证equals为true的2个key的哈希值一样[因为根据成员变量算出的]
     * 反过来hashCode相等的key,不一定equals为true
     */
    @Override
    public int hashCode() {
        int hashCode=Integer.hashCode(age);
        hashCode=hashCode*31 +Float.hashCode(height);
        hashCode=hashCode*31 + (name!=null?name.hashCode():0);
        return hashCode;
    }
}
