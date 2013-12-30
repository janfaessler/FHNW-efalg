package ch.fhnw.jf.uebung4;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ch.fhnw.jf.uebung4.elements.Line;
import ch.fhnw.jf.uebung4.elements.Point;
import ch.fhnw.jf.uebung4.elements.Rectangle;
import ch.fhnw.mv.convex_hull.JarvisMarch;

/**
 * Main class for this application. It is responsible for the GUI.
 * 
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 */
public class Application extends Canvas implements MouseListener {

	private static final long serialVersionUID = -3540681400690407797L;
	
	/**
	 * This variable saves all points on the screen
	 */
	private final List<Point> points  = new ArrayList<>();
	
	/**
	 * This variable saves all lines of the convex hull
	 */
	private List<Line> lines = new ArrayList<>();

	/**
	 * This variable saves all points of the convex hull
	 */
	private List<Point> hull = new ArrayList<>();
	
	/**
	 * This variable saves the smallest bounding rectangle
	 */
	private Rectangle rect;
	
	/**
	 * This is the main function that starts a frame with this class as canvas in it
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Uebung 4");
		frame.setTitle("Uebung 4");
		frame.setSize(500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		Canvas canvas = new Application();
		canvas.setBackground(Color.WHITE);
		frame.add(canvas, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	/**
	 * Constructor of the class
	 */
	public Application() {
		this.addMouseListener(this);
	}
	
	/**
	 * Paints the points, lines and rectangles
	 * @see java.awt.Canvas#paint()
	 */
	@Override
	public void paint(Graphics g){
		
		// draw lines of the convex hull
		g.setColor(Color.RED);
		for (Line l : lines) l.draw(g);
		
		// draw 
		g.setColor(Color.BLUE);
		if (rect != null) {
			rect.draw(g);
		}		
		
		for (Point p : points) {
			if (hull.contains(p)) g.setColor(Color.RED);
			else                  g.setColor(Color.BLACK);
			p.draw(g);
		}
	}

	/**
	 * Listen the mouse clicks
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		Point p = new Point(e.getPoint().x, e.getPoint().y);
		points.add(p);
		hull.add(p);
		hull = computeHull(hull);
		
		if (hull.size() > 0)  rect = new Rectangle(hull);
		
		lines.clear();
		for (int i = 0; i < hull.size(); i++) lines.add(new Line(hull.get(i), hull.get((i+1) % hull.size())));

		this.repaint();
	}
	
	/**
	 * Wrapps the computeHull function from Manfred Vogel's JarvisMarch class for collections
	 * @param points
	 * @return hullPoints
	 */
	public List<Point> computeHull(List<Point> points) {
		
		// prepare Point array
		ch.fhnw.mv.convex_hull.Point[] tmp = new Point[points.size()];
		for (int i = 0; i < points.size(); i++) 
			tmp[i] = points.get(i);
		
		// calculate
		int h = JarvisMarch.computeHull(tmp);
		
		List<Point> result = new ArrayList<>();
		for (int i = 0; i < h; i++) result.add(new Point(tmp[i]));
		return result;
	}
	
	@Override public void mouseClicked(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }
}
