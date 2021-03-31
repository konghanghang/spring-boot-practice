package com.test.tree.sort;

import com.test.tree.BinaryTree;
import com.test.tree.BinaryTreeNode;
import lombok.Data;

import java.util.Objects;
import java.util.Stack;

/**
 * 二叉排序树
 * @author yslao@outlook.com
 * @since 2021/3/30
 */
@Data
public class BinarySortTree {

    private BinaryTreeNode root;

    /**
     * 添加节点
     * @param node
     */
    public void add(BinaryTreeNode node) {
        if (root == null) {
            root = node;
        } else {
            BinaryTreeNode temp = root;
            while (temp != null) {
                if (node.getData() < temp.getData()) {
                    BinaryTreeNode left = temp.getLeft();
                    if (left != null) {
                        temp = left;
                    } else {
                        temp.setLeft(node);
                        temp = null;
                    }
                } else {
                    BinaryTreeNode right = temp.getRight();
                    if (right != null) {
                        temp = right;
                    } else {
                        temp.setRight(node);
                        temp = null;
                    }
                }
            }
        }
        // 对当前二叉排序树进行平衡(平衡二叉树)
        int leftHeight = BinaryTree.getHeightNoRecursion(root.getLeft());
        int rightHeight = BinaryTree.getHeightNoRecursion(root.getRight());
        // 右子树的高度大于左字数的高度 并且大于1 ,则进行平衡(左旋)
        if (rightHeight - leftHeight > 1) {
            // 右子树的左子树高度大于右子树的右子树的高度
            if (root.getRight() != null
                    && BinaryTree.getHeightNoRecursion(root.getRight().getLeft()) > BinaryTree.getHeightNoRecursion(root.getRight().getRight())) {
                // 先对右子树进行右旋
                rightRotate(root.getRight());
            }
            leftRotate(root);
        } else if (leftHeight - rightHeight > 1) {
            // 左子树的右子树高度大于左子树的左子树的高度
            if (root.getLeft() != null
                    && BinaryTree.getHeightNoRecursion(root.getLeft().getRight()) > BinaryTree.getHeightNoRecursion(root.getLeft().getLeft())) {
                // 先对左子树进行左旋
                leftRotate(root.getLeft());
            }
            // 再对根节点进行右旋
            rightRotate(root);
        }
    }

    /**
     * 查看待删除的节点
     * @param value
     * @return
     */
    public BinaryTreeNode search(int value) {
        Stack<BinaryTreeNode> stack = new Stack<>();
        stack.push(root);
        BinaryTreeNode result = null;
        while (!stack.isEmpty()) {
            BinaryTreeNode pop = stack.pop();
            if (pop != null) {
                if (pop.getData() == value) {
                    result = pop;
                } else if (pop.getData() > value) {
                    stack.push(pop.getLeft());
                } else if (pop.getData() < value) {
                    stack.push(pop.getRight());
                }
            }
        }
        return result;
    }

    /**
     * 查看待删除的节点的父节点
     * @param value
     * @return
     */
    public BinaryTreeNode searchParent(int value) {
        // 先判断是不是根节点
        if (root == null) {
            return null;
        }
        if (root.getData() == value) {
            return null;
        }
        Stack<BinaryTreeNode> stack = new Stack<>();
        stack.push(root);
        BinaryTreeNode result = null;
        while (!stack.isEmpty()) {
            BinaryTreeNode pop = stack.pop();
            if (pop != null) {
                if ((pop.getLeft() != null && pop.getLeft().getData() == value)
                        || (pop.getRight() != null && pop.getRight().getData() == value)) {
                    result = pop;
                } else if (pop.getData() > value) {
                    stack.push(pop.getLeft());
                } else if (pop.getData() < value) {
                    stack.push(pop.getRight());
                }
            }
        }
        return result;
    }

