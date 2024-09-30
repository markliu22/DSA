// ref: Leetcode Connecting Cities With Minimum Cost

// note: unionfind find() is basically O(1) with path compression, rank, etc.

// constantly pick edge with min cost, if edge nodes not already in same component, can use this edge

class Solution {
    class UnionFind {
        int[] parent;
        int size;
        public UnionFind(int n) {
            parent = new int[n + 1];
            size = n;
            for(int i = 1; i <= n; ++i) parent[i] = i;
        }

        // find with path compression, rank, etc is almost O(1)
        public int find(int a) {
            if(a != parent[a]) {
                return find(parent[a]);
            } else {
                return a;
            }
        }

        public boolean union(int a, int b) {
            int parentA = find(a);
            int parentB = find(b);

            if(parentA == parentB) return false;

            size--;
            parent[parentA] = parentB;
            return true;
        }

        public int getSize() {
            return size;
        }
    }

    // n = n given
    // e = number of edges
    public int minimumCost(int n, int[][] connections) {
        // O(n)
        UnionFind uf = new UnionFind(n);

        // O(eloge)
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[2] - b[2]);
        for(int[] edge : connections) minHeap.add(edge);

        // O(e)
        int res = 0;
        while(!minHeap.isEmpty()) {
            // O(loge)
            int[] curr = minHeap.poll();
            // O(1) around
            if(uf.union(curr[0], curr[1])) {
                res += curr[2]; // we're adding this edge, thus count this cost
            }
        }

        if(uf.getSize() != 1) return -1; // was not able to make 1 connected component
        return res;
    }
}