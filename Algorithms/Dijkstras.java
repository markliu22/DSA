// AKA greedy BFS based on weight

class Solution {

    //  returns cheaptest way to get from src to all other nodes
    public Map<Integer, Integer> shortestPath(int n, List<List<Integer>> edges, int src) {
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adj.put(i, new ArrayList<>());
        }
    
        // s = src, d = dst, w = weight
        for (List<Integer> edge : edges) {
            int s = edge.get(0), d = edge.get(1), w = edge.get(2);
            adj.get(s).add(new int[]{d, w});
        }
    
        // Compute shortest paths
        Map<Integer, Integer> shortest = new HashMap<>();
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minHeap.offer(new int[]{0, src});
    
        while (!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int w1 = curr[0], n1 = curr[1];
    
            if (shortest.containsKey(n1)) continue;

            // recall BFS, seeing node for first time? guaranteed this is fastest way to get to it
            // now disjktra's, seeing node for first time? guaranteed this is the cheapest way to get to it!
            shortest.put(n1, w1);

            for (int[] edge : adj.get(n1)) {
                int n2 = edge[0], w2 = edge[1];
                if (!shortest.containsKey(n2)) {
                    minHeap.offer(new int[]{w1 + w2, n2});
                }
            }
        }

        for (int i = 0; i < n; i++) {
            shortest.putIfAbsent(i, -1);
        }
    
        return shortest;
    }  
}

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
