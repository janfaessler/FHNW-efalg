
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class Stadion {

	private static int width = 0;
	private static int height = 0;
	private static int wStadion = 0;
	private static int hStadion = 0;
	private static int[][] sum = new int[2000][2000];

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("stadion.in"))));
	    String line;
	    line = reader.readLine();
	    String[] parts = line.split(" ");
	    wStadion = Integer.parseInt(parts[0]);
	    hStadion = Integer.parseInt(parts[1]);

	    line = reader.readLine();
	    parts = line.split(" ");
	    width = Integer.parseInt(parts[0]);
	    height = Integer.parseInt(parts[1]);

	    for (int r = 0; r < height; r++) {
	    	line = reader.readLine();
	    	int c = 0;
	    	for (int i = 0; i < line.length(); i++) {
	    		if (i+1 == line.length() || line.charAt(i+1) == ' '){
	    			sum[r][c] = sum(r, c++, line.charAt(i++)-48);
	    		}
	    		else {
	    			sum[r][c] = sum(r, c++, (line.charAt(i++)-48) * 10 + (line.charAt(i++)-48));
	    		}
	    	}
	    }
	    int best = Integer.MAX_VALUE;
	    int row = 0, column = 0;
	    for (int r = 0; r < height; r++) {
	    	for (int c = 0; c < width; c++) {
	    		int result = sumStadium(r, c);
	    		if (result >= 0 && result < best) {
	    			row = r;
	    			column = c;
	    			best = result;
	    		}
	    	}
	    }
	    System.out.println("row: "+row+" column: "+column+" time: "+best+"min");

		PrintWriter out = new PrintWriter("stadion.out");
		out.print(best);
		out.close();
	}

	private static int sum(int r, int c, int e) {
		return get(r, c - 1) + get(r - 1, c) - get(r - 1, c - 1) + e;
	}

	private static int get(int r, int c) {
		return (r >= 0 && c >= 0 && r < height && c < width) ? sum[r][c] : 0;
	}
	
	private static int sumStadium(int r, int c) {
		int endRow = r+hStadion-1;
		int endColumn = c+wStadion-1;
		if (endColumn >= width || endRow >= height) return -1;
		return get(endRow, endColumn) - get(r - 1, endColumn) - get(endRow, c - 1) + get(r - 1, c - 1);
	}
}
