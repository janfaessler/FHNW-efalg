import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class StreetParade {

	public static void main(String[] args) throws Exception {
		
		int[] incomingCars = new int[1000];
		
		Scanner in = new Scanner(new File("streetparade.in"));
		int a = in.nextInt();
		for (int i = 0; i < a; ++i) incomingCars[i] = in.nextInt();
		in.close();

		PrintWriter out = new PrintWriter("streetparade.out");
		out.write(hasSolution(a, incomingCars) ? "yes" : "no");
		out.close();
	}
	
	public static boolean hasSolution(int carCount, int[] list) {
		int car = 1;
		int i = 0;
		Stack<Integer> sideStreet = new Stack<Integer>();
		while (i < carCount) {			
			if (car != list[i]) {
				
				while (sideStreet.size() > 0 && sideStreet.peek() == car) {
					sideStreet.pop();
					car++;
				}
				
				if (car != list[i]) {
					
					if (sideStreet.size() > 0 && sideStreet.peek() < list[i]) 
						return false;
					else 
						sideStreet.push(list[i]);
					
				} else car++;

			} else car++;
			i++;
		}
		return true;
	}

}