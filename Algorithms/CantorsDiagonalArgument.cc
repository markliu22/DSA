// Proves the infinity of real numbers (decimals) is strictly larger than the infinity of natural numbers
// Imagine you write down an infinite list of *every possible* decimal number between 0 and 1.
// EX:
    // position     num
    // 0            0.01011...
    // 1            0.11100...
    // 2            0.00010...
    // ...
// Can easily disprove claim "this infinite list contains every single decimal number" by creating a new number with:
    // Opposite of 0th digit of 0th num
    // Opposite of 1st digit of 1st num
    // Opposite of 2nd digit of 2nd num
    // ...
// This new number can't be in the list. It can't be the 0th num bc 0th digit is diff. Can't be 1st num bc 1st digit is diff. Can't be nth num bc nth digit is diff.
// Even though you used an infinite amount of natural numbers to make your list, you still couldn't capture all the decimals.
// Thus infinity of decimal numbers is fundamentally "bigger" and more dense than the infinity of natural numbers

// EX: Leetcode 1980. Find Unique Binary String

// time: O(n)
// space: O(n)

// Cantor's diagonal argument

class Solution {
public:
    string findDifferentBinaryString(vector<string>& nums) {
        int n = nums.size();
        string res = "";
        for(int i = 0; i < n; ++i) {
            res += nums[i][i] == '0' ? '1' : '0';
        }
        return res;
    }
};