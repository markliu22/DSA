// Binary Lifting tutorial https://youtu.be/oib-XsjFa-M

// O(nk) because O(k) per getKthAncestor, this is too slow, must be O(nlogk)
// Usually this means binary search or divide and conquer
// However... there is another method, which is to use powers of 2
    // Segment trees
    // Binary lifting
    // Sparse tables
// What's the idea with binary lifting?
// Well... there is logarithmic number of powers of 2

// Example:
// N = 100. Then powers of 2 are 1, 2, 4, 8, 16, 32, 64. log(N) total numbers.

// Questions:
// 1) Can I solve the question quickly is K is always a power of 2?
// 2) Using this, can I solve problem for general K?

// For 2), assume you have some black box that can help you jump up in powers of 2
// Thus, at some node, you want to get 6th ancestor. Quickly jump up 4, then jump up 2
// Every number can be represented as a sum of powers of 2

// In the preprocessing, for every node, need to know how to go up by 1, 2, 4, 8, ...

// up[v][j] = (2^j)th ancestor of v:
// for v = 0, ... , N-1:
    // up[v][0] = parent[v]
    // up[v][1] = up[ up[v][0] ][0]
    // up[v][2] = up[ up[v][1] ][1]
    // up[v][3] = up[ up[v][2] ][2] // ex: 2nd ancestor of my 2nd ancestor will give me my 4th ancestor
    // ... this continues logarithmic times

// REQUIRES: parent[i] < i

// EXAMPLE:
// 1483. Kth Ancestor of a Tree Node


class TreeAncestor {
    int[][] jump;
    int maxPow;

    // Constructor: O(nlogk) time complexity
    public TreeAncestor(int n, int[] parent) {
        // Calculate the maximum power of 2 needed to represent n
        // This determines the number of rows in our jump table
        maxPow = (int) (Math.log(n) / Math.log(2)) + 1;
        
        // Initialize the jump table
        // jump[p][j] stores the (2^p)th ancestor of node j
        jump = new int[maxPow][n];
        jump[0] = parent; // First row is the immediate parent

        // Fill the jump table using dynamic programming
        for (int p = 1; p < maxPow; p++) {
            for (int j = 0; j < n; j++) {
                int pre = jump[p - 1][j];
                // If the 2^(p-1) ancestor exists, find its 2^(p-1) ancestor
                // This gives us the 2^p ancestor of the current node
                jump[p][j] = pre == -1 ? -1 : jump[p - 1][pre];
            }
        }
    }

    // Method to get the k-th ancestor: O(logk) time complexity
    public int getKthAncestor(int node, int k) {
        int maxPow = this.maxPow;
        
        // Continue while we haven't reached the k-th ancestor and node is valid
        while (k > 0 && node > -1) {
            // Check if the bit at position maxPow in k is set
            // (k & (1 << maxPow)) performs a bitwise AND operation
            // If this bit is set, we can make a jump of size 2^maxPow
            if ((k & (1 << maxPow)) != 0) {
                node = jump[maxPow][node];  // Make the jump
                k -= 1 << maxPow;  // Subtract 2^maxPow from k
            } else {
                // If this bit isn't set, we check the next lower power of 2
                maxPow -= 1;
            }
        }
        return node;
    }
}