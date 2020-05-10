package jg.other.bloom_filter;

import org.junit.Test;

public class Main {
    @Test
    public void test1() {
        BloomFilter<Integer> bf = new BloomFilter<>(1_00_0000, 0.01);
        for (int i = 1; i <= 1_00_0000; i++) {
            bf.put(i);
        }
        int count = 0;
        for (int i = 1_00_0001; i <= 2_00_0000; i++) {
            if (bf.contains(i)) {
                count++;
            }
        }
        System.out.println(count); // 误判数
    }

    @Test
    public void spider() {
        String[] urls = {};
        BloomFilter<String> bf = new BloomFilter<>(10_0000_0000, 0.01);
//        for (String url : urls) {
//            if (bf.contains(url)) continue; // 可能漏掉,但是不可能重复
//            // 爬这个url
//            // 放进过滤器中
//            bf.put(url);
//        }
        for (String url : urls) {
            if (bf.put(url)==false) continue; // 可能漏掉,但是不可能重复
            // 爬这个url
            //....
        }

    }


}
