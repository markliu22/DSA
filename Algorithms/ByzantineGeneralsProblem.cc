// A group of generals surround a city. They must decide to Attack or Retreat
// A coordinate attack leads to victor; a coordinated retreat is safe
// A haphazard attack would be worse that either of these^
// Problem is that they can only communicate via messengers. SOme generals might be traitors who
// prevent a consensus by sending conflicting messages (tell one general to attack, another to retreat)

// Solution:
// To tolerate m traitors, you need at least 3m + 1

// Recursive Algo:
    // 1. Commander sends thir order (Attack/Retreat) to all lietenants
    // 2. Lietenants cross-check:
        // If m > 0 (we suspect traitors exist), every lieutenant acts as a 'new commander'
        // They relay the message they just received to all other lietenants
        // This creates a recursive tree of message validation
    // 3. Majority Vote; each lieutenant collects all the messages they received (direct from command
    // and relayed by peers) and takes a majority vote to decide their final decision

// time: O(n^m) where n = total number of generals, m = number of traitors
    // Each recursive call reaches out to n different lieutenants, recursion goes m deep
// space: O(nm)

#include <iostream>
#include <vector>
#include <memory>
#include <random>
#include <algorithm>
#include <map>

// attack = true, retreat = false
struct Message {
    bool attack;
};

struct General {
    int id;
    bool is_traitor;
    bool decision;

    General(int id, bool is_traitor): id(id), is_traitor(is_traitor), decision(false) {}
};

class OMAlgorithm {
public:
    bool is_first_commander_loyal;
    Message original_order;

    // helper to calc majority vote
    bool get_majority(const std::vector<bool>& votes) {
        int count_true = 0;
        for(bool v : votes) {
            if(v) count_true++;
        }
        // if tie, default to true
        return count_true > (votes.size() / 2);
    }

    // core algorithm: OM(m)
    // returns decision
    bool om_algorithm_recursive(const std::vector<std::shared_ptr<General>>& lieutenants, std::shared_ptr<General> commander, int m, bool incoming_order) {
        // 1. The Commander sends an order to the lieutenants.
        // If the commander is a TRAITOR, they send conflicting orders.
        // (In this simulation, we simulate a traitor sending the OPPOSITE of what they received/intended half the time).

        bool order_to_send = incoming_order;
        if(commander->is_traitor) {
            order_to_send = !incoming_order;
        }

        // base case: m = 0
        // no more traitors suspected
        if(m == 0) {
            return order_to_send;
        }

        // recursive step:
        // every lieutenant acts as a commander, cross-checks with peers
        std::vector<bool> votes;

        // vote 1: the direct message from the current commander
        votes.push_back(order_to_send);

        // vote 2...N: messages relayed by other lieutenants
        for(size_t i = 0; i < lieutenants.size(); ++i) {
            std::shared_ptr<General> new_commander = lieutenants[i];
            std::vector<std::shared_ptr<General>> next_lieuttenants;
            for(size_t j = 0; j < lieutenants.size(); ++j) {
                if(i != j) next_lieuttenants.push_back(lieutenants[j]);
            }
            // recurse:
            bool result = om_algorithm_recursive(next_lieuttenants, new_commander, m - 1, order_to_send);
            votes.push_back(result);
        }

        return get_majority(votes);
    }

    // entry point for the simulation
    void om_algorithm(const std::vector<std::shared_ptr<General>>& lieutenants, std::shared_ptr<General> commander, int m) {
        // check all top-level lieutenants to determine their individual decisions
        for (size_t i = 0; i < lieutenants.size(); ++i) {
            auto current_lieutenant = lieutenants[i];
            
            // Create a sub-group of peers (everyone except the current lieutenant)
            // The current lieutenant will ask these peers what THEY heard.
            std::vector<std::shared_ptr<General>> peers;
            for(size_t j = 0; j < lieutenants.size(); ++j) {
                if(i != j) peers.push_back(lieutenants[j]);
            }

            // Start the recursion for this lieutenant
            // Note: We pass original_order.attack. 
            // If the MAIN commander is a traitor, the recursive function handles the flip.
            bool final_decision = om_algorithm_recursive(peers, commander, m, original_order.attack);
            
            current_lieutenant->decision = final_decision;
        }
    }

