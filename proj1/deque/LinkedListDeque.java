package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T> {
    private int size;
    private Node sentinel;
    private Node last;

    private class Node {
        public Node prev;
        public T item;
        public Node next;

        public Node(Node p, T i, Node n) {
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

    @Override
    public void addLast(T item) {
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

//    @Override
//    public boolean isEmpty(){
//        return size == 0;
//    }

    @Override
    public int size() {
        return size;
    }

//    public String printDeque(){
//        String s = "";
//
//        Node p = sentinel.next;
//        if (p.item == null)
//            return s;
//
//        while(p.next != sentinel){
//            s = s + p.item + " -> ";
//            p = p.next;
//        }
//
//        return s + p.item;
//    }

    @Override
    public void printDeque() {
        Node p = sentinel.next;
        if (p.item == null)
            System.out.println("");

        while(p.next != sentinel){
            System.out.print(p.item + " -> ");
            p = p.next;
        }
        System.out.println(p.item);
    }

    @Override
    public T removeFirst() {
        if (isEmpty())
            return null;

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

    @Override
    public T removeLast() {
        if (isEmpty())
            return null;

        size--;

        Node p = last.prev;
        last = p;
        T x = p.next.item;

        p.next = sentinel;
        sentinel.prev = p;

        return x;
    }

    @Override
    public T get(int index) {
        if (isEmpty())
            return null;

        Node p = sentinel;

        for (int i = 1; i <= index; i++) {
            if (p.next != null) {
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
    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }
    public T getRecursive(int index, Node n) {
        if (index == 0)
            return n.item;
        else
            return getRecursive(--index, n.next);
    }
}
