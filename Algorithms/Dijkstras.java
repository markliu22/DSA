// TLDR: Basically just BFS with priority queue instead of queue

// Time: O(|V| + |E|log|E|)
// Space: O(|V| + |E|)
// ref: https://www.youtube.com/watch?v=YMyO-yZMQ6g


// ASIDE NOTE:
// Some questions are tricky to determine if Dijkstra's or DP should be used, such as Grid Teleportation Travel
// Use Dijkstra's if:
    // Cost to reach a state always the same regardless of how you got there
    // State space is linear (1D, 2D, 3D) without cycles
// Use DP if:
    // Conditional transitions / multiple ways to reach a state with different costs
    // State transitions resemble graph edges with varying cost/usage constraints


// EX: Leetcode Minimum Time to Visit Disappearing Nodes
class Solution {
    public int[] minimumTime(int n, int[][] edges, int[] disappear) {
        // build graph
        // have to do this Math.min thing because there could be edge [1, 2] with cost 10, and also edge [1, 2] with cost 3 so we just keep the min cost for each edge
        HashMap<Integer, HashMap<Integer, Integer>> graph = new HashMap<>();
        for(int i = 0; i < n; ++i) graph.put(i, new HashMap<>());
        for(int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            int cost = edge[2];
            graph.get(from).put(to, Math.min(cost, graph.get(from).getOrDefault(to, Integer.MAX_VALUE)));
            graph.get(to).put(from, Math.min(cost, graph.get(to).getOrDefault(from, Integer.MAX_VALUE)));
        }

        // dijkstras
        int[] res = new int[n];
        Arrays.fill(res, Integer.MAX_VALUE);
        res[0] = 0;

        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        minHeap.add(new int[]{0, 0}); // node, cost

        while(!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int currNode = curr[0];
            int currCost = curr[1];
            if(currCost > disappear[currNode]) continue;
            if(currCost > res[currNode]) continue; // Need this to avoid TLE

            for(Integer neighbour : graph.get(currNode).keySet()) {
                int edgeCost = graph.get(currNode).get(neighbour);
                int newCost = currCost + edgeCost;
                if(newCost < disappear[neighbour] && newCost < res[neighbour]) {
                    minHeap.add(new int[]{neighbour, newCost});
                    res[neighbour] = newCost;
                }
            }
        }

        for(int i = 0; i < n; ++i) if(res[i] == Integer.MAX_VALUE) res[i] = -1;
        return res;
    }
}