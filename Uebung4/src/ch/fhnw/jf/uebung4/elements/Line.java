package ch.fhnw.jf.uebung4.elements;
import java.awt.Graphics;

/**
 * This class represents a Line
 * 
 * @author Jan Faessler <jan.faessler@students.fhnw.ch>
 */
public class Line {

	/**
	 * This saves the start point of the line
	 */
	private final Point start;
	
	/**
	 * This saves the end point of the line
	 */
    private final Point end;
    
    /**
     * Constructor of the class
     * @param start
     * @param end
     */
    public Line(Point s, Point e) {
        this.start = s;
        this.end = e;
    }

    /**
     * draws this line in a draw context
     * @param g
     */
    public void draw(Graphics g) {
         g.drawLine((int)start.x, (int)start.y, (int)end.x, (int)end.y);
    }
}