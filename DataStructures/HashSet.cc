import java.util.LinkedList;

class MyHashSet {
    private static final int SIZE = 1000; // Number of buckets
    private LinkedList<Integer>[] buckets;

    public MyHashSet() {
        buckets = new LinkedList[SIZE];
        for (int i = 0; i < SIZE; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int getHash(int key) {
        return key % SIZE;
    }

    public void add(int key) {
        int hash = getHash(key);
        if (!buckets[hash].contains(key)) {
            buckets[hash].add(key);
        }
    }

    public void remove(int key) {
        int hash = getHash(key);
        buckets[hash].remove((Integer) key);
    }

    public boolean contains(int key) {
        int hash = getHash(key);
        return buckets[hash].contains(key);
    }

    public static void main(String[] args) {
        MyHashSet hashSet = new MyHashSet();
        hashSet.add(1);
        hashSet.add(2);
        System.out.println(hashSet.contains(1)); // true
        System.out.println(hashSet.contains(3)); // false
        hashSet.add(2);
        System.out.println(hashSet.contains(2)); // true
        hashSet.remove(2);
        System.out.println(hashSet.contains(2)); // false
    }
}
