// See java implementation

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