/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

/*
定义 Point类，实现比较、求斜率
 */
public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }


    // string representation
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        else if (this.y == that.y && this.x < that.x) return -1;
        else if (this.y == that.y && this.x == that.x) return 0;
        else return 1;
    }


    // the slope between this point and that point
    public double slopeTo(Point that) {
        //位置相同点，返回-∞ ，水平返回0，竖直返回+∞，正常情况正常计算
        if (this.x == that.x) {
            if (this.y == that.y) {
                return Double.NEGATIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
        if (this.y == that.y) return +0.0;
        else return (double) (that.y - this.y) / (that.x - this.x);
    }


    private class SlopeComparator implements Comparator<Point> {
        public int compare(Point o1, Point o2) {
            Double slope1 = slopeTo(o1);
            Double slope2 = slopeTo(o2);
            return slope1.compareTo(slope2) != 0 ? slope1.compareTo(slope2):o1.compareTo(o2);
        }
    }


    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        return new SlopeComparator();
    }

}
