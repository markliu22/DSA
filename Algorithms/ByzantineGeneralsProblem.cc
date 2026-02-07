// ref: https://www.rodrigoaraujo.me/posts/coding-the-byzantine-generals-problem/
// ref: https://github.com/AnantJoshiCZ/Byzantine

#include <iostream>
#include <vector>
#include <memory>
#include <random>
#include <algorithm>

struct Message {
    bool attack;
};

struct General {
    int id;
    bool is_traitor;
    bool decision; 

    General(int id, bool is_traitor) 
        : id(id), is_traitor(is_traitor), decision(false) {}
};

class OMAlgorithm {
public:
    bool is_first_commander_loyal;
    Message original_order;

    void om_algorithm(const std::vector<std::shared_ptr<General>>& lieutenants, 
                      std::shared_ptr<General> commander, 
                      int m) {
    }

    void get_total_messages(const std::vector<std::shared_ptr<General>>& generals) {
    }

    bool was_successful(const std::vector<std::shared_ptr<General>>& lieutenants) {
        return true; 
    }
};

int main() {
    int num_of_generals = 4;
    int num_of_traitors = 1;
    int m = num_of_traitors;
    int num_of_experiments = 10;

    std::random_device rd;
    std::mt19937 rng(rd());

    for (int i = 0; i < num_of_experiments; ++i) {
        std::vector<std::shared_ptr<General>> generals;

        for (int j = 0; j < num_of_generals; ++j) {
            generals.push_back(std::make_shared<General>(j + 1, false));
        }

        std::vector<std::shared_ptr<General>> potential_traitors = generals;
        std::shuffle(potential_traitors.begin(), potential_traitors.end(), rng);

        for (int k = 0; k < num_of_traitors; ++k) {
            auto traitor = potential_traitors[k];
            std::cout << "Traitor: General #" << traitor->id << "\n";
            traitor->is_traitor = true;
        }

        Message order = { true };

        OMAlgorithm om_algorithm;
        om_algorithm.is_first_commander_loyal = true;
        om_algorithm.original_order = order;

        std::uniform_int_distribution<int> uni(0, generals.size() - 1);
        int first_commander_index = uni(rng);
        std::shared_ptr<General> first_commander_rc = generals[first_commander_index];

        first_commander_rc->decision = order.attack;

        generals.erase(generals.begin() + first_commander_index);

        if (first_commander_rc->is_traitor) {
            om_algorithm.is_first_commander_loyal = false;
        }

        om_algorithm.om_algorithm(generals, first_commander_rc, m);

        om_algorithm.get_total_messages(generals);

        bool successful = om_algorithm.was_successful(generals);

        std::cout << "successful: " << (successful ? "true" : "false") << "\n\n";

        if (!successful) {
            std::cout << "Found a case where consensus isn't achieved\n\n";
            break;
        }
    }

    return 0;
}