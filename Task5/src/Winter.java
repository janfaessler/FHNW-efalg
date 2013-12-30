import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Winter {
	
	public static void main(String[] args) throws FileNotFoundException {

		Scanner s = new Scanner(new File("winter.in"));
		
		int cityCount = s.nextInt();
		int connCount = s.nextInt();
				
		HashMap<Integer, ArrayList<Node>> cities = new HashMap<>();
		for (int i = 1; i <= cityCount; i++) cities.put(i, new ArrayList<Node>());

		for (int i = 0; i < connCount; ++i) {
			
			long depart  = s.nextLong();
			int  city    = s.nextInt();
			
			long arrival = s.nextLong();
			int dest     = s.nextInt();
			
			long start   = depart * 10000 + city;
			long end     = arrival * 10000 + dest;
			Node startNode = new Node(start), endNode = new Node(end);
			startNode.conns.add(new Connection(endNode, 0));
			
			cities.get(city).add(startNode);
			cities.get(dest).add(endNode);
		}
		
		Iterator<Integer> it = cities.keySet().iterator();
		while (it.hasNext()) {
			
			List<Node> list = cities.get(it.next());
			Collections.sort(list, new Comparator<Node>() {
	            @Override public int compare(Node o1, Node o2) { return Long.compare(o1.ident, o2.ident); }
	        });
			
			for (int i = 0; i < list.size()-1; i++) {
				
				Node start = list.get(i);
				Node end   = list.get(i+1);
				start.conns.add(new Connection(end,(int) (end.ident/10000) - (int) (start.ident/10000)));
			}
		}
		
		int waitingTime = 0;
		PriorityQueue<Node> pq = new PriorityQueue<>();
		List<Node> startPoints = cities.get(1);
		for (int i = 0; i < startPoints.size(); i++){
			startPoints.get(i).waitingTime = 0;
			pq.add(startPoints.get(i));
		}
		cities = null;
		while(!pq.isEmpty()) {
			Node n = pq.remove();
			
			if(n.visited) continue;
			n.visited = true;
			
			if ((n.ident % 10000) == cityCount) {
				waitingTime = n.waitingTime;
				break;
			}
			
			for(Connection e: n.conns) {
				int newCost =  n.waitingTime + e.cost;
				if ( newCost < e.dest.waitingTime ) {
					e.dest.waitingTime = newCost;
					pq.add(e.dest);
				}
			}
		}
		
		System.out.println(waitingTime);
		PrintWriter out = new PrintWriter("winter.out");
		out.println(waitingTime);
		out.close();
		
		
	}
	
	public static class Node implements Comparable<Node> {
		long ident;
		int waitingTime = Integer.MAX_VALUE;
		boolean visited = false;
		List<Connection> conns = new ArrayList<>();
		
		public Node(long i) {
			ident = i;
		}
		
		@Override
		public int compareTo(Node o) {
			return Integer.compare(waitingTime, o.waitingTime);
		}
		
		@Override
		public String toString() {
			return "[city: "+(ident % 10000) + " time: "+(ident / 10000) + " waitingTime: "+ waitingTime+"]";
		}
		
	}
	
	public static class Connection {
		Node dest;
		int cost;
		
		public Connection(Node d, int c) {
			dest = d;
			cost = c;
		}
		
		@Override
		public String toString() {
			return "[dest: "+dest + " cost: "+cost+"]";
		}
		
	}

}
