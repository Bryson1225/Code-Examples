package tools;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;


@SuppressWarnings("serial")
public class Board implements Serializable{

    private Loc[][] grid;
    private int bomb;
    private int rowSize;
    private int colSize;

    /**
     * The constructor for the Board.
     *
     * @param row Number of rows in the board.
     * @param col Number of columns in the board.
     * @param bomb Number of bombs placed on the board.
     */
    public Board(int row, int col,int bomb) {
        this.grid = new Loc[row][col];
        for (int i = 0 ; i < row; i++) {
            for (int j = 0 ; j < col; j++) {
                this.grid[i][j] = new Loc(i,j);
            }
        }
        this.bomb = bomb;
        this.rowSize = row;
        this.colSize = col;
    }

    /**
     * Returns a Loc object of the Board at the specified row and col.
     * @param r Specified row of location.
     * @param c Specified column of location.
     * @return The location as a Loc object.
     */
    public Loc getLoc(int r, int c) {
        return this.grid[r][c];
    }

    /**
     * This method places bombs on the board.
     *
     * @param firstMoveR Row the bomb will be placed.
     * @param firstMoveC Column the bomb will be placed.
     */
    public void placeBomb(int firstMoveR, int firstMoveC) {
        Random randRow = new Random();
        Random randCol = new Random();
        for (int i = 0; i< this.bomb;i++) {
            int rb = randRow.nextInt(this.rowSize);
            int cb = randCol.nextInt(this.colSize);
            while (this.grid[rb][cb].getHasBomb() || (rb == firstMoveR && cb == firstMoveC)) {
                rb = randRow.nextInt(this.rowSize);
                cb = randCol.nextInt(this.colSize);
            }
            this.grid[rb][cb].plantBomb();
            adjustNeighbor(rb,cb);
        }
    }

    /**
     * add 1 to all the neighbor location where a bomb is planted.
     * @param row - location where bomb is set.
     * @param col - location where bomb is set.
     */
    public void adjustNeighbor(int row, int col) {
        if (row - 1 >= 0 && col - 1 >= 0) {
            this.grid[row-1][col-1].addNeighbor();
        }
        if (row - 1 >= 0 && col + 1 < this.colSize) {
            this.grid[row -1][col+1].addNeighbor();
        }
        if (row - 1 >= 0) {
            this.grid[row-1][col].addNeighbor();
        }
        if (col - 1 >= 0) {
            this.grid[row][col-1].addNeighbor();
        }
        if (col + 1 < this.colSize) {
            this.grid[row][col+1].addNeighbor();
        }
        if (row + 1 < this.rowSize && col - 1 >= 0) {
            this.grid[row+1][col-1].addNeighbor();
        }
        if (row + 1 < this.rowSize && col + 1 < this.colSize ) {
            this.grid[row+1][col+1].addNeighbor();
        }
        if (row + 1 < this.rowSize) {
            this.grid[row+1][col].addNeighbor();

        }
    }

    /**
     * For debugging of the board.
     * @return String of the board.
     */
    @Override
    public String toString() {
        String out = "";
        for (Loc[] row : grid){
            out += Arrays.toString(row) + "\n";
        }
        return out;
    }
}
