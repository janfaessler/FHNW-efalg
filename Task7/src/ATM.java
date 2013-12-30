import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


public class ATM {
	
	static int N = 0;
	static int low = 0;
	static int high = 0;

	public static void main(String[] args) throws FileNotFoundException {
	    Scanner in = new Scanner(new File("atm.in"));
	    PrintWriter out = new PrintWriter("atm.out");
	    
	    N = in.nextInt();
	    low = in.nextInt();
	    high = in.nextInt() + 1;
	    
	    boolean[] results = new boolean[high+1];
	    ArrayList<Boolean> res = new ArrayList<Boolean>();
	    results[0] = true;
	    
	    int max = 1;
	    for (int i = 0; i < N; i++) {
	    	int count = in.nextInt();
	    	int value = in.nextInt();
	    	
	    	for (int j = 0; j < count; j++) {
	    		if (max <= high)
		    		for (int k = max; k >= 0; k--) {
		    			int nVal = k+value;
		    			if (results[k] && nVal <= high) {
		    				results[nVal] = true;
		    				max = nVal > max ? nVal : max;
		    			}
		    				
		    		}
	    		
	    	}
	    }

	    for (int i = low; i < high; i++) {
	    	if (results[i]) {
 	    		out.println(i);
	    	}
	    }
	    
	    
	    out.close();
	}


}
