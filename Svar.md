# Answer File - Semester 2
# Description of each Implementation
Briefly describe your implementation of the different methods. What was your idea and how did you execute it? If there were any problems and/or failed implementations please add a description.

## Task 1 - mst
For this task I used prims algorithm to make a minimum spanning tree. And to make this possible i used a PriorityQueue to automatically sort the edges that was found. Then we polled out of this queue to get where we were going next. 

## Task 2 - lca
For this task I used the BSF algorithm to map out the graph with the level/distance from the root that was given. With this information I made a pathing algorithm that found its way from the given node up to the root using the level/distance we found using BSF. And to find the lca I compared the two paths to one another and found the lowest node that was shared it both of the node's path.

## Task 3 - addRedundant
*In this task we use the euler algorithm to map out and find the weight of each of the nodes. This could also be used in task to but its 2 minutes to the deadline so theres not much more to be done, for the rest of the task we sort the children of the parents and then use the select node to calculate the distanse and the wait of all the children from the children of the root. Then we find the most optimal node to drav the power line.*


# Runtime Analysis
For each method of the different strategies give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented any helper methods you must add these as well.**

* ``mst(WeightedGraph<T, E> g)``: O(m log n)
    * *This is because the while loop will in the worst case make m loops and toSearch takes the most amount of time inside this while loop*
  

* ``update(Graph<T> g, HashSet<T> found, Queue<Edge<T>> toSearch, T newNode)``: O(degree)
  * *Here we have a for loop that looks through all the edges to a given node which makes degree iterations*
  

* ``otherNode(Edge<V> e, HashSet<V> found)``: O(1)
  * *Uses .contains on Hashset which have the expected runtime of O(1) if we have a good hash*

* ``lca(Graph<T> g, T root, T u, T v)``: O(degree log n)
    * *The longest runtime in this method is the pathing algorithm which also has O(degree log n)*

* ``BFSMapping(Graph<T> g, T root)``: O(m)
  * *Here we have a while loop which takes m iterations, and we have a function which have the runtime of O(degree), but we know that the while loop only comes down to this function n times which makes the overall runtime O(m)*

* ``pathing(Graph<T> g, HashMap<T, Integer> level, T root, T startNode)``: O(degree log n)
  * *Here we have a while loop that iterates the same amount of times as the length of the graph, which is log n. And inside this graph we have a for loop that runs through some adjacent edges = O(degree). This makes the runtime of O((log n) * degree) = O(degree log n)*

* ``getFoundNode(Edge<T> e, HashSet<T> found)``: O(1)
  * *Uses .contains on Hashset which have the expected runtime of O(1) if we have a good hash*

* ``other(Edge<T> e, T node)``: O(1)
  * *Just uses equals*

* ``addRedundant(Graph<T> g, T root)``: O(n log n)
    * *what takes time here is to select the node*

* ``getChildren(Graph<T> g, T parent, HashMap<T, Integer> weight)``: O(n log n)
  * *degree log n because the value of the input can change*

* ``bestNode(Graph<T> g, T root, T neighbourChildren, HashMap<T, Integer> size, Integer neighbourSize)``: O(n log n)
  * *What takes time here is the get children function*

