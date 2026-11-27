// See java implementation

// ================================ Example: Number of Good Paths: ================================ 

struct UnionFind {
    vector<int> parent;
    vector<int> rank;
    int cnt;

    UnionFind(int cnt) : cnt(cnt) {
        parent = vector<int>(cnt);
        rank = vector<int>(cnt);

        for(int i = 0; i < cnt; ++i) parent[i] = i;
    }

    int find(int p) {
        if(parent[p] == p) return p;
        parent[p] = find(parent[p]); // NOTE: this is the path compression here, instead of just return find(parent[p]);
        return parent[p];
    }

    bool united(int p, int q) {
        return find(p) == find(q);
    }

    void unite(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP == rootQ) return;

        // NOTE: this is the union by rank, instead of just blindly setting parent of one equal to the other
        if(rank[rootP] < rank[rootQ]) {
            parent[rootP] = rootQ;
        } else {
            parent[rootQ] = rootP;
            if(rank[rootP] == rank[rootQ]) rank[rootQ]++;
        }
    }
};

class Solution {
public:
    int numberOfGoodPaths(vector<int>& vals, vector<vector<int>>& edges) {
        int n = vals.size();
        int res = 0;

        vector<vector<int>> adj(n);
        map<int, vector<int>> sameVals;
        
        for(int i = 0; i < n; ++i) {
            sameVals[vals[i]].push_back(i);
        }

        for(const auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];

            if(vals[u] >= vals[v]) {
                adj[u].push_back(v);
            } else {
                adj[v].push_back(u);
            }
        }



        UnionFind uf(n);

        // process nodes by ascending vals
        for(const auto& [val, allNodes] : sameVals) {
            // connect nodes with val `val` to neighbours with val <= `val`
            for(int u : allNodes) {
                for(int v : adj[u]) {
                    uf.unite(u, v);
                }
            }

            // count how many nodes of curr `val` belong to each component root
            unordered_map<int, int> group;
            for(int u : allNodes) {
                group[uf.find(u)]++;
            }

            // edge case: question counts single nodes as a valid good path too for some reason
            res += allNodes.size();

            // Pairs of same-value nodes in the same component: n * (n - 1) / 2
            for (auto &[_, size] : group) {
                res += (1LL * size * (size - 1) / 2);
            }
        }
        return res;
    }
};

// ===================== Example: Remove Max Number of Edges to Keep Graph Fully Traversable: ===================== 

struct UnionFind {
    vector<int> parent;
    vector<int> rank;
    int cnt;

    UnionFind(int cnt) : cnt(cnt) {
        parent = vector<int>(cnt);
        rank = vector<int>(cnt);

        for(int i = 0; i < cnt; ++i) parent[i] = i;
    }

    int find(int p) {
        if(parent[p] == p) return p;
        parent[p] = find(parent[p]); // NOTE: this is the path compression here, instead of just return find(parent[p]);
        return parent[p];
    }

    bool united(int p, int q) {
        return find(p) == find(q);
    }

    bool unite(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return false;

        // NOTE: this is the union by rank, instead of just blindly setting parent of one equal to the other
        if (rank[rootP] < rank[rootQ]) {
            parent[rootP] = rootQ;
        } else {
            parent[rootQ] = rootP;
            if (rank[rootP] == rank[rootQ]) rank[rootQ]++;
        }

        cnt--;
        return true;
    }

    bool isConnected() const {
        return cnt == 1;
    }
};

class Solution {
public:
    int maxNumEdgesToRemove(int n, vector<vector<int>>& edges) {
        // Multiple UF !
        UnionFind Alice(n);
        UnionFind Bob(n);

        // Find MIN num edges to ADD (instead of MAX num edges to REMOVE)
        int minNumEdgesToAdd = 0;
        // Intuitively, first add edges A & B can both traverse
        for(const vector<int>& edge : edges) {
            if(edge[0] == 3) {
                int u = edge[1] - 1;
                int v = edge[2] - 1;
                minNumEdgesToAdd += (Alice.unite(u, v) | Bob.unite(u, v));
            }
        }

        for(const vector<int>& edge : edges) {
            int u = edge[1] - 1;
            int v = edge[2] - 1;
            if(edge[0] == 1) {
                minNumEdgesToAdd += Alice.unite(u, v);
            } else if(edge[0] == 2) {
                minNumEdgesToAdd += Bob.unite(u, v);
            }
        }

        // Alice and Bob UF data structures EACH track n nodes
        // So Alice's n nodes are all a part of 1 connected component
        // and Bob's (same) n nodes are all a part of 1 connected component
        // Thus, Alice & Bob must be 1 connected component
        if(Alice.isConnected() && Bob.isConnected()) {
            return edges.size() - minNumEdgesToAdd;
        }

        return -1;
    }
};