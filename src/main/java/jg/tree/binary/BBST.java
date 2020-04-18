package jg.tree.binary;

import java.util.Comparator;

/**
 * 平衡二叉搜索树
 * @param <E>
 */
public class BBST<E> extends BST<E> {

    public BBST() {
        this(null);
    }

    public BBST(Comparator<E> comparator) {
        super(comparator);
    }

    // 左旋 RR
    // 有些教程里面: 把左旋转叫做zag,旋转之后的状态叫做zagged
    protected void rotateLeft(Node<E> g) {
        // 旋转
        Node<E> p = g.right;
        Node<E> child = p.left;
        g.right = child;
        p.left = g;
        // 维护相关内容,更新相关parent和相关height
        // 让p成为子树的根节点
        afterRotate(g, p, child);
    }

    // 右旋 LL
    // 有些教程里面: 把右旋转叫做zig,旋转之后的状态叫做zigged
    protected void rotateRight(Node<E> g) {
        Node<E> p = g.left;
        Node<E> child = p.right;
        g.left = child;
        p.right = g;
        // 维护相关内容
        // 让p为子树的根节点
        afterRotate(g, p, child);
    }

    // 旋转后的维护
    protected void afterRotate(Node<E> g, Node<E> p, Node<E> child) {
        p.parent = g.parent;     // 变换p的父节点=>变成根节点

        // 当前子树根节点的父节点指向p,把根节点可能性考虑进去
        if (g.isLeftChild()) {
            g.parent.left = p;
        } else if (g.isRightChild()) {
            g.parent.right = p;
        } else {
            root = p;
        }
        // 更改child的parent指向
        if (child != null) {
            child.parent = g;
        }
        // 更改g的parent指向
        g.parent = p;
        // 更新高度 g p
    }

    /**
     * 根据二叉搜索树的性质,值a<b<c<d<e<f<g
     * @param r 当初的根节点
     */
    protected void rotate(Node<E> a, Node<E> b, Node<E> c,
                        Node<E> d,
                        Node<E> e, Node<E> f, Node<E> g,
                        Node<E> r) {
        // 让d成为子树的根节点
        d.parent = r.parent;
        if (r.isLeftChild()) {
            r.parent.left = d;
        } else if (r.isRightChild()) {
            r.parent.right = d;
        } else {
            root = d;
        }
        // a-b-c
        // 让a成为b的left,让c成为b的right,ac可能为空
        b.left = a;
        if (a != null) {
            a.parent = b;
        }
        b.right = c;
        if (c != null) {
            c.parent = b;
        }


        // e-f-g
        f.left = e;
        if (e != null) {
            e.parent = f;
        }
        f.right = g;
        if (g != null) {
            g.parent = f;
        }


        // b-d-f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;

    }

}
