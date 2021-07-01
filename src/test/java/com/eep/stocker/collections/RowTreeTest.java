package com.eep.stocker.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RowTreeTest {
    private RowTree<Character> rowTree;
    private TreeNode<Character> a;
    private TreeNode<Character> b;
    private TreeNode<Character> c;
    private TreeNode<Character> d;
    private TreeNode<Character> e;
    private TreeNode<Character> f;
    private TreeNode<Character> g;
    private TreeNode<Character> h;
    private TreeNode<Character> i;
    private TreeNode<Character> j;


    @BeforeEach
    public void setup() {
        a = new TreeNode<Character>('a');
        b = new TreeNode<Character>('b');
        c = new TreeNode<Character>('c');
        d = new TreeNode<Character>('d');
        e = new TreeNode<Character>('e');
        f = new TreeNode<Character>('f');
        g = new TreeNode<Character>('g');
        h = new TreeNode<Character>('h');
        i = new TreeNode<Character>('i');
        j = new TreeNode<Character>('j');

        rowTree = new RowTree<>(a);
        rowTree.add(b, a);
        rowTree.add(e, b);
        rowTree.add(g, e);
        rowTree.add(f, b);
        rowTree.add(c, a);
        rowTree.add(d, a);
        rowTree.add(h, d);
        rowTree.add(i, d);
        rowTree.add(j, d);

    }

    @Test
    void getParentTest() {
        assertThat(rowTree.getParent(f)).isEqualTo(b);
        assertThat(rowTree.getParent(c)).isEqualTo(a);
        assertThat(rowTree.getParent(a)).isNull();
    }

    @Test
    void getAncestorsTest() {
        assertThat(rowTree.getAncestors(i)).contains(d, a);
        assertThat(rowTree.getAncestors(i).contains(i)).isFalse();
        assertThat(rowTree.getAncestors(a).isEmpty()).isTrue();
    }

    @Test
    void getChildrenTest() {
        assertThat(rowTree.getChildren(b)).contains(e, f);
        assertThat(rowTree.getChildren(g).isEmpty()).isTrue();
    }

    @Test
    void getDescendentsTest() {
        assertThat(rowTree.getDescendents(b)).contains(e, f, g);
        assertThat(rowTree.getDescendents(f).isEmpty()).isTrue();
    }

    @Test
    void getRowTest() {
        assertThat(rowTree.getRow(2)).contains(e, f, h, i, j);
        assertThat(rowTree.getRow(0)).contains(a);
    }

    @Test
    void isAncestorTest() {
        assertThat(rowTree.isAncestor(f, b)).isTrue();
        assertThat(rowTree.isAncestor(f, j)).isFalse();
    }

    @Test
    void isDescendantTest() {
        assertThat(rowTree.isDescendant(b, f)).isTrue();
        assertThat(rowTree.isDescendant(d, f)).isFalse();
    }

    @Test
    void addTest() {
        assertThat(rowTree.getCount()).isEqualTo(10);
        assertThat(rowTree.getNumRows()).isEqualTo(4);
    }

    @Test
    void getAllNodesTest() {
        assertThat(rowTree.getAll()).contains(a,b,c,d,e,f,g,h,i,j);
    }

    @Test
    void containsTest() {
        TreeNode<Character> newNode = new TreeNode<>('k');
        assertThat(rowTree.contains(g)).isTrue();
        assertThat(rowTree.contains(newNode)).isFalse();
    }

    @Test
    void removeTest() {
        rowTree.remove(d);
        assertThat(rowTree.contains(d)).isFalse();
        assertThat(rowTree.contains(h)).isFalse();
        assertThat(rowTree.contains(i)).isFalse();
        assertThat(rowTree.contains(j)).isFalse();
        assertThat(a.getChildren().contains(d)).isFalse();
    }

    @Test
    void removeNoCascadeTest() {
        rowTree.remove(d, false);
        assertThat(rowTree.contains(h)).isTrue();
        assertThat(rowTree.contains(i)).isTrue();
        assertThat(rowTree.contains(j)).isTrue();
        assertThat(h.getParent()).isEqualTo(a);
        assertThat(a.getChildren()).contains(h, i, j);
    }
}