package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Problem1_TwoSumTest {

    @Test
    void testCase1() {
        Problem1_TwoSum solver = new Problem1_TwoSum();

        // Test case 1
        int[] result1 = solver.twoSum(new int[]{2, 7, 11, 15}, 9);
        assertArrayEquals(new int[]{0, 1}, result1);
    }

    @Test
    void testCase2() {
        Problem1_TwoSum solver = new Problem1_TwoSum();

        // Test case 2
        int[] result2 = solver.twoSum(new int[]{3, 2, 4}, 6);
        assertArrayEquals(new int[]{1, 2}, result2);

    }

    @Test
    void testCase3() {
        Problem1_TwoSum solver = new Problem1_TwoSum();

        // Test case 3
        int[] result3 = solver.twoSum(new int[]{3, 3}, 6);
        assertArrayEquals(new int[]{0, 1}, result3);
    }
}