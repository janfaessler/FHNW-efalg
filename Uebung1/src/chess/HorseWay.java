package chess;

import java.util.LinkedList;
import java.util.PriorityQueue;

public class HorseWay {
	
	public static void main(String[] args) {
		
		// Initialize the chess field
		HorseWay hw = new HorseWay(8);
		
		try {
			// save starting time for calculations
			long time = System.nanoTime();
			
			// find a way from a starting point
			LinkedList<Point> way = hw.findWay();
			
			// prints the way to the console
			System.out.println(way.size() + " : " + way);
			
			// prints the calculation time
			System.out.println("Time: "+(System.nanoTime()-time)+"ns");
			
		} catch (Exception e) { 
			
			// if there is no way for this field print a message to the console
			System.out.println("kein Weg gefunden");
			
		}
	}
	
	/**
	 * Helds the size of the chess field
	 */
	private final int fieldSize;
	
	/**
	 * Represents the chess field
	 */
	private int[][] field;

	/**
	 * Constructor of the HorseWay class<br />
	 * Initialize and prepares the chess field
	 * @param fieldSize
	 */
	public HorseWay(int fieldSize) {
		this.fieldSize = fieldSize;
		field = new int[fieldSize][fieldSize];
		prepareField();
	}

	/**
	 * Prepares the chess field. Fill all field with the amount of the
	 * possible sources form which the horse could come from
	 */
	private void prepareField() {
		for (int r = 0; r < fieldSize; r++)      // loop rows
			for (int c = 0; c < fieldSize; c++)  // loop columns
				field[r][c] = getPossibleSourcesCount(c, r);
		
	}
	
	/**
	 * Returns the amount of possible sources a horse can come from
	 * @param column
	 * @param row
	 * @return amount of possible sources
	 */
	private int getPossibleSourcesCount(int c, int r) {
		int count = 0;
		
		// check above
		if (r > 0 			&& c > 1            && field[r-1][c-2] >= 0) count++;
		if (r > 1 			&& c > 0            && field[r-2][c-1] >= 0) count++;
		if (r > 0 			&& c < fieldSize-2  && field[r-1][c+2] >= 0) count++;
		if (r > 1 			&& c < fieldSize-1  && field[r-2][c+1] >= 0) count++;
		
		// check below
		if (r < fieldSize-1 && c > 1            && field[r+1][c-2] >= 0) count++;
		if (r < fieldSize-2 && c > 0            && field[r+2][c-1] >= 0) count++;
		if (r < fieldSize-1 && c < fieldSize-2  && field[r+1][c+2] >= 0) count++;
		if (r < fieldSize-2 && c < fieldSize-1  && field[r+2][c+1] >= 0) count++;
		
		return count;
	}
	
	/**
	 * Returns a way of the horse if there exists one
	 * @return way of the horse
	 * @throws Exception if now way could be found
	 */
	private LinkedList<Point> findWay() throws Exception {
		LinkedList<Point> result = findAWay(fieldSize*fieldSize, 0, 0);
		if (result == null) throw new Exception("no way found");
		return result;
	}
	
	/**
	 * Recursive helper function to find the horse way with a amount of open points in the chess field
	 * @param amount of the open points
	 * @param column to start with
	 * @param row to start with
	 * @return rest of the way
	 */
	private LinkedList<Point> findAWay(int openPoints, int c, int r) {

		// if there is just one open point we are at the finish
		if (openPoints == 1) {
			
			// create linked list to return
			LinkedList<Point> way = new LinkedList<>();
			
			// add the current point
			way.add(new Point(r,c,0));
			
			// return the list
			return way;
			
		} else {
			
			// get the possible points to move on sorted by a priority
			// additionally this function updates the field and decrement the amount of sources,
			// at the points that can be reached from this point
			PriorityQueue<Point> newPoints = getPossiblePoints(c,r);
			
			// set the current position to a negative value to mark as taken
			field[r][c] = -field[r][c];

			// check all possible points
			while (!newPoints.isEmpty()) {
				
				// take one point from the priority queue
				Point current = newPoints.poll();
				
				// get the rest of the list after the taken point
				LinkedList<Point> way = findAWay(openPoints - 1, current.getColumn(), current.getRow());
				
				// if there is no solution with this point the way is null
				if (way != null) {
					
					// if there is a solution add the current point to the list...
					way.addFirst(new Point(r,c,0));
					
					// .. and return the list
					return way;
				}
				
			}
			
			// if there is no solution increment the amount of sources again
			incrFields(c, r);
			
			// Give this point free to use later
			field[r][c] = -field[r][c];
		}
		
		// no solution possible at the moment from this point -> return null
		return null;
	}
	
