import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

// /* *****************************************************************************
//  *  Name:              Ada Lovelace
//  *  Coursera User ID:  123456
//  *  Last modified:     October 16, 1842
//  **************************************************************************** */
//

/*
对于每个点，通过sort排序，得到关于该点的斜率排序的点序，随后遍历。如果相等则纳入segments
 */
public class FastCollinearPoints {
    // finds all line segments containing 4 or more points
    private final ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("argument to constructor is null");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("one point is null");
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("repeated point");
                }
            }
        }

        //遍历点集points，则不要排序points，采用新数组tmp排序
        Point[] tmp = Arrays.copyOf(points, points.length);
        for (Point p : points) {
            Arrays.sort(tmp, p.slopeOrder());

            for (int i = 1; i < tmp.length; ) {
                double slope = p.slopeTo(tmp[i]);
                int j = i + 1;

                //针对index i的slope，遍历接下来若干点，如果相等则j++
                while (j < tmp.length && slope == p.slopeTo(tmp[j])) {
                    j++;
                }

                //如果有三个点在同一直线上，则纳入
                //为了防止重复加入LineSegment，找到线段左端点时，才加入
                if (j - i > 2 && tmp[0].compareTo(tmp[i]) < 0) {
                    segments.add(new LineSegment(tmp[0], tmp[j - 1]));
                }
                i = j;
            }
        }


    }

    // private Point minPoint(Point[] array,int low,int high){
    //     Point ret=array[low];
    //     for(int i=low+1;i<high;i++){
    //         if(ret.compareTo(array[i])>0){
    //             ret=array[i];
    //         }
    //     }
    //     return ret;
    // }
    //
    // private Point maxPoint(Point[] array,int low,int high){
    //     Point ret=array[low];
    //     for(int i=low+1;i<high;i++){
    //         if(ret.compareTo(array[i])<0){
    //             ret=array[i];
    //         }
    //     }
    //     return ret;
    // }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] retValue = new LineSegment[numberOfSegments()];
        int i = 0;
        for (LineSegment x : segments) {
            retValue[i++] = x;
        }
        return retValue;
    }

}
