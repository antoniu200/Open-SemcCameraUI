// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common.itu_t4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class HuffmanTree<T>
{
    private final List<Node<T>> nodes;
    
    HuffmanTree() {
        this.nodes = new ArrayList<Node<T>>();
    }
    
    private Node<T> growAndGetNode(final int i) {
        while (i >= this.nodes.size()) {
            this.nodes.add(new Node<T>());
        }
        final Node node = this.nodes.get(i);
        node.empty = false;
        return node;
    }
    
    public final T decode(final BitInputStreamFlexible bitInputStreamFlexible) throws HuffmanTreeException {
        final List<Node<T>> nodes = this.nodes;
        int n = 0;
        Node node = nodes.get(0);
        while (node.value == null) {
            try {
                if (bitInputStreamFlexible.readBits(1) == 0) {
                    n = (n << 1) + 1;
                }
                else {
                    n = n + 1 << 1;
                }
                if (n >= this.nodes.size()) {
                    throw new HuffmanTreeException("Invalid bit pattern");
                }
                if ((node = this.nodes.get(n)).empty) {
                    throw new HuffmanTreeException("Invalid bit pattern");
                }
                continue;
            }
            catch (final IOException ex) {
                throw new HuffmanTreeException("Error reading stream for huffman tree", ex);
            }
            break;
        }
        return (T)node.value;
    }
    
    public final void insert(final String s, final T value) throws HuffmanTreeException {
        int i = 0;
        Node<T> node = this.growAndGetNode(0);
        if (node.value != null) {
            throw new HuffmanTreeException("Can't add child to a leaf");
        }
        int n = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '0') {
                n = (n << 1) + 1;
            }
            else {
                n = n + 1 << 1;
            }
            node = this.growAndGetNode(n);
            if (node.value != null) {
                throw new HuffmanTreeException("Can't add child to a leaf");
            }
            ++i;
        }
        node.value = value;
    }
    
    private static final class Node<T>
    {
        boolean empty;
        T value;
        
        private Node() {
            this.empty = true;
        }
    }
}
