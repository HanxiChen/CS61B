package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private static final int DEFAULT_CAPACITY = 8;

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }

        T max = get(0);

        for (int i = 1; i < size(); i++) {
            if (comparator.compare(max, get(i)) < 0) {
                max = get(i);
            }
        }
        return max;
    }
    public T max(Comparator<T> c) {
        comparator = c;
        return max();
    }

}
