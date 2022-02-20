package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

import java.util.Random;
import java.util.StringJoiner;

public class TestArrayDequeEC {
    @Test
    public void test1() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ad = new ArrayDequeSolution<>();

        Integer expected = 0;
        Integer actual = 0;
        StringJoiner sj = new StringJoiner("");

        int N = 5000;
        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            int randVal = StdRandom.uniform(0, 100);

            if (operationNumber == 0) {
                student.addFirst(randVal);
                ad.addFirst(randVal);
                sj.add("\naddFirst(" + randVal + ")");
                assertEquals(sj.toString(), ad.size(), student.size());
            } else if (operationNumber == 1) {
                student.addLast(randVal);
                ad.addLast(randVal);
                sj.add("\naddLast(" + randVal + ")");
                assertEquals(sj.toString(), ad.size(), student.size());
            } else if (operationNumber == 2) {
                if (student.size() == 0) {
                    continue;
                }
                actual = student.removeFirst();
                expected = ad.removeFirst();
                sj.add("\nremoveFirst()");
                assertEquals(sj.toString(), expected, actual);
            } else {
                if (student.size() == 0) {
                    continue;
                }
                actual = student.removeLast();
                expected = ad.removeLast();
                sj.add("\nremoveLast()");
                assertEquals(sj.toString(), expected, actual);
            }
        }
    }



}
