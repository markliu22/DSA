// time: O(nlogn)

// Useful for Longest Increasing Subsequence
// https://leetcode.com/problems/longest-increasing-subsequence/\

// EX:
// [4, 5, 6, 3]

// Initially, curr number us 4
// Set tails[0] = 4
// This means, smallest ending/tail number of a LIS of length 1 is 4
    // which makes sense, just [4]

// Next, curr number is 5
// lo = 0, hi = 0
// Binary Search of leftmost satisfiable pile gives lo = 1
// Set tails[1] = 5
// This means, smallest ending number of a LIS of length 2 is 5
    // which makes sense, just [4, 5]

// Next, curr number is 6
// lo = 0, hi = 1
// Binary search gives lo = 2
// Set tails[2] = 6
// This means, smallest ending number of a LIS of length 3 is 6
    // which makes sense, just [4, 5, 6]

// Next, curr number is 3
// lo = 0, hi = 2
// Binary search gives lo = 0
// Set tails[0] = 3
// This means, smallest ending number of a LIS of length 1 is 3
    // which makes sense, just [3]
    // You might think, won't this mess up the later tails values?
        // Like tails[1] should be 4 now for [3, 4] instead of 5 for [4, 5] since we just found a smaller LIS of length 1 with value 3
    // It won't because [3, 4] not even valid LIS

public int lengthOfLIS(int[] nums) {
    if(nums == null || nums.length == 0) return 0;
    int lastPileIndex = 0;
    int n = nums.length;
    
    // track last number in each pile
    int[] tails = new int[n];
    tails[0] = nums[0];
    
    for(int i = 1; i < n; i++) {
        int num = nums[i];
        
        // binary search to find leftmost pile that satisfies
        int lo = 0;
        int hi = lastPileIndex;
        
        while(lo <= hi) {
            int mid = (hi - lo) / 2 + lo;
            // if my number too big, move right
            if(num > tails[mid]) lo = mid + 1;
            // if my number satisfies, try to go even more left (greedy)
            else hi = mid - 1;
        }
        
        // now found leftmost pile that satisfies, insert card into pile
        tails[lo] = num;
        if(lo == lastPileIndex + 1) lastPileIndex++;
    }        
    return lastPileIndex + 1;
}