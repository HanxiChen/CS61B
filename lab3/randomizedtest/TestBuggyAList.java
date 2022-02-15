package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void addTest(){
        BuggyAList<Integer> B = new BuggyAList<>();
        B.addLast(0);
        B.addLast(1);
        B.addLast(2);
        B.addLast(3);
        B.addLast(4);
        B.addLast(5);
        B.addLast(6);
        B.addLast(7);
        B.addLast(8);
    }



}