    void get_total_messages(const std::vector<std::shared_ptr<General>>& generals) {
        // Just for display in this simulation
        std::cout << "--- Final Decisions ---" << "\n";
        for(auto& g : generals) {
            std::cout << "General " << g->id << (g->is_traitor ? " (Traitor)" : " (Loyal)") 
                      << " decided: " << (g->decision ? "ATTACK" : "RETREAT") << "\n";
        }
    }

    // Success Check: 
    // 1. All LOYAL lieutenants must have the same decision.
    // 2. If the Commander was LOYAL, the lieutenants' decision must match the Commander's order.
    bool was_successful(const std::vector<std::shared_ptr<General>>& lieutenants) {
        if (lieutenants.empty()) return true;

        // Filter for loyal lieutenants only
        std::vector<bool> loyal_decisions;
        for (auto& g : lieutenants) {
            if (!g->is_traitor) {
                loyal_decisions.push_back(g->decision);
            }
        }

        if (loyal_decisions.empty()) return true;

        // Condition 1: Agreement (All loyal lieutenants have same value)
        bool first_decision = loyal_decisions[0];
        for (bool d : loyal_decisions) {
            if (d != first_decision) return false; // Failed consensus
        }

        // Condition 2: Validity (If commander loyal, decision must match order)
        if (is_first_commander_loyal) {
            if (first_decision != original_order.attack) return false;
        }

        return true; 
    }
};

int main() {
    int num_of_generals = 4; // Minimal N for 1 traitor (3m + 1)
    int num_of_traitors = 1;
    int m = num_of_traitors;
    int num_of_experiments = 5;

    std::random_device rd;
    std::mt19937 rng(rd());

    for (int i = 0; i < num_of_experiments; ++i) {
        std::cout << "Experiment #" << i + 1 << "\n";
        std::vector<std::shared_ptr<General>> generals;

        for (int j = 0; j < num_of_generals; ++j) {
            generals.push_back(std::make_shared<General>(j + 1, false));
        }

        // Shuffle to pick random traitors
        std::vector<std::shared_ptr<General>> potential_traitors = generals;
        std::shuffle(potential_traitors.begin(), potential_traitors.end(), rng);

        for (int k = 0; k < num_of_traitors; ++k) {
            auto traitor = potential_traitors[k];
            std::cout << "Traitor assigned: General #" << traitor->id << "\n";
            traitor->is_traitor = true;
        }

        Message order = { true }; // The order is ATTACK

        OMAlgorithm om_algorithm;
        om_algorithm.is_first_commander_loyal = true;
        om_algorithm.original_order = order;

        // Pick a random commander
        std::uniform_int_distribution<int> uni(0, generals.size() - 1);
        int first_commander_index = uni(rng);
        std::shared_ptr<General> first_commander_rc = generals[first_commander_index];

        // Set commander's decision (what they INTEND to send)
        first_commander_rc->decision = order.attack;
        
        std::cout << "Commander is General #" << first_commander_rc->id << "\n";

        // Remove commander from the list of lieutenants
        // (The vector 'generals' now acts as 'lieutenants')
        generals.erase(generals.begin() + first_commander_index);

        if (first_commander_rc->is_traitor) {
            om_algorithm.is_first_commander_loyal = false;
            std::cout << "NOTE: Commander is a TRAITOR.\n";
        }

        // Run the algorithm
        om_algorithm.om_algorithm(generals, first_commander_rc, m);

        om_algorithm.get_total_messages(generals);

        bool successful = om_algorithm.was_successful(generals);

        std::cout << "Consensus Successful: " << (successful ? "TRUE" : "FALSE") << "\n";
        std::cout << "--------------------------------------------------\n\n";

        if (!successful) {
            std::cout << "Found a case where consensus isn't achieved!\n";
            break;
        }
    }

    return 0;
}