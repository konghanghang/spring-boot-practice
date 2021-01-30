package com.test.tree.threaded;

/**
 * 线索化二叉树
 * todo 前序和后序
 */
public class ThreadedTree {

    // 当前节点的前驱节点
    private ThreadedTreeNode pre = null;

    /**
     * 中序线索化
     * @param node
     */
    public void threadedNodes(ThreadedTreeNode node) {
        if (node == null) {
            return;
        }
        // 1. 先线索化左节点
        threadedNodes(node.getLeft());
        // 2. 线索化当前节点
        if (node.getLeft() == null) {
            // 如果当前节点左节点为空，则左节点指向前驱节点
            node.setLeft(pre);
            node.setLeftType(1);
        }
        // 右节点应该在下一个节点的时候进行处理，所以这里是对pre的处理
        if (pre != null && pre.getRight() == null) {
            // 如果当前节点的前一个节点的右节点为空，则右节点指向当前节点，也就是后继节点
            pre.setRight(node);
            pre.setRightType(1);
        }
        pre = node;
        // 3. 线索化右节点
        threadedNodes(node.getRight());
    }

    public void midOrderThreadedNodes(ThreadedTreeNode node) {
        while (node != null) {
            // 找到leftType == 1的节点，第一次也就是中序遍历的第一个节点
            while (node.getLeftType() == 0) {
                node = node.getLeft();
            }
            // 输出节点的值
            System.out.print(node.getId() + "\t");
            // 遍历节点的后继节点
            while (node.getRightType() == 1) {
                node = node.getRight();
                System.out.print(node.getId() + "\t");
            }
            node = node.getRight();
        }
    }

}
