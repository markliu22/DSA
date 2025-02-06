// ref: https://algorithmtutor.com/Data-Structures/Tree/Red-Black-Trees/
// ref: https://www.youtube.com/watch?v=qvZGUFHWChY&ab_channel=MichaelSambol

// Red black tree is a self balancing Binary Searce Tree. 
// It has a guaranteed height of logn for n nodes 

#include <iostream>
using namespace std;

struct Node {
	int data;
	Node *parent;
	Node *left;
	Node *right;
	int color; // 1 = Red, 0 = Black
};

typedef Node *NodePtr;
// typedef is used to create a alias(additional name) for some data type
// here, we create the "NodePtr" alias for Node *

class RBTree {
private:
	NodePtr root;
	NodePtr NULL;

	// initializes the nodes with appropriate values
	// all the poointers are set to point to the null pointer
	void initializeNULLNode(NodePtr node, NodePtr parent) {
		node->data = 0;
		node->parent = parent;
		node->left = NULL;
		node->right = NULL;
		node->color = 0; // Black by default
	}

	void preOrderHelper(NodePtr node) {
		if(node == NULL) return;
		cout << node->data << " ";
		preOrderHelper(node->left);
		preOrderHelper(node->right);
	}

	void inOrderHelper(NodePtr node) {
		if(node == NULL) return;
		inOrderHelper(node->left);
		cout << node->data << " ";
		inOrderHelper(node->right);
	}

	void postOrderHelper(NodePtr node) {
		if(node == NULL) return;
		postOrderHelper(node->left);
		postOrderHelper(node->right);
		cout << node->data << " ";
	}

	NodePtr searchTreeHelper(NodePtr node, int key) {
		if(node == NULL || key == node->data) {
			return node;
		}

		if(key < node->data) {
			return searchTreeHelper(node->left, key);
		}
		return searchTreeHelper(node->right, key);
	}

	// TODO: https://www.youtube.com/watch?v=eO3GzpCCUSg&ab_channel=AlenaChang
	// fix the RB tree modified by the delete operation
	void fixDelete(NodePtr x) {
		NodePtr s;
		while(x != root && x->color == 0) {
			if(x == x->parent->left) {
				s = x->parent->right;
				if (s->color == 1) {
					// case 3.1
					s->color = 0;
					x->parent->color = 1;
					leftRotate(x->parent);
					s = x->parent->right;
				}

				if (s->left->color == 0 && s->right->color == 0) {
					// case 3.2
					s->color = 1;
					x = x->parent;
				} else {
					if (s->right->color == 0) {
						// case 3.3
						s->left->color = 0;
						s->color = 1;
						rightRotate(s);
						s = x->parent->right;
					} 

					// case 3.4
					s->color = x->parent->color;
					x->parent->color = 0;
					s->right->color = 0;
					leftRotate(x->parent);
					x = root;
				}
			} else {
				s = x->parent->left;
				if (s->color == 1) {
					// case 3.1
					s->color = 0;
					x->parent->color = 1;
					rightRotate(x->parent);
					s = x->parent->left;
				}

				if (s->right->color == 0 && s->right->color == 0) {
					// case 3.2
					s->color = 1;
					x = x->parent;
				} else {
					if (s->left->color == 0) {
						// case 3.3
						s->right->color = 0;
						s->color = 1;
						leftRotate(s);
						s = x->parent->left;
					} 

					// case 3.4
					s->color = x->parent->color;
					x->parent->color = 0;
					s->left->color = 0;
					rightRotate(x->parent);
					x = root;
				}
			} 
		}
		x->color = 0;
	}

	void rbTransplant(NodePtr u, NodePtr v){
		if (u->parent == nullptr) {
			root = v;
		} else if (u == u->parent->left){
			u->parent->left = v;
		} else {
			u->parent->right = v;
		}
		v->parent = u->parent;
	}

	void deleteNodeHelper(NodePtr node, int key) {
		// find the node containing key
		NodePtr z = TNULL;
		NodePtr x, y;
		while (node != TNULL){
			if (node->data == key) {
				z = node;
			}

			if (node->data <= key) {
				node = node->right;
			} else {
				node = node->left;
			}
		}

		if (z == TNULL) {
			cout<<"Couldn't find key in the tree"<<endl;
			return;
		}

		y = z;
		int y_original_color = y->color;
		if (z->left == TNULL) {
			x = z->right;
			rbTransplant(z, z->right);
		} else if (z->right == TNULL) {
			x = z->left;
			rbTransplant(z, z->left);
		} else {
			y = minimum(z->right);
			y_original_color = y->color;
			x = y->right;
			if (y->parent == z) {
				x->parent = y;
			} else {
				rbTransplant(y, y->right);
				y->right = z->right;
				y->right->parent = y;
			}

			rbTransplant(z, y);
			y->left = z->left;
			y->left->parent = y;
			y->color = z->color;
		}
		delete z;
		if (y_original_color == 0){
			fixDelete(x);
		}
	}

	// fix the red-black tree after insertion
	void fixInsert(NodePtr k) {
		NodePtr u;
		while(k->parent->color == 1) {
			// k's parent is a right child
			if(k->parent == k->parent->parent->right) {
				u = k->parent->parent->left; // uncle
				// if uncle is red
				if(u->color == 1) {
					// CASE 2) recolor k's parent, grandparent, and uncle
					u->color = 0;
					k->parent->color = 0;
					k->parent->parent->color = 1;
					// move k up to the new (possibly offending) node
					k = k->parent->parent;
				} else {
				// uncle is black
					// CASE 3 V1) (triangle case 1) 
					// if k is a left child 
					if(k == k->parent->left) {
						k = k->parent;
						// rotate k's parent opposite direction (right)
						rightRotate(k);
					}
					// CASE 4 V1) (line case 1)
					// k is a right child
					// parent turn black
					k->parent->color = 0;
					// grandparent turn red
					k->parent->parent->color = 1;
					// rotate k's grandparent opposite direction (left)
					leftRotate(k->parent->parent);
				}
			} else {
			// k's parent is a left child
				u = k->parent->parent->right; // uncle
				// if uncle is red
				if (u->color == 1) {
					// CASE 2)
					u->color = 0;
					k->parent->color = 0;
					k->parent->parent->color = 1;
					k = k->parent->parent;	
				} else {
				// uncle is red
					// CASE 3 V2) (triangle case 2)
					if (k == k->parent->right) {
						k = k->parent;
						leftRotate(k);
					}
					// CASE 4 V2) (line case 2)
					k->parent->color = 0;
					k->parent->parent->color = 1;
					rightRotate(k->parent->parent);
				}
			}
			if (k == root) {
				break;
			}
		}
		// CASE 1)
		root->color = 0;
	}
}


