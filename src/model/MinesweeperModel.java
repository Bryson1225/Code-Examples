/**
 * Models a game of Minesweeper
 */

package model;

import tools.Board;

import java.io.Serializable;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class MinesweeperModel extends Observable implements Serializable {

	private Board board;
	private boolean gameover;
	private int rowSize;
	private int colSize;
	private int bomb;

	/**
	 * The constructor for MinesweeperModel.
	 *
	 * @param row The rows requested by the user.
	 * @param col The columns requested by the user.
	 * @param bomb The bomb count requested by the user.
	 */
	public MinesweeperModel(int row, int col,int bomb) {
		board = new Board(row,col,bomb);
		this.gameover = false;
		this.rowSize = row;
		this.colSize = col;
		this.bomb = bomb;
	}

	/**
	 * return the board.
	 * @return The board of the model.
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Place the bombs to the board.
	 *
	 * The number of the bomb placed is depending on the board size.
	 * This will automatically increment the neightbor location by 1 when a bomb
	 * is placed.
	 * @param firstMoveR The row the bomb is placed.
	 * @param firstMoveC The column the bomb is placed.
	 */
	public void placeBomb(int firstMoveR, int firstMoveC) {
		this.board.placeBomb(firstMoveR,firstMoveC);
		setChanged();
		notifyObservers(this.board);
	}


	/**
	 * flag or unflag a location.
	 * flag: guess if a location contains a bomb.
	 * @param row The row the flag will be placed.
	 * @param col The column the flag will be placed.
	 */
	public void mark(int row, int col) {
		if (this.board.getLoc(row, col).getFlagged()) {
			if (this.board.getLoc(row, col).getFlagCount() == 1) {
				this.board.getLoc(row, col).setFlagCount();
			} else if (this.board.getLoc(row, col).getFlagCount() == 2){
				this.bomb+=1;
				this.board.getLoc(row, col).setFlagCount();
				this.board.getLoc(row, col).setFlagged();
			}
		} else {
			this.board.getLoc(row, col).setFlagged();
			this.board.getLoc(row, col).setFlagCount();
			this.bomb-=1;
		}
		if (this.bomb == 0) {
			if (this.checkAllFlagged()) {
				this.gameover = true;
			}
		}
		setChanged();
		notifyObservers(this.board);
	}

	/**
	 * Check if all flagged are correctly placed. i.e every flag is placed
	 * on a location that has a bomb.
	 * @return Returns true if the flags are correctly placed, false otherwise.
	 */
	private boolean checkAllFlagged() {
		for (int i = 0; i < this.rowSize; i++) {
			for (int j = 0; j < this.rowSize; j++) {
				if (this.board.getLoc(i, j).getFlagged()) {
					if (this.board.getLoc(i, j).getHasBomb() == false) {
						return true;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Check if the move contain a bomb or not, it yes, set this.gameover to true.
	 * Else, reveal a location.
	 * notify the Observer.
	 * @param row The row of the selected move.
	 * @param col The column of the selected move.
	 */
	public void move(int row, int col) {
		if (this.board.getLoc(row, col).getFlagged()) {
			System.out.println("No change");
		} else {
			if (this.board.getLoc(row, col).getHasBomb()) {
				this.gameover = true;
				this.board.getLoc(row, col).reveal();
			} else {
				revLoc(row, col);
			}
		}
		setChanged();
		notifyObservers(this.board);
	}

	/**
	 * reveal a location, and if any of its neighbor's "neighbor (in Loc)" is 0
	 * recursive call this function on that location.
	 * Do not recurse if it's neighbor is not 0.
	 * @param row The row for the selected location.
	 * @param col The column for the selected location.
	 */
	private void revLoc(int row, int col) {
		this.board.getLoc(row, col).reveal();
		this.board.getLoc(row, col).setVisited();
		if (row - 1 >= 0 && col - 1 >= 0
				&& this.board.getLoc(row-1, col-1).getIsVisited() == false
				&& this.board.getLoc(row-1, col-1).getHasBomb() == false) {

			if (this.board.getLoc(row-1, col-1).getNeighbor() != 0) {
				this.board.getLoc(row-1, col-1).reveal();
				this.board.getLoc(row-1, col-1).setVisited();
			} else if (this.board.getLoc(row-1, col-1).getNeighbor() == 0) {
				revLoc(row-1,col-1);
			}
		}
		if (row - 1 >= 0 && col + 1 < this.colSize
				&& this.board.getLoc(row-1, col+1).getIsVisited() == false
				&& this.board.getLoc(row-1, col+1).getHasBomb() == false) {
			if (this.board.getLoc(row-1, col+1).getNeighbor() != 0) {
				this.board.getLoc(row-1, col+1).reveal();
				this.board.getLoc(row-1, col+1).setVisited();
			} else if (this.board.getLoc(row-1, col+1).getNeighbor() == 0) {
				revLoc(row-1,col+1);
			}
		}
		if (row - 1 >= 0
				&& this.board.getLoc(row-1, col).getIsVisited() == false
				&& this.board.getLoc(row-1, col).getHasBomb() == false) {

			if (this.board.getLoc(row-1, col).getNeighbor() != 0) {
				this.board.getLoc(row-1, col).reveal();
				this.board.getLoc(row-1, col).setVisited();
			} else if (this.board.getLoc(row-1, col).getNeighbor() == 0) {
				revLoc(row-1,col);
			}
		}
		if (col - 1 >= 0
				&& this.board.getLoc(row, col-1).getIsVisited() == false
				&& this.board.getLoc(row, col-1).getHasBomb() == false) {
			if (this.board.getLoc(row, col-1).getNeighbor() != 0) {
				this.board.getLoc(row, col-1).reveal();
				this.board.getLoc(row, col-1).setVisited();
			} else if (this.board.getLoc(row, col-1).getNeighbor() == 0) {
				revLoc(row,col-1);
			}

		}
		if (col + 1 < this.colSize
				&& this.board.getLoc(row, col+1).getIsVisited() == false
				&& this.board.getLoc(row, col+1).getHasBomb() == false) {
			if (this.board.getLoc(row, col+1).getNeighbor() != 0) {
				this.board.getLoc(row, col+1).reveal();
				this.board.getLoc(row, col+1).setVisited();
			} else if (this.board.getLoc(row, col+1).getNeighbor() == 0) {
				revLoc(row,col+1);
			}
		}
		if (row + 1 < this.rowSize && col - 1 >= 0
				&& this.board.getLoc(row+1, col-1).getIsVisited() == false
				&& this.board.getLoc(row+1, col-1).getHasBomb() == false) {

			if (this.board.getLoc(row+1, col-1).getNeighbor() != 0) {
				this.board.getLoc(row+1, col-1).reveal();
				this.board.getLoc(row+1, col-1).setVisited();
			} else if (this.board.getLoc(row+1, col-1).getNeighbor() == 0) {
				revLoc(row+1,col-1);
			}
		}
		if (row + 1 < this.rowSize && col + 1 < this.colSize
				&& this.board.getLoc(row+1, col+1).getIsVisited() == false
				&& this.board.getLoc(row+1, col+1).getHasBomb() == false) {
			if (this.board.getLoc(row+1, col+1).getNeighbor() != 0) {
				this.board.getLoc(row+1, col+1).reveal();
				this.board.getLoc(row+1, col+1).setVisited();
			} else if (this.board.getLoc(row+1, col+1).getNeighbor() == 0) {
				revLoc(row+1,col+1);
			}
		}
		if (row + 1 < this.rowSize
				&& this.board.getLoc(row+1, col).getIsVisited() == false
				&& this.board.getLoc(row+1, col).getHasBomb() == false) {
			if (this.board.getLoc(row+1, col).getNeighbor() != 0) {
				this.board.getLoc(row+1, col).reveal();
				this.board.getLoc(row+1, col).setVisited();
			} else if (this.board.getLoc(row+1, col).getNeighbor() == 0) {
				revLoc(row+1,col);
			}

		}

	}

	/**
	 * if this.gameover is true and bomb is 0, user win.
	 * else user lose.
	 * @return True if the game is over, false otherwise.
	 */
	public boolean isGameOver() {
		return this.gameover;
	}

	/**
	 * return the bombs left in the game.
	 * @return numbers of bomb left.
	 */
	public int getBombLeft() {
		return this.bomb;
	}

	/**
	 * Returns the starting values of the model.
	 * @return
	 */
	public int[] getStartingVars(){
		return new int[]{this.rowSize, this.colSize, this.bomb};
	}



}
