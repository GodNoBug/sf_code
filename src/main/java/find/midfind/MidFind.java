package find.midfind;


import org.junit.Test;

// 假设在 [begin, end) 范围内搜索某个元素 v,  mid == (begin + end) / 2
//◼ 如果 v < m, 去 [begin, mid) 范围内二分搜索
//◼ 如果 v > m,去 [mid + 1, end) 范围内二分搜索
//◼ 如果 v == m,直接返回 mid
public class MidFind {
    @Test
    public void test1() {
        int[] ints = {1,1,1,2,3,5,5,5,7,7,9};
        System.out.println(ints.length);
        System.out.println(search(ints, 6));
    }

    // 不考虑重复项
    public int binSearch(int[] nums, int target) {
        if (nums == null || nums.length == 0) return -1;

        int low = 0;
        int high = nums.length;

        while (low < high) {
            int mid = (low + high) >> 1;
            if (target < nums[mid]) {
                high = mid;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // 考虑重复项
    public int binSearch2(int[] nums, int target) {
        if (nums == null || nums.length == 0) return -1;

        int low = 0;
        int high = nums.length;

        while (low < high) {
            int mid = (low + high) >> 1;
            if (target < nums[mid]) {
                high = mid;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }



    // 搜索插入位置,要求返回第1个大于v的元素位置.
    // 假设在 [begin, end) 范围内搜索某个元素 v, mid == (begin + end) / 2
    //◼ 如果 v < m,去 [begin, mid) 范围内二分搜索
    //◼ 如果 v ≥ m,去 [mid + 1, end) 范围内二分搜索 [如果当前mid位置是等于v的,那么要插入的位置肯定是在mid后面]
    public int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) return -1;

        int low = 0;
        int high = nums.length;

        while (low < high) {
            int mid = (low + high) >> 1;
            System.out.println(mid);
            if (target < nums[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;

    }
}
