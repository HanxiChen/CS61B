package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 64; i++) {
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
    }

    @Test
    public void resizeTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 17; i++) {
            ad.addLast(i);
        }

        for (int i = 0; i < 16; i++) {
            ad.removeLast();
        }

        assertEquals("0", ad.printDequeString());
        assertEquals(1, ad.size());
    }
    @Test
    public void getTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(0);
        ad.addLast(1);
        assertEquals((Integer)1, ad.get(1));
    }

    @Test
    public void equalsTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addLast(1);
        ad.addLast(2);

        LinkedListDeque<Integer> lld = new LinkedListDeque<Integer>();
        lld.addLast(1);
        lld.addLast(2);

        assertTrue(ad.equals(lld));
    }
}
