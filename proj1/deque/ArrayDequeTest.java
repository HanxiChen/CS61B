package deque;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void addTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addLast(0);
        ad.addFirst(1);
        ad.removeLast();
        ad.removeLast();
        ad.addFirst(4);
        assertEquals((Integer) 4, ad.removeLast());
    }

    @Test
    public void removeTest1(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.removeLast();
        ad.removeLast();
        ad.removeLast();
        assertNotNull(ad.removeFirst());
        assertNotNull(ad.removeLast());
        assertEquals(0, ad.size());
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

//        assertEquals("0", ad.printDequeString());
        assertEquals(1, ad.size());
    }
    @Test
    public void getTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addLast(0);
        ad.removeFirst() ;
        ad.addFirst(2);
        ad.addLast(3);
        ad.removeLast()      ;
        ad.addFirst(5);
        ad.addFirst(6);
        ad.addLast(7);
        ad.removeLast()    ;
        ad.get(0)     ;
        ad.addFirst(10);
        ad.addFirst(11);
        ad.addLast(12);
        ad.removeFirst()    ;
        ad.get(4)      ;
        ad.addLast(15);
        ad.get(3)      ;
        ad.addFirst(17);
        ad.addLast(18);
        ad.addFirst(19);
        ad.removeFirst()   ;
        ad.removeFirst();
        ad.addFirst(22);

        assertEquals((Integer) 18, ad.removeLast());
    }

    @Test
    public void equalsTest(){
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        ArrayDeque<Integer> ad2 = new ArrayDeque<>();

        LinkedListDeque<Integer> lld = new LinkedListDeque<Integer>();

        assertTrue(ad1.equals(ad2));
    }

    @Test
    public void IteratorTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
//        ad.addFirst(1);
//        ad.addLast(2);
//        ad.addLast(3);
        Iterator<Integer> c = ad.iterator();
        int i = 0;
        while(c.hasNext()) {
            assertEquals(ad.get(i), c.next());
            i++;
        }
    }
}
