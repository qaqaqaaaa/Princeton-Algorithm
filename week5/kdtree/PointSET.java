/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

/*
采用暴力算法找到range点以及nearest点
 */
public class PointSET {
    private final SET<Point2D> pointset;

    public PointSET()                          // construct an empty set of points
    {
        pointset = new SET<>();
    }


    public boolean isEmpty()                      // is the set empty?
    {
        return pointset.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return pointset.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointset.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointset.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : pointset) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> containedPoints = new ArrayList<>();
        for (Point2D p : pointset) {
            if (rect.contains(p)) {
                containedPoints.add(p);
            }
        }
        return containedPoints;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearestPoint = null;
        double dist = Double.POSITIVE_INFINITY;
        for (Point2D pthat : pointset) {
            if (p.distanceSquaredTo(pthat) < dist) {
                nearestPoint = pthat;
                dist = p.distanceSquaredTo(pthat);
            }
        }
        return nearestPoint;
    }
}
