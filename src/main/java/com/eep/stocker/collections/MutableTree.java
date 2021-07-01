package com.eep.stocker.collections;

import java.io.Serializable;

public interface MutableTree<N extends Serializable> extends Tree<N> {
    boolean add(N node, N parent);
    default boolean remove(N node) {
        return remove(node, true);
    }
    boolean remove(N node, boolean cascade);
}
