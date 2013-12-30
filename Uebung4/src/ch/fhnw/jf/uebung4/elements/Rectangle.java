package ch.fhnw.jf.uebung4.elements;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a rectangle and calculates the minimal
 * bounding rectangle around a list of points
 * 
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 */
public class Rectangle {

	/**
	 * Represents the top left point of the rectangle
	 */
	private Point tl;
	
	/**
	 * Represents the top right point of the rectangle
	 */
	private Point tr;
	
	/**
	 * Represents the bottom left point of the rectangle
	 */
	private Point bl;
	
	/**
	 * Represents the bottom right point of the rectangle
	 */
	private Point br;

	/**
	 * Constructor of a class
	 * @param minX
	 * @param minY
	 * @param maxX
	 * @param maxY
	 */
	public Rectangle(double minX, double minY, double maxX, double maxY) {
		tl = new Point(minX, maxY);
		tr = new Point(maxX, maxY);
		bl = new Point(minX, minY);
		br = new Point(maxX, minY);
	}

	/**
	 * Constructor of the class that creates the smallest rectangle around the given points.
	 * That works only if the points are sorted and on the convex hull
	 * @param points
	 */
	public Rectangle(final List<Point> points) {
		
		//copy the list
		List<Point> list = new ArrayList<>();
		for (Point p : points)
			list.add(new Point(p));

		double minArea = Double.MAX_VALUE;
		if (list.size() > 1) {

			double angle, minX, minY, maxX, maxY, area;
			List<Point> rotated;
			Point last = list.get(list.size() - 1);
			
			// loop over all lines of the convex hull
			for (Point current : list) {

				angle = Math.atan2(current.y - last.y, current.x - last.x);
				last = current;
				
				// rotate the points of the hull
				rotated = rotate(list, -angle);
				
				// find max and min values
				minX = Integer.MAX_VALUE;
				minY = Integer.MAX_VALUE;
				maxX = Integer.MIN_VALUE;
				maxY = Integer.MIN_VALUE;
				for (Point p : rotated) {
					minX = Math.min(p.x, minX);
					minY = Math.min(p.y, minY);
					maxX = Math.max(p.x, maxX);
					maxY = Math.max(p.y, maxY);
				}
				
				// check if tha area of the new rectangle is smaller
				area = (maxX - minX) * (maxY - minY);
				if (area < minArea) {
					minArea = area;
					tl = (new Point(minX, maxY)).rotate(angle);
					bl = (new Point(minX, minY)).rotate(angle);
					br = (new Point(maxX, minY)).rotate(angle);
					tr = (new Point(maxX, maxY)).rotate(angle);
				}
			}
			
		} else {
			
			tl = new Point((int) list.get(0).x, (int) list.get(0).y);
			bl = new Point((int) list.get(0).x, (int) list.get(0).y);
			br = new Point((int) list.get(0).x, (int) list.get(0).y);
			tr = new Point((int) list.get(0).x, (int) list.get(0).y);
		}

	}

	/**
	 * rotates the points in the list by an angle
	 * @param points
	 * @param angle
	 * @return newList
	 */
	public List<Point> rotate(List<Point> points, double angle) {
		List<Point> result = new ArrayList<>();
		for (Point p : points)
			result.add(p.rotate(angle));
		return result;
	}

	/**
	 * draws the rectangle in a grafic context
	 * @param g
	 */
	public void draw(Graphics g) {
		for (Line l : getLines())
			l.draw(g);
	}

	/**
	 * returns the lines of the rectangle
	 * @return lines
	 */
	private List<Line> getLines() {
		List<Line> lines = new ArrayList<>();
		lines.add(new Line(tl, bl));
		lines.add(new Line(bl, br));
		lines.add(new Line(br, tr));
		lines.add(new Line(tr, tl));
		return lines;
	}
}
