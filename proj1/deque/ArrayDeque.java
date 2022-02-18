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

    @Override
    public void addFirst(T item) {
        if (size >= array.length) {
            resize(size * 2);
            rear = (rear + size) % (size + 1);
        }
        size++;
        if (size != 0) {
            front = (front - 1 + array.length) % array.length;
        }
        array[front] = item;
    }

    @Override
    public void addLast(T item) {
        if (size >= array.length) {
            resize(size * 2);
            rear = (rear + size) % (size + 1);
        }
        size++;
        array[rear] = item;
        rear = (rear + 1 + array.length) % array.length;
    }

    private void resize(int capacity) {
        T[] a1 = (T[]) new Object[capacity];

        System.arraycopy(array, 0, a1, 0, size);
        array = a1;
    }

    @Override
    public T removeFirst() {
        if ((size < array.length / 4) && (size >= 4)) {
            resize(array.length / 4);
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

        if ((size <= array.length / 4) && (size >= 4)) {
            resize(array.length / 4);
        }

        size--;
        rear = (rear - 1 + array.length) % array.length;
        T x = array[rear];
        array[rear] = null;
        return x;
    }
//    @Override
//    public boolean isEmpty(){
//        return size == 0;
//    }

    @Override
    public int size() {
        return size;
    }

    public String printDequeString() {
        String s = "";
        if (!isEmpty()) {
            for (int i = front; i != rear; i = (i + 1) % array.length) {
                s = s + array[i];
                if ((i + 1) % array.length != rear) {
                    s = s + " -> ";
                }
            }
        } else {
            s = "队列为空";
        }
        return s;
    }

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
    public T get(int index) {
        int pos = (front + index + array.length) % array.length;
        return array[pos];
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
            return wizPos + 1 < rear;
        }

        public T next() {
            if (!hasNext()) {
                return null;
            }

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

        if (other instanceof Deque) {
            Deque o = (Deque) other;
            if (this.size != o.size()) {
                return false;
            }
            for (int i = 0; i < size(); i++) {
                if (this.get(i) != o.get(i)) {
                    return false;
                }
            }
        }

        return true;
    }
}
