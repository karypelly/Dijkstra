import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Graphs {
	
	/**
	 * Logic from:
	 * 	https://github.com/ilija139/Algorithms/blob/master/src/my/code/Dijkstra.java
	 * 	https://improve.dk/generic-dijkstras-algorithm/
	 */
	public static Path shortestPath(Graph graph, String from, String to) {
		Path path = new Path();
		Map<String, Double> minDistances = new HashMap<>();
		Map<String, String> previous = new HashMap<String, String>();
		for (String s : graph.adj.keySet()) {
			minDistances.put(s, Double.POSITIVE_INFINITY);
		}
		minDistances.put(from, 0.);
		PriorityQueue<String> vertexQueue = new PriorityQueue<String>();
		vertexQueue.add(from);
		while (!vertexQueue.isEmpty()) {
			String u = vertexQueue.poll();
			for (Edge e : graph.adj.get(u)) {
				String v = e.other(new Vertex(u)).name;
				double weight = e.weight();
				double distance = minDistances.get(u) + weight;
				if (distance < minDistances.get(v)) {
					vertexQueue.remove(v);
					minDistances.put(v, distance);
					v = e.other(new Vertex(u)).name;
					vertexQueue.add(v);
					previous.put(v, u);
				}
			}
		}
		List<String> result = new ArrayList<String>();
		String target = to;
		while (previous.containsKey(target)) {
			result.add(target);
			target = previous.get(target);
			
		}
		result.add(from);
		Collections.reverse(result);
		List<Double> weights = new ArrayList<Double>();
		for (int i = 0; i < result.size() - 1 ; i++) {
			for (String s : graph.adj.keySet()) {
				for (Edge e : graph.adj.get(s)) {
					if (result.get(i).equals(s) && result.get(i+1).equals(e.other(e.either()).name)) {
						weights.add(e.weight());
					}
				}
			}
		}
		for (int i = 0; i < result.size() - 1; i ++) {
			path.getList().add(new Edge(new Vertex(result.get(i)), new Vertex(result.get(i+1)), weights.get(i)));
		}
		return path;
	}
	
	/**
	 * Logic from:
	 *  https://steemit.com/programming/@drifter1/programming-java-graph-minimum-spanning-tree-algorithms
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Graph MST(Graph graph) {
		List<Edge> mst = new ArrayList<Edge>();
		String start = null;
		for (String s : graph.adj.keySet()) {
			start = s;
			break;
		}
		PriorityQueue<Edge> edges = new PriorityQueue<Edge>();
		Map<String, Boolean> marked = new HashMap<>();
		edges.addAll(graph.adj.get(start));
		for (String s : graph.adj.keySet()) {
			marked.put(s, false);
		}
		marked.put(start, true);
		while (!edges.isEmpty()) {
			Edge e = edges.remove();
			if (!marked.get(e.other(e.either()).name)) {
				mst.add(e);
				marked.put(e.other(e.either()).name, true);
				ArrayList<Edge> n = new ArrayList<Edge>();
				for (Edge k : graph.adj.get(e.other(e.either()).name)) {
					n.add(k);
				}
				ArrayList<Edge> it = n;
				for (int i = 0; i < it.size(); i++) {
					edges.add(it.get(i));
				}
				marked.put(e.other(e.either()).name, true);
			}
		}
		int count = 0;
		List<Edge> edge = new ArrayList<>();
		for (String v : graph.vertices()) {
			count++;
			for (Edge e : mst) {
				
				if (v.equals(e.either().name)) {
					edge.add(e);
				}
			}
			graph.adj.put(v, new ArrayList(edge));
			edge.clear();
		}
		graph.V = count;
		graph.E = mst.size();
		return graph;
	}
}
	


