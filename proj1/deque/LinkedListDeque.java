package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private Node sentinel;
    private Node last;

    private class Node {
        Node prev;
        T item;
        Node next;

        Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        if (size++ == 0) {
            Node first = new Node(sentinel, item, sentinel);
            sentinel.next = first;
            sentinel.prev = first;
            last = first;
            return;
        }

        Node first = sentinel.next;
        Node add = new Node(sentinel, item, first);
        sentinel.next = add;
        first.prev = add;

    }

    @Override
    public void addLast(T item) {
        if (size++ == 0) {
            Node first = new Node(sentinel, item, sentinel);
            sentinel.next = first;
            sentinel.prev = first;
            last = first;
            return;
        }

        Node p = last;
        Node add = new Node(p, item, sentinel);
        p.next = add;
        sentinel.prev = add;
        last = add;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node p = sentinel.next;
        if (p.item == null) {
            System.out.println("");
        }

        while (p.next != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.print(p.item);
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        T x = sentinel.next.item;

        Node first = sentinel.next.next;
        sentinel.next = first;
        first.prev = sentinel;

        size--;
        return x;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        T x = last.item;

        Node p = last.prev;
        p.next = sentinel;
        sentinel.prev = p;
        last = p;

        size--;
        return x;
    }

    @Override
    public T get(int index) {
        if (isEmpty()) {
            return null;
        }

        Node p = sentinel;

        for (int i = 0; i <= index; i++) {
            if (p.next != sentinel) {
                p = p.next;
            }
        }

        return p.item;
    }

    //与给get相同，但要使用递归
    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }
    private T getRecursive(int index, Node n) {
        if (index == 0) {
            return n.item;
        } else {
            return getRecursive(--index, n.next);
        }
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node p = sentinel.next;

            public boolean hasNext() {
                if (p == null) {
                    return false;
                }
                return p != sentinel;
            }

            public T next() {
                if (!hasNext()) {
                    return null;
                }

                T x = p.item;
                p = p.next;
                return x;
            }
        };
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (other instanceof Deque) {
            Deque o = (Deque) other;
            if (this.size != o.size()) {
                return false;
            }

            for (int i = 0; i < size(); i++) {
                if (!this.get(i).equals(o.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
