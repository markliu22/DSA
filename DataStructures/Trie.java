class Trie {
    class TrieNode {
        char val; 
        TrieNode[] children;
        boolean isWord;
        public TrieNode(char val) {
            this.val = val;
            this.children = new TrieNode[26];
            this.isWord = false;
            for(int i = 0; i < 26; ++i) children[i] = null;
        }
    }

    TrieNode root;
    public Trie() {
        root = new TrieNode('.');
    }
    
    public void insert(String word) {
        int n = word.length();
        TrieNode curr = root;
        for(int i = 0; i < n; ++i) {
            char c = word.charAt(i);
            // add this child if needed
            if(curr.children[c - 'a'] == null) {
                curr.children[c - 'a'] = new TrieNode(c);
            }
            
            // iterate down to this node
            curr = curr.children[c - 'a'];

            // mark node as isWord if needed
            if(i == n - 1) {
                curr.isWord = true;
            }
        }
    }
    
    public boolean search(String word) {
        int n = word.length();
        TrieNode curr = root;
        for(int i = 0; i < n; ++i) {
            char c = word.charAt(i);
            if(curr.children[c - 'a'] == null) return false;

            // iterate down to this node
            curr = curr.children[c - 'a'];

            // if at last letter
            if(i == n - 1) return curr.isWord;
        }
        return false;
    }
    
    public boolean startsWith(String prefix) {
        int n = prefix.length();
        TrieNode curr = root;
        for(int i = 0; i < n; ++i) {
            char c = prefix.charAt(i);
            if(curr.children[c - 'a'] == null) return false;

            // iterate down to this node
            curr = curr.children[c - 'a'];

            // if at last letter
            if(i == n - 1) return true;
        }
        return false;
    }
}
s