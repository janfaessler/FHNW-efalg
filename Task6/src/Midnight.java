import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Midnight {

	public static void main(String[] args) throws IOException {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("midnight.in"))));
	    PrintWriter out = new PrintWriter("midnight.out");
	    
	    String[] parts = reader.readLine().split(" ");
	    int width = Integer.parseInt(parts[0]);
	    int height = Integer.parseInt(parts[1]);
	    int maxDoors = Integer.parseInt(parts[2]);
	    
	    boolean[] walls = new boolean[width * height];
	    @SuppressWarnings("unchecked")
		ArrayList<Field>[] graph = new ArrayList[width * height];
	    int start = 0, end = 0, index = 0;
	    
	    for (int i = 0; i < height; i++) {
	    	String line = reader.readLine();
	    	for (int j = 0; j < width; j++) {
	    		
	    		char inputChar = line.charAt(j);
	    		
	            graph[index] = new ArrayList<Field>();
	    		
	    		switch(inputChar) {
					case '.':
						walls[index] = false;
						break;
					case '#':
						walls[index] = true;
						break;
					case 'B':
						start = index;
						break;
					case 'K':
						end = index;
						break;
	    		}
	    		
	            int oben = index - width;
	            if (oben >= 0) {
	            	graph[oben].add(new Field(oben, index, walls[index]));
 					graph[index].add(new Field(index, oben, walls[oben]));
	            }

	            int links = index - 1;
	            if (links >= 0 && (links / width) == (index / width)) {
	            	graph[links].add(new Field(links, index, walls[index]));
	            	graph[index].add(new Field(index, links, walls[links]));
	            }
	            index++;
	    	}
	    }
	    
	    
	    PriorityQueue<Node> pq = new PriorityQueue<Node>();
	    pq.add(new Node(start, 0, 0));
	    
	    boolean[][] visited = new boolean[graph.length][maxDoors+1];
	    
	    int finalCost = 0;
	    
	    while (!pq.isEmpty()) {
	    	Node current = pq.remove();
	    	
	    	if (current.pos == end) {
	    		finalCost = current.cost;
	    		break;
	    	}
	    	
	    	if (visited[current.pos][current.wallCounter]) continue;
	    	visited[current.pos][current.wallCounter] = true;
	    	
	    	for (Field field : graph[current.pos]) {
	    		int newWallCost = current.wallCounter + (field.wall ? 1 : 0);
	    		if (newWallCost <= maxDoors && !visited[field.to][newWallCost]) {
	    			pq.add(new Node(field.to, newWallCost, current.cost + 1));  
	    		}
	    	}
	    }
	    System.out.println(finalCost);
	    out.println(finalCost);
	    out.close();
	}
	
	public static class Field {
	
		public final int from;
		public final int to;
		public boolean wall;
		
		private Field(int start, int end, boolean needsWall) {
			from = start;
			to = end;
			wall = needsWall;
		}
	}
	  
	public static class Node implements Comparable<Node>{

		private final int pos;
	    private final int wallCounter;
	    public int cost;

	    public Node(int to, int walls, int costs) {
	    	pos = to;
	    	wallCounter = walls;
	    	cost = costs;
	    	
	    }

		@Override
		public int compareTo(Node o) {
			return Integer.compare(cost, o.cost);
		}
	}
}