    /**
     * 查看待删除的节点的父节点
     * @param value
     * @return
     */
    public void deleteNode(int value) {
        BinaryTreeNode search = search(value);
        // search为null，表明没有查找到要删除的节点
        if (Objects.isNull(search)) {
            System.out.println("未找到要删除的节点：" + value);
            return;
        }
        // 找到了要删除的节点后，查找要删除的节点的父节点
        BinaryTreeNode parent = searchParent(value);
        // 如果父节点是空，则表明要删除的节点是根节点
        if (Objects.isNull(parent)) {
            // 判断根节点的子树情况,这里只考虑没有左子树和右子树的情况
            // 其他只有一个子树或有两棵子树交给下边的统一进行删除
            // 如果根节点没有左子树和右子树
            if (search.getLeft() == null && search.getRight() == null) {
                root = null;
                return;
            } else if (search.getLeft() != null && search.getRight() == null) {
                root = search.getLeft();
                return;
            }
            else if (search.getLeft() == null && search.getRight() != null) {
                root = search.getRight();
                return;
            }
        }
        // 判断要删除的节点是否是叶子节点
        if (search.getLeft() == null && search.getRight() == null) {
            if (parent.getLeft() != null && parent.getLeft().getData() == value) {
                parent.setLeft(null);
            } else if (parent.getRight() != null && parent.getRight().getData() == value) {
                parent.setRight(null);
            }
        } else if (search.getLeft() != null && search.getRight() != null) {
            // 删除的节点有左右两棵子树
            // 查找待删除节点的右子树的最小值
            // int searchValue = deleteTreeMinOrMax(search.getRight(), 1);
            // 查找待删除节点的左子树的最大值
             int searchValue = deleteTreeMinOrMax(search.getLeft(), 2);
            search.setData(searchValue);
        } else {
            // 删除的节点有一个子树
            // 一个子树是左子节点
            if (search.getLeft() != null) {
                if (parent.getLeft().getData() == value) {
                    parent.setLeft(search.getLeft());
                } else if (parent.getRight().getData() == value) {
                    parent.setRight(search.getLeft());
                }
            } else {
                //一个子树是右子节点
                if (parent.getLeft().getData() == value) {
                    parent.setLeft(search.getRight());
                } else if (parent.getRight().getData() == value) {
                    parent.setRight(search.getRight());
                }
            }
        }
    }

    /**
     * 删除右子树中最小的节点
     * 在进行删除两个子树的节点时，我们有两种操作方法
     * 1. 查找右子树的最小值，并在树中删除它，然后把要删除的节点的值赋值成已删除的节点的值
     * 2. 查找左子树的最大值，并在树中删除它，然后把要删除的节点的值赋值成已删除的节点的值
     * @param node
     * @param type 1:第一种情况 2:第二种情况
     * @return 返回已删除的最小节点值
     */
    public int deleteTreeMinOrMax(BinaryTreeNode node, int type) {
        BinaryTreeNode target = node;
        if (type == 1) {
            // 向右找，找最小
            while (target.getLeft() != null) {
                target = target.getLeft();
            }
        } else if (type == 2) {
            // 向左找，找最大
            while (target.getRight() != null) {
                target = target.getRight();
            }
        }
        // 走出while的时候就已经找到了
        // 删除最小的节点
        deleteNode(target.getData());
        return target.getData();
    }

    /**
     * 进行左旋转
     * 说明右子树的高度大于左子树的高度
     */
    private void leftRotate(BinaryTreeNode node) {
        // 以当前根节点的值创建新的节点
        BinaryTreeNode newNode = new BinaryTreeNode(node.getData());
        // 把新的结点的左子树设置成根节点的左子树
        newNode.setLeft(node.getLeft());
        // 把新的节点的右子树设置成根节点的右子树的左子树
        newNode.setRight(node.getRight().getLeft());
        // 把根节点的值替换成右子节点的值
        node.setData(node.getRight().getData());
        // 把根节点的右子树设置成根节点右子树的右子树
        node.setRight(node.getRight().getRight());
        // 把根节点的左子树设置成新的节点
        node.setLeft(newNode);
    }

    /**
     * 进行右旋转
     * 说明左子树的高度大于右子树的高度
     */
    private void rightRotate(BinaryTreeNode node) {
        // 以当前根节点的值创建新的节点
        BinaryTreeNode newNode = new BinaryTreeNode(node.getData());
        // 把新的结点的右子树设置成根节点的右子树
        newNode.setRight(node.getRight());
        // 把新的节点的左子树设置成根节点的左子树的右子树
        newNode.setLeft(node.getLeft().getRight());
        // 把根节点的值替换成左子节点的值
        node.setData(node.getLeft().getData());
        // 把根节点的左子树设置成根节点左子树的左子树
        node.setLeft(node.getLeft().getLeft());
        // 把根节点的右子树设置成新的节点
        node.setRight(newNode);
    }
}
