// 1. Naive Union-Find:
// Find: O(n)
// Union: O(1)
// This implementation is inefficient because find can be slow in the worst case.

// 2. Weighted Quick-Union:
// Find: O(log n)
// Union: O(1)
// In this implementation, the union operation always joins the smaller tree to the 
// larger one, which helps to keep the tree height balanced.

// 3. Path Compression:
// Find: O(log n) (amortized)
// Union: O(1)
// Path compression is an optimization technique that flattens the path from the root
// to the target node during the find operation, making subsequent find operations faster.



class UnionFind {
    private int[] parent;
    private int[] rank;
    // CONSTRUCTOR
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for(int i = 0; i < n; i++) parent[i] = i;
    }
    // FIND
    public int find(int p) {
        while(p != parent[p]) {
            p = parent[parent[p]]; // Each node is initially its own absolute root
        }
        return p;
    }
    // UNION, Returns true if we merged 2 components toegether, false otherwise
    public boolean union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP == rootQ) return false;
        // Attach smaller tree to larger tree
        if(rank[rootQ] > rank[rootP]) {
            parent[rootP] = rootQ;
        } else {
            parent[rootQ] = rootP;
            if(rank[rootQ] == rank[rootP]) {
                rank[rootP]++;
            }
        }
        return true;
    }
}


// ============================== Example: Number of Islands 2: ============================== 
public List<Integer> numIslands2(int m, int n, int[][] positions) {
    UnionFind uf = new UnionFind(m * n);
    ArrayList<Integer> res = new ArrayList<>();
    HashSet<Integer> processed = new HashSet<>(); // Holds processed islands. Need this to make sure we're only performin union with other islands
    int[][] dirs = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    int numIslands = 0;
    
    // Iterate through island position, for any islands adjacent to us, do union
    for(int[] pos : positions) {
        int curX = pos[0];
        int curY = pos[1];
        int p = n * curX + curY; // curr mapping
        if(processed.contains(p)) {
            res.add(numIslands);
            continue;
        } 
        processed.add(p);
        numIslands++; // !!! Initially increase number of islands
        for(int[] dir : dirs) {
            int X = curX + dir[0];
            int Y = curY + dir[1];
            int q = n * X + Y; // NEW MAPPING
            if(X < 0 || X >= m || Y < 0 || Y >= n) continue;
            if(!processed.contains(q)) continue; // Make sure we're only doing union with other islands
            
            // If these islands were originally separate, connect now, numIslands--
            boolean united = uf.union(p, q);
            if(united) numIslands--;
        }
        // Done processing current new islands (and 4 neighbours), now add this updated numIslands to res
        res.add(numIslands);
    }
    return res;
}