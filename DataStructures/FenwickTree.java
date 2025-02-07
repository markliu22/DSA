import java.util.Arrays;

// Prefix sum queries O(logn)
// Point updates in O(logn)

class FenwickTree {
    int n;
    int[] tree;

    FenwickTree(int n) {
        this.n = n;
        tree = new int[n + 1]; // 1-based index
    }

    // Update:
    // adds value to index i
    void update(int i, int val) {
        for (++i; i <= n; i += i & -i) {
            tree[i] += val;
        }
    }

    // Prefix sum: 
    // Sum of elements from index 0 to i
    int prefixSum(int i) {
        int sum = 0;
        for (++i; i > 0; i -= i & -i) {
            sum += tree[i];
        }
        return sum;
    }

    // Range sum: 
    // Sum from index l to r
    int rangeSum(int l, int r) {
        return prefixSum(r) - prefixSum(l - 1);
    }

    public static void main(String[] args) {
        FenwickTree ft = new FenwickTree(10);
        ft.update(3, 5);  // Add 5 at index 3
        ft.update(5, 2);  // Add 2 at index 5
        System.out.println(ft.rangeSum(1, 5)); // Output: 5 (index 3) + 2 (index 5) = 7
    }
}

// Example: Leetcode Maximum Difference Between Even and Odd Frequency II
// Slightly different Fenwick:
// Fenwick tree class
// Instead of efficient prefix sum, it's efficient Range Minimum Queries (RMQ)
// RMQ find the minimum element within some subarray
static class FenwickTree {
    int n;
    int[] tree;

    FenwickTree(int n) {
        this.n = n;
        tree = new int[n + 1];
        Arrays.fill(tree, Integer.MAX_VALUE);
    }

    void update(int i, int val) {
        // start at node i + 1 like usual
        // instead of setting node value = subarray sum, set node val = min subarray element
        for (int pos = i + 1; pos <= n; pos += pos & -pos) {
            tree[pos] = Math.min(tree[pos], val);
        }
    }

    int query(int i) {
        int res = Integer.MAX_VALUE;
        // start at node i + 1 like usual
        // instead of returning subarray sum, return min subarray element
        for (int pos = i + 1; pos > 0; pos -= pos & -pos) {
            res = Math.min(res, tree[pos]);
        }
        return res;
    }
}