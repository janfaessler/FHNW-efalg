import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import java.util.PriorityQueue;

public final class Shopping {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("shopping.in"))));

		String[] tmp = reader.readLine().split(" ");
		int width = Integer.parseInt(tmp[0]);
		int height = Integer.parseInt(tmp[1]);

		int start = 0;
		int end = 0;

		ArrayList<ArrayList<Edge>> graph = new ArrayList<ArrayList<Edge>>(width * height);
		int[] costs = new int[width * height];

		String line;
		for (int i = 0; i < height; ++i) {
			line = reader.readLine();
			if (!line.isEmpty()) {
				for (int j = 0; j < width; ++j) {
					graph.add(new ArrayList<Edge>());
					
					int index = graph.size() - 1;
					char inputChar = line.charAt(j);
					
					switch(inputChar) {
						case 'S':
							start = index;
							costs[index] = 0;
							break;
						case 'D':
							costs[index] = 0;
							end = index;
							break;
						case 'X':
							costs[index] = 9999999;
							break;
						default:
							costs[index] = Character.getNumericValue(inputChar);
					}

					int oben = index - width;
					if (oben >= 0) {
						graph.get(oben).add(new Edge(oben, index, costs[index]));
						graph.get(index).add(new Edge(index, oben, costs[oben]));
					}
					int links = index - 1;
					if (links >= 0 && (links / width) == (index / width)) {
						graph.get(links).add(new Edge(links, index, costs[index]));
						graph.get(index).add(new Edge(index, links, costs[links]));
					}
					
				}
			}
		}


		int[] result = new int[graph.size()];
		PriorityQueue<Node> pq = new PriorityQueue<Node>(graph.size());

		Edge[] shortestPath = new Edge[graph.size()];
		Edge[] border = new Edge[graph.size()];

		Node startNode = new Node(start, costs[start]);
		result[start] = costs[start];
		
		pq.add(startNode);

		while (!pq.isEmpty()) {
			
			Node current = pq.remove();
			shortestPath[current.index] = border[current.index];

			if (current.index != end) {

				for (Edge edge : graph.get(current.index)) {
					
					int newCost = result[current.index] + edge.cost;
					
					if (border[edge.to] == null || (newCost < result[edge.to] && shortestPath[edge.to] == null) ) {

						pq.add(new Node(edge.to, newCost));
						border[edge.to] = edge;
						result[edge.to] = newCost;
					} 
				}
			}
		}

		PrintWriter out = new PrintWriter("shopping.out");
		out.println(result[end]);
		//System.out.println(costsToNode[end]);
		out.close();
	}


	private static class Node implements Comparable<Node> {

		private final int index;
		private int cost;

		public Node(int i, int c) { index = i; cost = c; }
		public int compareTo(Node o) { return Integer.compare(cost, o.cost); }
	}

	private static class Edge {

		private final int from;
		private final int to;
		private final int cost;

		private Edge(int from, int to, int cost) {
			this.from = from;
			this.to = to;
			this.cost = cost;
		}

		public String toString() { return "("+from + " -> "+to+")["+cost+"]"; }
	}

}