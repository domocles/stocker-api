package com.eep.stocker.collections;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Tree<N extends Serializable> extends Serializable {
    N getParent(N node);

    List<N> getAncestors(N node);
    List<N> getChildren(N node);
    List<N> getDescendents(N node);
    List<N> getRow(int row);

    Set<N> getAll();

    boolean isAncestor(N node, N ancestor);
    boolean isDescendant(N node, N descendant);
    boolean contains(N node);
}
