package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 9; i++) {
            ad.addLast(i);
        }
        assertEquals("0 -> 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8", ad.printDequeString());
    }

    @Test
    public void removeTest1(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 9; i++) {
            ad.addLast(i);
        }

        assertNotNull(ad.removeFirst());
        assertNotNull(ad.removeLast());
//        assertEquals("队列为空", ad.printDeque());
    }

}
