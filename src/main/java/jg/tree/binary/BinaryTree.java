package jg.tree.binary;


import jg.linear_list.stack.Stack;
import jg.tree.binary.printer.BinaryTreeInfo;
import lombok.ToString;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树
 * 规定: TODO 不同教材有不同规定,待比较
 * 结点的深度也是从根结点开始数，是第几层也决定于根结点在第0层还是第1层。
 * 结点的高度则取决于它的子树，该节点子树中最远的那个叶子结点作为1开始数。
 *
 * @param <E>
 */
public class BinaryTree<E> implements BinaryTreeInfo {
    protected int size;      // 树大小
    protected Node<E> root;

    // 元素数量
    public int size() {
        return size;
    }

    // 是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 清空所有元素
    public void clear() {
        root = null;
        size = 0;
    }

    /***********************************************树的遍历增强*********************************************************/
    public void levelOrder(Visitor<E> visitor) {
        if (root == null || visitor == null) return;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (visitor.visit(node.element))  // 符合条件停止遍历,增强性能
                return;
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }


    public void preOrder(Visitor<E> visitor) {
        if (visitor == null) return;
        preOrder(root, visitor);
    }

    private void preOrder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;   // 符合条件停止遍历,增强性能
        //if (visitor.stop) return; //因为重复了
        visitor.stop = visitor.visit(node.element);
        preOrder(node.left, visitor);
        preOrder(node.right, visitor);
    }

    public void inOrder(Visitor<E> visitor) {
        if (visitor == null) return;
        inOrder(root, visitor);
    }

    private void inOrder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;  // 符合条件停止遍历,增强性能
        inOrder(node.left, visitor);
        if (visitor.stop) return;
        visitor.stop = visitor.visit(node.element);
        inOrder(node.right, visitor);
    }

    public void postOrder(Visitor<E> visitor) {
        if (visitor == null) return;
        postOrder(root, visitor);
    }

    private void postOrder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;  // 符合条件停止遍历,增强性能
        postOrder(node.left, visitor);
        postOrder(node.right, visitor);

        if (visitor.stop) return;   // 递归仍在执行,但不会打印,在句首也判断,此句也有存在的意义. visitor.stop 防止打印
        visitor.stop = visitor.visit(node.element);
    }

    // 前序遍历-非递归方式实现
    // 利用栈实现 - 思路1
    // 1.设置node=root
    // 2.循环执行以下操作
    //   a.如果node!=null
    //     对node进行访问
    //     对node.right入栈
    //     设置node=node.left
    //   b.如果node==null
    //     如果栈为空,结束遍历
    //     如果栈不为空,弹出栈顶元素并复制给node


    public void preOrder2(Visitor<E> visitor) {
        if (visitor == null || root == null) return;
        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>();
        while (true) {
            if (node != null) {
                Node<E> pop = stack.pop();
                // 访问node节点
                if (visitor.visit(node.element)) return;
                // 将右子节点入栈
                if (node.right != null) {
                    stack.push(node.right);
                }
                // 向左走
                node = node.left;
            } else if (stack.isEmpty()) {
                return;
            } else {  // 出来右边节点
                node = stack.pop();
            }
        }
    }

    // 利用栈实现 - 思路2
    // 1.将root入栈
    // 2.循环执行以下操作,直到栈为空
    //  弹出栈顶节点top,进行访问
    //  将top.right入栈
    //  将top.left入栈
    public void preOrder3(Visitor<E> visitor) {
        if (visitor == null || root == null) return;
        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<E> node = stack.pop();
            // 访问node节点
            if (visitor.visit(node.element)) return;

            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
    }

    // 中序遍历-非递归
    // 利用栈实现
    // 1.设置node=root
    // 2.循环执行以下操作
    //   a.如果node!=null
    //     将node入栈
    //     设置node=node.left
    //   b.如果node==null [不能再左了]
    //     如果栈为空,结束遍历
    //     如果栈不为空,弹出栈顶元素并赋值给node
    //        对node进行访问
    //         node=node.right
    public void inOrder2(Visitor<E> visitor) {
        if (visitor == null || root == null) return;
        Node<E> node = root;
        Stack<Node<E>> stack = new Stack<>();
        while (true) {
            if (node != null) {
                stack.push(node);
                node = node.left;  // 一路往左走
            }
            if (stack.isEmpty()) {
                return;
            } else {
                node = stack.pop();
                if (visitor.visit(node.element)) return;
                node = node.right;  // 让右节点进行中序遍历
            }
        }
    }

    // 后续遍历-非递归
    // 利用栈实现
    // 1.将root入栈
    // 2.循环执行以下操作,直到栈为空
    //   如果栈节点是叶子节点 或者 上一次访问的节点是栈顶节点的子节点,弹出栈顶节点进行访问.
    //   否则,将栈顶节点的right、left按顺序入栈
    public void postOrder2(Visitor<E> visitor) {
        if (visitor == null || root == null) return;

        // 记录上一次弹出访问的节点.
        Node<E> prev = null;
        Stack<Node<E>> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node<E> top = stack.top();  // 查看栈顶元素
            if (top.isLeaf() || (prev != null && prev.parent == top)) {  // 如果是叶子节点或上一次弹出的不为空并且是这一次弹出的子节点
                prev = stack.pop();
                // 访问结点
                if (visitor.visit(prev.element)) return;
            } else {
                if (top.right != null) {
                    stack.push(top.right);
                }
                if (top.left != null) {
                    stack.push(top.left);
                }
            }
        }

    }

    /*-----------------------------------------求任意节点的前驱节点/后继节点----------------------------------------------/
    /**
     * 前驱节点(predecessor)
     * 1. 中序遍历是的前一个节点
     * 2. 根据二叉搜索树的性质,意味着前驱节点就是前一个比当前节点小的节点(左子树中最大的节点)
     * 3. 从一般性二叉树来说,
     * <p>
     * 代码思路:
     * 对于一般性的二叉树
     * 1.当前节点的左子树不为空,左子树的最右的元素即是前驱节点
     * - node.left!=null
     * - predecessor=node.left.right.right.right.right.....
     * - 终止条件: right为null找到
     * 2.当前节点的左子树为空,且父亲不为空,往上找,直到是祖父节点的右子树中,那么这个祖父节点就是前驱
     * - node.left==null && node.parent!=null
     * - predecessor=node.parent.parent.....
     * - 终止条件: node在parent的右子树中
     * 3.node.left==null && node.parent==null
     * - 那就没有前驱节点,返回的也是null
     */
    public E predecessor(E element) {
        return predecessor(node(element)).element;
    }

    public E successor(E element) {
        return successor(node(element)).element;
    }

    protected Node<E> node(E element) {
        return null;
    }

    protected Node<E> predecessor(Node<E> node) {
        if (node == null) return null;
        Node<E> p = node.left; // 假定前驱就是左子树中(left.right.right.right.right.....)
        // 1. 左子树不为空,前驱节点就是左子树一直往右找,直到不能再右为止返回节点
        if (p != null) {
            while (p.right != null) {
                p = p.right;
            }
            return p;
        }

        // 能到这里左子树是空的
        // 从父节点,祖父节点中寻找前驱节点,父节点不为空,且是父节点的左子树,循环一直执行,直到当前节点是父节点的左子结点为止
        // 那么当前节点的父节点就是目标节点的前驱节点
        while (node.parent != null && node == node.parent.left) {
            node = node.parent;
        }
        // node.parent==null
        // node==node.parent.right
        return node.parent;
    }

    /**
     * 后续节点(successor):
     * 1.中序遍历的后一个节点
     * 2.如果是二叉搜索树,后续节点就是后一个比它大的节点
     * <p>
     * 对于一般的二叉树
     * 1.当前右子树不为空,左子树的最左元素即是前驱节点
     * - node.right !=null
     * - successor=right.left.left.left....
     * - 终止条件: left为null找到
     * 2.当前节点的右子树为空,且父亲不为空
     * - node.right==null && node.parent!=null
     * - predecessor=node.parent.parent.....
     * - 终止条件: node在parent的左子树中
     * 3.node.right==null && node.parent==null
     * - 那就没有前驱节点,返回的也是null
     */
    protected Node<E> successor(Node<E> node) {
        if (node == null) return null;
        Node<E> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    /***********************************************树的高度************************************************************/
    // 二叉树的高度 递归法
    // 二叉树的高度其实是根节点的高度,从当前节点开始到最远的子节点的距离
    public int height() {
        return height(root);
    }

    // 当前节点的高度=左右子节点高度最大的一个+1
    private int height(Node<E> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 层序遍历法获取树的高度,
    // 每访问完一层,也就是每一层被访问出队后,height++
    // 规律: 每当每一层的最后一个元素访问完后,队列剩下的就是下一层的元素数量,
    public int height2() {
        if (root == null) return 0;
        int height = 0;      // 树的高度
        int leaveSize = 1;   // 每一层的元素数量,默认一个[根节点那一层]
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            leaveSize--;  // 一旦减为0代表一层访问完毕
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (leaveSize == 0) { // 意味着即将访问下一层,重新赋值leaveSize,height++
                leaveSize = queue.size();
                height++;
            }
        }
        return height;
    }
    /**********************************************是否完全二叉树*************************************************************/
    /**
     * TODO 利用层序遍历判断是否是完全二叉树
     * 层序遍历法判断是否是完全二叉树.
     * 1.如果左边为空,右边不为空,则不为完全二叉树
     * 2.左右为空,或者左不为空,右为空,接下来层序遍历的节点都应该为叶子节点,才是完全二叉树.否则不是,返回false
     * 代码实现思路:
     * 如果树为空,则返回false
     * 如果树不为空,开始层序遍历二叉树(用队列)
     * -如果node.left!=null && node.right!=null,将node.left、node.right按顺序入队
     * -如果node.left==null&&node.right!=null,返回false
     * -如果node.left!=null&&node.right==null 或者node.left!=null && node.right==null 那么后面遍历的节点应该都为叶子节点,才是完全二叉树否则返回false
     * 遍历结束,返回true
     * 小心判断bug
     */
    public boolean isComplete() {
        if (root == null) return false;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false;
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) {  // TODO 前一节点为叶子节点,并且当前节点不为叶子节点的话返回false
                return false;
            }

            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) { // node.left==null && node.right!=null
                return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else {  // 后面遍历的节点都必须是叶子节点
                // node.left==null && node.right==null
                // node.left!=null && node.right==null
                leaf = true;
            }
        }
        return true; // 遍历结束,返回true
    }

    /***********************************************树的节点************************************************************/
    protected static class Node<E> {
        E element;
        // 沿着"线"找,如何记住这些"线"
        Node<E> left;
        Node<E> right;
        Node<E> parent;

        public Node(E element, Node<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        // 判断度为2
        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        // 当前节点是否是父节点的左孩子
        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        // 当前节点是否是父节点的右孩子
        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        // 返回兄弟节点
        public Node<E> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }
            if (isRightChild()) {
                return parent.left;
            }
            return null;//没有兄弟节点
        }

        @Override
        public String toString() {
            return "Node{" +
                    "element=" + element +
                    '}';
        }
    }

    /**
     * 访问器,允许遍历定制化
     *
     * @param <E>
     */
    public static abstract class Visitor<E> {
        boolean stop;

        /**
         * @return 如果返回true, 就代表停止遍历
         */
        protected abstract boolean visit(E element);
    }

    protected Node<E> createNode(E element, Node<E> parent) {
        return new Node<>(element, parent);
    }


    /***********************************打印器,便于打印,与知识无关****************************************/
    @Override
    public Object root() {
        return this.root;
    }

    @Override
    public Object left(Object node) {
        return ((Node) node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node) node).right;
    }

/*    @Override
    public Object string(Object node) {
        Node<E> myNode = (Node<E>) node;
        String parentString = "null";
        if (myNode.parent != null) {
            parentString = myNode.parent.element.toString();
        }
        return myNode.element + "_p(" + parentString + ")";
    }*/

    @Override
    public Object string(Object node) {
        //return ((Node) node).element;
        return node;
    }


}
