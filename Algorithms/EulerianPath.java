// Existence of Eulerian Path:
// A path that visists every edge exactly once.
// Choosing the wrong starting nodes can lead to having unreachable edges.

// Eulerian Circuit: 
// An Eulerian Path that starts and ends on the same node.
// If you know the graph cotnains an Eulerian Circuit, can start and end on any node.

// Conditions for Eulerian Path/Circuit:
// Undirected Graph:
    // Eulerian Path - either every vertex has even degree OR exactly 2 vertices have odd degree
    // Eulerian Circuit - every vertex has even degree

// Directed Graph:
    // Eulerian Path - at most 1 vertex has [outDegree - inDegree = 1] AND at most 1 vertex has [inDegree - outDegree = 1]. All other vertices have equal inDegree and outDegree
    // Eulerian Circuit - Every vertex has equal inDegree and outDegree

// ---------------------------------------------------------------------------------------------------------------------

// EX: 
// [0->1]
// [1->2]
// [2->1]
// [1->3]
// [3->4]

// start at 0, dfs, once curr node is stuck (no more unvisited outgoing edges), add curr node to FRONT of solution and backtrack
// sol:
// [4]
// [3, 4]
// [1, 3, 4]
// [2, 1, 3, 4]
// [1, 2, 1, 3, 4]
// [0, 1, 2, 1, 3, 4]

// time: O(E)

// Ex: Leetcode Reconstruct Itinerary
class Solution {
    List<String> res = new LinkedList<>();

    public List<String> findItinerary(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> graph = new HashMap<>();
        for(List<String> ticket : tickets) {
            String from = ticket.get(0);
            String to = ticket.get(1);

            if(!graph.containsKey(from)) graph.put(from, new PriorityQueue<>());
            graph.get(from).add(to);
        }

        dfs(graph, "JFK");

        return res;
    }

    public void dfs(Map<String,PriorityQueue<String>> adj, String current) {
        
        
        System.out.println("CURR: " + current);
        while(adj.containsKey(current) && !adj.get(current).isEmpty()) {
            String next = adj.get(current).poll();
            System.out.println("JUST GOT NEXT: " + next);
            dfs(adj,next);
        }
        System.out.println("CURR: " + current + " ADDED TO RES");

        // !!!
        res.addFirst(current);
    }
}