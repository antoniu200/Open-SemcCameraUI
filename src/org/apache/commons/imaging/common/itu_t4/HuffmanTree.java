package org.apache.commons.imaging.common.itu_t4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class HuffmanTree<T> {
    private final List<Node<T>> nodes = new ArrayList();

    HuffmanTree() {
    }

    private static final class Node<T> {
        boolean empty;
        T value;

        private Node() {
            this.empty = true;
        }
    }

    public final void insert(String str, T t) throws HuffmanTreeException {
        Node<T> nodeGrowAndGetNode = growAndGetNode(0);
        if (nodeGrowAndGetNode.value != null) {
            throw new HuffmanTreeException("Can't add child to a leaf");
        }
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            i = str.charAt(i2) == '0' ? (i << 1) + 1 : (i + 1) << 1;
            nodeGrowAndGetNode = growAndGetNode(i);
            if (nodeGrowAndGetNode.value != null) {
                throw new HuffmanTreeException("Can't add child to a leaf");
            }
        }
        nodeGrowAndGetNode.value = t;
    }

    private Node<T> growAndGetNode(int i) {
        while (i >= this.nodes.size()) {
            this.nodes.add(new Node<>());
        }
        Node<T> node = this.nodes.get(i);
        node.empty = false;
        return node;
    }

    public final T decode(BitInputStreamFlexible bitInputStreamFlexible) throws HuffmanTreeException {
        int i = 0;
        Node<T> node = this.nodes.get(0);
        while (node.value == null) {
            try {
                i = bitInputStreamFlexible.readBits(1) == 0 ? (i << 1) + 1 : (i + 1) << 1;
                if (i >= this.nodes.size()) {
                    throw new HuffmanTreeException("Invalid bit pattern");
                }
                node = this.nodes.get(i);
                if (node.empty) {
                    throw new HuffmanTreeException("Invalid bit pattern");
                }
            } catch (IOException e) {
                throw new HuffmanTreeException("Error reading stream for huffman tree", e);
            }
        }
        return node.value;
    }
}
