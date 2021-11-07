package student;

import java.util.*;
import java.util.function.DoubleToIntFunction;

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

    //O(n log n)
    @Override
    public <T> Edge<T> addRedundant(Graph<T> g, T root) {
        // Task 3
        // TODO implement method

        HashSet<T> visited = new HashSet<>(); //O(1)
        HashMap<T, Integer> weight = new HashMap<>(); //O(1)
        LinkedList<T> leaves = new LinkedList<>(); //O(1)
        HashMap<T, Integer> distance = new HashMap<>(); //O(1)

        eulerTree(g, weight, distance, visited, leaves, root, 0); //O(n)

        PriorityQueue<T> sortedNeighbours = getChildren(g, root, weight); //O(1)
        T path1 = bestNode(g, root, sortedNeighbours.poll(), weight, 0); //O(n log n)
        T path2 = bestNode(g, root, sortedNeighbours.poll(), weight, weight.get(sortedNeighbours.poll()));//O(n log n)

        return new Edge<T>(path1, path2); //O(n)
    }

    //O(degree log n)
    private <T> PriorityQueue<T> getChildren(Graph<T> g, T parent, HashMap<T, Integer> weight) {
        PriorityQueue<T> sortedChildren = new PriorityQueue<>((o1, o2) -> -Integer.compare(weight.get(o1), weight.get(o2))); //O(log n)

        for (T n : g.neighbours(parent)) { //n iterations
            if (weight.get(n) < weight.get(parent))
                sortedChildren.add(n);
        }
        return sortedChildren;
    }

    //Runtime: O(n log n)
    private <T> T bestNode(Graph<T> g, T root, T neighbourChildren, HashMap<T, Integer> weight, Integer neighbourSize) {
        if (neighbourChildren == null) //O(1)
            return root;

        int smallestNeighbour = (neighbourSize == null ? 0 : neighbourSize); //O(n)
        int neighbourWeight = weight.get(neighbourChildren); //O(n)

        while (smallestNeighbour <= neighbourWeight) {
            PriorityQueue<T> children = getChildren(g, neighbourChildren, weight); //O(n log n)
            neighbourChildren = children.poll(); //O(log n)

            if (weight.get(neighbourChildren) != null) //O(1)
                neighbourWeight = weight.get(neighbourChildren); //O(1)

            if (weight.get(children.poll()) != null) //O(log n)
                try {
                    if (smallestNeighbour < weight.get(children.poll())) //O(1)
                        smallestNeighbour = weight.get(children.poll()); //O(1)
                } catch (NullPointerException ignored) {
                }
        }
        return neighbourChildren;
    }

    //O(n)
    public <T> Integer eulerTree(Graph<T> g, HashMap<T, Integer> weight, HashMap<T, Integer> distance, HashSet<T> visited, LinkedList<T> leaves, T node, Integer depth) {
        visited.add(node); //O(1)
        int childNode = 1;
        for (T n : g.neighbours(node)) { //O(n)
            if (g.degree(n) == 1) {
                leaves.add(n);
            }
            if (visited.contains(n)) {
                continue;
            }
            childNode += eulerTree(g, weight, distance, visited, leaves, n, depth);
        }
        weight.put(node, childNode); //O(1)
        distance.put(node, depth); //O(1)
        return childNode;
    }
}
