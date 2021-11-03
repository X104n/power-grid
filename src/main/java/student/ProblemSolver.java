package student;

import java.util.*;

import graph.*;

public class ProblemSolver implements IProblem {

    private static <T, E extends Comparable<E>> void updateMst(WeightedGraph<T, E> g, HashSet<T> found, PriorityQueue<Edge<T>> toSearch, T newNode) {
        found.add(newNode);
        for (Edge<T> edge : g.adjacentEdges(newNode)) {
            if (found.contains(edge.a) && found.contains(edge.b)) {
                continue;
            }
            toSearch.add(edge);
        }
    }

    private <T> LinkedList<T> pathing(T powerNode, T root, Graph<T> g, HashMap<T, Integer> level) {
        LinkedList<T> pathing = new LinkedList<>();

        T currentNode = powerNode;

        T parentNode = powerNode;
        pathing.add(powerNode);
        boolean foundRoot = false;

        while (!foundRoot) {

            for (Edge<T> edgeToCurrent : g.adjacentEdges(currentNode)) {
                parentNode = other(edgeToCurrent, currentNode);

                if (currentNode.equals(root)) {
                    foundRoot = true;
                }

                if (level.get(parentNode) < level.get(currentNode)) {
                    pathing.add(parentNode);
                    currentNode = parentNode;
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

    private static <V> V missing(Edge<V> e, HashSet<V> found) {
        if (found.contains(e.a))
            return e.b;
        if (found.contains(e.b))
            return e.a;

        throw new IllegalArgumentException("da fuk");
    }

    @Override
    public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) {
        // Task 1
        // TODO implement method
        ArrayList<Edge<T>> myGraph = new ArrayList<>();
        HashSet<T> found = new HashSet<>();
        PriorityQueue<Edge<T>> toSearch = new PriorityQueue<>(g);

        T start = g.vertices().iterator().next();
        updateMst(g, found, toSearch, start);

        while (!toSearch.isEmpty()) {
            Edge<T> e = toSearch.poll();
            T newNode = missing(e, found);
            if (found.contains(newNode)) {
                continue;
            }
            updateMst(g, found, toSearch, newNode);
            myGraph.add(e);
        }
        return myGraph;
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

            //compute distance for newNode
            level.put(newNode, level.get(foundNode) + 1);

            //update newNode
            updateLca(g, found, toSearch, newNode);

        }

//		T lowest;
//		T highest;
//		if(level.get(u) > level.get(v)){
//			lowest = v;
//			highest = u;
//		}
//		else{
//			highest = u;
//			lowest = v;
//		}
//		LinkedList<T> pathingLow = new LinkedList<>();
//		LinkedList<T> pathingHigh = new LinkedList<>();
//
//		T parentNode = lowest;
//		pathingLow.add(lowest);
//		boolean foundRoot = false;
//
//		while(!foundRoot) {
//
//			for (Edge<T> toParent : g.adjacentEdges(lowest))
//				parentNode = other(toParent, lowest);
//
//			if(parentNode.equals(root)){
//				foundRoot = true;
//			}
//
//			if (level.get(parentNode) > level.get(lowest)) {
//				pathingLow.add(parentNode);
//				lowest = parentNode;
//			}
//
//		}

        LinkedList<T> pathingU = pathing(u, root, g, level);
        LinkedList<T> pathingV = pathing(v, root, g, level);
		System.out.println(pathingU);
		System.out.println(pathingV);

		T lastNode = root;

		int length;

		if(pathingU.size() > pathingV.size()){
			length = pathingV.size();
		}else{
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


//		for(Edge<T> edge : g.adjacentEdges(root)){
//			toSearch.add(edge);
//		}
//		found.add(root);
//
//		while(!found.contains(u) && !found.contains(v)){
//			Edge<T> vert = toSearch.removeFirst();
//			T inspect = null;
//			if(found.contains(vert.a) && found.contains(vert.b)){
//				continue;
//			}
//			if(found.contains(vert.a)){
//				inspect = vert.b;
//			}
//			else{
//				inspect = vert.a;
//			}
//			if(vert == null){
//				throw new IllegalArgumentException("Da fuk");
//			}
//			pathing.put(vert.a, vert.b);
//			pathing.put(vert.b, vert.a);
//			newNode = inspect;
//
//			for(Edge<T> edge : g.adjacentEdges(inspect)){
//				toSearch.add(edge);
//			}
//			found.add(inspect);
//
//			level.put(newNode, level.get(lastNode)+1);
//
//			lastNode = newNode;
//		}
//
//		while (u != v) {
//			if (level.get(u) > level.get(v)) {
//				while(true){
//					if(level.get(pathing.get(u)) < level.get(u))
//						u = pathing.get(u);
//				}
//			} else {
//				while(true){
//					if(level.get(pathing.get(v)) < level.get(v))
//						u = pathing.get(v);
//				}
//			}
//		}
//		return u;
        //      static Node lca(Node root,int v1,int v2)

//		if (root == null || root.data == v1 || root.data == v2) {
//			return root;
//		}
//
//		Node left = lca(root.left, v1, v2);
//		Node right = lca(root.right, v1, v2);
//
//		if (left != null && right != null) {
//			return root;
//		}
//
//		return (left != null) ? left : right;


//		int count = 0;
//		T temp = null;
//		for(T child : g.neighbours(root)) {
//			T result = lca(g, child, v, u);
//			if(result != null) {
//				count++;
//				temp = result;
//			}
//		}
//
//		if(count == 2) {
//			return root;
//		}
//
//		return temp;
    }

    @Override
    public <T> Edge<T> addRedundant(Graph<T> g, T root) {
        // Task 3
        // TODO implement method'

        return null;
    }
}
