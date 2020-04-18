package jg.tree.set;


import org.junit.Test;

public class Main {
    @Test
    public void listSet() {
        Set<Integer> listSet = new ListSet<>();
        add(listSet);
    }

    @Test
    public void treeSet() {
        Set<Integer> listSet = new TreeSet<>();
        add(listSet);
    }

    private void add(Set<Integer> listSet) {
        listSet.add(10);
        listSet.add(11);
        listSet.add(11);
        listSet.add(12);
        listSet.add(10);
        listSet.traversal(new Set.Visitor<Integer>() {
            @Override
            public boolean visit(Integer element) {
                System.out.println(element);
                return false;
            }
        });
    }

    @Test
    public void test(){
        // ????
        Set<String> set =new TreeSet2<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("c");
        set.traversal(new Set.Visitor<String>() {
            @Override
            public boolean visit(String element) {
                System.out.println(element);
                return false;
            }
        });
    }


}
