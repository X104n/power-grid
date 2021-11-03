package student;

import java.util.*;

import graph.*;

public class ProblemSolver implements IProblem {

	private static <T, E extends Comparable<E>> void update(WeightedGraph<T, E> g, HashSet<T> found, PriorityQueue<Edge<T>> toSearch, T newNode){
		found.add(newNode);
		for(Edge<T> edge : g.adjacentEdges(newNode)){
			if(found.contains(edge.a) && found.contains(edge.b)){
				continue;
			}
			toSearch.add(edge);
		}
	}
	private static <V> V getFoundNode(Edge<V> e, HashSet<V> found) {
		if(found.contains(e.a))
			return e.a;
		if(found.contains(e.b))
			return e.b;

		throw new IllegalArgumentException("da fuk");
	}
	private static <V> V other(Edge<V> e, HashSet<V> found) {
		if(found.contains(e.a))
			return e.b;
		if(found.contains(e.b))
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

		update(g, found, toSearch, start);

		while(!toSearch.isEmpty()){
			Edge<T> e = toSearch.poll();
			T newNode = other(e, found);
			if(found.contains(newNode)){
				continue;
			}

			update(g, found, toSearch, newNode);

			myGraph.add(e);
		}
		return myGraph;
	}

	@Override
	public <T> T lca(Graph<T> g, T root, T u, T v) {
		// Task 2
		// TODO implement method
		HashSet<T> found = new HashSet<>();
		HashMap<T, T> pathing = new HashMap<>();
		HashMap<T, Integer> level = new HashMap<>();
		LinkedList<Edge<T>> toSearch = new LinkedList<>();
		T lastNode = root;
		T newNode;

		level.put(root, 0);

		for(Edge<T> edge : g.adjacentEdges(root)){
			toSearch.add(edge);
		}
		found.add(root);

		while(!found.contains(u) && !found.contains(v)){
			Edge<T> vert = toSearch.removeFirst();
			T inspect = null;
			if(found.contains(vert.a) && found.contains(vert.b)){
				continue;
			}
			if(found.contains(vert.a)){
				inspect = vert.b;
			}
			else{
				inspect = vert.a;
			}
			if(vert == null){
				throw new IllegalArgumentException("Da fuk");
			}
			pathing.put(vert.a, vert.b);
			pathing.put(vert.b, vert.a);
			newNode = inspect;

			for(Edge<T> edge : g.adjacentEdges(inspect)){
				toSearch.add(edge);
			}
			found.add(inspect);

			level.put(newNode, level.get(lastNode)+1);

			lastNode = newNode;
		}

		while (u != v) {
			if (level.get(u) > level.get(v)) {
				while(true){
					if(level.get(pathing.get(u)) < level.get(u))
						u = pathing.get(u);
				}
			} else {
				while(true){
					if(level.get(pathing.get(v)) < level.get(v))
						u = pathing.get(v);
				}
			}
		}
		return u;
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
