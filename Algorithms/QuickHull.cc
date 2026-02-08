// Convex Hull Problem:
    // Formal definition: smallest convex polygon (2D) or polyhedron (3D) that contains a specific set of points
    // EX: board with nails, you stretch a rubber band around all the nails. The shape the rubber band forms is the Convex Hull
    // Aside: 'convex' means ifyou draw a line between any 2 points inside the shape, that line stays entirely inside the shape, no dents or indentations

// QuickHull:
// time: O(nlogn) average case (worst case O(n^2) when points are arranged in a circle)
// Is a popular algorithm to calculate this shape. Is a Divide and Conquer algorithm
// Steps:
    // 1. Find the point with the minimum x-coordinate (A) and maximum x-coordinate (B). These 2 points are guarnteed to be on the hull
    // 2. Draw a line connecting A and B. This splits the remaining points into 2 subsets Left and Right
    // 3. Look at one side (EX: Right), find the point C that is furthest away from line AB. This forms triangle ABC
    // 4. Eliminate. Any points inside ABC cannot be part of the hull; they're already inside the rubber band. Ignore them
    // 5. Recurse. Repeat this process for the 2 new lines formed by the triangle (line AC and line CB)

// These used in Counter Strike, such as the Player Model
// Instead of checking if a 3D with thousands of polygons hits a wall is incredibly slow
// developers use Hitboxes and Collision Hulls instead
// Player: the player isn't checked against the world as a complex human shape. In CS, the player is esssentially a tall cyclinder with rounded ends
// World: static objects (cars, walls, etc) are often wrapped in a Convex Collision Hull
// To determine if two Convex objects are touching, a fast algorithm called Gilbert-Johnson-Keerthi is used

#include <iostream>
#include <vector>
#include <set>
#include <algorithm>

struct Point {
    int x, y;
    // Need this to store Points in a set
    bool operator<(const Point& other) const {
        if(x != other.x) return x < other.x;
        return y < other.y;
    }
};

// Returns the side of the line p1p2 that p3 is on
// > 0 : left side
// < 0 : right side
// = 0 : on the line
int getSide(Point p1, Point p2, Point p3) {
    return (p3.y - p1.y) * (p2.x - p1.x) - (p2.y - p1.y) * (p3.x - p1.x);
}

// Returns abs distance from point p to line p1p2
int getDistance(Point p1, Point p2, Point p) {
    return abs((p.y - p1.y) * (p2.x - p1.x) - (p2.y - p1.y) * (p.x - p1.x));
}

void findHull(const std::vector<Point>& pts, Point p1, Point p2, std::set<Point>& hull) {
    if (pts.empty()) return;

    // 1. Find point furthest from line p1p2
    int maxDist = -1;
    int furthestIdx = -1;

    for (int i = 0; i < pts.size(); i++) {
        int dist = abs(getSide(p1, p2, pts[i])); 
        if (dist > maxDist) {
            maxDist = dist;
            furthestIdx = i;
        }
    }

    Point A = pts[furthestIdx];
    // this point definitely on hull
    hull.insert(A);

    // 2. Split remaining points into 2 new set
        // points to the left of p1-A and points to the right of A-p2
    std::vector<Point> leftSet1;
    std::vector<Point> leftSet2;

    for (const auto& p : pts) {
        if (getSide(p1, A, p) > 0) {
            leftSet1.push_back(p);
        } else if (getSide(A, p2, p) > 0) {
            leftSet2.push_back(p);
        }
    }

    // 3. Recurse for the 2 new regions
    findHull(leftSet1, p1, A, hull);
    findHull(leftSet2, A, p2, hull);
}

std::vector<Point> quickHull(std::vector<Point>& points) {
    if (points.size() < 3) return points;

    std::set<Point> hull;

    // 1. Find points with min and max X coordinates
    int minX_idx = 0, maxX_idx = 0;
    for (int i = 1; i < points.size(); i++) {
        if (points[i].x < points[minX_idx].x) minX_idx = i;
        if (points[i].x > points[maxX_idx].x) maxX_idx = i;
    }

    Point pMin = points[minX_idx];
    Point pMax = points[maxX_idx];

    hull.insert(pMin);
    hull.insert(pMax);

    // 2. Separate points into two halves (Above and Below the line pMin-pMax)
    std::vector<Point> leftPoints, rightPoints;
    for (const auto& p : points) {
        int side = getSide(pMin, pMax, p);
        if (side > 0) leftPoints.push_back(p);
        else if (side < 0) rightPoints.push_back(p);
    }

    // 3. Recursively find the hull points for both sides
    findHull(leftPoints, pMin, pMax, hull);
    findHull(rightPoints, pMax, pMin, hull);

    // Convert set back to vector for the caller
    return std::vector<Point>(hull.begin(), hull.end());
}

int main() {
    std::vector<Point> points = {{0, 3}, {2, 2}, {1, 1}, {2, 1}, {3, 0}, {0, 0}, {3, 3}, {1, 2}};
    
    std::vector<Point> result = quickHull(points);

    std::cout << "Points on the Convex Hull:\n";
    for (auto p : result) {
        std::cout << "(" << p.x << ", " << p.y << ") ";
    }
    return 0;
}