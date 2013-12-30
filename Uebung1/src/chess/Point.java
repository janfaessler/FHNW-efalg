package chess;

/**
 * Class Point<br />
 * Represents a point in a chess field.
 * 
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 */
public class Point implements Comparable<Point> {
	
	/**
	 * row of the point
	 */
	private final int row;
	
	/**
	 * column of the point
	 */
	private final int column;
	
	/**
	 * amount of possible sources
	 */
	private final int sources;
	
	/**
	 * Constructor of the Point<br />
	 * Initialize and sets the attributes of a point
	 * 
	 * @param row of the point
	 * @param column of the point
	 * @param amount of possible sources
	 */
	public Point(int r, int c, int s) {
		row = r;
		column = c;
		sources = s;
	}
	
	/**
	 * Returns the row of the point
	 * @return row
	 */
	public int getRow() { return row; }
	
	/**
	 * Returns the column of the point
	 * @return column
	 */
	public int getColumn() { return column; }
	
	/**
	 * Returns the amount of possible sources
	 * @return amount
	 */
	public int getSources() { return sources; }
	
	@Override public String toString() { return "("+column+"/"+row+")"; }
	
	@Override public int compareTo(Point o) {
		if (o.sources > sources) return -1;
		else if (o.sources < sources) return 1;
		return 0;
	}
}