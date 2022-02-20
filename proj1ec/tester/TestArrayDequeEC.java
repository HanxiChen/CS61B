package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void test1() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ad = new ArrayDequeSolution<>();

        Integer expected = 0;
        Integer actual = 0;

        int N = 500;
        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            int randVal = StdRandom.uniform(0, 100);
            String str = "";
            if (operationNumber == 0) {
                student.addFirst(randVal);
                ad.addFirst(randVal);
                str = "addFirst(" + randVal + ")\n";
                assertEquals(str, ad.size(), student.size());
            } else if (operationNumber == 1) {
                student.addLast(randVal);
                ad.addLast(randVal);
                str = "addLast(" + randVal + ")\n";
                assertEquals(str, ad.size(), student.size());
            } else if (operationNumber == 2) {
                actual = student.removeFirst();
                expected = ad.removeFirst();
                str = "removeFirst()";
                assertEquals(str, expected, actual);
            } else {
                actual = student.removeLast();
                expected = ad.removeLast();
                str = "removeLast()";
                assertEquals(str, expected, actual);
            }
        }
    }
}
