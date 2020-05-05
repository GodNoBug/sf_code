package sf.sort.sort.no_cmp.sleep;

import org.junit.Test;
// 最垃圾的排序,睡的时间最短最先输出
public class SleepSort {
    public static class SortThread extends Thread{
        private int value;

        public SortThread(int value) {
            this.value = value;
        }


        @Override
        public void run() {
            try {
                Thread.sleep(value);
                System.out.println(value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    @Test
    public void test1() throws InterruptedException {

        int [] arr ={10,100,50,30,60};
        for (int i : arr) {
            new SortThread(i).start();
        }
        Thread.sleep(10000);
    }
}
