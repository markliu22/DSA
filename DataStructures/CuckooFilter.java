// https://bdupras.github.io/filter-tutorial/
// https://www.cs.cmu.edu/~dga/papers/cuckoo-conext2014.pdf
// time: O(1) amortized insertion, O(1) query

// Don't store entire item, just store a short fingerprint. Save space. Fingerprints may collide, causing false positives.

// Proposed as a better alternative to Bloom Filters
// No false negatives, low false positives, ability to delete items

// Uses Cuckoo Hashing, which places an item in one of 2 possible 'buckets' using hash functions
// 1. Compute fingerprint of the item. EX: "apple" -> 0b10101100.
// 2. Compute 2 candidate bucket indices:
    // index1 = hash("apple")
    // index2 = index1 XOR hash(fingerprint)
// 3. Store fingerprint into one of these 2 buckets if there's space
// 4. If both are full, evict and existing fingerprint (like in cuckoo hashing), move it to its alternative bucket, and repeat
// 5. If no slot can be found after several evictions, resize or rebuild the filter
// *each bucket holds a new fingerprints*

// Query for "apple":
// Compute fingerprint, index1, index2
// If fingerprint is NOT in either bucket index1 or index2, not present
// Otherwise, probably present

// Deletion:
// Just remove its fingerprint from the appropriate bucket

import java.util.Arrays;
import java.util.Random;

public class CuckooFilter {
    private final int bucketSize; // slots per bucket
    private final int numBuckets;
    private final int fingerprintSize; // bits per fingerprint
    private final byte[][] table;
    private final Random rand = new Random();

    public CuckooFilter(int capacity, int bucketSize, int fingerprintSize) {
        this.bucketSize = bucketSize;
        this.fingerprintSize = fingerprintSize;
        this.numBuckets = capacity / bucketSize;
        this.table = new byte[numBuckets][bucketSize];
    }

    private int hash(byte[] data) {
        return Arrays.hashCode(data) & 0x7fffffff;
    }

    private byte fingerprint(String item) {
        return (byte) (item.hashCode() & ((1 << fingerprintSize) - 1));
    }

    // Primary bucket idx
    private int index1(String item) {
        return (hash(item.getBytes()) % numBuckets);
    }

    // Secondary bucket idx
    private int index2(int i1, byte fp) {
        return (i1 ^ (hash(new byte[]{fp}) % numBuckets)) % numBuckets;
    }

    public boolean insert(String item) {
        byte fp = fingerprint(item);
        int i1 = index1(item);
        int i2 = index2(i1, fp);

        // Try placing fingerprint in either bucket
        if (insertIntoBucket(i1, fp) || insertIntoBucket(i2, fp)) return true;

        // If full, start eviction loop
        int i = (rand.nextBoolean()) ? i1 : i2;
        for (int n = 0; n < 500; n++) {
            int pos = rand.nextInt(bucketSize);
            byte oldFp = table[i][pos];
            table[i][pos] = fp;
            fp = oldFp;
            i = index2(i, fp);
            if (insertIntoBucket(i, fp)) return true;
        }
        return false; // Rehash needed
    }

    private boolean insertIntoBucket(int index, byte fp) {
        for (int j = 0; j < bucketSize; j++) {
            if (table[index][j] == 0) {
                table[index][j] = fp;
                return true;
            }
        }
        return false;
    }

    public boolean contains(String item) {
        byte fp = fingerprint(item);
        int i1 = index1(item);
        int i2 = index2(i1, fp);
        return bucketContains(i1, fp) || bucketContains(i2, fp);
    }

    private boolean bucketContains(int index, byte fp) {
        for (byte b : table[index]) {
            if (b == fp) return true;
        }
        return false;
    }

    public boolean delete(String item) {
        byte fp = fingerprint(item);
        int i1 = index1(item);
        int i2 = index2(i1, fp);
        return deleteFromBucket(i1, fp) || deleteFromBucket(i2, fp);
    }

    private boolean deleteFromBucket(int index, byte fp) {
        for (int j = 0; j < bucketSize; j++) {
            if (table[index][j] == fp) {
                table[index][j] = 0;
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        CuckooFilter cf = new CuckooFilter(1000, 4, 8);
        cf.insert("apple");
        cf.insert("banana");
        System.out.println(cf.contains("apple"));  // true
        System.out.println(cf.contains("orange")); // false
        cf.delete("apple");
        System.out.println(cf.contains("apple"));  // false
        cf.printStats();
    }
}
