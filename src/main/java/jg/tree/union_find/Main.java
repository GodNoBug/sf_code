package jg.tree.union_find;

import jg.tree.union_find.pojo.Student;
import jg.tree.union_find.union.*;
import org.junit.Test;
import sort.tools.Times;

public class Main {
    @Test
    public void qf() {
        // 初始化了
        UnionFind uf = new UnionFind_QF(12);
        // 合并
        union(uf);
    }
    @Test
    public void qu(){
        // 初始化了
        UnionFind uf = new UnionFind_QU(12);
        // 合并
        union(uf);
    }
    @Test
    public void qu_size(){
        // 初始化了
        UnionFind uf = new UnionFind_QU_Size(12);
        // 合并
        union(uf);
    }

    private void union(UnionFind uf) {
        uf.union(0, 1);
        uf.union(0, 3);
        uf.union(0, 4);
        uf.union(2, 3);
        uf.union(2, 5);

        uf.union(6, 7);

        uf.union(8, 10);
        uf.union(9, 10);
        uf.union(9, 11);

        System.out.println(uf.isSame(0, 6));
        System.out.println(uf.isSame(0, 5));

        uf.union(4,6);
        System.out.println(uf.isSame(2, 7));
    }

    private static final int count=100000;
    @Test
    public void test1(){
//        time(new UnionFind_QF(count));
//        time(new UnionFind_QU(count));
//        time(new UnionFind_QU_Size(count));
//        time(new UnionFind_QU_Rank(count));
//        time(new UnionFind_QU_Rank_Path_Compression(count));
//        time(new UnionFind_QU_Rank_Path_Spliting(count));
//        time(new UnionFind_QU_Rank_Path_Halving(count));
        GenericUnionFind<Student> uf = new GenericUnionFind<>();
        Student stu1=new Student(1,"jack");
        Student stu2=new Student(2,"rose");
        Student stu3=new Student(3,"jack");
        Student stu4=new Student(4,"rose");

        uf.makeSet(stu1);
        uf.makeSet(stu2);
        uf.makeSet(stu3);
        uf.makeSet(stu4);

        uf.union(stu1,stu2);
        uf.union(stu3,stu4);

        uf.union(stu1,stu4);
        System.out.println(uf.isSame(stu1, stu2));
        System.out.println(uf.isSame(stu3, stu4));
        System.out.println(uf.isSame(stu1, stu3));

    }

    public void time(UnionFind uf){
        Times.test(uf.getClass().getSimpleName(),()->{
            for (int i = 0; i < count; i++) {
                uf.union((int) (Math.random() * count), (int) (Math.random() * count));
            }
            for (int i = 0; i < count; i++) {
                uf.isSame((int) (Math.random() * count), (int) (Math.random() * count));
            }
        });
    }

}
