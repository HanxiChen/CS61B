package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T extends Comparable<? super T>> implements Deque<T> {
    private static final int DEFAULT_CAPACITY = 8;

    private int size;
    private T[] array;
    private int front;          //当前数组第一个
    private int rear;           //当前数组最后一个
    private Comparator<T> comparator;

    public MaxArrayDeque() {
        size = 0;
        array = (T[]) new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = 0;
    }
    public MaxArrayDeque(Comparator<T> c) {
        size = 0;
        array = (T[]) new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = 0;
        comparator = c;
    }
    @Override
    //如果size 和 数组长度相等，扩大数组
    //首先size+1，赋值，front指针向前移动
    //如果front向前 和 rear向后重合后，调整后面数组的位置
    public void addFirst(T item) {
        if (size == array.length) {
            resize(size * 2);
        }

        //插入第一个数据的判断
        if (!(front == 0 && size == 0)) {
            front = (front - 1 + array.length) % array.length;
        }
        array[front] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == array.length) {
            resize(size * 2);
        }

        //插入第一个数据的判断
        if (!(rear == 0 && size == 0)) {
            rear = (rear + 1 + array.length) % array.length;
        }

        array[rear] = item;
        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (size < array.length / 4 && size > 4) {
            resize(array.length / 4);
        }

        T x = array[front];
        array[front] = null;
        if (!(--size == 0)) {
            front = (front + 1 + array.length) % array.length;
        }

        return x;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (size < array.length / 4 && size > 4) {
            resize(array.length / 4);
        }

        T x = array[rear];
        array[rear] = null;
        if (!(--size == 0)) {
            rear = (rear - 1 + array.length) % array.length;
        }
        return x;
    }

    @Override
    public T get(int index) {
        int pos = (front + index + array.length) % array.length;
        return array[pos];
    }

    public void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];

        int pos = front;
        for (int i = 0; i < size; i++) {
            a[i] = array[pos];
            pos = (pos + 1 + array.length) % array.length;
        }
        array = a;
        front = 0;
        rear = size - 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void printDeque() {
        if (!isEmpty()) {
            int pos = front;
            for (int i = 0; i < size - 1; i++) {
                System.out.print(array[pos] + " ");
                pos = (pos + 1 + array.length) % array.length;
            }
            System.out.println();
        }
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int wizPos;

            @Override
            public boolean hasNext() {
                return wizPos + 1 < rear;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    return null;
                }

                T returnItem = (T) array[wizPos];
                wizPos++;
                return returnItem;
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
                if (this.get(i) != o.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }

        T max = array[front];
        int pos = (front + 1 + array.length) % array.length;
        for (int i = 1; i < size; i++) {
            if (comparator.compare(max, array[pos]) < 0) {
                max = array[pos];
                pos = (pos + 1 + array.length) % array.length;
            }
        }
        return max;
    }
    public T max(Comparator<T> c) {
        comparator = c;
        return max();
    }

    public Comparator<T> comparator() {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.compareTo(o2);
            }
        };
    }
}
