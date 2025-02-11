package org.example;

import org.example.model.ListNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Problem2_addTwoNumbersTest {

    private ListNode createList(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (int val : values) {
            current.next = new ListNode(val);
            current = current.next;
        }
        return dummy.next;
    }

    private int[] linkedListToArray(ListNode node) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        while (node != null) {
            result.add(node.val);
            node = node.next;
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

    @Test
    void testAddTwoNumbers_Case1() {
        Problem2_AddTwoNumbers solver = new Problem2_AddTwoNumbers();
        ListNode l1 = createList(2, 4, 3);
        ListNode l2 = createList(5, 6, 4);

        ListNode result = solver.addTwoNumbers(l1, l2);
        assertArrayEquals(new int[]{7, 0, 8}, linkedListToArray(result));
    }

    @Test
    void testAddTwoNumbers_Case2() {
        Problem2_AddTwoNumbers solver = new Problem2_AddTwoNumbers();
        ListNode l1 = createList(0);
        ListNode l2 = createList(0);

        ListNode result = solver.addTwoNumbers(l1, l2);
        assertArrayEquals(new int[]{0}, linkedListToArray(result));
    }

    @Test
    void testAddTwoNumbers_Case3() {
        Problem2_AddTwoNumbers solver = new Problem2_AddTwoNumbers();
        ListNode l1 = createList(9, 9, 9, 9, 9, 9, 9);
        ListNode l2 = createList(9, 9, 9, 9);

        ListNode result = solver.addTwoNumbers(l1, l2);
        assertArrayEquals(new int[]{8, 9, 9, 9, 0, 0, 0, 1}, linkedListToArray(result));
    }
}