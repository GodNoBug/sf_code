package jg.tree.binary;



import lombok.Getter;
import lombok.Setter;
// 定义实现线索化功能的二叉树   中序线索化
// 难点就在当前结点的后继结点是父节点的，而父节点无法直接通过当前结点获得，所以只能返回上层递归把它作为上层结点的前驱结点或者左子节点
@Setter
public class ThreadedBinaryTree {
    private Node root; // 从root开始
    // 为了实现线索化需要创建一个指向当前节点的前驱节点的指针
    private Node pre = null;  // 在递归进行线索化时,pre总是保留当前节点的前一个节点

    public ThreadedBinaryTree(Node root) {
        this.root = root;
    }

    /*************************************线索化*************************************/

    public void threadedNodes() {
        this.threadedNodes(root);
    }

    private void threadedNodes(Node node) {
        if (node == null) // 如果当前需要线索化的节点为空,则不能线索化
            return;
        // 先处理左子树
        threadedNodes(node.getLeft());
        // 再处理当前节点
        //   处理当前节点的前驱节点
        if (node.getLeft() == null) {
            // 让当前节点的左指正指向前驱节点
            node.setLeft(pre);
            // 修改当前节点的左指针的类型
            node.setLeftType(1);
        }

        // 处理后续节点
        if (pre != null && pre.getRight() == null) {
            // 让前驱节点的右指针指向当前节点
            pre.setRight(node);
            // 修改前驱节点的右指针类型
            pre.setRightType(1);
        }
        // !!! 每处理一个节点后,让当前结点是下一个节点的前驱节点
        pre = node;
        // 后处理右子树
        threadedNodes(node.getRight());
    }

    /**************************************遍历**************************************/
    // 不能按照原先的遍历方式来遍历,因为条件是左子树或右子树判空,会出现死循环
    // 这时需要使用新的方式遍历线索化二叉树,各个节点可以通过线行方式遍历,因此无需使用递归方式,这样也提高了遍历的效率
    // 遍历的次序应当和中序遍历保持一致


    // 前序遍历
    public void preOrder() {
        if (this.root != null) {
            this.root.preOrder();
        } else {
            System.out.println("二叉树为空,无法遍历");
        }
    }

    // 中序遍历线索化二叉树
    public void threadedList() {
        // 定义一个变量,存储当前遍历的节点,从root开始
        Node node = root;
        while (node != null) {
            // 循环的找到leftType=1的节点,第一个找到的应该就是8
            // 后面随着遍历而变化,因为当leftType==1是,说明该节点是按照线索化处理后的有效节点
            while (node.getLeftType() == 0) {
                node = node.getLeft();
            }
            // 打印当前节点
            System.out.println(node);
            // 如果当前节点的右指针指向的是后续节点,就一直输出
            while (node.getRightType() == 1) {
                // 获取当前节点的后续节点
                node= node.getRight();
                System.out.println(node);
            }
            // 替换这个遍历的节点
            node = node.getRight();
        }
    }

    // 后续遍历
    public void postOrder() {
        if (this.root != null) {
            this.root.postOrder();
        } else {
            System.out.println("二叉树为空,无法遍历");
        }
    }

    /**************************************查询**************************************/
    // 前序查找
    public Node preOrderSearch(int no) {
        if (root != null) {
            return root.preOrderSearch(no);
        } else {
            return null;
        }
    }

    // 中序查找
    public Node infixOrderSearch(int no) {
        if (root != null) {
            return root.infixOrderSearch(no);
        } else {
            return null;
        }
    }

    // 后序查找
    public Node postOrderSearch(int no) {
        if (root != null) {
            return root.postOrderSearch(no);
        } else {
            return null;
        }
    }

    /**************************************删除**************************************/
    public void delete(int no) {
        if (this.root != null) {
            // 如果只有一个root节点,这里立即判断root是不是就要删除的节点
            if (root.getNo() == no) {
                root = null;
            } else {
                // 递归删除
                root.delete(no);
            }
        } else {
            System.out.println("空树,不能删除");
        }
    }

    // 基本单位Node
    @Getter
    @Setter
    private static class Node {
        private int no;
        private String name;
        private Node left;  // 默认null
        private Node right; // 默认null

        // 1. 如果leftType == 0 表示左子树 ,如果 1 表示指向前驱节点
        // 2. 如果rightType == 0 表示右子树 ,如果 1 表示指向后驱节点
        private int leftType;
        private int rightType;

