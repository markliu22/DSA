// Used to efficiently perform range queries and updates on an array.
// It is particularly useful when dealing with problems that require frequent sum, minimum, maximum, 
// greatest common divisor (GCD), or other associative operations over subarrays.

// Example Use Case:
// Suppose we have an array A = [1, 3, 5, 7, 9, 11], and we want to compute the sum in a given range [L, R] efficiently while allowing modifications to individual elements.

// time:
    // build: O(n)
    // sum(L, R): O(logn) 
    // update(pos, val): O(logn)
// as compared to brute force:
    // sum(L, R): O(N)
    // update(pos, Val): O(1)

class SegmentTree {
    private int[] tree; // Segment Tree array
    private int n; // Size of the input array

    // Constructor to build the Segment Tree from the input array
    public SegmentTree(int[] nums) {
        if (nums.length == 0) return;
        n = nums.length;
        tree = new int[4 * n]; // Allocate sufficient space for the segment tree
        build(nums, 0, 0, n - 1);
    }

    // Recursive function to build the Segment Tree
    private void build(int[] nums, int node, int start, int end) {
        if (start == end) {
            // Leaf node: directly store the array value
            tree[node] = nums[start];
        } else {
            int mid = (start + end) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;
            // Recursively build left and right child nodes
            build(nums, leftChild, start, mid);
            build(nums, rightChild, mid + 1, end);
            // Internal node stores the sum of its children
            tree[node] = tree[leftChild] + tree[rightChild];
        }
    }

    // Function to update an element at a specific index
    // The leaf updates, the nodes in the path going up to root also changes
    public void update(int index, int value) {
        update(0, 0, n - 1, index, value);
    }

    private void update(int node, int start, int end, int index, int value) {
        if (start == end) {
            // Directly update the value at the leaf node
            tree[node] = value;
        } else {
            int mid = (start + end) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;
            if (index <= mid) {
                // If index is in the left half, update the left child
                update(leftChild, start, mid, index, value);
            } else {
                // Otherwise, update the right child
                update(rightChild, mid + 1, end, index, value);
            }
            // !! After updating the child, recalculate the current node's sum
            tree[node] = tree[leftChild] + tree[rightChild];
        }
    }

    // Function to query the sum of elements in the range [left, right]
    // TLDR: can select some nodes from tree, will give answer because we have pre-computed sums of various segments
    public int query(int left, int right) {
        return query(0, 0, n - 1, left, right);
    }

    private int query(int node, int start, int end, int left, int right) {
        if (right < start || end < left) {
            // If the range is completely outside, return 0 (neutral element for sum)
            return 0;
        }
        if (left <= start && end <= right) {
            // If the current segment is completely inside the query range, return its sum
            return tree[node];
        }
        // Partial overlap: search both left and right children
        int mid = (start + end) / 2;
        int leftChild = 2 * node + 1;
        int rightChild = 2 * node + 2;
        int leftSum = query(leftChild, start, mid, left, right);
        int rightSum = query(rightChild, mid + 1, end, left, right);
        return leftSum + rightSum;
    }

    // Example usage
    public static void main(String[] args) {
        int[] nums = {1, 3, 5, 7, 9, 11}; // Input array
        SegmentTree st = new SegmentTree(nums);

        System.out.println(st.query(1, 3)); // Output: 15 (3+5+7)
        st.update(1, 10); // Update index 1 to 10
        System.out.println(st.query(1, 3)); // Output: 22 (10+5+7)
    }
}
