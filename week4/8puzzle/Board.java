/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;

/*
定义8puzzle的board类，实现apis便于求解问题
 */
public class Board {
    private final int[][] board;
    private final int N;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        board = copy(tiles);
        N = board.length;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        for (Board b : initial.neighbors()) {
            System.out.println(b);
        }
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of tiles out of place
    // hamming求解距离，如果该点不在该在的地方上则++(0除外)
    public int hamming() {
        int ret = 0;
        int cnt = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] != cnt) {
                    ret++;
                }
                cnt++;
            }
        }
        return ret - 1;
    }

    // sum of Manhattan distances between tiles and goal
    // manhattan求解距离，街区距离,求解每个点到应在位置上的街区距离(0除外)
    public int manhattan() {
        int ret = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] != 0) {
                    //求解board[i][j]上的点该在的row、col，与i，j现位置
                    int row = (board[i][j] - 1) / N;
                    int col = (board[i][j] - 1) % N;
                    ret += Math.abs(row - i) + Math.abs(col - j);
                }
            }
        }
        return ret;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        //deepEqual
        if (!Arrays.deepEquals(this.board, that.board)) return false;
        return true;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(board[i][j]);
                if (j != N - 1) {
                    s.append(" ");
                } else {
                    s.append("\n");
                }
            }
        }
        return s.toString();
    }

    private int[][] copy(int[][] array) // 拷贝棋盘元素
    {
        int N = array.length;
        int[][] newblocks = new int[N][N];
        for (int i1 = 0; i1 < N; i1++)
            for (int j1 = 0; j1 < N; j1++)
                newblocks[i1][j1] = array[i1][j1];
        return newblocks;
    }


    private Board swap(int i1, int j1, int i2, int j2) {
        int[][] newblocks = copy(board);
        int temp = newblocks[i1][j1];
        newblocks[i1][j1] = newblocks[i2][j2];
        newblocks[i2][j2] = temp;
        return new Board(newblocks);
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int i = 0;
        int j = 0;
        for (int q = 0; q < N; q++) {
            for (int w = 0; w < N; w++) {
                if (board[q][w] == 0) {
                    i = q;
                    j = w;
                    break;
                }
            }
        }
        ArrayList<Board> boards = new ArrayList<Board>();
        if (i > 0) {
            boards.add(swap(i, j, i - 1, j));
        }
        if (j > 0) {
            boards.add(swap(i, j, i, j - 1));
        }
        if (i < N - 1) {
            boards.add(swap(i, j, i + 1, j));
        }
        if (j < N - 1) {
            boards.add(swap(i, j, i, j + 1));
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    // 直接交换任意两个位置数字改变奇偶性
    public Board twin() {
        int i1 = 0, j1 = 0, i2 = 1, j2 = 1;

        if (board[i1][j1] == 0) {
            i1 = 1;
        }
        if (board[i2][j2] == 0) {
            j2 = 0;
        }

        Board newBoard = swap(i1, j1, i2, j2);
        return newBoard;
    }

}
