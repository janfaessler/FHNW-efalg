package ch.fhnw.efalg.schwammberger.jonas.uebung1.springerpfad;

import java.awt.Point;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * This board keeps track of the possibilities
 * 
 * This class is meant to be used with a backtrack algorithm.
 * @author Jon
 *
 */
public class Board {
	private int[][] b;	//board with possibilities
	private int[] springer = {+1,-2, +2,-1, +2,+1, +1,+2, -1,+2, -2,+1, -2,-1, -1,-2}; //jump fields for the springer
	
	/**
	 * 
	 * @param n > 3
	 */
	public Board(int n) {
		if(n < 3)
			throw new IllegalArgumentException("n should be greater than 3");
		
		this.b = new int[n][n];
		
		
		
		initBoard();
	}
	
	/**
	 * init b in this form:
	 * 
	 * 	2 3 4 .. 4 3 2
	 *  3 4 6 .. 6 4 3
	 *  4 6 8 .. 8 4 6
	 *  . . . .. . . .
	 *  . . . .. . . .
	 *  4 6 8 .. 8 4 6
	 *  3 4 6 .. 6 4 3
	 *  2 3 4 .. 4 3 2
	 */
	private void initBoard() {
		int last = b.length-1;
		
		for(int i = 0; i < b.length;i++) {
			b[i][0] = 4;
			b[i][last]=4;
			b[0][i] = 4;
			b[last][i] = 4;
		}
		
		for(int i = 1; i < last;i++) {
			b[i][1] = 6;
			b[i][last-1]=6;
			b[1][i] = 6;
			b[last-1][i] = 6;
		}
		
		for(int i = 2; i < last -1;i++) {
			for(int j = 2; j < last-1;j++) 
				b[i][j] = 8;
		}
		
		//set the corner values
		b[1][1] = 4;
		b[1][last-1] = 4;
		b[last-1][1] = 4;
		b[last-1][last-1] = 4;
		
		b[0][1] = 3;b[1][0] =3;
		b[0][last-1] = 3; b[last-1][0] =3;
		b[1][last] = 3; b[last][1] = 3; 
		b[last][last-1] = 3; b[last-1][last] = 3;
		
		b[0][0] = 2;
		b[last][last] = 2;
		b[0][last] = 2;
		b[last][0] = 2;
	}
	
	/**
	 * update fields around the new/ old position of the springer
	 * 
	 * if doBacktrack = false:
	 * 	invert the value of the new position, indicating that this field was already visited
	 * 	substract 1 from all possible jump points
	 * 	
	 * doBacktrack = true: (roll back the state changes) 
	 *	invert the value of the old position, the field now contains the same number as before
	 *	add 1 to all possible jump points
	 *
	 * @param x
	 * @param y
	 * @param doBacktrack
	 */
	private void updateBoard(int x, int y, boolean doBacktrack) {
		b[x][y] = (-1)*b[x][y];
		
		for(int i = 0; i < springer.length;i+=2) {
			int newX = x+ springer[i]; //x position of next jump point
			int newY = y+ springer[i+1];
			
			//check if springer can jump on this position
			if(fieldAvailable(newX,newY)) {
				b[newX][newY] = doBacktrack ? b[newX][newY] +1 :b[newX][newY] -1; 
			}
		}
	}
	
	/**
	 * check if current xy coordinates are inside the board and the possiblity value is higher or equal zero.
	 * @param x xpos
	 * @param y ypos
	 * @return true if current field is available
	 */
	private boolean fieldAvailable(int x, int y) {
		boolean answer = false;
		
		if(x >= 0 && x < b.length && y>= 0 && y < b.length)
		{
			if(b[x][y] >= 0)
				answer = true;
		}
		
		return answer;
	}
	
	/**
	 * Backtrack from state performed by getGreedy()
	 * @param x
	 * @param y
	 */
	public void backTrack(Field f) {
		this.updateBoard(f.x, f.y, true);
	}
	
	/**
	 * get an iterator that returns the next best field according to the Warnsdorf heuristic.
	 * This method changes the state of the Board! Call backTrack()
	 * @param number
	 */
	public Iterator<Field> getGreedy(Field f) {
		PriorityQueue<Field> greedy = new PriorityQueue<>(8);
		
		this.updateBoard(f.x, f.y, false);
		
		//add next fields
		for(int i = 0; i < springer.length;i+=2)
		{
			int newX = f.x+ springer[i]; //x position of next jump point
			int newY = f.y+ springer[i+1];
			
			if(fieldAvailable(newX,newY)) 
				greedy.add(new Field(newX,newY,b[newX][newY]));
		}
		
		/* if greedy contains 0, then this should be the only available jump point. 
		 * if it is the last field not visited, everything is fine. If not then we should backtrack. 
		 * Otherwise we would keep on searching for a solution that isn't there.
		 */
		Field tmp = greedy.peek();
		if(tmp != null && tmp.possibilities == 0)
		{
			greedy.clear();
			greedy.add(tmp);
		}
		
		return greedy.iterator();
	}

	/**
	 * Display board for debugging purposes
	 */
	public void display() {
		for(int i = 0; i < b.length;i++) {
			StringBuilder s = new StringBuilder("");
			for(int j = 0; j< b.length;j++) {
				s.append(b[i][j] + " ");
			}
			
			System.out.println(s);
		}
	}
	
	/**
	 * 
	 * @return Maximum number of steps possible or dimension of the board
	 */
	public int getMaxStep() {return this.b.length;}
	
	public Field getField(int x,int y)
	{
		return new Field(x,y,b[x][y]);
	}
	
	/**
	 * Needed so the caller can easily use a priorityqueue containing the next field according to the Warnsdorf heuristic.
	 * @author Jon
	 */
	public class Field implements Comparable<Field>
	{
		int x;
		int y;
		int possibilities;
		
		public Field(int x,int y,int poss) {this.x = x;this.y = y;this.possibilities = poss;}
		
		@Override
		public int compareTo(Field o) {
			return Integer.compare(possibilities, o.possibilities);
		}
		
	}
}
