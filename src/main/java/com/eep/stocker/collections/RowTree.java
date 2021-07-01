package com.eep.stocker.collections;

import java.util.*;
import java.util.stream.Collectors;

public class RowTree<E> implements MutableTree<TreeNode<E>> {
    private final Map<Integer, List<TreeNode<E>>> nodes;
    private final Map<TreeNode<E>, Integer> nodeRows;
    private TreeNode<E> root;

    private RowTree() {
        this.nodes = new HashMap<>();
        this.nodeRows = new HashMap<>();
    }

    public RowTree(TreeNode<E> root) {
        this();
        nodes.put(0, Arrays.asList(root));
        nodeRows.put(root, 0);
        this.root = root;
    }

    public RowTree(E rootValue) {
        this();
        TreeNode<E> rootNode = new TreeNode<>(rootValue);
        nodes.put(0, Arrays.asList(rootNode));
        this.root = rootNode;
    }

    public int getCount() {
        return this.nodeRows.size();
    }

    @Override
    public TreeNode<E> getParent(TreeNode<E> node) {
        return node.getParent();
    }

    @Override
    public List<TreeNode<E>> getAncestors(TreeNode<E> node) {
        if(node != root)
            return getAncestorsInclusive(node.getParent());
        return Collections.emptyList();
    }

    private List<TreeNode<E>> getAncestorsInclusive(TreeNode<E> node) {
        List<TreeNode<E>> ancestors = new ArrayList<>();
        ancestors.add(node);
        if(node != root)
            ancestors.addAll(getAncestorsInclusive(node.getParent()));
        return ancestors;
    }

    @Override
    public List<TreeNode<E>> getChildren(TreeNode<E> node) {
        return new ArrayList<>(node.getChildren());
    }

    @Override
    public List<TreeNode<E>> getDescendents(TreeNode<E> node) {
        List<TreeNode<E>> descendants = new ArrayList<>();
        for(TreeNode<E> child : node.getChildren()) {
            descendants.add(child);
            descendants.addAll(getDescendents(child));
        }
        return descendants;
    }

    @Override
    public List<TreeNode<E>> getRow(int row) {
        return nodes.get(row);
    }

    @Override
    public boolean isAncestor(TreeNode<E> node, TreeNode<E> ancestor) {
        return getAncestors(node).contains(ancestor);
    }

    @Override
    public boolean isDescendant(TreeNode<E> node, TreeNode<E> descendant) {
        return getDescendents(node).contains(descendant);
    }

    @Override
    public Set<TreeNode<E>> getAll() {
        return nodeRows.keySet();
    }

    @Override
    public boolean contains(TreeNode<E> node) {
        return nodeRows.containsKey(node);
    }

    @Override
    public boolean add(TreeNode<E> node, TreeNode<E> parent) {
        if(parent == null || node == null) return false;
        int parentRow = nodeRows.get(parent);
        node.setParent(parent);
        parent.addChild(node);
        if(nodes.get(parentRow+1) == null)
            nodes.put(parentRow+1, new ArrayList<TreeNode<E>>(Arrays.asList(node)));
        else
            nodes.get(parentRow+1).add(node);
        nodeRows.put(node, parentRow+1);
        return true;
    }

    public boolean add(E value, E parent) {
        Optional<TreeNode<E>> parentNodeOptional = findNodeForValue(parent);
        if(parentNodeOptional.isPresent()) {
            TreeNode<E> parentNode = parentNodeOptional.get();
            add(new TreeNode<>(value), parentNode);
            return true;
        }
        return false;
    }

    public Optional<TreeNode<E>> findNodeForValue(E value) {
        for(TreeNode<E> node : nodeRows.keySet()) {
            if(node.getValue().equals(value))
                return Optional.of(node);
        }
        return Optional.empty();
    }

    @Override
    public boolean remove(TreeNode<E> node, boolean cascade) {
        int currentRow = nodeRows.get(node);
        if(cascade) {
            List<TreeNode<E>> descendants = getDescendents(node);

            for(TreeNode<E> child : descendants) {
                child.getChildren().clear();
                child.setParent(null);
            }
            TreeNode<E> parent = node.getParent();
            parent.getChildren().remove(node);
            node.setParent(null);
            node.getChildren().clear();
            nodeRows.remove(node);

            List<TreeNode<E>> nodesList = nodes.get(currentRow);
            nodesList.remove(node);

        } else {
            TreeNode<E> parent = node.getParent();
            parent.getChildren().remove(node);
            node.setParent(null);
            parent.getChildren().addAll(node.getChildren());
            for(TreeNode<E> child : node.getChildren()) {
                child.setParent(parent);
                nodeRows.remove(child);
                nodeRows.put(child, currentRow);
            }

            node.getChildren().clear();
            nodeRows.remove(node);
            List<TreeNode<E>> nodesList = nodes.get(currentRow);
            nodesList.remove(node);
        }
        return true;
    }

    public int getNumRows() {
        return nodes.size();
    }
}
