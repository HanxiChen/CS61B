package bstmap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTNode root;
    private HashSet<K> set = new HashSet<>();

    private class BSTNode {
        private K key;
        private V v;
        private BSTNode left, right;
        private int size;

        public BSTNode(K key, V v, int size) {
            this.key = key;
            this.v = v;
            this.size = size;
        }
    }
    @Override
    public void clear() {
        set = null;
        root = null;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        if (set == null) {
            return false;
        }

        return set.contains(key);
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode x, K key) {
        if (key == null) {
            throw new UnsupportedOperationException("Cannot get null");
        }
        if (x == null) {
            return null;
        }

        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.v;
        }
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(BSTNode x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new UnsupportedOperationException("Cannot put null");
        }
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode x, K k, V v) {
        set.add(k);
        if (x == null) {
            return new BSTNode(k, v, 1);
        }

        int cmp = k.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, k, v);
        } else if (cmp > 0) {
            x.right = put(x.right, k, v);
        } else {
            x.v = v;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    @Override
    public Set<K> keySet() {
        return set;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    public void printInOrder() {
        Stack<BSTNode> stack = new Stack<>();
        BSTNode node = root;
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            if (!stack.isEmpty()) {
                node = stack.pop();
                System.out.print(node.v + " ");
                node = node.right;
            }
        }
    }
}
