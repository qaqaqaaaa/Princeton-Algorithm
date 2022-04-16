/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufForFull;
    private boolean[][] isOpen;
    private int openSites = 0;
    private final int gridWidth;
    private final byte[] dx = { -1, 0, 0, 1 };
    private final byte[] dy = { 0, -1, 1, 0 };

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("the given site (row, col) must in the n-by-n grid");
        }
        gridWidth = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufForFull = new WeightedQuickUnionUF(n * n + 1);
        isOpen = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                isOpen[i][j] = false;
            }
        }
    }

    private int calpos(int row, int col) {
        return (row - 1) * gridWidth + col;
    }

    private boolean isInGrid(int row, int col) {
        if (row <= 0 || row > gridWidth || col <= 0 || col > gridWidth) {
            return false;
        }
        return true;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isInGrid(row, col)) {
            throw new IllegalArgumentException("the given site (row, col) must in the n-by-n grid");
        }
        if (!isOpen(row, col)) {
            isOpen[row - 1][col - 1] = true;
            openSites++;
            int p = calpos(row, col);
            if (row == 1) {
                uf.union(0, p);
                ufForFull.union(0, p);
            }
            if (row == gridWidth) {
                uf.union(gridWidth * gridWidth + 1, p);
            }
            for (int i = 0; i < 4; i++) {
                int nrow = row + dy[i];
                int ncol = col + dx[i];
                if (isInGrid(nrow, ncol) && isOpen(nrow, ncol)) {
                    uf.union(p, calpos(nrow, ncol));
                    ufForFull.union(p, calpos(nrow, ncol));
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isInGrid(row, col)) {
            throw new IllegalArgumentException("the given site (row, col) must in the n-by-n grid");
        }
        return isOpen[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isInGrid(row, col)) {
            throw new IllegalArgumentException("the given site (row, col) must in the n-by-n grid");
        }
        return ufForFull.find(0) == ufForFull.find(calpos(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(gridWidth * gridWidth + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(1, 3);
        perc.open(2, 3);
        perc.open(3, 3);
        perc.open(3, 1);
        System.out.println(perc.percolates());
    }
}
