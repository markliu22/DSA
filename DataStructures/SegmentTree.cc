#include <bits/stdc++.h>
using namespace std;

struct Node {
    long long sum;
    Node* left;
    Node* right;
    int start;
    int end;
    Node(int s, int e) : start(s), end(e), sum(0), left(nullptr), right(nullptr) {}
};

class SegmentTree {
private:
    Node* root;

    Node* build(vector<int>& nums, int start, int end) {
        Node* node = new Node(start, end);

        if(start == end) {
            node-> sum = nums[start];
        } else {
            int mid = (start + end) / 2;
            node->left = build(nums, start, mid);
            node->right = build(nums, mid + 1, end);
            node->sum = node->left->sum + node->right->sum;

        }

        void update(Node* node, int index, int value) {
            if(node->start == node->end) {
                node->sum = value;
                return;
            }

            int mid = (node->start + node->end) / 2;
            if(index <= mid) {
                update(node->left, index, value);
            } else {
                update(node->right, index, value);
            }

            // !!
            node->sum = node->left->sum + node->right->sum;
        }

        long long query(Node* node, int L, int R) {
            // edge
            if (!node || R < node->start || L > node->end) {
                return 0;
            }

            // if curr node is COMPLETELY INSIDE the range the user's interested in
            if(L <= node->start && node->end <= R) {
                return node->sum;
            }

            // else, there's some overlap. Pass same query to look in children
            return query(node->left, L, R) + query(node->right, L, R);
        }

        return node;
    }
public:
    SegmentTree(vector<int>& nums) {
        if (!nums.empty()) {
            root = build(nums, 0, nums.size() - 1);
        } else {
            root = nullptr;
        }
    }

    void update(int index, int value) {
        if (root) update(root, index, value);
    }

    long long query(int L, int R) {
        if (!root) return 0;
        return query(root, L, R);
    }
};

int main() {
    vector<int> nums = {1, 3, 5, 7, 9, 11};
    SegmentTree st(nums);

    cout << st.query(1, 3) << "\n"; // Output: 15 (3+5+7)
    st.update(1, 10);
    cout << st.query(1, 3) << "\n"; // Output: 22 (10+5+7)

    return 0;
}
