
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public final class WinterSchrott {
	static ArrayList<Connection>[] connections;
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {

		Scanner s = new Scanner(new File("winter.in"));
		
		int cityCount = s.nextInt();
		int connCount = s.nextInt();
		int avarageConnections = connCount / cityCount;
		
		connections = new ArrayList[cityCount];
		for (int i = 0; i < cityCount; i++) {
			connections[i] = new ArrayList<>(avarageConnections);
		}
		
		int city, dest;
		long depart, arrival;
		for (int i = 0; i < connCount; ++i) {
			depart = s.nextLong(); // depart
			city = s.nextInt()-1; // city
			arrival = s.nextLong(); // arrival
			dest = s.nextInt()-1; // destination
			
			connections[city].add(new Connection(depart, arrival, dest));
		}

		int resultWaitingTime = 0;
		PriorityQueue<Connection> pq = new PriorityQueue<>();
		pq.add(new Connection(0, 0, 0));
		while(!pq.isEmpty()) {
			Connection current = pq.remove();
		
			if (current.destination ==  cityCount-1) {
				resultWaitingTime = current.waitingTime;
				break;
			}
			
			for (int i = 0; i < connections[current.destination].size(); i++) {
				Connection con = connections[current.destination].get(i);
				if (con.departTime >= current.arrivalTime) {
					con.waitingTime =  current.waitingTime + (int) (con.departTime - current.arrivalTime);
					pq.add(con);
				}
			}
		}
		
		
		//System.out.println(resultWaitingTime);
		PrintWriter out = new PrintWriter("winter.out");
		out.println(resultWaitingTime);
		out.close();
	}

	
	public static class Connection implements Comparable<Connection> {
		private int waitingTime;
		private long departTime;
		private long arrivalTime;
		private int destination;
		
		public Connection(long depart, long arrival, int dest) {
			departTime = depart;
			arrivalTime = arrival;
			destination =  dest;
		}
		
		@Override
		public int compareTo(Connection o) {
			return Integer.compare(waitingTime, o.waitingTime);
		}

	}
}
