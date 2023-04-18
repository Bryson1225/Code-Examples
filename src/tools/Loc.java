package tools;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Loc implements Serializable{
    public final int row;
    public final int col;
    private int neighbor;
    private boolean hasBomb;
    private boolean isVisited;
    private boolean revealed;
    private boolean flagged;
    private int flag_count;

    //constructor
    //x is row, y is column

    /**
     * This is the constructor of the Loc object. It represents a location on
     * the Board.
     * @param x The row (x coordinate) of the board.
     * @param y The col (y coordinate) of the board.
     */
    public Loc(int x, int y) {
        this.row = x;
        this.col = y;
        this.hasBomb = false;
        this.isVisited = false;
        this.revealed = false;
        this.flagged = false;
        this.neighbor = 0;
        this.flag_count = 0;

    }

    /**
     * Returns if the location has a bomb.
     *
     * @return True if there is a bomb, false otherwise.
     */
    public boolean getHasBomb() {
        return this.hasBomb;
    }

    /**
     * Returns if the space has been visited.
     * @return boolean
     */
    public boolean getIsVisited() {
        return this.isVisited;
    }

    /**
     * Returns true if the location has been revealed.
     * @return boolean
     */
    public boolean getRevealed() {
        return this.revealed;
    }

    /**
     * Returns true if the location has been flagged.
     * @return boolean
     */
    public boolean getFlagged() {
        return this.flagged;
    }

    /**
     * Sets the location as visited.
     */
    public void setVisited() {
        this.isVisited = true;
    }

    /**
     * Sets the location as flagged.
     */
    public void setFlagged() {
        this.flagged = !this.flagged;
    }

    /**
     * Returns the current flag count.
     * @return int
     */
    public int getFlagCount() {return this.flag_count;}

    /**
     * Sets the current flag count.
     */
    public void setFlagCount() {
        if (this.flag_count < 2) {
            this.flag_count++;
        } else {
            this.flag_count = 0;
        }
    }

    /**
     * Sets a bomb at this location.
     */
    public void plantBomb() {
        this.hasBomb = true;
    }

    /**
     * Adds a neighbor to this location.
     */
    public void addNeighbor() {
        this.neighbor += 1;
    }

    /**
     * Returns neighbor count of this locaiton.
     * @return
     */
    public int getNeighbor() {
        return this.neighbor;
    }

    /**
     * This method reveals this location.
     */
    public void reveal() {
        this.revealed = true;
    }

    /**
     * This method returns a string representation of the location.
     * @return
     */
    @Override
    public String toString() {
        if (hasBomb){
            return "B";
        }
        if (isVisited){
            if (hasBomb){
                return "B";
            } else if (neighbor == 0){
                return "0";
            } else {
                return "" + neighbor;
            }
        } else {
            return "_";
        }

    }
}
