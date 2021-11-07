package student;

import java.util.*;

import graph.*;

public class ProblemSolver implements IProblem {

    //O(m log n)
    @Override
    public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) {
        // Task 1
        // TODO implement method
        ArrayList<Edge<T>> myGraph = new ArrayList<>(); //O(1)
        HashSet<T> found = new HashSet<>(); //O(1)
        PriorityQueue<Edge<T>> toSearch = new PriorityQueue<>(g); //O(1)

        T start = g.vertices().iterator().next(); //O(1)

        update(g, found, toSearch, start); //O(degree)

        while (!toSearch.isEmpty()) { //m iterations
            Edge<T> e = toSearch.poll(); //O(log n)
            T newNode = otherNode(e, found); //O(1)
            if (found.contains(newNode)) { //O(1)
                continue;
            }

            //This only happens O(n)-1
            update(g, found, toSearch, newNode); //O(degree) * O(n) = O(m)
            myGraph.add(e); //O(1)
        }
        return myGraph;
    }

    //O(degree)
    private static <T> void update(Graph<T> g, HashSet<T> found, Queue<Edge<T>> toSearch, T newNode) {
        found.add(newNode); //O(1)
        for (Edge<T> edge : g.adjacentEdges(newNode)) { //Degree iterations + O(degree)
            if (found.contains(edge.a) && found.contains(edge.b)) { //O(1)
                continue;
            }
            toSearch.add(edge); //O(1)
        }
    }

    //O(1)
    private static <V> V otherNode(Edge<V> e, HashSet<V> found) {
        if (found.contains(e.a)) //O(1)
            return e.b;
        if (found.contains(e.b)) //O(1)
            return e.a;

        throw new IllegalArgumentException("da fuk");
    }

    //O(degree log n)
    @Override
    public <T> T lca(Graph<T> g, T root, T u, T v) {
        // Task 2
        // TODO implement method
        HashMap<T, Integer> level = BFSMapping(g, root); //O(m)

        LinkedList<T> pathingU = pathing(g, level, root, u); //O(degree log n)
        LinkedList<T> pathingV = pathing(g, level, root, v); //O(degree log n)

        T previousCA = root;

        int length;

        //Finding the largest of the two list to avoid nullException
        if (pathingU.size() > pathingV.size()) { //O(1)
            length = pathingV.size();
        } else {
            length = pathingU.size();
        }

        //Pathing from the root to the LCA and return it
        for (int i = 0; i < length; i++) { //O(log n)
            T U = pathingU.removeLast(); //O(1) on LinkedList
            T V = pathingV.removeLast(); //O(1) on LinkedList
            if (U.equals(V)) { //O(1)
                previousCA = U;
            } else {
                return previousCA;
            }
        }
        return previousCA;
    }

    //O(m)
    private <T> HashMap<T, Integer> BFSMapping(Graph<T> g, T root) {
        HashMap<T, Integer> level = new HashMap<>(); //O(1)
        HashSet<T> found = new HashSet<>(); //O(1)
        LinkedList<Edge<T>> toSearch = new LinkedList<>(); //O(1)

        level.put(root, 0); //O(1)
        update(g, found, toSearch, root); //O(degree)

        while (!toSearch.isEmpty()) { //m iterations
            Edge<T> e = toSearch.removeFirst(); //O(1)
            T foundNode = getFoundNode(e, found); //O(1)
            T newNode = otherNode(e, found); //O(1)
            if (found.contains(newNode)) { //O(1)
                continue;
            }

            //This happens n-1 times * degree = O(m)
            level.put(newNode, level.get(foundNode) + 1); //O(1)
            update(g, found, toSearch, newNode); //O(degree)
        }
        return level;
    }

    //O(degree log n)
    private <T> LinkedList<T> pathing(Graph<T> g, HashMap<T, Integer> level, T root, T startNode) {
        LinkedList<T> pathing = new LinkedList<>(); //O(1)

        //The parent to starting node
        T parentNode; //O(1)

        //Adding the starting node to our pathing list
        pathing.add(startNode); //O(1)

        //As long as the starting node isn't the root we keep on pathing
        while (!startNode.equals(root)) { // log n iterations

            //For each of the edges to the starting node we look for the prent of the node
            for (Edge<T> edgeToCurrent : g.adjacentEdges(startNode)) { //degree iteration

                //Finding the other end to the edge
                parentNode = other(edgeToCurrent, startNode); //O(1)

                //Checks if this end is the parent node with a level below the starting node
                if (level.get(parentNode) < level.get(startNode)) { //O(1)

                    //If this is true we add this parent node to the pathing and then make this our starting node
                    pathing.add(parentNode); //O(1)
                    startNode = parentNode; //O(1)
                }
            }
        }
        return pathing;
    }

    //O(1)
    private static <T> T getFoundNode(Edge<T> e, HashSet<T> found) {
        if (found.contains(e.a)) //O(1)
            return e.a;
        if (found.contains(e.b)) //O(1)
            return e.b;

        throw new IllegalArgumentException("da fuk");
    }

    //O(1)
    private static <T> T other(Edge<T> e, T node) {
        if (e.a.equals(node)) { //O(1)
            return e.b;
        } else {
            return e.a;
        }
    }

    @Override
    public <T> Edge<T> addRedundant(Graph<T> g, T root) {
        // Task 3
        // TODO implement method'

        //Our leaf list
        ArrayList<T> leaves = new ArrayList<>();

        //A list to see witch nodes we have been on
        HashSet<T> found = new HashSet<>();

        //A list that holds the current path
        LinkedList<T> path = new LinkedList<>();

        //A list that keeps track where to search next
        LinkedList<Edge<T>> toSearch = new LinkedList<>();

        //Forgot level!!!!!!!!!!!!
        HashMap<T, Integer> level = new HashMap<>(); //O(1)
        level.put(root, 0); //O(1)

        //First we add the root to pathing
        path.add(root);
        //Here we update the toSearch and found list
        update(g, found, toSearch, root);

        //So while the toSearch list is not empty we keep on searching
        while(!toSearch.isEmpty()){

            //Begin with the edge that was last added to the searching list
            Edge<T> e = toSearch.removeLast();

            //Then we find the node that we are currently standing on
            T currentNode = getFoundNode(e, found);

            //Then the node we are looking at
            T newNode = otherNode(e, found);

            //If the node we have found the node we are looking at we skip and move forward
            if(found.contains(newNode)){
                continue;
            }

            //If it is a new node we check to see if it is a leaf the the degree method witch takes O(1) then we can add this to a list
            if(g.degree(newNode) == 1){
                leaves.add(newNode);
            }

            //And at the end we update the toSearch and continue the loop
            update(g,found,toSearch,newNode);
            //Forgot level!!!!
            level.put(newNode, level.get(currentNode) + 1); //O(1)
        }

        // After this we have a list witch contains all the leaves of the tree. With this we path from all the leaves and make a list for each of them.

        //We need a hashmap to store all of the values
        LinkedList<LinkedList<T>> pathing = new LinkedList<>();

        for(T leaf : leaves){
            pathing.add(pathing(g,level,root,leaf));
        }

        //Now that we have a list of nodes we can start in the beginning of the list and use the weightNode class to add weight to all of the nodes

        HashMap<T, Integer> weight = new HashMap<>();

        for(LinkedList<T> list : pathing){
            System.out.println(list);
            T lastNode = list.getFirst();
            for(T someNode : list){
                if(lastNode.equals(someNode)){
                    weight.put(someNode, 0);
                }
                weight.put(someNode, weight.get(lastNode)+1);
                System.out.println(weight.get(lastNode));
                lastNode = someNode;
            }
        }

        //Then using the path lists again we add upp all of the weight in that path and find the two biggest values, this is the edge we return


        int first = 0;
        int second = 0;
        T firstNode = root;
        T secondNode = root;

        for(LinkedList<T> list : pathing){
            int counter = 0;
            for(T someNode : list){
                int number = weight.get(someNode);
                counter += number;
            }
            if(counter > first){
                first = counter;
                firstNode = list.getFirst();
            }
            else if(counter > second){
                second = counter;
                secondNode = list.getFirst();
            }

        }

        return new Edge(firstNode, secondNode);
    }

    private <T> HashMap<T, Integer> DFSMapping(Graph<T> g, T root) {
        HashMap<T, Integer> level = new HashMap<>(); //O(1)
        HashSet<T> found = new HashSet<>(); //O(1)
        LinkedList<Edge<T>> toSearch = new LinkedList<>(); //O(1)

        level.put(root, 0); //O(1)
        update(g, found, toSearch, root); //O(degree)

        while (!toSearch.isEmpty()) { //m iterations
            Edge<T> e = toSearch.removeLast(); //O(1)
            T foundNode = getFoundNode(e, found); //O(1)
            T newNode = otherNode(e, found); //O(1)
            if (found.contains(newNode)) { //O(1)
                continue;
            }

            //This happens n-1 times * degree = O(m)
            level.put(newNode, level.get(foundNode) + 1); //O(1)
            update(g, found, toSearch, newNode); //O(degree)
        }
        return level;
    }
}
