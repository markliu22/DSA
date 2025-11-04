// Probabilistic data structure
// Used to test whether an element is in a set
    // If output 'no': element definitely not in set
    // If output 'yes': element is probably in the set

// Bloom filter is a implemented as a bit array of length m, all 
// initialized to 0, and k independent hash functions, each mapping an element to one of the m positions

// EX:
// Insert "apple"
// Compute its k hash values hash1("apple"), hash2("apple"), ... hashk("apple")
// Set those bit positions in array to 1
// Query "banana"
// Compute its k hash values
// If any bit is 0, "banana" definitely not in set
// If all bits are 1, "banana" might be in set

// False Positives:
// Diff elements can set overlappings bits

// Given desired false positive rate p and number of elements n, pick:
// m = -n*ln(p) / (ln(2))^2
// k = (m/n)*ln(2)

// Example Use Cases:
// Google Chrome used Bloom filters to identify malicious URLs. Any URL was first checked against a local Bloom filter
    // Only if the Bloom filter returned a positive result was a full check of the URL performed (+ warning sent to user)
// Medium uses Bloom filters to avoid recommending articles a user has previously read
// Ethereum uses Bloom filters to quickly find logs on Eth blockchain

// NOTE: you can't remove elements

// TODO: see Cuckoo filter, alternative to Bloom filter, uses space-efficient cuckoo hashing

import java.util.BitSet;
import java.util.List;
import java.util.ArrayList;

public class BloomFilter {
    private final int bitArraySize;
    private final int numHashFunctions;
    private final BitSet bitset;

    public BloomFilter(int bitArraySize, int numHashFunctions) {
        this.bitArraySize = bitArraySize;
        this.numHashFunctions = numHashFunctions;
        this.bitset = new BitSet(bitArraySize);
    }

    // Simple hash function family using String.hashCode() variations
    private int hash(String item, int seed) {
        int h = item.hashCode() ^ (seed * 0x5bd1e995);
        h ^= (h >>> 15);
        return Math.abs(h % bitArraySize);
    }

    // Add an item to the Bloom filter
    public void add(String item) {
        for (int i = 0; i < numHashFunctions; i++) {
            int index = hash(item, i);
            bitset.set(index);
        }
    }

    // Check if an item might be in the Bloom filter
    public boolean mightContain(String item) {
        for (int i = 0; i < numHashFunctions; i++) {
            int index = hash(item, i);
            if (!bitset.get(index)) {
                return false; // definitely not in set
            }
        }
        return true; // possibly in set
    }

    // For debugging — how many bits are set
    public int getBitCount() {
        return bitset.cardinality();
    }

    // Example usage
    public static void main(String[] args) {
        BloomFilter bf = new BloomFilter(1000, 4);

        bf.add("apple");
        bf.add("orange");
        bf.add("grape");

        System.out.println("apple?  " + bf.mightContain("apple"));   // true
        System.out.println("banana? " + bf.mightContain("banana"));  // probably false
        System.out.println("orange? " + bf.mightContain("orange"));  // true
        System.out.println("grape?  " + bf.mightContain("grape"));   // true
        System.out.println("Bit count: " + bf.getBitCount());
    }
}
