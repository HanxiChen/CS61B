package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private static final int DEFAULT_CAPACITY = 8;

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
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

}
