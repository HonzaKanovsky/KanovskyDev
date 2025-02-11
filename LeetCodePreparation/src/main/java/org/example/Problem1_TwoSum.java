package org.example;

import java.util.HashMap;
import java.util.Map;

public class Problem1_TwoSum {
    public int[] twoSum(int[] nums, int target) {

        Map<Integer, Integer> checkedNumbers = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (checkedNumbers.containsKey(target - nums[i])) {
                return new int[]{checkedNumbers.get(target - nums[i]), i};
            }
            checkedNumbers.put(nums[i], i);
        }
        return new int[]{0, 0};
    }
}
