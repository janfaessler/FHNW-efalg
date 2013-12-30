import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ships<Ship> {
	
	private static boolean[][] field = new boolean[10][10];
	private static int[][] probability = new int[10][10];
	private static int[][] fails;
	private static int maxX = 0;
	private static int maxY = 0;
	private static int solutionCounter = 0;
	
	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(new File("ships.in"));
		PrintWriter out = new PrintWriter("ships.out");

		
		maxX = in.nextInt();
		maxY = in.nextInt();
		int ships = in.nextInt();
		
		fails = new int[in.nextInt()][2];
		for (int i = 0; i < fails.length; i++) {
			fails[i][0] = in.nextInt()-1;
			fails[i][1] = in.nextInt()-1;
		}
		in.close();
		findShips(ships, 0, 0);
		System.out.println(getBestResult());
		out.write(getBestResult());
		out.close();
	}
	
	public static void findShips(int shipCount, int x, int y) {

		for (int yi = y; yi < maxY; yi++) {
			for (int xi = (y == yi ? x : 0); xi < maxX; xi++) {
				if (isNotTried(xi, yi)) {
					if (xi+3<maxX && !field[yi][xi] && !field[yi][xi+1] && !field[yi][xi+2] && !field[yi][xi+3]) { 
						if (isNotTried(xi+1, yi) && isNotTried(xi+2, yi) && isNotTried(xi+3, yi)) {
							field[yi][xi] = true;
							field[yi][xi+1] = true;
							field[yi][xi+2] = true;
							field[yi][xi+3] = true;
							next(shipCount-1, xi, yi);
							field[yi][xi] = false;
							field[yi][xi+1] = false;
							field[yi][xi+2] = false;
							field[yi][xi+3] = false;
						}
					}
					if (yi+3<maxY && !field[yi][xi] && !field[yi+1][xi] && !field[yi+2][xi] && !field[yi+3][xi]) {
						if (isNotTried(xi, yi+1) && isNotTried(xi, yi+2) && isNotTried(xi, yi+3)) {
							field[yi][xi] = true;
							field[yi+1][xi] = true;
							field[yi+2][xi] = true;
							field[yi+3][xi] = true;
							next(shipCount-1, xi, yi);
							field[yi][xi] = false;
							field[yi+1][xi] = false;
							field[yi+2][xi] = false;
							field[yi+3][xi] = false;
						}
					}
				}
			}
		}
		
	}
	
	private static boolean isNotTried(int x, int y) {
		for (int i =0; i < fails.length; i++) 
			if (fails[i][0] == x && fails[i][1] == y)  return false;
		return true;
	}
	
	private static void next(int shipCount, int x, int y) {
		if (shipCount==0) {
			for (int yi = 0; yi < maxY; yi++)
				for (int xi = 0; xi < maxX; xi++) 
					if (field[yi][xi]) probability[yi][xi] += 1;
			solutionCounter++;
		}
		else if (x+1 < maxX) findShips(shipCount,x+1,y);
		else if (y+1 < maxY) findShips(shipCount,0, y+1);
		
	}
	
	private static String getBestResult() {
		int bestX=0;
		int bestY=0;
		int bestValue= 0;
		for (int yi = 0; yi < maxY; yi++) {
			for (int xi = 0; xi < maxX; xi++) {
				if (probability[yi][xi] > bestValue) {
					bestValue = probability[yi][xi];
					bestX = xi;
					bestY = yi;
				}
			}
		}
		return (bestX+1) + " " + (bestY+1) + " " + String.format("%.2f",(100*(double)bestValue/solutionCounter))+"%";
	}
}
