// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Set;
import java.io.ObjectStreamException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Comparator;
import java.io.Serializable;
import java.util.AbstractMap;

public final class LinkedTreeMap<K, V> extends AbstractMap<K, V> implements Serializable
{
    static final boolean $assertionsDisabled = false;
    private static final Comparator<Comparable> NATURAL_ORDER;
    Comparator<? super K> comparator;
    private EntrySet entrySet;
    final Node<K, V> header;
    private KeySet keySet;
    int modCount;
    Node<K, V> root;
    int size;
    
    static {
        NATURAL_ORDER = new Comparator<Comparable>() {
            @Override
            public int compare(final Comparable comparable, final Comparable comparable2) {
                return comparable.compareTo(comparable2);
            }
        };
    }
    
    public LinkedTreeMap() {
        this((Comparator)LinkedTreeMap.NATURAL_ORDER);
    }
    
    public LinkedTreeMap(Comparator<? super K> natural_ORDER) {
        this.size = 0;
        this.modCount = 0;
        this.header = new Node<K, V>();
        if (natural_ORDER == null) {
            natural_ORDER = LinkedTreeMap.NATURAL_ORDER;
        }
        this.comparator = (Comparator<? super K>)natural_ORDER;
    }
    
    private boolean equal(final Object o, final Object obj) {
        return o == obj || (o != null && o.equals(obj));
    }
    
    private void rebalance(Node<K, V> parent, final boolean b) {
        while (parent != null) {
            final Node<K, V> left = parent.left;
            final Node<K, V> right = parent.right;
            final int n = 0;
            final int n2 = 0;
            int height;
            if (left != null) {
                height = left.height;
            }
            else {
                height = 0;
            }
            int height2;
            if (right != null) {
                height2 = right.height;
            }
            else {
                height2 = 0;
            }
            final int n3 = height - height2;
            if (n3 == -2) {
                final Node<K, V> left2 = right.left;
                final Node<K, V> right2 = right.right;
                int height3;
                if (right2 != null) {
                    height3 = right2.height;
                }
                else {
                    height3 = 0;
                }
                int height4 = n2;
                if (left2 != null) {
                    height4 = left2.height;
                }
                final int n4 = height4 - height3;
                if (n4 != -1 && (n4 != 0 || b)) {
                    this.rotateRight(right);
                    this.rotateLeft(parent);
                }
                else {
                    this.rotateLeft(parent);
                }
                if (b) {
                    break;
                }
            }
            else if (n3 == 2) {
                final Node<K, V> left3 = left.left;
                final Node<K, V> right3 = left.right;
                int height5;
                if (right3 != null) {
                    height5 = right3.height;
                }
                else {
                    height5 = 0;
                }
                int height6 = n;
                if (left3 != null) {
                    height6 = left3.height;
                }
                final int n5 = height6 - height5;
                if (n5 != 1 && (n5 != 0 || b)) {
                    this.rotateLeft(left);
                    this.rotateRight(parent);
                }
                else {
                    this.rotateRight(parent);
                }
                if (b) {
                    break;
                }
            }
            else if (n3 == 0) {
                parent.height = height + 1;
                if (b) {
                    break;
                }
            }
            else {
                parent.height = Math.max(height, height2) + 1;
                if (!b) {
                    break;
                }
            }
            parent = parent.parent;
        }
    }
    
    private void replaceInParent(final Node<K, V> node, final Node<K, V> root) {
        final Node<K, V> parent = node.parent;
        node.parent = null;
        if (root != null) {
            root.parent = parent;
        }
        if (parent != null) {
            if (parent.left == node) {
                parent.left = root;
            }
            else {
                parent.right = root;
            }
        }
        else {
            this.root = root;
        }
    }
    
