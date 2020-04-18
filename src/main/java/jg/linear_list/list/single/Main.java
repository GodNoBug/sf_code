package jg.linear_list.list.single;

public class    Main {
    public static void main(String[] args) {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.add(20);
        list.add(0, 10);
        list.add(30);
        list.add(list.size(), 40);
        //[10, 20, 30, 40]
        //list.remove(1);
        //[10, 30, 40]
        System.out.println(list);
        list.reverse2();
        System.out.println(list);

    }
}
