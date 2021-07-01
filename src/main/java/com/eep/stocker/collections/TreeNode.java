package com.eep.stocker.collections;

import java.io.Serializable;
import java.util.*;

public class TreeNode<E> implements Serializable {
    private TreeNode<E> parent;
    private Set<TreeNode<E>> children;
    private E value;

    public TreeNode() {
        parent = null;
        children = new HashSet<>();
        value = null;
    }

    public TreeNode(TreeNode<E> parent, E value) {
        this();
        this.parent = parent;
        this.value = value;
    }

    public TreeNode(E value) {
        this();
        this.value = value;
    }

    public boolean addChild(TreeNode<E> child) {
        return this.children.add(child);
    }

    public boolean addChildren(TreeNode<E>...children) {
        return this.children.addAll(Arrays.asList(children));
    }

    public TreeNode<E> getParent() {
        return this.parent;
    }

    public void setParent(TreeNode<E> parent) {
        this.parent = parent;
    }

    public Set<TreeNode<E>> getChildren() {
        return this.children;
    }

    public E getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "TreeNode{" + (parent == null ? "parent=null" :
                "parent=" + parent.value) +
                ", children=" + children.toString() +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeNode)) return false;
        TreeNode<?> treeNode = (TreeNode<?>) o;
        return Objects.equals(getParent(), treeNode.getParent()) &&
                Objects.equals(getChildren(), treeNode.getChildren()) &&
                Objects.equals(value, treeNode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParent(), value);
    }
}
