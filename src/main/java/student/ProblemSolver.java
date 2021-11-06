package student;

import java.util.*;

import graph.*;

public class ProblemSolver implements IProblem {

    @Override
    public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) { //O(m log n)
        // Task 1
        // TODO implement method
        ArrayList<Edge<T>> myGraph = new ArrayList<>(); //O(1)
        HashSet<T> found = new HashSet<>(); //O(1)
        PriorityQueue<Edge<T>> toSearch = new PriorityQueue<>(g); //O(1)

        T start = g.vertices().iterator().next(); //O(1)
        updateMst(g, found, toSearch, start); //O(degree)

        while (!toSearch.isEmpty()) { //m iterations
            Edge<T> e = toSearch.poll(); //O(log n)
            T newNode = missing(e, found); //O(1)
            if (found.contains(newNode)) { //O(1)
                continue;
            }
            updateMst(g, found, toSearch, newNode); //O(degree)
            myGraph.add(e); //O(1)
        }
        return myGraph;
    }

    private static <T, E extends Comparable<E>> void updateMst(WeightedGraph<T, E> g, HashSet<T> found, PriorityQueue<Edge<T>> toSearch, T newNode) { //O(degree)
        found.add(newNode); //O(1)
        for (Edge<T> edge : g.adjacentEdges(newNode)) { //Degree iterations
            if (found.contains(edge.a) && found.contains(edge.b)) { //O(1)
                continue;
            }
            toSearch.add(edge); //O(1)
        }
    }

    private static <V> V missing(Edge<V> e, HashSet<V> found) { //O(1)
        if (found.contains(e.a)) //O(1)
            return e.b;
        if (found.contains(e.b)) //O(1)
            return e.a;

        throw new IllegalArgumentException("da fuk");
    }

    @Override
    public <T> T lca(Graph<T> g, T root, T u, T v) {
        // Task 2
        // TODO implement method
        HashSet<T> found = new HashSet<>();
        HashMap<T, Integer> level = new HashMap<>();
        LinkedList<Edge<T>> toSearch = new LinkedList<>();

        level.put(root, 0);
        updateLca(g, found, toSearch, root);

        while (!toSearch.isEmpty()) {
            Edge<T> e = toSearch.removeFirst();
            T foundNode = getFoundNode(e, found);
            T newNode = missing(e, found);
            if (found.contains(newNode)) {
                continue;
            }
            level.put(newNode, level.get(foundNode) + 1);

            updateLca(g, found, toSearch, newNode);
        }

        LinkedList<T> pathingU = pathing(g, level, root, u);
        LinkedList<T> pathingV = pathing(g, level, root, v);

        T lastNode = root;

        int length;

        if (pathingU.size() > pathingV.size()) {
            length = pathingV.size();
        } else {
            length = pathingU.size();
        }

        for (int i = 0; i < length; i++) {
            T U = pathingU.removeLast();
            T V = pathingV.removeLast();
            if (U.equals(V)) {
                lastNode = U;
            } else {
                return lastNode;
            }
        }
        return lastNode;
    }

//    private <T> HashMap<T, Integer> BFSMapping(){
//        return new HashMap<>();
//    }

    private <T> LinkedList<T> pathing(Graph<T> g, HashMap<T, Integer> level, T root, T powerNode) {
        LinkedList<T> pathing = new LinkedList<>();

        T parentNode;
        pathing.add(powerNode);
        boolean foundRoot = false;

        while (!foundRoot) {

            for (Edge<T> edgeToCurrent : g.adjacentEdges(powerNode)) {
                parentNode = other(edgeToCurrent, powerNode);

                if (powerNode.equals(root)) {
                    foundRoot = true;
                }

                if (level.get(parentNode) < level.get(powerNode)) {
                    pathing.add(parentNode);
                    powerNode = parentNode;
                }
            }

        }
        return pathing;
    }

    private static <T> void updateLca(Graph<T> g, HashSet<T> found, LinkedList<Edge<T>> toSearch, T newNode) {
        found.add(newNode);
        for (Edge<T> edge : g.adjacentEdges(newNode)) {
            if (found.contains(edge.a) && found.contains(edge.b)) {
                continue;
            }
            toSearch.add(edge);
        }
    }

    private static <V> V getFoundNode(Edge<V> e, HashSet<V> found) {
        if (found.contains(e.a))
            return e.a;
        if (found.contains(e.b))
            return e.b;

        throw new IllegalArgumentException("da fuk");
    }

    private static <T> T other(Edge<T> e, T node) {
        if (e.a.equals(node)) {
            return e.b;
        } else {
            return e.a;
        }
    }

    @Override
    public <T> Edge<T> addRedundant(Graph<T> g, T root) {
        // Task 3
        // TODO implement method'
        HashSet<T> found = new HashSet<>();
        HashMap<T, Integer> level = new HashMap<>();
        LinkedList<Edge<T>> toSearch = new LinkedList<>();

        level.put(root, 0);
        updateLca(g, found, toSearch, root);

        while (!toSearch.isEmpty()) {
            Edge<T> e = toSearch.removeFirst();
            T foundNode = getFoundNode(e, found);
            T newNode = missing(e, found);
            if (found.contains(newNode)) {
                continue;
            }
            level.put(newNode, level.get(foundNode) + 1);

            updateLca(g, found, toSearch, newNode);
        }
//        g.
//        for(int i = 0; i<)
        return null;
    }
}
