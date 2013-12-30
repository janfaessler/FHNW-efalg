package ch.fhnw.mv.convex_hull;

/*  -----------------------------------------------------------------
 *  -------  Effiziente Algorithmen                  ----------------
 *  -------                                          ----------------
 *  -------  helper class for the calculation of     ----------------
 *  -------  the convex hull of points               ----------------
 *  -------  in the xy-plane                         ----------------
 *  -------                                          ----------------
 *  -------  @author  Manfred Vogel                  ----------------
 *  -------  2013 october                            ----------------
 *  -----------------------------------------------------------------
 */
public class JarvisMarch extends ConvexHull  {
	
    public static int computeHull(Point[] p) {
        setPoints(p);
        jarvisMarch();
        return h;
    }

    private static void jarvisMarch() {
        int i=indexOfLowestPoint();
        do  {
            exchange(h, i);
            i=indexOfRightmostPointFrom(p[h]);
            h++;
        }
        while (i>0);
    }
    
    private static int indexOfRightmostPointFrom(Point q) {
        int i=0, j;
        for (j=1; j<n; j++)
            if (p[j].relTo(q).isLess(p[i].relTo(q)))
                i=j;
        return i;
    }
}