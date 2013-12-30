
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public final class WinterBak {
	
	public static void main(String[] args) throws IOException {

		Scanner s = new Scanner(new File("winter.in"));
		
		int cityCount = s.nextInt();
		int connCount = s.nextInt();
		long[][] connections = new long[connCount][4];

		for (int i = 0; i < connCount; ++i) {
			connections[i][0] = s.nextLong(); // depart
			connections[i][1] = s.nextInt(); // city
			connections[i][2] = s.nextLong(); // arrival
			connections[i][3] = s.nextInt(); // destination
		}
		
		HashMap<Integer, City> cities = new HashMap<>();
		for (int i = 1; i <= cityCount; i++) cities.put(i, new City(i));
		
		City destination = cities.get(cityCount);
		City startCity = cities.get(1);
		
		startCity.connections.put((long) 0, new ArrayList<Connection>());
		for (int i = 0; i < connections.length; i++) 
			cities.get((int)connections[i][3]).connections.put(connections[i][2], new ArrayList<Connection>());
		
		for (int i = 0; i < connections.length; i++) {
			City current    = cities.get(connections[i][1]);
			City currDest   = cities.get(connections[i][3]);
			long departTime  = connections[i][0];
			long arrivalTime = connections[i][2];
			
			Iterator<Long> it = current.connections.keySet().iterator();
			Connection v = new Connection(0, departTime, arrivalTime, currDest);
			
			while (it.hasNext()) {
			    arrivalTime = it.next();
				if (arrivalTime <= departTime) 
					current.connections.get(arrivalTime).add(v);
			}
		}
		connections = null;

		PriorityQueue<Connection> pq = new PriorityQueue<>();
		pq.add(new Connection(0, 0, 0, startCity));
		
		while(!pq.isEmpty()) {
			
			Connection current = pq.remove();

			if (!current.destination.visited) {
				current.destination.visited = true;
				current.destination.waitingTime = current.waitingTime;
			}
		
			if (current.destination ==  destination)
				break;
			
			for (Connection conn : current.getDestinationConnections(current.arrivalTime)) {
				conn.waitingTime = current.waitingTime + (conn.departTime- current.arrivalTime);
				pq.add(conn);
			}
			
		}
		System.out.println(destination.waitingTime);
		PrintWriter out = new PrintWriter("winter.out");
		out.println(destination.waitingTime);
		out.close();

	}
	
	public static void printCities(HashMap<Integer, City> cities) {
		Iterator<Integer> it = cities.keySet().iterator();
		while (it.hasNext()) {
			int city = it.next();
			City c = cities.get(city);
			System.out.println(c);
			Iterator<Long> it2 = c.connections.keySet().iterator();
			while (it2.hasNext()) {
				long waitingTime = it2.next();
				System.out.print(waitingTime + " : ");
				List<Connection> list = c.connections.get(waitingTime);
				for (Connection v : list) {
					System.out.print(v + ", ");
				}
				System.out.println();
			}
		}
		
	}
	
	public static class City {
		private boolean visited = false;
		private int city = 0;
		private long waitingTime = 0;
		private HashMap<Long, List<Connection>> connections = new HashMap<>();
		
		public City(int n) {
			city = n;
		}
		
		@Override
		public String toString() {
			return "City[n:"+city+" w:"+waitingTime+" edges:"+connections.size()+"]";
		}
	}
	
	public static class Connection implements Comparable<Connection> {
		private long waitingTime;
		private long departTime;
		private long arrivalTime;
		private City destination;
		
		public Connection(long waiting, long dpart, long arrival, City dest) {
			waitingTime = waiting;
			departTime = dpart;
			arrivalTime = arrival;
			destination =  dest;
		}
		
		public List<Connection> getDestinationConnections(long time) {
			return destination.connections.get(time);
		}
		
		@Override
		public int compareTo(Connection o) {
			return Long.compare(waitingTime, o.waitingTime);
		}
		
		
		@Override
		public String toString() {
			return "Connection[depart:"+departTime+" arrival:"+arrivalTime+" waiting:"+waitingTime+" dest:"+destination.city+"]";
		}
	}
}
