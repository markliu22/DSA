// time: O(n * m^2)
// because for each boy (n), we use a set to make sure each girl is only asked once (m). thus dfs can go m deep. each call iterates over m girls also
// space: O(nm)

// TLDR:
// Hungarian Algorithm - Maximum Bipartite Matching
// Basically, if you got no date for prom, you can ask other boys if they will go with another girl they asked, you go with their girl
// Why do we re-use the same seen set for the recursive call?
    // During a recursive DFS call, we're effectively "asking" a currently matched girl to switch partners.
    // Thus, without reusing seen, the DFS might enter infinite loops or duplicate work, repeatedly trying to match the same girl multiple times.
// Then why do we give each boy a new seen set initially?
    // Each boy gets a new seen set because their matching attempt is independent of the other boys'.
    // A girl who couldn't be matched for one boy might be matchable when considered for another boy due to the differing recursive paths.


    class Solution {
        int bLength;
        int gLength;
        public int maximumInvitations(int[][] grid) {
            bLength = grid.length;
            gLength = grid[0].length;
    
            // stores matches
            Map<Integer, Integer> matches = new HashMap<>();
            for(int boy = 0; boy < bLength; ++boy) {
                // for each boy, resuse matches set but create new seen set
                dfs(grid, matches, new HashSet<>(), boy);
            }
    
            return matches.size();
        }
    
        private boolean dfs(int[][] grid, Map<Integer, Integer> matches, Set<Integer> seen, int boy) {
            for(int girl = 0; girl < gLength; ++girl) {
                // rule 1: only ask that girl if you haven't asked her before
                if(grid[boy][girl] == 1 && !seen.contains(girl)) {
                    seen.add(girl);
    
                    // rule 2: if you wish to ask a girl that's taken, she will only 
                    // go with you if her current partner finds a new girl
                    if(!matches.containsKey(girl) || dfs(grid, matches, seen, matches.get(girl))) {
                        matches.put(girl, boy);
                        return true;
                    }
                }
            }
            return false;
        }
    }