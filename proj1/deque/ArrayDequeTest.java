package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(3);
        ad.addFirst(2);
        ad.addLast(4);
//        assertEquals("2 -> 3 -> 4", ad.printDeque());
    }

    @Test
    public void removeTest1(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(3);
        ad.addFirst(2);
        ad.addFirst(1);
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.addFirst(1);
        ad.addFirst(2);
        ad.addFirst(3);
//        assertEquals("队列为空", ad.printDeque());
    }

}
