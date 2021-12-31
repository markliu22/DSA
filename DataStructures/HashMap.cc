// ref: https://leetcode.com/problems/design-hashmap/discuss/1097755/JS-Python-Java-C%2B%2B-or-(Updated)-Hash-and-Array-Solutions-w-Explanation
// ref: https://stackoverflow.com/questions/31112852/how-stdunordered-map-is-implemented

#include <iostream>
using namespace std;

struct Node {
public:
	int key;
	int val;
	Node *next;
	Node(int k, int v, Node *n): key{k}, val{v}, next{n} {}
};

// TODO: use template
class HashMap {
public:
	const static int size = 19997;
	const static int mult = 12582917;
	// array of linked lists (Node pointers)
	Node *data[size];

	int hash(int key) {
		return (int)((long)key * mult % size);
	}

	void put(int key, int val) {
		// remove prev val at this key
		remove(key);
		// get hash/index from this key
		int h = hash(key);
		// create new node, set next node as the linked list at this hash/index
		Node *node = new Node(key, val, data[h]);
		data[h] = node;
	}

	int get(int key) {
		int h = hash(key);
		// get linked list at this hash/index
		Node *node = data[h];
		// keep traversing until the keys match
		while(node != NULL) {
			if(node->key == key) return node->val;
			node = node->next;
		}
		return -1;
	}

	void remove(int key) {
		int h = hash(key);
		Node *node = data[h];
		if(node == NULL) return;
		if(node->key == key) data[h] = node->next;
		else {
			while(node->next != NULL) {
				if(node->next->key == key) {
					node->next = node->next->next;
					return;
				}
				node = node->next;
			}
		}
	}
};

/*
int main() {
	HashMap map;
	map.put(1, 1);
	cout << map.get(1) << endl;
}
*/


