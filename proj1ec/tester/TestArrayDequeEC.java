package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

import java.util.Random;
import java.util.StringJoiner;

public class TestArrayDequeEC {
    ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
    StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
    Random random = new Random();
    StringJoiner stackTrace = new StringJoiner("");
    private final int iterations = 100000;

    @Test
    public void randomlyCompareStudentArrayDequeToSolution() {
        double variate;
        int key;
        int count = 0;
        Integer exptected = 0;
        Integer actual = 0;
        stackTrace.add("\n"); // Ensures first invocation is on a new line.
        while (count < iterations) {
            variate = random.nextDouble();
            key = random.nextInt(100);
            count++;
            if (variate < 0.25)  {
                if (student.size() == 0) {
                    continue;
                }
                actual = student.removeLast();
                exptected = solution.removeLast();
                stackTrace.add("removeLast()\n");
                assertEquals(stackTrace.toString(), exptected, actual);
            } else if (0.26 <= variate && variate < 0.5) {
                solution.addFirst(key);
                student.addFirst(key);
                stackTrace.add(String.format("addFirst(%d)\n", key));
                assertEquals(stackTrace.toString(), solution.size(), student.size());
            } else if (0.5 <= variate && variate < 0.75) {
                if (student.size() == 0) {
                    continue;
                }
                actual = student.removeFirst();
                exptected = solution.removeFirst();
                stackTrace.add("removeFirst()\n");
                assertEquals(stackTrace.toString(), exptected, actual);
            } else {
                solution.addLast(key);
                student.addLast(key);
                stackTrace.add(String.format("addLast(%d)\n", key));
                assertEquals(stackTrace.toString(), solution.size(), student.size());
            }
        }
    }
}
