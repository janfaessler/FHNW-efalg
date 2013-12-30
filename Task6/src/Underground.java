import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Underground {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in=new Scanner(new File("underground.in"));
	    PrintWriter out=new PrintWriter("underground.out");
	    
	    int homes = in.nextInt();
	    int tunnels = in.nextInt();
	    int queries = in.nextInt();
	    
	    ArrayList<ArrayList<Integer>> conns = new ArrayList<>();
	    
	    for (int i = 0; i < tunnels; i++) {
	    	int start = in.nextInt();
	    	int end   = in.nextInt();
	    	int existsIn = 0;
	    	boolean exists = false;
	    	
	    	for (int index = 0; index < conns.size(); index++) {
	    		List<Integer> options = conns.get(index);
		    	boolean containsStart = options.contains(start);
		    	boolean containsEnd   = options.contains(end);
		    		
	    		if (containsStart || containsEnd) {
	    			if (exists) {
	    				// merge
	    				conns.get(existsIn).addAll(options);
	    				conns.remove(index--);
	    				//conns.set(index, null);
	    				
	    			} else {
	    				exists = true;
	    				existsIn = index;
	    				if (!containsStart) options.add(start);
	    	    		if (!containsEnd) options.add(end);
	    			}
		    		
	    		}
	    	}
	    	
	    	if (!exists) {
	    		ArrayList<Integer> list = new ArrayList<>();
	    		list.add(start);
	    		list.add(end);
	    		conns.add(list);
	    	}
	    }
	    
	    for (int i = 0; i < queries; i++) {
	    	int start = in.nextInt();
	    	int end   = in.nextInt();
	    	boolean found = false;
	    	for (ArrayList<Integer> options : conns) {
	    		if (options.contains(start) && options.contains(end)) {
	    			found = true;
	    			break;
	    		}
	    		
	    	}
	    	System.out.println(found ? "Y" : "N");
	    	out.println(found ? "Y" : "N");
	    }
	    
	    out.close();
	}

}
