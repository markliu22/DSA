// BFS: Primarily used to find the shortest path in terms of the number of edges in unweighted graphs.
// Bellman-Ford: Used to find the shortest path in weighted graphs, including those with negative edge weights.
// Traversal Method:
// BFS: Explores nodes level by level, moving outward from the start node.
// Bellman-Ford: Relaxes all edges V-1 times (where V is the number of vertices), not necessarily in a level-wise manner.
// Distance Updates:
// BFS: Once a node is visited, its shortest distance from the source is finalized.
// Bellman-Ford: A node's distance can be updated multiple times as the algorithm progresses.

// ref: Hackerrank
void BellmanFord(Graph graph, int src) {
    int V = graph.V, E = graph.E;
    int dist[] = new int[V];

    // Step 1: Initialize distances from src to all
    // other vertices as INFINITE
    for (int i = 0; i < V; ++i)
        dist[i] = Integer.MAX_VALUE;
    dist[src] = 0;

    // Step 2: Relax all edges |V| - 1 times. A simple
    // shortest path from src to any other vertex can
    // have at-most |V| - 1 edges
    for (int i = 1; i < V; ++i) {
        for (int j = 0; j < E; ++j) {
            int u = graph.edge[j].src;
            int v = graph.edge[j].dest;
            int weight = graph.edge[j].weight;
            if (dist[u] != Integer.MAX_VALUE
                && dist[u] + weight < dist[v])
                dist[v] = dist[u] + weight;
        }
    }

    // Step 3: check for negative-weight cycles. The
    // above step guarantees shortest distances if graph
    // doesn't contain negative weight cycle. If we get
    // a shorter path, then there is a cycle.
    for (int j = 0; j < E; ++j) {
        int u = graph.edge[j].src;
        int v = graph.edge[j].dest;
        int weight = graph.edge[j].weight;
        if (dist[u] != Integer.MAX_VALUE
            && dist[u] + weight < dist[v]) {
            System.out.println(
                "Graph contains negative weight cycle");
            return;
        }
    }
    printArr(dist, V);
}

// ============================== Example: ============================== 
class Solution {
    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        int[] cost=new int[n];
        Arrays.fill(cost,Integer.MAX_VALUE);
        cost[src] = 0;

        for(int i = 0; i <= K; ++i) {

            // temp is just a copy of cost, cost updates to be temp after relaxing all nodes
            int[] temp= Arrays.copyOf(cost,n);

            for(int[] f: flights) {

                int curr = f[0];
                int next = f[1];
                int price = f[2];

                if(cost[curr]==Integer.MAX_VALUE) continue;

                temp[next] = Math.min(temp[next], cost[curr] + price);
            }

            // update cost to be temp
            cost=temp;
        }

        return cost[dst] == Integer.MAX_VALUE ? -1 : cost[dst];
    }
}
// Question: how does the K iterations ensure that we satisfied the "within k stops" clause?
// In the 0th iteration (i=0), we're only considering direct flights from the source.
// In the 1st iteration (i=1), we're considering routes with at most 1 stop.
// In the 2nd iteration (i=2), we're considering routes with at most 2 stops.
// ...and so on, until we've considered routes with at most K stops.