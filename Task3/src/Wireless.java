import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wireless {
	public static double[] lengths1;
	public static double[] angles = new double[2000];;
	public static double[] radiuses = new double[2000];;
	public static double maxRadius;
	public static double maxAngele;
	public static Point[] centers = new Point[2000];
	public static Point maxCenter;
	public static int index = 0;
	public static double PId2 = Math.PI / 2;
	public static double PIm2 = Math.PI * 2;
	public static double PIm4 = PIm2 * 2;

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(new File("wireless.in"));
		PrintWriter out = new PrintWriter("wireless.out");

		int count = in.nextInt();
		Point[] points = new Point[count];
		for (int i = 0; i < count; i++) {
			points[i] = new Point(in.nextDouble(), in.nextDouble());
		}
		if (count == 1) {
			maxCenter = points[0];
			maxRadius = 0;
		} else {
			int h = Graham.computeHull(points);
			calculate(points, h);
		}

//		System.out.println(String.format("%.2f",maxRadius));
//		System.out.println(String.format("%.2f",maxCenter.x)+" "+String.format("%.2f",maxCenter.y));
		out.println(String.format("%.2f",maxRadius));
		out.println(String.format("%.2f",maxCenter.x)+" "+String.format("%.2f",maxCenter.y));
		out.close();
	}

	public static void calcualteAngles(Point points[], int h) {
		
		for (int i = 0; i < h; i++) {
			Point a = points[(i + h - 1) % h];
			Point b = points[i];
			Point c = points[(i + 1) % h];
			angles[i] = calcAngle(a, b, c);
//			System.out.println(angles[i]);
		}

	}

	public static double calcAngle(Point a, Point b, Point c) {
		return (PIm4 + Math.atan2(b.y - a.y, b.x - a.x) - Math.atan2(b.y - c.y, b.x - c.x)) % PIm2;
	}

	public static void calculateRadius(Point[] points, int h) {
		maxRadius = Double.MIN_VALUE;
		Point a;
		Point b;
		Point c;
		for (int i = 0; i < h; i++) {
			a = points[(i + h - 1) % h];
			b = points[i];
			c = points[(i + 1) % h];
			
			centers[i] = getCenter(a,b,c);
			radiuses[i] = b.dist(centers[i]);
			
			if (radiuses[i] > maxRadius || (radiuses[i] ==  maxRadius && maxAngele < angles[i])) {
				maxRadius = radiuses[i];
				maxCenter = centers[i];
				maxAngele = angles[i];
				index = i;
			} 
		}
	}
	
	public static Point getCenter(Point a, Point b, Point c) {
		if (a == b && b == c)
			return a;
		else if (a == b || b==c) 
			return new Point((c.x-a.x)/2+a.x, (c.y-a.y)/2+a.y);
		else if (a == c)
			return new Point((b.x-a.x)/2+a.x, (b.y-a.y)/2+a.y);
		else {
			double d = 2 * (a.x * (b.y - c.y) + b.x * (c.y - a.y) + c.x * (a.y - b.y));
			double x = ((a.x * a.x + a.y * a.y) * (b.y - c.y) + (b.x * b.x + b.y * b.y) * (c.y - a.y) + (c.x * c.x + c.y * c.y) * (a.y - b.y)) / d;
			double y = ((a.x * a.x + a.y * a.y) * (c.x - b.x) + (b.x * b.x + b.y * b.y) * (a.x - c.x) + (c.x * c.x + c.y * c.y) * (b.x - a.x)) / d;
			return new Point(x, y);
		}
	}
	


	public static void calculate(Point points[], int h) {
		boolean finish = false;
		while (!finish) {
			calcualteAngles(points, h);
			calculateRadius(points, h);
			int i;
			if (!(angles[index] <= PId2)) {
				finish = false;
				i = index;
				while (i < h-1) points[i] = points[i++ + 1];
				h--;

			} else {
				finish = true;
			}
		}

	}

	static class Point {

		public double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public Point(Point p) {
			this(p.x, p.y);
		}

		public Point relTo(Point p) {
			return new Point(x - p.x, y - p.y);
		}

		public void makeRelTo(Point p) {
			x -= p.x;
			y -= p.y;
		}


		public Point reversed() {
			return new Point(-x, -y);
		}

		public boolean isLower(Point p) {
			return y < p.y || y == p.y && x < p.x;
		}

		public double mdist() { // Manhattan-distance
			return Math.abs(x) + Math.abs(y);
		}

		public double mdist(Point p) {
			return relTo(p).mdist();
		}

		public boolean isFurther(Point p) {
			return mdist() > p.mdist();
		}

		public boolean isBetween(Point p0, Point p1) {
			return p0.mdist(p1) >= mdist(p0) + mdist(p1);
		}

		public double cross(Point p) {
			return x * p.y - p.x * y;
		}

		public double dot(Point p) {
			return x * p.x + y * p.y;
		}

		public double dist(Point p) {
			double dx = x - p.x;
			double dy = y - p.y;
			return Math.sqrt(dx * dx + dy * dy);
		}

		public boolean isLess(Point p) {
			double f = cross(p);
			return f > 0 || f == 0 && isFurther(p);
		}

		public double area2(Point p0, Point p1) {
			return p0.relTo(this).cross(p1.relTo(this));
		}

		public boolean isConvex(Point p0, Point p1) {
			double f = area2(p0, p1);
			return f < 0 || f == 0 && !isBetween(p0, p1);
		}
	}

	public static class Graham extends ConvexHull {

		public static int computeHull(Point[] p) {
			setPoints(p);
			if (n < 3) {
				return n;
			}
			graham();
			return h;
		}

		private static void graham() {
			exchange(0, indexOfLowestPoint());
			Point pl = new Point(p[0]);
			makeRelTo(pl);
			sort();
			makeRelTo(pl.reversed());
			int i = 3, k = 3;
			while (k < n) {
				exchange(i, k);
				while (!isConvex(i - 1)) {
					exchange(i - 1, i--);
				}
				k++;
				i++;
			}
			h = i;
		}

		private static void sort() {
			quicksort(1, n - 1); // without point 0
		}

		protected static void quicksort(int lo, int hi) {
			int i = lo, j = hi;
			Point q = p[(lo + hi) / 2];
			while (i <= j) {
				while (p[i].isLess(q)) {
					i++;
				}
				while (q.isLess(p[j])) {
					j--;
				}
				if (i <= j) {
					exchange(i++, j--);
				}
			}
			if (lo < j) {
				quicksort(lo, j);
			}
			if (i < hi) {
				quicksort(i, hi);
			}
		}
	}

	static abstract class ConvexHull {

		protected static Point[] p;
		protected static int n;
		protected static int h;

		public static void setPoints(Point[] p0) {
			p = p0;
			n = p.length;
			h = 0;
		}

		protected static void exchange(int i, int j) {
			Point t = p[i];
			p[i] = p[j];
			p[j] = t;
		}

		protected static void makeRelTo(Point p0) {
			int i;
			Point p1 = new Point(p0); // necessary, because p0 migth be in p[]
			for (i = 0; i < n; i++) {
				p[i].makeRelTo(p1);
			}
		}

		protected static int indexOfLowestPoint() {
			int i, min = 0;
			for (i = 1; i < n; i++) {
				if (p[i].y < p[min].y || p[i].y == p[min].y
						&& p[i].x < p[min].x) {
					min = i;
				}
			}
			return min;
		}

		protected static boolean isConvex(int i) {
			return p[i].isConvex(p[i - 1], p[i + 1]);
		}
	}
}