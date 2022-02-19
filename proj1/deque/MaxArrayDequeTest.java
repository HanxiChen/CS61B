package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    @Test
    public void getTest() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        MaxArrayDeque<Integer> ad = new MaxArrayDeque<>(comparator);
        ad.addLast(0);
        ad.addLast(1);
        ad.removeFirst()  ;
        ad.removeFirst()  ;
        ad.addFirst(4);
        ad.get(0);
        ad.addLast(6);
        ad.addLast(7);
        assertEquals((Integer) 7, ad.get(2));
    }

    @Test
    public void getMaxTest() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(comparator);
        mad.addLast(-2);
        mad.addLast(0);
        mad.addLast(-10);
        mad.addLast(-5);
        mad.addLast(-6);

        assertEquals((Integer) 0, mad.max(comparator));

    }
}
