package jg.linear_list.queue;

import jg.linear_list.list.circle.CircleLinkedList;
import jg.linear_list.queue.circle.CircleDeque;
import jg.linear_list.queue.circle.CircleQueue;
import org.junit.Test;

public class Main {
    @Test
    public void queue() {
        Queue<Integer> queue = new Queue<>();
        queue.offer(11);
        queue.offer(22);
        queue.offer(33);
        queue.offer(44);
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }

    @Test
    public void queue2() {
        Queue2<Integer> queue = new Queue2<>();
        queue.offer(11);
        queue.offer(22);
        queue.offer(33);
        queue.offer(44);
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }

    @Test
    public void deque() {
        Deque<Integer> deque = new Deque<>();
        deque.enQueueFront(11);
        deque.enQueueFront(22);
        deque.enQueueRear(33);
        deque.enQueueRear(44);

        // 尾 44 33 11 22 头

        while (!deque.isEmpty()) {
            System.out.println(deque.deQueueFront());
        }
    }

    @Test
    public void circleQueue() {
        CircleQueue<Integer> queue = new CircleQueue<>();
        // 0 1 2 3 4 5 6 7 8 9
        for (int i = 0; i < 10; i++) {
            queue.offer(i);
        }
        // null null null null null 5 6 7 8 9
        for (int i = 0; i < 5; i++) {
            queue.poll();
        }
        //  15 16 17 18 19 | 5 6 7 8 9
        for (int i = 15; i < 23; i++) {
            queue.offer(i);
        }
        System.out.println(queue);
        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }

    @Test
    public void circleDeque() {
        CircleDeque<Integer> deque = new CircleDeque<>();
        // 头  5 4 3 2 1 100 101 102 103 104 105 106 8 7 6 尾
        // 头  8 7 6 5 4 3 2 1 100 101 102 103 104 105 106  107 108 109 null null 10 9尾
        for (int i = 0; i < 10; i++) {
            deque.enQueueFront(i + 1);
            deque.enQueueRear(i + 100);
        }
        // 头  null 7 6 5 4 3 2 1 100 101 102 103 104 105 106  null null null null null null null尾
        for (int i = 0; i < 3; i++) {
            deque.deQueueFront();
            deque.deQueueRear();
        }
        // 头  11 7 6 5 4 3 2 1 100 101 102 103 104 105 106  null null null null null null 12尾
        deque.enQueueFront(11);
        deque.enQueueFront(12);
        System.out.println(deque);
        while (!deque.isEmpty()) {
            System.out.println(deque.deQueueFront());
        }
    }

}
