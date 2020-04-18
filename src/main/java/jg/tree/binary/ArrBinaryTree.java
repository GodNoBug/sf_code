package jg.tree.binary;

import lombok.AllArgsConstructor;

// 编写一个ArrBinaryTree,实现顺序存储二叉树遍历
// 顺序存储的二叉树

// 给定数组来存储二叉树
// 按照顺序存储结构定义,在此约定,用一组连续的存储单元依次自上而下、由左至右存储二叉树的节点元素
// n表示数组的下标,若树的节点编码为n,对应数组的下标.
// 其左孩子编码为2*n+1
// 其右孩子编码为2*n+2
// 其父节点编码为(n-1)/2   1/2 赋给int类型输出为0
// 0为根节点

// 在堆排序中使用到顺序存储二叉树
@AllArgsConstructor
public class  ArrBinaryTree {
    private int[] arr; // 存储数据

    public void preOrder(){
        preOrder(0);
    }
    // 前序遍历
    public void preOrder(int index) {
        // 如果数组为空,或者arr.length = 0
        if (arr == null || arr.length == 0) {
            System.out.println("数组为空,不能按照二叉树的前序遍历");
        }
        System.out.println(arr[index]);
        // 向左递归遍历
        if ((2 * index + 1) < arr.length)
            preOrder(2 * index + 1);
        // 向右递归遍历
        if ((2*index+2<arr.length))
            preOrder(2*index+2);

    }
}