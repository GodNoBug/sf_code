package jg.tree.binary.test.bst;

import jg.tree.binary.BST;
import jg.tree.binary.file.Files;
import jg.tree.binary.pojo.Person;
import jg.tree.binary.printer.BinaryTreeInfo;
import jg.tree.binary.printer.BinaryTrees;
import org.junit.Test;


import java.util.Comparator;
@SuppressWarnings("unused")
public class Main {
    public static Integer[] data = new Integer[]{
            7, 4, 9, 2, 5, 8, 11, 1, 3  // 层序遍历的顺序添加
    };

    //
    @Test
    public void test1() {
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);

        System.out.println(bst.isComplete());
    }

    @Test
    public void comparator1() {
        BST<Person> bst = new BST<>(new PersonComparator());
        for (int i = 0; i < data.length; i++) {
            bst.add(new Person(data[i]));
        }
        BinaryTrees.println(bst);
    }

    @Test
    public void comparator2() {
        BST<Person> bst = new BST<>(new PersonComparator2());
        for (int i = 0; i < data.length; i++) {
            bst.add(new Person(data[i]));
        }
        BinaryTrees.println(bst);
    }

    // 大型数生成测试
    @Test
    public void bigData() {
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < 3000; i++) {
            bst.add((int) (Math.random() * 100));
        }
        // BinaryTrees.println(bst);
        String str = BinaryTrees.printString(bst);
        str += "\n";
        System.out.println(bst.height2());
        Files.writeToFile("D:/1.txt", str);
    }

    // 打印相关测试
    @Test
    public void print() {
        BinaryTrees.println(new BinaryTreeInfo() {
            @Override
            public Object root() {
                return "A";
            }

            @Override
            public Object left(Object node) {
                if (node.equals("A")) return "B";
                if (node.equals("C")) return "D";
                return null;
            }

            @Override
            public Object right(Object node) {
                if (node.equals("A")) return "C";
                if (node.equals("C")) return "E";
                return null;
            }

            @Override
            public Object string(Object node) {
                return node;
            }
        });
    }


    // 值覆盖测试
    @Test
    public void coverTest() {
        BST<Person> bst = new BST<>();
        bst.add(new Person(10, "jack"));
        bst.add(new Person(12, "jack"));
        bst.add(new Person(6, "jack"));

        bst.add(new Person(10, "michael"));

        BinaryTrees.println(bst);
    }

    // 遍历测试
    @Test
    public void traversal() {
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
        bst.preOrder2();
        // bst.preOrder();
        // bst.inOrder();
        //bst.postOrder();
        // bst.levelOrder();
       /* bst.postOrder(new BinarySearchTree.Visitor<Integer>() {
            @Override
            boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 2;
            }
        });
        System.out.println();
        bst.inOrder(new BinarySearchTree.Visitor<Integer>() {
            @Override
            boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 4;
            }
        });
        System.out.println();
        bst.postOrder(new BinarySearchTree.Visitor<Integer>() {
            @Override
            boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 4;
            }
        });
        System.out.println();
        bst.levelOrder(new BinarySearchTree.Visitor<Integer>() {
            @Override
            boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 9;
            }
        });
        System.out.println();*/
       // System.out.println(bst);
        //System.out.println(bst.height());
    }


    @Test
    public void height(){
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        System.out.println(bst.height());
    }

    @Test
    public void invertTree(){
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
        //bst.invertTree();
        bst.invertTree2();
        BinaryTrees.println(bst);
    }

    @Test
    public void remove() {
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
        bst.remove(7);
        BinaryTrees.println(bst);
    }
    @Test
    public void successorAndPredecessor(){
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);
        System.out.println(bst.predecessor(4));
        System.out.println(bst.successor(4));
    }

    private static class PersonComparator implements Comparator<Person> {
        @Override
        public int compare(Person e1, Person e2) {
            return e1.getAge() - e2.getAge();
        }
    }

    private static class PersonComparator2 implements Comparator<Person> {
        @Override
        public int compare(Person e1, Person e2) {
            return e2.getAge() - e1.getAge();
        }
    }
}
