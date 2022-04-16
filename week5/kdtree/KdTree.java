/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

/*
使用2D-Tree求解range点以及nearest点
 */
public class KdTree {

    private Node root;
    private double dist = Double.POSITIVE_INFINITY;
    private Point2D nearestPoint = null;

    public KdTree()                               // construct an empty set of points
    {
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return size(root);
    }

    private int size(Node node) {
        if (node != null) {
            return node.size;
        } else {
            return 0;
        }
    }

    //插入point，返回根节点
    private Node nodeInsert(Point2D p, Node node, Node parent, boolean isleft) {
        if (node == null) {
            if (parent == null) {
                //创建根节点
                node = new Node(p, true, null, null, new RectHV(0, 0, 1, 1));
            } else {
                //创建叶子节点
                if (isleft) {
                    node = new Node(p, !parent.isVertical(), null, null, parent.leftRect());
                } else {
                    node = new Node(p, !parent.isVertical(), null, null, parent.rightRect());
                }
            }
        }
        //如果找到直接返回该节点
        else if (node.p.equals(p)) {
            return node;
        }
        //竖直点
        else if (node.isVertical()) {
            //按照x排序
            if (p.x() < node.p.x()) {
                node.left = nodeInsert(p, node.left, node, true);
            } else {
                node.right = nodeInsert(p, node.right, node, false);
            }
        }
        //水平点
        else {
            //按照y排序
            if (p.y() < node.p.y()) {
                node.left = nodeInsert(p, node.left, node, true);
            } else {
                node.right = nodeInsert(p, node.right, node, false);
            }
        }
        //计算node的size
        node.size = size(node.left) + size(node.right) + 1;
        return node;
    }


    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = nodeInsert(p, root, null, true);
    }

    //是否contains
    private boolean contains(Node node, Point2D p) {
        if (node == null) {
            return false;
        } else if (node.p.equals(p)) {
            return true;
        }
        if (node.isVertical()) {
            if (node.p.x() > p.x()) {
                return contains(node.left, p);
            } else {
                return contains(node.right, p);
            }
        } else {
            if (node.p.y() > p.y()) {
                return contains(node.left, p);
            } else {
                return contains(node.right, p);
            }
        }
    }


    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p);
    }

    //draw
    private void draw(Node node,
                      Node parent)                         // draw all points to standard draw
    {
        if (node == null) {
            return;
        }
        //root节点
        else if (parent == null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.p.x(), 0, node.p.x(), 1);
        } else if (node.isVertical()) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        draw(node.left, node);
        draw(node.right, node);
    }

    public void draw() {
        draw(root, null);
    }

    //range
    private void range(Node node, RectHV rect, ArrayList<Point2D> points) {
        if (node == null) {
            return;
        }
        //rect contains point
        else if (rect.contains(node.p)) {
            points.add(node.p);
        }

        //竖直点
        if (node.isVertical()) {
            //left
            if (node.p.x() > rect.xmax()) {
                range(node.left, rect, points);
            }
            //right
            else if (node.p.x() < rect.xmin()) {
                range(node.right, rect, points);
            }
            //both
            else {
                range(node.left, rect, points);
                range(node.right, rect, points);
            }
        }
        //水平点
        else {
            //same as above
            if (node.p.y() > rect.ymax()) {
                range(node.left, rect, points);
            } else if (node.p.y() < rect.ymin()) {
                range(node.right, rect, points);
            } else {
                range(node.left, rect, points);
                range(node.right, rect, points);
            }
        }
    }


    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> points = new ArrayList<>();
        range(root, rect, points);
        return points;
    }


    /*
    To find a closest point to a given query point
    start at the root and recursively search in both subtrees using the following pruning rule:
        if the closest point discovered so far is closer than the distance
        between the query point and the rectangle corresponding to a node
        there is no need to explore that node
     */
    private void nearest(Node node, Point2D p) {
        if (node == null) {
            return;
        }
        //update nearestPoint、dist
        else if (node.p.distanceSquaredTo(p) < dist) {
            nearestPoint = node.p;
            dist = nearestPoint.distanceSquaredTo(p);
        }

        boolean searchLeft = false, searchRight = false;
        if (node.leftRect().distanceSquaredTo(p) < dist) {
            searchLeft = true;
        }
        if (node.rightRect().distanceSquaredTo(p) < dist) {
            searchRight = true;
        }

        if (searchLeft && searchRight) {
            if (node.isVertical()) {
                if (p.x() < node.p.x()) {
                    nearest(node.left, p);
                    //从递归函数返回后，重新比较距离
                    if (node.rightRect().distanceSquaredTo(p) < dist) {
                        nearest(node.right, p);
                    }
                } else {
                    nearest(node.right, p);
                    //从递归函数返回后，重新比较距离
                    if (node.leftRect().distanceSquaredTo(p) < dist) {
                        nearest(node.left, p);
                    }
                }
            } else {
                if (p.y() < node.p.y()) {
                    nearest(node.left, p);
                    //从递归函数返回后，重新比较距离
                    if (node.rightRect().distanceSquaredTo(p) < dist) {
                        nearest(node.right, p);
                    }
                } else {
                    nearest(node.right, p);
                    //从递归函数返回后，重新比较距离
                    if (node.leftRect().distanceSquaredTo(p) < dist) {
                        nearest(node.left, p);
                    }

                }
            }
        } else if (searchLeft) {
            nearest(node.left, p);
        } else if (searchRight) {
            nearest(node.right, p);
        }

    }


    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        //每次调用nearest函数前，初始化参数
        dist = Double.POSITIVE_INFINITY;
        nearestPoint = null;
        nearest(root, p);
        return nearestPoint;
    }

    private class Node {
        private final boolean isVertical;
        private final Point2D p;
        private final RectHV rect;
        private Node left;
        private Node right;
        private int size;


        private Node(Point2D p, boolean isVertical, Node left, Node right, RectHV rect) {
            this.p = p;
            this.isVertical = isVertical;
            this.left = left;
            this.right = right;
            this.rect = rect;
        }

        private RectHV leftRect() {
            if (isVertical()) {
                return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
            }
        }

        private boolean isVertical() {
            return isVertical;
        }

        private RectHV rightRect() {
            if (isVertical()) {
                return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
            }

        }

    }

}
