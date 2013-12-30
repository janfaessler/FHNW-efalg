import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Nitrogen {
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		Scanner in = new Scanner(new File("nitrogen.in"));
		PrintWriter out = new PrintWriter("nitrogen.out");
		int n = in.nextInt();
		int rate = in.nextInt();
		
		int bestDay = 0;
		double bestAmount = 0;
		double[] amounts = new double[n];		
		
		for (int i = 0; i < n; i++) {
			amounts[i] = in.nextInt();
		}
		
		for (int i = 0; i < n; i++) {
			double sum = 0;
			for (int j = i>rate ? i - rate : 0; j <= i; j++) {
				double currentDecay = amounts[j] / rate * (i - j);
				sum += (currentDecay < amounts[j]) ? amounts[j] - currentDecay : 0;
			}

			if (sum > bestAmount) {
				bestAmount = sum;
				bestDay = i;
			}				
		}
		bestDay++;
		
		out.println(bestDay);
		out.close();
		System.out.println("Time: "+ (System.currentTimeMillis() - startTime) +"ms startday: " + bestDay + " amount: "+bestAmount);
	}
}
