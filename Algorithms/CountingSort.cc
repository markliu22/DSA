// time: O(N + K)
// space: O(K)
    // where N = arr.size(), K = range of the input (maxElement - minElement)

// EX: Leetcode Minimum Absolute Difference

// Counting Sort
// Use a boolean-style array (a "line") to represent the presence of numbers across their entire range
// Map every number in input arr to an index in line
// To account for neg nums in og arr, calc shift. line[num + shift] = true.
// This sorts the numbers
// After this can just have single pass to calc min abs diff and add pairs to res


class Solution {
public:
    vector<vector<int>> minimumAbsDifference(vector<int>& arr) {
        int minElement = *min_element(arr.begin(), arr.end());
        int maxElement = *max_element(arr.begin(), arr.end());
        int shift = -minElement;

        vector<int> line(maxElement - minElement + 1);
        vector<vector<int>> res;

        for(const int& num : arr) {
            line[num + shift] = 1;
        }

        int minPairDiff = maxElement - minElement;
        int prev = 0;

        for(int curr = 1; curr <= maxElement + shift; ++curr) {
            if(line[curr] == 0) continue;

            if(curr - prev == minPairDiff) {
                res.push_back({prev - shift, curr - shift});
            } else if(curr - prev < minPairDiff) {
                minPairDiff = curr - prev;
                res = {{prev - shift, curr - shift}};
            }

            prev = curr;
        }

        return res;
    }
};