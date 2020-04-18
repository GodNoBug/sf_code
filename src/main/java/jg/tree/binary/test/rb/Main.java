package jg.tree.binary.test.rb;

import jg.tree.binary.printer.BinaryTrees;
import jg.tree.binary.RBTree;
import org.junit.Test;


public class Main {
    public static Integer[] data = new Integer[]{
            55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50
    };

    @Test
    public void createRBTree() {
        RBTree<Integer> rb = new RBTree<>();
        for (int i = 0; i < data.length; i++) {
            rb.add(data[i]);
            System.out.println("["+data[i]+"]");
            BinaryTrees.println(rb);
            System.out.println("-----------------------------");
        }
    }

    @Test
    public void delete() {

        RBTree<Integer> rb = new RBTree<>();
        for (int i = 0; i < data.length; i++) {
            rb.add(data[i]);
        }
        BinaryTrees.println(rb);
        for (int i = 0; i < data.length; i++) {
            rb.remove(data[i]);
            System.out.println("----------------------");
            System.out.println("["+data[i]+"]");
            BinaryTrees.println(rb);
        }

    }
}
