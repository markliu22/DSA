// A monotonic Queue is a data structure the elements from the front to the end is strictly either increasing or decreasing. 
// For example, there is an line at the hair salon, and you would naturally start from the end of the line. 
// However, if you are allowed to kick out any person that you can win at a fight, if every one follows the rule, then the line would start with the most powerful man and end up with the weakest one. This is an example of monotonic decreasing queue. We have either increasing or decreasing queue.

// - Monotonic increasing queue: to push an element `e`, starts from the rear element, we pop out element `s≥e`(violation);
// - Monotonic decreasing queue: we pop out element s<=e (violation).
// - Sometimes, we can relax the strict monotonic condition, and can allow the stack or queue have repeat value.

class MonotonicQueue {
private:
    deque<int> data;
public:
    void push(int n) {
        while (!data.empty() && data.back() < n) 
            data.pop_back();
        data.push_back(n);
    }
};
