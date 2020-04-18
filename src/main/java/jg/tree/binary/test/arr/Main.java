package jg.tree.binary.test.arr;

import jg.tree.binary.ArrBinaryTree;
import org.junit.Test;


public class Main {
    @Test
    public  void test1() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7}; //用二叉树的前序遍历应该为
        ArrBinaryTree tree=new ArrBinaryTree(arr);// 1245367
        tree.preOrder();
    }
}