    private void rotateLeft(final Node<K, V> node) {
        final Node<K, V> left = node.left;
        final Node<K, V> right = node.right;
        final Node<K, V> left2 = right.left;
        final Node<K, V> right2 = right.right;
        node.right = left2;
        if (left2 != null) {
            left2.parent = node;
        }
        this.replaceInParent(node, right);
        right.left = node;
        node.parent = right;
        final int n = 0;
        int height;
        if (left != null) {
            height = left.height;
        }
        else {
            height = 0;
        }
        int height2;
        if (left2 != null) {
            height2 = left2.height;
        }
        else {
            height2 = 0;
        }
        node.height = Math.max(height, height2) + 1;
        final int height3 = node.height;
        int height4 = n;
        if (right2 != null) {
            height4 = right2.height;
        }
        right.height = Math.max(height3, height4) + 1;
    }
    
    private void rotateRight(final Node<K, V> node) {
        final Node<K, V> left = node.left;
        final Node<K, V> right = node.right;
        final Node<K, V> left2 = left.left;
        final Node<K, V> right2 = left.right;
        node.left = right2;
        if (right2 != null) {
            right2.parent = node;
        }
        this.replaceInParent(node, left);
        left.right = node;
        node.parent = left;
        final int n = 0;
        int height;
        if (right != null) {
            height = right.height;
        }
        else {
            height = 0;
        }
        int height2;
        if (right2 != null) {
            height2 = right2.height;
        }
        else {
            height2 = 0;
        }
        node.height = Math.max(height, height2) + 1;
        final int height3 = node.height;
        int height4 = n;
        if (left2 != null) {
            height4 = left2.height;
        }
        left.height = Math.max(height3, height4) + 1;
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap(this);
    }
    
    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
        ++this.modCount;
        final Node<K, V> header = this.header;
        header.prev = header;
        header.next = header;
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.findByObject(o) != null;
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        EntrySet entrySet = this.entrySet;
        if (entrySet == null) {
            entrySet = new EntrySet();
            this.entrySet = entrySet;
        }
        return entrySet;
    }
    
    Node<K, V> find(final K k, final boolean b) {
        final Comparator<? super K> comparator = this.comparator;
        Node<K, V> root = this.root;
        int n;
        if (root != null) {
            Comparable comparable;
            if (comparator == LinkedTreeMap.NATURAL_ORDER) {
                comparable = (Comparable)k;
            }
            else {
                comparable = null;
            }
            while (true) {
                if (comparable != null) {
                    n = comparable.compareTo(root.key);
                }
                else {
                    n = comparator.compare(k, root.key);
                }
                if (n == 0) {
                    return root;
                }
                Node<K, V> node;
                if (n < 0) {
                    node = root.left;
                }
                else {
                    node = root.right;
                }
                if (node == null) {
                    break;
                }
                root = node;
            }
        }
        else {
            n = 0;
        }
        if (!b) {
            return null;
        }
        final Node<K, V> header = this.header;
        Node right;
        if (root == null) {
            if (comparator == LinkedTreeMap.NATURAL_ORDER && !(k instanceof Comparable)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(k.getClass().getName());
                sb.append(" is not Comparable");
                throw new ClassCastException(sb.toString());
            }
            right = new Node<K, V>((Node<Object, Object>)root, k, (Node<Object, Object>)header, (Node<Object, Object>)header.prev);
            this.root = (Node<K, V>)right;
        }
        else {
            right = new Node<K, V>(root, k, header, header.prev);
            if (n < 0) {
                root.left = (Node<K, V>)right;
            }
            else {
                root.right = (Node<K, V>)right;
            }
            this.rebalance(root, true);
        }
        ++this.size;
        ++this.modCount;
        return (Node<K, V>)right;
    }
    
    Node<K, V> findByEntry(final Entry<?, ?> entry) {
        final Node<K, V> byObject = this.findByObject(entry.getKey());
        Node<K, V> node;
        if (byObject != null && this.equal(byObject.value, entry.getValue())) {
            node = byObject;
        }
        else {
            node = null;
        }
        return node;
    }
    
    Node<K, V> findByObject(final Object o) {
        if (o != null) {
            try {
                final Node<Object, V> find = this.find(o, false);
                return (Node<K, V>)find;
            }
            catch (final ClassCastException ex) {
                return null;
            }
        }
        final Node<Object, V> find = null;
        return (Node<K, V>)find;
    }
    
    @Override
    public V get(Object value) {
        final Node<K, V> byObject = this.findByObject(value);
        if (byObject != null) {
            value = byObject.value;
        }
        else {
            value = null;
        }
        return (V)value;
    }
    
    @Override
    public Set<K> keySet() {
        KeySet keySet = this.keySet;
        if (keySet == null) {
            keySet = new KeySet();
            this.keySet = keySet;
        }
        return keySet;
    }
    
    @Override
    public V put(final K k, final V value) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        final Node<K, V> find = this.find(k, true);
        final V value2 = find.value;
        find.value = value;
        return value2;
    }
    
    @Override
    public V remove(Object value) {
        final Node<K, V> removeInternalByKey = this.removeInternalByKey(value);
        if (removeInternalByKey != null) {
            value = removeInternalByKey.value;
        }
        else {
            value = null;
        }
        return (V)value;
    }
    
    void removeInternal(final Node<K, V> node, final boolean b) {
        if (b) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        final Node<K, V> left = node.left;
        final Node<K, V> right = node.right;
        final Node<K, V> parent = node.parent;
        int height = 0;
        if (left != null && right != null) {
            Node<K, V> node2;
            if (left.height > right.height) {
                node2 = left.last();
            }
            else {
                node2 = right.first();
            }
            this.removeInternal(node2, false);
            final Node<K, V> left2 = node.left;
            int height2;
            if (left2 != null) {
                height2 = left2.height;
                node2.left = left2;
                left2.parent = node2;
                node.left = null;
            }
            else {
                height2 = 0;
            }
            final Node<K, V> right2 = node.right;
            if (right2 != null) {
                height = right2.height;
                node2.right = right2;
                right2.parent = node2;
                node.right = null;
            }
            node2.height = Math.max(height2, height) + 1;
            this.replaceInParent(node, node2);
            return;
        }
        if (left != null) {
            this.replaceInParent(node, left);
            node.left = null;
        }
        else if (right != null) {
            this.replaceInParent(node, right);
            node.right = null;
        }
        else {
            this.replaceInParent(node, null);
        }
        this.rebalance(parent, false);
        --this.size;
        ++this.modCount;
    }
    
    Node<K, V> removeInternalByKey(final Object o) {
        final Node<K, V> byObject = this.findByObject(o);
        if (byObject != null) {
            this.removeInternal(byObject, true);
        }
        return byObject;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    class EntrySet extends AbstractSet<Entry<K, V>>
    {
        final LinkedTreeMap this$0;
        
        EntrySet(final LinkedTreeMap this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void clear() {
            this.this$0.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return o instanceof Entry && this.this$0.findByEntry((Entry<?, ?>)o) != null;
        }
        
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new LinkedTreeMapIterator<Entry<K, V>>(this) {
                final EntrySet this$1;
                
                @Override
                public Entry<K, V> next() {
                    return ((LinkedTreeMapIterator)this).nextNode();
                }
            };
        }
        
        @Override
        public boolean remove(final Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            final Node<K, V> byEntry = this.this$0.findByEntry((Entry<?, ?>)o);
            if (byEntry == null) {
                return false;
            }
            this.this$0.removeInternal(byEntry, true);
            return true;
        }
        
        @Override
        public int size() {
            return this.this$0.size;
        }
    }
    
    private abstract class LinkedTreeMapIterator<T> implements Iterator<T>
    {
        int expectedModCount;
        Node<K, V> lastReturned;
        Node<K, V> next;
        final LinkedTreeMap this$0;
        
        private LinkedTreeMapIterator(final LinkedTreeMap this$0) {
            this.this$0 = this$0;
            this.next = this.this$0.header.next;
            this.lastReturned = null;
            this.expectedModCount = this.this$0.modCount;
        }
        
        @Override
        public final boolean hasNext() {
            return this.next != this.this$0.header;
        }
        
        final Node<K, V> nextNode() {
            final Node<K, V> next = this.next;
            if (next == this.this$0.header) {
                throw new NoSuchElementException();
            }
            if (this.this$0.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.next = next.next;
            return this.lastReturned = next;
        }
        
        @Override
        public final void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            this.this$0.removeInternal(this.lastReturned, true);
            this.lastReturned = null;
            this.expectedModCount = this.this$0.modCount;
        }
    }
    
    class KeySet extends AbstractSet<K>
    {
        final LinkedTreeMap this$0;
        
        KeySet(final LinkedTreeMap this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void clear() {
            this.this$0.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.this$0.containsKey(o);
        }
        
        @Override
        public Iterator<K> iterator() {
            return new LinkedTreeMapIterator<K>(this) {
                final KeySet this$1;
                
                @Override
                public K next() {
                    return ((LinkedTreeMapIterator)this).nextNode().key;
                }
            };
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.this$0.removeInternalByKey(o) != null;
        }
        
        @Override
        public int size() {
            return this.this$0.size;
        }
    }
    
    static final class Node<K, V> implements Entry<K, V>
    {
        int height;
        final K key;
        Node<K, V> left;
        Node<K, V> next;
        Node<K, V> parent;
        Node<K, V> prev;
        Node<K, V> right;
        V value;
        
        Node() {
            this.key = null;
            this.prev = this;
            this.next = this;
        }
        
        Node(final Node<K, V> parent, final K key, final Node<K, V> next, final Node<K, V> prev) {
            this.parent = parent;
            this.key = key;
            this.height = 1;
            this.next = next;
            this.prev = prev;
            prev.next = this;
            next.prev = this;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Entry;
            final boolean b2 = false;
            if (b) {
                final Entry entry = (Entry)o;
                if (this.key == null) {
                    final boolean b3 = b2;
                    if (entry.getKey() != null) {
                        return b3;
                    }
                }
                else {
                    final boolean b3 = b2;
                    if (!this.key.equals(entry.getKey())) {
                        return b3;
                    }
                }
                if (this.value == null) {
                    final boolean b3 = b2;
                    if (entry.getValue() != null) {
                        return b3;
                    }
                }
                else {
                    final boolean b3 = b2;
                    if (!this.value.equals(entry.getValue())) {
                        return b3;
                    }
                }
                return true;
            }
            return false;
        }
        
        public Node<K, V> first() {
            Node<K, V> node = this.left;
            Node node2 = this;
            Node node3;
            while (true) {
                node3 = node2;
                node2 = node;
                if (node2 == null) {
                    break;
                }
                node = node2.left;
            }
            return node3;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public V getValue() {
            return this.value;
        }
        
        @Override
        public int hashCode() {
            final K key = this.key;
            int hashCode = 0;
            int hashCode2;
            if (key == null) {
                hashCode2 = 0;
            }
            else {
                hashCode2 = this.key.hashCode();
            }
            if (this.value != null) {
                hashCode = this.value.hashCode();
            }
            return hashCode2 ^ hashCode;
        }
        
        public Node<K, V> last() {
            Node<K, V> node = this.right;
            Node node2 = this;
            Node node3;
            while (true) {
                node3 = node2;
                node2 = node;
                if (node2 == null) {
                    break;
                }
                node = node2.right;
            }
            return node3;
        }
        
        @Override
        public V setValue(final V value) {
            final V value2 = this.value;
            this.value = value;
            return value2;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.key);
            sb.append("=");
            sb.append(this.value);
            return sb.toString();
        }
    }
}
