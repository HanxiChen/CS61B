package deque;

import java.util.Iterator;

public class LinkedListDeque<T> {
    private int size;
    private Node sentinel;
    private Node last;

    private class Node{
        public Node prev;
        public T item;
        public Node next;

        public Node(Node p, T i, Node n){
            prev = p;
            item = i;
            next = n;
        }
    }

    public LinkedListDeque(){
        sentinel = new Node(null, null, null);
        size = 0;
    }

    public void addFirst(T item){
        size++;

        if (size == 1){
            Node add = new Node(sentinel, item, sentinel);
            sentinel.next = add;
            sentinel.prev = add;
            last = add;
            return;
        }

        Node first = sentinel.next;
        Node add = new Node(sentinel, item, first);
        sentinel.next = add;
        first.prev = add;
    }

    public void addLast(T item){
        size++;

        if (size == 1){
            Node add = new Node(sentinel, item, sentinel);
            sentinel.next = add;
            sentinel.prev = add;
            last = add;
            return;
        }

        Node p = last;
        Node add = new Node(p, item, sentinel);
        p.next = add;
        sentinel.prev = add;

        last = add;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public String printDeque(){
        String s = "";

        Node p = sentinel.next;
        if (p == null)
            return s;

        while(p.next != sentinel){
            s = s + p.item + " -> ";
            p = p.next;
        }

        return s + p.item;
    }

    public T removeFirst(){
        size--;
        T x = sentinel.next.item;

        if (size == 0){
            sentinel.next = null;
            sentinel.prev = null;
            return x;
        }

        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;

        return x;
    }

    public T removeLast(){
        size--;

        Node p = last;
        T x = p.next.item;

        p.next = sentinel;
        sentinel.prev = p;

        return x;
    }

    public T get(int index){
        if (isEmpty())
            return null;

        Node p = sentinel;

        for (int i = 1; i <= index; i++) {
            if (p.next != null){
                p = p.next;
            }
        }

        return p.item;
    }

//    public Iterator<T> iterator(){
//
//    }
//
//    public boolean equals(Object o){
//
//    }

    //与给get相同，但要使用递归
    public T getRecursive(int index){
        return getRecursive(index, sentinel.next);
    }
    public T getRecursive(int index, Node n){
        if (index == 0)
            return n.item;
        else
            return getRecursive(--index, n.next);
    }
}
