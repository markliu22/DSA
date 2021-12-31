#include <iostream>
using namespace std;

struct Node {
	int data;
  	Node *next = NULL;
  	explicit Node(int data): data{data} {}
  	Node(int data, Node *next): data{data}, next{next} {}
  	Node(const Node &n): data{n.data}, next{n.next ? new Node{*n.next} : NULL} {}
	~Node() {
		delete next;
	}
};

void pretty_print(Node *n) {
	while(n != NULL) {
		cout << n->data << " ";
		n = n->next;
	}
}

/*
int main() {
	Node *n = new Node{1, new Node{2, new Node{3, NULL}}};
	pretty_print(n);
}
*/


