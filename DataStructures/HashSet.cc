// ref: https://stackoverflow.com/questions/29458498/how-do-hashset-and-hashmap-work-in-java
// ref: first paragraph of https://www.geeksforgeeks.org/unordered_set-in-cpp-stl/
// TLDR: HashSet/std::unordered_map is backed by a HashMap internally. The value you add to the HashSet is used as the key in the HashMap, value is just a dummy value. 
// EX: calling .contains(element) really just calls the .containsKey(element) of the internal HashMap

// https://courses.cs.washington.edu/courses/cse373/13wi/lectures/02-11/
