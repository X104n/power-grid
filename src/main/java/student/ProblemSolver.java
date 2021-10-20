package student;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import graph.Edge;
import graph.Graph;
import graph.WeightedGraph;

public class ProblemSolver implements IProblem {

	@Override
	public <T, E extends Comparable<E>> ArrayList<Edge<T>> mst(WeightedGraph<T, E> g) {
		//get one vertex to start from
		T start = g.vertices().iterator().next();
		//Keep all vertices already connected to start
		HashSet<T> seen = new HashSet<T>();
		//Keep all edges that could be the smallest edge
		PriorityQueue<Edge<T>> toSearch = new PriorityQueue<Edge<T>>(g);
		//add start
		seen.add(start);
		addAll(toSearch,g.adjacentEdges(start));
		
		ArrayList<Edge<T>> treeEdges = new ArrayList<Edge<T>>();
		
		while(!toSearch.isEmpty() && seen.size()<g.numVertices()) {
			Edge<T> min = toSearch.poll();
			if(seen.contains(min.a) && seen.contains(min.b)) {
				continue;
			}

			T newNode;
			if(!seen.contains(min.a)) {
				newNode = min.a;
			}else {
				newNode = min.b;
			}
			seen.add(newNode);
			addAll(toSearch, g.adjacentEdges(newNode));
			treeEdges.add(min);
		}
		
		return treeEdges;
	}

	/**
	 * Helper method since Collections.addAll do not take iterable as input.
	 */
	private <T> void addAll(Collection<T> coll, Iterable<T> elems) {
		for(T v : elems) {
			coll.add(v);
		}
	}

	@Override
	public <T> T lca(Graph<T> g, T root, T u, T v) {
		HashSet<T> path_u = new HashSet<T>();
		path_u.addAll(pathTo(g, u, root));

		for(T node : pathTo(g, v, root)) {
			if(path_u.contains(node))
				return node;
		}
		return null;
	}
	
	public <T> LinkedList<T> pathTo(Graph<T> g, T from, T to){
		HashSet<T> seen = new HashSet<T>();
		return pathTo(g, from, to, seen);
	}

	public <T> LinkedList<T> pathTo(Graph<T> g, T from, T to, HashSet<T> seen){
		LinkedList<T> path = new LinkedList<T>();
		if(from.equals(to)) {
			path.add(from);
			return path;
		}

		for(T neighbour : g.neighbours(from)) {
			if(!seen.contains(neighbour)) {
				seen.add(neighbour);
				path = pathTo(g,neighbour,to,seen);
			}

			if(!path.isEmpty() && path.peekLast().equals(to)) {
				path.addFirst(from);
				return path;
			}
		}
		return path;
	}

	@Override
	public <T> Edge<T> addRedundant(Graph<T> g, T root) {
		//compute sizes of subtree
		HashMap<T,Integer> sizes = subtreeSize(g, root);
		
		ArrayList<T> children = new ArrayList<T>();
		addAll(children, g.neighbours(root));
		
		//select a and b as the 2 largest children
		T a = root;
		if(!children.isEmpty()) {
			a = maxChild(children, root, sizes);
			children.remove(a);
			a = walkDown(g, a, sizes);
		}
		T b = root;
		if(!children.isEmpty()) {
			b = maxChild(children, root, sizes);
			b = walkDown(g, b, sizes);
		}
		return new Edge<T>(a,b);
	}
	
	/**
	 * Selects the child with largest size subtree and walks in that direction.
	 * Stops when reaching a leaf.
	 */
	private <T> T walkDown(Graph<T> g, T a, HashMap<T,Integer> sizes) {
		while(sizes.get(a)>1) {
			a= maxChild(g.neighbours(a), a, sizes);
		}
		return a;
	}
	
	/**
	 * Finds the child with the largest subtree.
	 * Since sizes is made from a root the children has sizes smaller than node while 
	 * parent has size larger than node.
	 */
	private <T> T maxChild(Iterable<T> neighbours, T node, HashMap<T,Integer> sizes) {
		T best = null;
		int bestSize = 0;
		int sizeLimit = sizes.get(node);
		
		for(T neighbour : neighbours) {
			int s = sizes.get(neighbour);
			if(s<sizeLimit && s>bestSize) {
				best = neighbour;
				bestSize = s;
			}
		}
		return best;
	}
	
	public <T> HashMap<T,Integer> subtreeSize(Graph<T> tree, T root) {
		HashSet<T> seen = new HashSet<T>();
		HashMap<T,Integer> sizes = new HashMap<T, Integer>();
		subtreeSize(tree, root,sizes,seen);
		return sizes;
	}

	private <T> int subtreeSize(Graph<T> tree, T root, HashMap<T,Integer> sizes, HashSet<T> seen) {
		seen.add(root);
		int sumSizes = 1;
		for(T neighbour : tree.neighbours(root)) {
			if(!seen.contains(neighbour)) {
				sumSizes += subtreeSize(tree, neighbour, sizes, seen);
			}
		}
		sizes.put(root, sumSizes);
		return sumSizes;
	}
}


