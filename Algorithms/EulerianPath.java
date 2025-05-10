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