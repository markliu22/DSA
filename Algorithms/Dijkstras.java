// Basically just BFS with priority queue instead of queue

// this sometimes right, see NOTE below.
// time: O((V + E)logV) because we process each node once
    // For each node, we have to poll/add from minHeap which is a logV operation
// space: O(V) to store each node in minHeap


// NOTE: 
// Some questions like 'Leetcode Minimum Time to Visit Disappearing Nodes', 
// a node can indeed be added to the minHeap multiple times with different costs.
// Then the logV part doesn't make sense because minHeap size no longer bounded by V...

// Actual Complexity:
// In the worst case, we might add each edge to the queue, leading to O(E) insertions. 
// Each insertion and extraction takes O(log E) time (not log V, because the queue can grow larger than V).

// This gives us Time Complexity: O(ElogE).
// Space complexity: O(E)
