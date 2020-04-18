package jg.tree.binary.test.avl;

import jg.tree.binary.printer.BinaryTrees;
import jg.tree.binary.AVLTree;
import jg.tree.binary.BST;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unused")
public class Main {
    public static Integer[] data = new Integer[]{
            85, 19, 69, 3, 7, 99, 95, 2, 1, 70, 44, 58, 11, 21, 14, 93, 57, 4, 56
    };
    public static Integer[] del = new Integer[]{
            85, 19, 69, 3, 7, 99, 95
    };
    @Test
    public void test1() {
        AVLTree<Integer> avl = new AVLTree<>();
        for (int i = 0; i < data.length; i++) {
            avl.add(data[i]);
            System.out.println("[" + data[i] + "]");
            BinaryTrees.println(avl); // afterAdd注释掉比较
            System.out.println("----------------------");
        }
    }
    @Test
    public void test2() {
        AVLTree<Integer> avl = new AVLTree<>();
        for (int i = 0; i < del.length; i++) {
            avl.add(del[i]);
        }
        for (int i = 0; i < del.length; i++) {
            avl.remove(del[i]);
            System.out.println("[" + del[i] + "]");
            BinaryTrees.println(avl); // afterAdd注释掉比较
            System.out.println("----------------------");
        }
    }
    @Test
    public void test3(){
        List<Integer> data =new ArrayList<>();
        for (int i = 0; i < 100_0000; i++) {
            data.add((int) (Math.random() * 100_0000));
        }
        BST<Integer> bst =new BST<>();
        for (int i = 0; i < data.size(); i++) {
            bst.add(data.get(i));
        }
        AVLTree<Integer> avl =new AVLTree<>();
        // TODO测试增删改查的
    }
}
