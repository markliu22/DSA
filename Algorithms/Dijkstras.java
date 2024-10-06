// BFS with priority queue instead of queue

// EXAMPLE:
// https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner

// time: O(nm * log(nm)) because we process each node once. For each node, we have to poll/add from minHeap which is a log(nm) operation.
// space: O(nm) to store each node in minHeap
    // NOTE: we don't store the same node multiple times in minHeap
    // if our neighbour is a node we've never seen before, USING US TO GET THERE IS THE CHEAPEST WAY
    // thus, can "prematurely" mark as visited

// TLDR:
// Trick is that you can add weight to the edges
// a -> b, if b is an obstacle, this edge weight should increase by 1
// Then, just use Disjkstra's

class Solution {
    public int minimumObstacles(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[][] dirs = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        int[][] res = new int[n][m];
        for(int i = 0; i < n; ++i) {
            for(int j = 0; j < m; ++j) {
                res[i][j] = -1;
            }
        }

        // min heap based on total cost to get to node
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        minHeap.add(new int[]{0, 0, 0}); // add weight 0, cell (0, 0)
        res[0][0] = 0; // mark as visited

        while(!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int weight = curr[0];
            int row = curr[1];
            int col = curr[2];

            for(int[] dir : dirs) {
                int newRow = dir[0] + row;
                int newCol = dir[1] + col;
                if(newRow < 0 || newRow >= n || newCol < 0 || newCol >= m) continue;
                if(res[newRow][newCol] != -1) continue; // already reached this node before (and thus, was a cheapter way)

                int newWeight = weight;
                newWeight += grid[newRow][newCol] == 1 ? 1 : 0;
                minHeap.add(new int[]{newWeight, newRow, newCol}); // add neighbour
                res[newRow][newCol] = newWeight; // mark as vistied
            }
        }

        return res[n - 1][m - 1];
    }
}