	/**
	 * Returns a priority queue of possible points. The point that has the most less possible sources will get the highest priority.
	 * The function also updates the these points in the field by decrementing the possible sources with one.
	 * @param column
	 * @param row
	 * @return possible points
	 */
	private PriorityQueue<Point> getPossiblePoints(int c, int r) {
		PriorityQueue<Point> result = new PriorityQueue<>();
		
		// check above
		if (r > 0   		&& c > 1            && field[r-1][c-2] > 0) { result.add(new Point(r-1, c-2, field[r-1][c-2])); field[r-1][c-2] -= 1; }
		if (r > 1  			&& c > 0            && field[r-2][c-1] > 0) { result.add(new Point(r-2, c-1, field[r-2][c-1])); field[r-2][c-1] -= 1; }
		if (r > 0   		&& c < fieldSize-2  && field[r-1][c+2] > 0) { result.add(new Point(r-1, c+2, field[r-1][c+2])); field[r-1][c+2] -= 1; }
		if (r > 1   		&& c < fieldSize-1  && field[r-2][c+1] > 0) { result.add(new Point(r-2, c+1, field[r-2][c+1])); field[r-2][c+1] -= 1; }
		
		// check below
		if (r < fieldSize-1 && c > 1            && field[r+1][c-2] > 0) { result.add(new Point(r+1, c-2, field[r+1][c-2])); field[r+1][c-2] -= 1; }
		if (r < fieldSize-2 && c > 0            && field[r+2][c-1] > 0) { result.add(new Point(r+2, c-1, field[r+2][c-1])); field[r+2][c-1] -= 1; }
		if (r < fieldSize-1 && c < fieldSize-2  && field[r+1][c+2] > 0) { result.add(new Point(r+1, c+2, field[r+1][c+2])); field[r+1][c+2] -= 1; }
		if (r < fieldSize-2 && c < fieldSize-1  && field[r+2][c+1] > 0) { result.add(new Point(r+2, c+1, field[r+2][c+1])); field[r+2][c+1] -= 1; }
				
		return result;
	}

	/**
	 * As long as the getPossiblePoints function decrements the amount of sources, there must be a function to revert that.
	 * @param column
	 * @param row
	 */
	private void incrFields(int c, int r) {
		
		// check above
		if (r > 0   		&& c > 1                    && field[r-1][c-2] >= 0) { field[r-1][c-2] += 1; }
		if (r > 1   		&& c > 0                    && field[r-2][c-1] >= 0) { field[r-2][c-1] += 1; }
		if (r > 0   		&& c < fieldSize-2          && field[r-1][c+2] >= 0) { field[r-1][c+2] += 1; }
		if (r > 1   		&& c < fieldSize-1          && field[r-2][c+1] >= 0) { field[r-2][c+1] += 1; }
		
		// check below
		if (r < fieldSize-1 && c > 1            && field[r+1][c-2] >= 0) { field[r+1][c-2] += 1; }
		if (r < fieldSize-2 && c > 0            && field[r+2][c-1] >= 0) { field[r+2][c-1] += 1; }
		if (r < fieldSize-1 && c < fieldSize-2  && field[r+1][c+2] >= 0) { field[r+1][c+2] += 1; }
		if (r < fieldSize-2 && c < fieldSize-1  && field[r+2][c+1] >= 0) { field[r+2][c+1] += 1; }
	}
	

}
