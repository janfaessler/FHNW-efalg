package ch.fhnw.jf.uebung4.elements;
import java.awt.Graphics;

/**
 * This class represents a Point
 * 
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 */
public class Point extends ch.fhnw.mv.convex_hull.Point {

	/**
	 * constant variable with the size of the point
	 */
	private static final int SIZE = 7;

	/**
	 * Wrapper constructor for Point's of the super class
	 * @param p
	 */
	public Point(ch.fhnw.mv.convex_hull.Point p) {
		super(p.x, p.y);
	}

	/**
	 * Constructor of the class
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		super(x, y);
	}

	/**
	 * draws this point in a grafic context
	 * @param g
	 */
	public void draw(Graphics g) {
		g.fillOval((int) x - SIZE / 2, (int) y - SIZE / 2, (int) SIZE, (int) SIZE);
	}

	/**
	 * rotates this point by an angle
	 * @param angle
	 * @return newPoint
	 */
	public Point rotate(double angle) {
		return new Point(x * Math.cos(angle) - y * Math.sin(angle), 
				         x * Math.sin(angle) + y * Math.cos(angle));
	}
	
	/**
	 * Compares this Point with a object
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Point) {
			Point p = (Point) o;
			if (x != p.x) return false;
			if (y != p.y) return false;
			return true;
		}
		return false;
	}
}