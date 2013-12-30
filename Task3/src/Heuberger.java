
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class Heuberger {
	static double pi2 = Math.PI * 2;
	static Point center;
	static ArrayList<Point> sortedPoints; // only one of the lines pair
	static Stack<Line> demLines = new Stack<>();

	public static void read() throws Exception {
		Scanner in = new Scanner(new File("heuberger.in"));

		center = new Point(in.nextInt(), in.nextInt());
		int size = in.nextInt();
		sortedPoints = new ArrayList<>(size);

		for (int i = 0; i < size; i += 2) {
			Point p1 = new Point(in.nextInt(), in.nextInt());
			Point p2 = new Point(in.nextInt(), in.nextInt());
			Line l;
			sortedPoints.add(p1);
			l = new Line(p1, p2);
			p1.line = l;
			p2.line = l;
			// l.setObstacle();
		}

		Collections.sort(sortedPoints);
	}

	public static void main(String[] args) throws Exception {
		center = new Point(0, 0);

		ArrayList<Point> bäm = new ArrayList<>();

		Point p1 = new Point(0, 3);
		Point p2 = new Point(0, 4);

		bäm.add(new Point(-3, 2));
		bäm.add(p2);
		bäm.add(p1);
		bäm.add(new Point(-3, -2));
		bäm.add(new Point(3, 1));
		bäm.add(new Point(3, 2));
		// bäm.add(new Point(3, -4));

		/*
		 * 
		 */

		Collections.sort(bäm);
		

		for (Point p : bäm) {
			System.out.println(p.x + " " + p.y);
		}
		// System.out.println(p1.compareTo(p2));
		// System.out.println(Integer.compare(0, 1));
		read();
		check();
		
		HashSet<Line> visible = new HashSet<>();

		Iterator<Line> it = visible.iterator();
		while (it.hasNext()) {
			Line l = it.next();
			System.out.println(l.startPoint.x + " " + l.startPoint.y + " "
					+ l.endPoint.x + " " + l.endPoint.y);
		}
		PrintWriter out = new PrintWriter("heuberger.out");
		out.close();

	}

	public static void check() {
		for (int i = 0; i < sortedPoints.size(); i++) {
			Point currentPoint = sortedPoints.get(i);
			Line currentLine = currentPoint.line;

			// TODO: zähl öb d'Linie wo grad isch muess zu de gsehene Linie
			// hinzuegfüegt werde muess
			if (currentLine.startPoint == currentPoint) {
				demLines.add(currentLine);
			} else {
				boolean wasRemoved = demLines.remove(currentLine);
				if (!wasRemoved) {
					sortedPoints.add(currentPoint);
				}
			}
		}

	}

	public static class LineStack extends Stack<Line> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean add(Line line) {
			for (int i = 0; i < this.size(); i++) {
				Line currentLine = this.get(i);
				if (currentLine.mDist(center, line.startPoint) < line.mDist(
						center, line.startPoint)) {
					this.add(i, line);
					return true;
				}
			}
			return false;
		}

	}

	public static class Point implements Comparable<Point> {
		int x;
		int y;
		Line line;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int cross(Point p) {
			return x * p.y - p.x * y;
		}

		public int mDist() {
			return Math.abs(x) + Math.abs(y);
		}

		@Override
		public int compareTo(Point o) {
			Point v1 = new Point(o.x - center.x, o.y - center.y);
			Point v2 = new Point(x - center.x, y - center.y);

			double alpha = Math.atan2(v1.cross(v2), v1.x * v2.x + v1.y * v2.y);

			if (alpha < 0.0) {
				return -1;
			} else if (alpha > 0.0) {
				return 1;
			} else {
				int mDist1 = v1.mDist();
				int mDist2 = v2.mDist();

				if (mDist1 == mDist2) {
					return 0;
				} else {
					return mDist1 < mDist2 ? 1 : -1;
				}
			}

			// return Integer.compare(x, o.x);
		}
	}

	public static class Line {
		Point startPoint;
		Point endPoint;

		public Line(Point p1, Point p2) {
			if (p1.compareTo(p2) == -1) {
				this.startPoint = p1;
				this.endPoint = p2;
			} else {
				this.startPoint = p2;
				this.endPoint = p1;
			}
		}

		public double mDist(Point center, Point current) {
			Point intersectPoint = intersectLines(new Line(center, current));
			double deltaX = center.x - intersectPoint.x;
			double deltaY = center.y - intersectPoint.y;
			return Math.abs(deltaX) + Math.abs(deltaY);
		}

		public Point intersectLines(Line l) throws IllegalArgumentException {
			// Wegen der Lesbarkeit
			double x1 = l.startPoint.x;
			double x2 = l.endPoint.x;
			double x3 = this.startPoint.x;
			double x4 = this.endPoint.x;
			double y1 = l.startPoint.y;
			double y2 = l.endPoint.y;
			double y3 = this.startPoint.y;
			double y4 = this.endPoint.y;

			// Zaehler
			double zx = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2)
					* (x3 * y4 - y3 * x4);
			double zy = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2)
					* (x3 * y4 - y3 * x4);

			// Nenner
			double n = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

			// Koordinaten des Schnittpunktes
			double x = zx / n;
			double y = zy / n;

			return new Point((int) x, (int) y);
		}

		public void setObstacle() {
			startPoint.line = this;
			endPoint.line = this;
		}
	}

}