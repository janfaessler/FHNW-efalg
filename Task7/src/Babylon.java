import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Babylon {
	
	static int[][] quaders;
	static int[] cache;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("babylon.in"));
	   
	    int n = in.nextInt(); 
	    cache   = new int[n*3];
	    quaders = new int[n * 3][3];
	    
	    Arrays.fill(cache, -1);
	    
	    int index = 0;
	    for (int i = 0; i < n; i++) {
	    	int a = in.nextInt();
	    	int b = in.nextInt();
	    	int c = in.nextInt();
	    	
	    	quaders[index  ][0] = Math.max(a, b);
	    	quaders[index  ][1] = Math.min(a, b);
	    	quaders[index++][2] = c;
	    	
	    	quaders[index  ][0] = Math.max(a, c);
	    	quaders[index  ][1] = Math.min(a, c);
	    	quaders[index++][2] = b;

	    	quaders[index  ][0] = Math.max(c, b);
	    	quaders[index  ][1] = Math.min(c, b);
	    	quaders[index++][2] = a;
	    }
	    in.close();
	    
	    int maxHeight = 0;
	    for (int i=0; i < quaders.length; i++) 
	    	maxHeight = Math.max(maxHeight, getMaxPossibleHeight(i) + quaders[i][2] + 1);

	    PrintWriter out = new PrintWriter("babylon.out");
	    out.println(maxHeight);
	    out.close();
	}
	
	public static int getMaxPossibleHeight(int currentBaseIndex) {
		if (cache[currentBaseIndex] >= 0)  return cache[currentBaseIndex];
		
		for (int j = 0; j < quaders.length; j++)
			if (quaders[currentBaseIndex][0] > quaders[j][0] && quaders[currentBaseIndex][1] > quaders[j][1])
				cache[currentBaseIndex] = Math.max(cache[currentBaseIndex], getMaxPossibleHeight(j) + quaders[j][2]);
		
		return cache[currentBaseIndex];
	}
}
