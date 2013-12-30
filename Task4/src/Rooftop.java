import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Rooftop {

	private static int[][] points2;
	private static int maxX = Integer.MIN_VALUE;
	private static int minX = Integer.MAX_VALUE;
	private static int maxY = Integer.MIN_VALUE;
	private static int minY = Integer.MAX_VALUE;
	private static boolean[][] form;

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(new File("rooftop.in"));
		int size = in.nextInt();
		
		points2 = new int[size][2];

		for (int i = 0; i < size; i++) {
			int x = in.nextInt();
			int y = in.nextInt();
			if (x < minX) minX = x;
			if (x > maxX) maxX = x;
			if (y < minY) minY = y;
			if (y > maxY) maxY = y;
			points2[i][0] = x;
			points2[i][1] = y;
		}
		minX = Math.abs(minX);
		maxX = Math.abs(maxX);
		minY = Math.abs(minY);
		maxY = Math.abs(maxY);
		
		form = new boolean[maxX + minX][maxY + minY];

		buildForm();
		double result = calculateDemsSquares() / 2;
		
		PrintWriter out = new PrintWriter("rooftop.out");
		out.println(String.format("%.1f", result));
		out.close();

	}

	public static double calculateDemsSquares() {
		double result = 0;
		for (int x = 0; x < form.length; x++) {
			for (int y = 0; y < form[0].length; y++) {
				if (form[x][y]) {
					int size = 0;
					while (checkDiagonal(x, y, size + 1)) size++;
					size++;
					if (size > result) result = size;
					
					// speed up
					if (form.length - x < result)    return result;
					if (form[0].length - y < result) y = form[0].length;
				}
			}
		}
		return result;
	}

	public static boolean checkDiagonal(int x, int y, int size) {
		
		try {
			
			for (int i = x; i <= size + x; i++)
				if (!form[i][y + size])
					return false;

			for (int i = y; i < size + y; i++)
				if (!form[x + size][i])
					return false;
			
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}

		return true;
	}

	public static void buildForm() {
		for (int i = 0; i < form.length; i++) {
			for (int j = 0; j < form[0].length; j++) {
				form[i][j] = contains(i - minX, j - minY);
			}
		}
	}

	public static boolean contains(int x, int y) {

		boolean result = false;
		for (int i = 0, j = points2.length - 1; i < points2.length; j = i++) {
			if ((points2[i][1] > y) != (points2[j][1] > y) && (x < (points2[j][0] - points2[i][0]) * (y - points2[i][1]) / (points2[j][1] - points2[i][1]) + points2[i][0])) {
				result = !result;
			}
		}
		return result;
	}

}
