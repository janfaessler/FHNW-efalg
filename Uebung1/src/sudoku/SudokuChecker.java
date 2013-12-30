package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class SudokuChecker {
	
	public static void main(String[] args) {

//		int[][] game = { { 0, 0, 5, 3, 0, 0, 0, 0, 0 },
//						 { 8, 0, 0, 0, 0, 0, 0, 2, 0 }, 
//						 { 0, 7, 0, 0, 1, 0, 5, 0, 0 },
//						 { 4, 0, 0, 0, 0, 5, 3, 0, 0 }, 
//						 { 0, 1, 0, 0, 7, 0, 0, 0, 6 },
//						 { 0, 0, 3, 2, 0, 0, 0, 8, 0 }, 
//						 { 0, 6, 0, 5, 0, 0, 0, 0, 9 },
//						 { 0, 0, 4, 0, 0, 0, 0, 3, 0 },
//						 { 0, 0, 0, 0, 0, 9, 7, 0, 0 } };
		
		int[][] game = { { 9, 0, 6, 0, 7, 0, 4, 0, 3 },
						 { 0, 0, 0, 4, 0, 0, 2, 0, 0 }, 
						 { 0, 7, 0, 0, 2, 3, 0, 1, 0 },
						 { 5, 0, 0, 0, 0, 0, 1, 0, 0 }, 
						 { 0, 4, 0, 2, 0, 8, 0, 6, 0 },
						 { 0, 0, 3, 0, 0, 0, 0, 0, 5 }, 
						 { 0, 3, 0, 7, 0, 0, 0, 5, 0 },
						 { 0, 0, 7, 0, 0, 5, 0, 0, 0 },
						 { 4, 0, 5, 0, 1, 0, 7, 0, 8 } };


		// initialize the sudoku
		SudokuChecker sc = new SudokuChecker(game);
		
		// save starting time
		long time = System.currentTimeMillis();
		
		// calculate the solutions
		sc.calculate();
		
		// print calculation time
		System.out.println("Calculation Time: " + (System.currentTimeMillis() - time) + "ms");
		
		// print the solutions
		sc.printSolutions();
		
		// print the solution count
		System.out.println();
		System.out.println("Anzahl Lösungen: "+ sc.getSolutionCount());

	}

	/**
	 * represents the sudoku field
	 */
	private int[][] field;
	
	/**
	 * counter of possible solutions
	 */
	private int counter = 0;
	
	/**
	 * the dimention of the sudoku field
	 */
	private final int dim;
	
	/**
	 * list of all posible solutions
	 */
	private ArrayList<String> solutions = new ArrayList<>();
	
	/**
	 * Constructor of the class
	 * @param sudoku field
	 * @throws IllegalArgumentException if the game has a wrong dimension
	 */
	public SudokuChecker(int[][] game) throws IllegalArgumentException {
		this.dim = game.length;
		this.field = game;
		if (game.length != game[0].length)  throw new IllegalArgumentException("wrong dimension of game field");
	}
	
	/**
	 * Calculates the amount of solutions
	 */
	public void calculate() {
		solve(0,0);
	}
	
	/**
	 * Returns the amount of solutions
	 * @return
	 */
	public int getSolutionCount() {
		return counter;
	}
	
	/**
	 * Prints the solutions to the console
	 */
	public void printSolutions() {
		Iterator<String> it = solutions.iterator();
		while (it.hasNext()) System.out.println(it.next());
	}
	
	/**
	 * Search a solution starting from a given point
	 * @param row
	 * @param col
	 */
	private void solve(int row, int col) {
		
		// if in the field isn't a zero go to the next field
		if (field[row][col] != 0)
			next(row, col);
		else {
			
			// loop the possible numbers
			for (int num = 1; num <= 9; ++num) {
				
				// check if the number is possible in the actual field
				if (checkRow(row, num) && checkCol(col, num) && checkGroup(row, col, num)) {
					
					// sets the number
					field[row][col] = num;
					
					// check the next field
					next(row, col);
				}
			}
			
			// if none of the possible numbers worked, set this back to zero
			field[row][col] = 0;
		}
		
	}
	
	/**
	 * Checks the next field and increment the counter if a solution is found
	 * @param row
	 * @param col
	 */
	private void next(int row, int col) {
		
		if (col < dim - 1) {
			
			// if we aren't at the end of the columns, check the next column in the row
			solve(row, col + 1);
			
		} else if (row < dim -1) {
			
			// if we aren't at the rows, check the next row
			solve(row + 1, 0);
			
		} else {               
			
			// otherwise we found a solution
			counter++;
			solutions.add(getSolution());
		}
		
	}
	
	/**
	 * Returns a sting of the actual solution
	 * @return solution string
	 */
	private String getSolution() {
		String solution = new String();
		for (int[] currentRow : field) solution += "\n" + Arrays.toString(currentRow);
		return solution;
	}
	
	/**
	 * Helper function to check the row of the sudoku field
	 * @param row
	 * @param num
	 * @return true if it is possible
	 */
	private boolean checkRow(int row, int num) {
		for (int c = 0; c < dim; c++)  // loop columns
			if (field[row][c] == num)  // check if num is in the row
				return false;
		return true;
	}
	
	/**
	 * Helper function to check the column of the sudoku field
	 * @param row
	 * @param num
	 * @return true if it is possible
	 */
	private boolean checkCol(int col, int num) {
		for (int r = 0; r < dim; r++) // loop rows
			if (field[r][col] == num) // check if num is in the column
				return false;
		return true;
	}

	/**
	 * Helper function to check the group of the sudoku field
	 * @param row
	 * @param num
	 * @return true if it is possible
	 */
	private boolean checkGroup(int row, int col, int num) {
		
		// the group is a 3x3 field  and we have to start on the top left
		row -= row % 3;
		col -= col % 3;
		
		for (int r = 0; r < 3; r++)     			 // loop rows
			for (int c = 0; c < 3; c++)              // loop column
				if (field[row + r][col + c] == num)  // check if num is in the group
					return false;
		return true;
	}
}
