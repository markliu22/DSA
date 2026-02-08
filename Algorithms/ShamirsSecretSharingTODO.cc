#include <iostream>
#include <vector>
#include <cmath>
#include <random>

struct Point {
    double x;
    double y;
};

// Evaluate the polynomial at x
double evaluatePolynomial(const std::vector<int>& coefficients, double x) {
    double result = 0;
    for (int i = 0; i < coefficients.size(); ++i) {
        result += coefficients[i] * std::pow(x, i);
    }
    return result;
}

// Lagrange Interpolation to find f(0)
double reconstructSecret(const std::vector<Point>& shares) {
    double secret = 0;
    int k = shares.size();

    for (int i = 0; i < k; ++i) {
        double li = 1.0;
        for (int j = 0; j < k; ++j) {
            if (i != j) {
                li *= (0 - shares[j].x) / (shares[i].x - shares[j].x);
            }
        }
        secret += shares[i].y * li;
    }
    return secret;
}

int main() {
    int secret = 1234; // The secret we want to hide
    int n = 5;         // Total shares
    int k = 3;         // Threshold to reconstruct

    // 1. Generate random coefficients for a polynomial of degree k-1
    std::vector<int> coefficients = {secret};
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dis(1, 100);

    for (int i = 1; i < k; ++i) {
        coefficients.push_back(dis(gen));
    }

    // 2. Generate n shares
    std::vector<Point> shares;
    for (int i = 1; i <= n; ++i) {
        shares.push_back({(double)i, evaluatePolynomial(coefficients, i)});
    }

    std::cout << "Generated " << n << " shares. Threshold is " << k << ".\n";

    // 3. Reconstruct using a subset of shares (e.g., first 3)
    std::vector<Point> subset = {shares[0], shares[2], shares[4]};
    double recovered = reconstructSecret(subset);

    std::cout << "Reconstructed Secret: " << std::round(recovered) << std::endl;

    return 0;
}