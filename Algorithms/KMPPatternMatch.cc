// Time: O(n + m) where n = string length, m = pattern length
// Space: O(m) for table needed for pattern length

#include <string>
#include <iostream>
#include <vector>
using namespace std;

class KMPPatternMatch {
public:
	vector<int> getTable(string Y) {
		int j = 0;
		int i = 1;
		int n = s.length();
		vector<int> table(n, 0);
		while(i < n) {
			if(Y.at(j) == Y.at(i)) {
				table[i] = ++j;
				++i;
			} else {
				if(j > 0) {
					j = table[j - 1];
				} else {
					j = 0;
					++i;
				}
			}
		}
		return table;
	}

	// X is the string, Y is the pattern
	void match(string X, string Y) {
		int n = X.length();
		int m = Y.length();

		vector<int> table = getTable(Y);

		int i = 0; // index for X
		int j = 0; // index for Y

		while(i < n) {
			if(X.at(i) == Y.at(j)) {
				++i;
				++j;
			}

			if(j == m) {
				cout << "Found pattern at index " << (i - j) << endl;
			} else if(i < n && X.at(i) != Y.at(j)) {
				if(j > 0) {
					j = table[j - 1];
				} else {
					++i;
				}
			}
	    	}
	}
};
