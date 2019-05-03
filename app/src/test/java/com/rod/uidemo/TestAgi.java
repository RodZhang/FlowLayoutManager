package com.rod.uidemo;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Rod
 * @date 2019/4/16
 */
public class TestAgi {

    @Test
    public void checkInclusion() {
        Assert.assertTrue(checkInclusion("ab", "eidbaooo"));
    }

    public boolean checkInclusion(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() > s2.length()) {
            return false;
        }

        int[] char1 = new int[26];
        int[] char2 = new int[26];

        for (int i = 0, size = s1.length(); i < size; i++) {
            char1[s1.charAt(i) - 'a']++;
            char2[s2.charAt(i) - 'a']++;
        }

        if (s1.length() == s2.length() && isSame(char1, char2)) {
            return true;
        }

        for (int i = s1.length(), size = s2.length(); i < size; i++) {
            if (isSame(char1, char2)) {
                return true;
            }

            char2[s2.charAt(i - s1.length()) - 'a']--;
            char2[s2.charAt(i) - 'a']++;
        }

        return false;
    }

    private boolean isSame(int[] char1, int[] char2) {
        for (int i = 0; i < char1.length; i++) {
            if (char1[i] != char2[i]) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void testSearch() {
        Assert.assertEquals(search(new int[]{3,1}, 3), 0);
    }

    public int search(int[] nums, int target) {
        if (nums.length == 0) {
            return -1;
        }
        if (nums.length == 1) {
            return nums[0] == target ? 0 : -1;
        }
        int minNumIndex = findIndex(nums);
        int startIndex, endIndex;

        if (minNumIndex == 0 || (nums[minNumIndex] <= target && target <= nums[0])) {
            startIndex = minNumIndex;
            endIndex = nums.length - 1;
        } else if (minNumIndex >= 1 && nums[0] <= target && target <= nums[minNumIndex - 1]) {
            startIndex = 0;
            endIndex = minNumIndex - 1;
        } else {
            return -1;
        }

        return findTarget(nums, startIndex, endIndex, target);
    }

    private int findIndex(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1]) {
                return i;
            }
        }
        return 0;
    }

    private int findTarget(int[] nums, int startIndex, int endIndex, int target) {
        if (nums[startIndex] == target) {
            return startIndex;
        }
        if (nums[endIndex] == target) {
            return endIndex;
        }

        if (startIndex >= endIndex) {
            return -1;
        }

        int mid = (startIndex + endIndex) / 2;
        if (nums[mid] == target) {
            return mid;
        }
        if (nums[startIndex] < target && target < nums[mid]) {
            return findTarget(nums, startIndex+1, mid - 1, target);
        } else if (nums[mid] < target && target < nums[endIndex]) {
            return findTarget(nums, mid + 1, endIndex - 1, target);
        } else {
            return -1;
        }
    }

//    @Test
//    public void testThreeSum() {
//        int[] nums = {-1, 0, 1, 2, -1, -4};
//        System.out.println(threeSum(nums));
//    }
//
//    private List<List<Integer>> threeSum(int[] nums) {
//        List<List<Integer>> result = new ArrayList<>();
//        if (nums == null || nums.length < 3) {
//            return result;
//        }
//
//        Arrays.sort(nums);
//
//        int left, right, tmp;
//        for (int i = 0; i < nums.length; ) {
//            left = i + 1;
//            right = nums.length - 1;
//            tmp = -nums[i];
//            while (left < right) {
//                if (nums[left] + nums[right] < tmp) {
//                    left++;
//                } else if (nums[left] + nums[right] > tmp) {
//                    right--;
//                } else {
//                    List<Integer> elem = new ArrayList<>(3);
//                    elem.add(nums[i]);
//                    elem.add(nums[left]);
//                    elem.add(nums[right]);
//                    result.add(elem);
//                    break;
//                }
//            }
//
//            i = nums[i] == nums[left] ? left + 1 : i++;
//        }
//
//        return result;
//    }
//
//    public void push(int... elem) {
//Arrays.asList(elem);
//    }
}
