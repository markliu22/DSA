// https://medium.com/coinmonks/merkle-tree-a-simple-explanation-and-implementation-48903442bc08

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HexFormat;

public class MerkleTree {
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String HASH_ALGORITHM = "SHA-256";

    private static class Node {
        public String hash;
        public String direction;

        public Node(String hash, String direction) {
            this.hash = hash;
            this.direction = direction;
        }

        @Override
        public String toString() {
            // just for cleaner output, only first 8 chars of hash
            return "Hash: " + hash.substring(0, 8) + "... | Direction: " + direction;
        }
    }

    private static String sha256(String data) {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] hash = digest.digest(data.getBytes());
        // Convert the byte array hash to a hexadecimal string.
        return HexFormat.of().formatHex(hash);
    }

    private static void ensureEven(List<String> hashes) {
        if(hashes.size() % 2 != 0) {
            hashes.add(hashes.get(hashes.size() - 1));
        }
    }

    // Determine if a lead hash is a LEFT (even idx) or RIGHT (odd idx) child
    private static String getLeafNodeDirectionInMerkleTree(String hash, List<List<String>> merkleTree) {
        List<String> leafHashes = merkleTree.get(0);

        int hashIndex = leafHashes.indexOf(hash);
        if(hashIndex == -1) return "";
        return hashIndex % 2 == 0 ? LEFT : RIGHT;
    }

    // Creates the full Merkle Tree, represented as a list of hash lists
    // for each level. The first list is the leaf nodes, and the last
    // list contains only the Merkle Root.
    private static List<List<String>> generateMerkleTree(List<String> hashes) {
        if(hashes.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> currLevelHashes = new ArrayList<>(hashes);

        List<List<String>> tree = new ArrayList<>();
        tree.add(currLevelHashes);

        while(currLevelHashes.size() > 1) {
            ensureEven(currLevelHashes);
            List<String> nextLevelHashes = new ArrayList<>();
            for(int i = 0; i < currLevelHashes.size(); i += 2) {
                String hashPairConcatenated = currLevelHashes.get(i) + currLevelHashes.get(i + 1);
                String newHash = sha256(hashPairConcatenated);
                nextLevelHashes.add(newHash);
            }
            tree.add(nextLevelHashes);
            currLevelHashes = nextLevelHashes;
        }

        return tree;
    }

    // Generates the Merkle Root hash from a list of leaf hashes.
    private static String generateMerkleRoot(List<String> hashes) {
        // Uses the same logic as generateMerkleTree, but only keeps track of the root
        if (hashes.isEmpty()) {
            return "";
        }
        
        List<String> currLevelHashes = new ArrayList<>(hashes);
        
        while (currLevelHashes.size() > 1) {
            ensureEven(currLevelHashes);
            List<String> combinedHashes = new ArrayList<>();
            for (int i = 0; i < currLevelHashes.size(); i += 2) {
                String hashPairConcatenated = currLevelHashes.get(i) + currLevelHashes.get(i + 1);
                String hash = sha256(hashPairConcatenated);
                combinedHashes.add(hash);
            }
            currLevelHashes = combinedHashes;
        }
        
        return currLevelHashes.get(0);
    }

    // Generates the Merkle Proof for a given leaf hash 
    // The proof is the list of sibling hashes required to reconstruct the 
    // Merkle Root from the target leaf hash
    private static List<Node> generateMerkleProof(String hash, List<String> hashes) {
        if (hashes.isEmpty() || hash == null) {
            return new ArrayList<>();
        }
        
        // Build the full tree
        List<List<String>> tree = generateMerkleTree(hashes);
        
        int hashIndex = tree.get(0).indexOf(hash);
        if (hashIndex == -1) {
            return new ArrayList<>(); // Hash not found
        }
        
        // Init proof with the target hash itself
        List<Node> merkleProof = new ArrayList<>();
        merkleProof.add(new Node(hash, getLeafNodeDirectionInMerkleTree(hash, tree)));
        
        // Iterate through the levels of the tree (excluding the last level which is the root)
        for (int level = 0; level < tree.size() - 1; level++) {
            List<String> currentLevel = new ArrayList<>(tree.get(level));
            ensureEven(currentLevel); 
            boolean isLeftChild = hashIndex % 2 == 0;
            String siblingDirection = isLeftChild ? RIGHT : LEFT;
            int siblingIndex = isLeftChild ? hashIndex + 1 : hashIndex - 1;
            
            // Create the sibling Node and add it to the proof
            Node siblingNode = new Node(
                currentLevel.get(siblingIndex),
                siblingDirection
            );
            merkleProof.add(siblingNode);
            
            // Move up to the parent node's position in the next level
            hashIndex = hashIndex / 2;
        }
        
        return merkleProof;
    }

    // Merkle Proof Verification
    // Reconstruct Merkle Root hash from starting hash and its Merkle Proof
    // If resulting hash matches the expected Merkle Root, the proof is valid
    private static String getMerkleRootFromMerkleProof(List<Node> merkleProof) {
        if (merkleProof.isEmpty()) {
            return "";
        }
        
        String currentHash = merkleProof.get(0).hash;
        for (int i = 1; i < merkleProof.size(); i++) {
            Node siblingNode = merkleProof.get(i);
            // Concatenate the current hash with the sibling hash IN THE CORRECT ORDER
            String hashPairConcatenated;
            if (siblingNode.direction.equals(RIGHT)) {
                hashPairConcatenated = currentHash + siblingNode.hash;
            } else {
                hashPairConcatenated = siblingNode.hash + currentHash;
            }
            currentHash = sha256(hashPairConcatenated);
        }
        return currentHash;
    }

    public static void main(String[] args) {
        // Random hashes
        List<String> hashes = Arrays.asList(
            "95cd603fe577fa9548ec0c9b50b067566fe07c8af6acba45f6196f3a15d511f6",
            "709b55bd3da0f5a838125bd0ee20c5bfdd7caba173912d4281cae816b79a201b",
            "27ca64c092a959c7edc525ed45e845b1de6a7590d173fd2fad9133c8a779a1e3",
            "1f3cb18e896256d7d6bb8c11a6ec71f005c75de05e39beae5d93bbd1e2c8b7a9",
            "41b637cfd9eb3e2f60f734f9ca44e5c1559c6f481d49d6ed6891f3e9a086ac78", // Index 4 (hashToProve)
            "a8c0cce8bb067e91cf2766c26be4e5d7cfba3d3323dc19d08a834391a1ce5acf",
            "d20a624740ce1b7e2c74659bb291f665c021d202be02d13ce27feb067eeec837",
            "281b9dba10658c86d0c3c267b82b8972b6c7b41285f60ce2054211e69dd89e15",
            "df743dd1973e1c7d46968720b931af0afa8ec5e8412f9420006b7b4fa660ba8d",
            "3e812f40cd8e4ca3a92972610409922dedf1c0dbc68394fcb1c8f188a42655e2",
            "3ebc2bd1d73e4f2f1f2af086ad724c98c8030f74c0c2be6c2d6fd538c711f35c",
            "9789f4e2339193149452c1a42cded34f7a301a13196cd8200246af7cc1e33c3b",
            "aefe99f12345aabc4aa2f000181008843c8abf57ccf394710b2c48ed38e1a66a",
            "64f662d104723a4326096ffd92954e24f2bf5c3ad374f04b10fcc735bc901a4d",
            "95a73895c9c6ee0fadb8d7da2fac25eb523fc582dc12c40ec793f0c1a70893b4",
            "315987563da5a1f3967053d445f73107ed6388270b00fb99a9aaa26c56ecba2b",
            "09caa1de14f86c5c19bf53cadc4206fd872a7bf71cda9814b590eb8c6e706fbb",
            "9d04d59d713b607c81811230645ce40afae2297f1cdc1216c45080a5c2e86a5a",
            "ab8a58ff2cf9131f9730d94b9d67f087f5d91aebc3c032b6c5b7b810c47e0132",
            "c7c3f15b67d59190a6bbe5d98d058270aee86fe1468c73e00a4e7dcc7efcd3a0",
            "27ef2eaa77544d2dd325ce93299fcddef0fae77ae72f510361fa6e5d831610b2"
        );
        
        String merkleRoot = generateMerkleRoot(hashes);
        String hashToProve = hashes.get(4); 
        List<Node> generatedMerkleProof = generateMerkleProof(hashToProve, hashes);
        String merkleRootFromMerkleProof = getMerkleRootFromMerkleProof(generatedMerkleProof);
        System.out.println("--- Merkle Tree Statistics ---");
        System.out.println("merkleRoot: " + merkleRoot);
        System.out.println("hashToProve: " + hashToProve);
        System.out.println("generatedMerkleProof (nodes: " + generatedMerkleProof.size() + "):");
        for (int i = 0; i < generatedMerkleProof.size(); ++i) {
            String role = i == 0 ? " (Leaf)" : " (Sibling)";
            System.out.println("  - " + generatedMerkleProof.get(i) + role);
        }
        System.out.println("--- Verification ---");
        System.out.println("merkleRootFromMerkleProof: " + merkleRootFromMerkleProof);
        System.out.println("merkleRootFromMerkleProof === merkleRoot: " 
             + (merkleRootFromMerkleProof.equals(merkleRoot) ? "true" : "false"));
    }
}