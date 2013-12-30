import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Bus {

	private final int[][] lines;
	private boolean[][] knowledgeMap;

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(new File("bus.in"));
		int busCount = in.nextInt();
		int[][] busLines = new int[busCount][];
		for (int i = 0; i < busCount; ++i) {
			int stationCount = in.nextInt();
			busLines[i] = new int[stationCount];
			for (int j = 0; j < stationCount; ++j) busLines[i][j] = in.nextInt();
		}
		in.close();
		Bus bus = new Bus(busLines);
		
		int result = bus.calculate() - 1;
		PrintWriter out = new PrintWriter("bus.out");
		System.out.println("information is distributed after " + (result < 0 ? "fail" : result) + " stations");
		out.write(result < 0 ? "NEVER" : String.valueOf(result));
		out.close();
	}

	public Bus(int[][] lines) {
		this.lines = lines;
		knowledgeMap = new boolean[lines.length][lines.length];

		for (int i = 0; i < lines.length; ++i)
			for (int j = 0; j < lines.length; ++j)
				knowledgeMap[i][j] = (i == j);

	}

	public int calculate() {

		//int max = getMaxLoop();
		boolean solutionFound = false;
		long startTime = System.currentTimeMillis();
		int current = 0;
		//while (current <= max && !(solutionFound = solutionFound())) {
		while ((System.currentTimeMillis() - startTime) < 800 && !(solutionFound = solutionFound())) {
			for (int i = 0; i < lines.length; i++) {
				for (int j = i; j < lines.length; j++) {
					if (lines[i][current % lines[i].length] == lines[j][current % lines[j].length]) {
						for (int k = 0; k < lines.length; k++) {
							knowledgeMap[j][k] = knowledgeMap[i][k] = knowledgeMap[i][k] || knowledgeMap[j][k];
						}
					}
				}
			}
			current++;
			
		}
		
		return (solutionFound ? current : -1);
	}

	private boolean solutionFound() {
		for (int i = 0; i < lines.length; ++i) 
			for (int j = 0; j < lines.length; ++j)
				if (!knowledgeMap[i][j]) return false;
		return true;
	}

	private int getMaxLoop() {
		int max = 1;
		for (int i = 0; i < lines.length; i++)
			max = lcm(lines[i].length, max);
		return max * (lines.length * 3 / 2);
	}
	
	private int lcm(int a, int b) {
	    return (a/gcd(a, b)) * b;
	}
	
	private int gcd(int a, int b) {
	    while (b != 0) {
	        int t = b;
	        b = a % b;
	        a = t;
	    }
	    return a;
	}
}
