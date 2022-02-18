package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T> {
    private static final int DEFAULT_CAPACITY = 8;

    private int size;
    private T[] array;
    private int front;
    private int rear;

    public ArrayDeque() {
        size = 0;
        array = (T[]) new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = 0;
    }
    public ArrayDeque(int capacity) {
        size = 0;
        array = (T[]) new Object[capacity];
        front = 0;
        rear = 0;
    }

    @Override
    public void addFirst(T item) {
        size++;
        if (size > DEFAULT_CAPACITY) {
            resize(size * 2);
        }

        if (size != 0) {
            front = (front - 1 + array.length) % array.length;
        }

        array[front] = item;
    }

    @Override
    public void addLast(T item) {
        size++;
        if (size > DEFAULT_CAPACITY) {
            resize(size * 2);
        }

        array[rear] = item;
        rear = (rear + 1 + array.length) % array.length;
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
//        if (!isEmpty()){
//            for (int i = front; i != rear; i = (i+1) % array.length) {
//                s = s + array[i];
//                if ((i+1) % array.length != rear)
//                    s = s + " -> ";
//            }
//        }else{
//            s = "队列为空";
//        }
//        return s;
//    }

    @Override
    public void printDeque() {
        if (!isEmpty()) {
            for (int i = front; i != rear; i = (i + 1) % array.length) {
                System.out.print(array[i]);
                if ((i + 1) % array.length != rear) {
                    System.out.println(" -> ");
                }
            }
        } else {
            System.out.println("队列为空");
        }
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        size--;
        T x = array[front];
        array[front] = null;
        front = (front + 1 + array.length) % array.length;
        return x;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        size--;
        T x = array[rear];
        array[rear] = null;
        rear = (rear - 1 + array.length) % array.length;
        return x;
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    public void resize(int capacity) {

        T[] a1 = (T[]) new Object[capacity];
        System.arraycopy(array, 0, a1, 0, size);
        array = a1;
    }

    public ArrayIterator<T> iterator() {
        return new ArrayIterator<T>();
    }

    private class ArrayIterator<T> implements Iterator<T> {
        private int wizPos;

        ArrayIterator() {
            wizPos = front;
        }

        public boolean hasNext() {
            return wizPos < rear;
        }

        public T next() {
            T returnItem = (T) array[wizPos];
            wizPos++;
            return returnItem;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        ArrayDeque<T> o = (ArrayDeque<T>) other;
        if (o.size() != this.size()) {
            return false;
        }
        for (int i = front; i < rear; i = (i + 1 + array.length) % array.length) {
            if (this.get(i) != o.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder returnSB = new StringBuilder("{");
        for (int i = 0; i < size - 1; i = (i + 1 + array.length) % array.length) {
            returnSB.append(array[i].toString());
            returnSB.append(", ");
        }
        returnSB.append(array[size - 1]);
        returnSB.append("}");
        return returnSB.toString();
    }
}
