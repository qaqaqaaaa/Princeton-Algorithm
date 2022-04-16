/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

/*
A*算法求解8puzzle
 */
public class Solver {

    private boolean solvable;
    private int moves;
    private Iterable<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        //构建两个求解node，node与twinNode，两个必有一个有解，只需查看求解后node是否是twinNode。
        Node node = new Node(null, initial, false);
        Node twinNode = new Node(null, initial.twin(), true);
        MinPQ<Node> pq = new MinPQ<>();
        pq.insert(twinNode);
        while (!node.b.isGoal()) {
            for (Board bb : node.b.neighbors()) {
                //遍历b的eighbors，但防止加入重复的棋盘
                if (node.pre == null || !bb.equals(node.pre.b)) {
                    pq.insert(new Node(node, bb, node.isTwin));
                }
            }
            //每次都处理最小优先级的棋盘状态
            node = pq.delMin();
        }

        if (node.isTwin) {
            solvable = false;
            moves = -1;
        } else {
            moves = node.moves;
            solvable = true;
            ArrayList<Board> list = new ArrayList<>();
            //找到棋盘求解成功的序列
            while (node != null) {
                list.add(node.b);
                node = node.pre;
            }
            Collections.reverse(list);
            solution = list;
        }

    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return moves != -1 ? solution : null;
    }

    private class Node implements Comparable<Node> {
        private final boolean isTwin;
        private final int moves;
        private final int manhattan;
        private final int priority;
        private Node pre;
        private Board b;

        private Node(Node pre, Board b, boolean isTwin) {
            this.isTwin = isTwin;
            this.pre = pre;
            this.b = b;
            this.manhattan = b.manhattan();
            if (pre != null) {
                this.moves = pre.moves + 1;
            } else {
                this.moves = 0;
            }
            this.priority = this.manhattan + this.moves;
        }


        public int compareTo(Node node) {
            if (priority == node.priority) {
                return Integer.compare(manhattan, node.manhattan);
            } else {
                return Integer.compare(priority, node.priority);
            }
        }
    }

}