        public Node(int no, String name) {
            this.no = no;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "no=" + no +
                    ", name='" + name + '\'' +
                    '}';
        }

        // TODO 如何添加元素

        /**************************************遍历**************************************/

        // 编写前序遍历方法
        public void preOrder() {
            //先输出父节点
            System.out.println(this);
            // 递归向左子树前序遍历
            if (this.left != null) {
                this.left.preOrder();
            }
            if (this.right != null) {
                this.right.preOrder();
            }
        }

        // 编写中序遍历
        public void infixOrder() {
            //递归向左子树中序遍历
            if (this.left != null) {
                this.left.infixOrder();
            }
            System.out.println(this);
            if (this.right != null) {
                this.right.infixOrder();
            }
        }

        // 编写后序遍历
        public void postOrder() {
            //递归向左子树中序遍历
            if (this.left != null) {
                this.left.postOrder();
            }
            if (this.right != null) {
                this.right.postOrder();
            }
            System.out.println(this);
        }

        /**************************************查找**************************************/
        // 前序查找
        public Node preOrderSearch(int no) {
            // 比较当前节点是不是
            if (this.no == no) {
                return this;
            }
            // 1.则判断当前节点的左子节点是否为空,如果不为空,则递归前序查找
            // 2.如果左递归前序查找,找到结点,则返回
            Node resNode = null;
            if (this.left != null) {
                resNode = this.left.preOrderSearch(no);
            }
            if (resNode != null) {
                return resNode;
            }

            if (this.right != null) {
                resNode = this.right.preOrderSearch(no);
            }
            // 如果找没找到都得返回了
            return resNode;
        }

        // 中序遍历查找
        public Node infixOrderSearch(int no) {
            // 判断当前节点的左子节点是否为空,如果不为空,则递归中序查找
            Node resNode = null;
            if (this.left != null) {
                resNode = this.left.infixOrderSearch(no);
            }
            if (resNode != null) {
                return resNode;
            }
            // 如果找到,则返回,如果没有找到,就和当前节点比较,如果是则返回当前节点
            if (this.no == no) {
                return this;
            }
            // 否则继续进行右递归中序查找
            if (this.right != null) {
                resNode = this.right.infixOrderSearch(no);
            }
            return resNode;

        }

        // 后序遍历查找
        public Node postOrderSearch(int no) {
            // 判断当前节点的左子节点是否为空,如果不为空,则递归后序查找
            Node resNode = null;
            if (this.left != null) {
                resNode = this.left.postOrderSearch(no);
            }
            if (resNode != null) { // 说明在左子树找到
                return resNode;
            }
            // 如果左子树没有找到,则想右子树递归进行后续遍历查找
            if (this.right != null) {
                resNode = this.right.postOrderSearch(no);
            }
            // 如果左右子树都没有找到,就比较当前节点是不是
            if (this.no == no) {
                return this;
            }
            return resNode;
        }

        /**************************************删除**************************************/
        // 并不完整,因为不是二叉排序树,而是二叉树,这删除后对子树的操作不好定,后续写到二叉排序树再说
        public void delete(int no) {
            //1.因为二叉树是单向的,所以我们是判断当前节点的子节点是否需要删除,而不能去判断当前节点是否需要删除节点
            //2.如果当前节点的左子节点不为空,并且左子节点就是要删除的节点
            if (this.left != null && this.left.no == no) {
                this.left = null;
                return;
            }
            //3.如果当前节点的右子节点不为空,并且右子节点就是要删除的节点
            if (this.right != null && this.right.no == no) {
                this.right = null;
                return;
            }
            // 如果2、3步没有删除节点,则向左递归删除
            if (this.left != null) {
                this.left.delete(no);
            }
            // 如果第4步也没有删除节点,则应该向右递归删除
            if (this.right != null) {
                this.right.delete(no);
            }
        }

        /**************************************排序**************************************/
    }

}

//  n个节点的二叉链表中含有n+1个空指针域,充分利用空指针域.存放指向该结点在某种遍历次序下的前驱和后续节点的指针
//  线索化二叉树分前序/中序/后续线索二叉树
//  前驱节点：对一棵二叉树进行中序遍历，遍历后的顺序，当前节点的前一个节点为该节点的前驱节点；
//  后继节点：对一棵二叉树进行中序遍历，遍历后的顺序，当前节点的后一个节点为该节点的后继节点；

