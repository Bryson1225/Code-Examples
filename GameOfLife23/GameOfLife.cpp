/*
 * Filename: GameOfLife.cpp
 *
 *  Authors Rick Mercer and Bryson Mineart
 *
 * This class models a society of cells growing according
 * to the rules of John Conway's Game of Life.
 *
 */
#include <string>
#include <vector>
#include <iostream>

using namespace std;

// This class allows a society of cells to grow according
// to the rules from John Conway's Game of Life
class GameOfLife {

// --Data Member
private:
    std::vector<std::vector<bool>> theSociety;

public:
    // Construct a board that is rows by cols size
    // with all elements set to false
    GameOfLife(unsigned rows, unsigned cols) {
        this->theSociety.reserve(rows);
        for (int i = 0; i < rows; i++) {
            this->theSociety.push_back(std::vector<bool>(cols, false));
            //for (int j = 0; j < rows; j++) {
            //    this->theSociety[i][j] = false;
            //}
        }
    }

    // Grow a cell at the given location
    void growCellAt(unsigned row, unsigned col) {
        this->theSociety[row][col] = true;
    }

    // Check to see if a cell is at the given location
    bool cellAt(unsigned row, unsigned col) const {
        if (this->theSociety[row][col] == true) {
            return true;
        } else {
            return false;
        }
    }

    // Returns the colony as one big string
    string toString() const {
        string toReturn{""};
        for (int i = 0; i < this->theSociety.size(); i++) {
            for (int j = 0; j < this->theSociety[i].size(); j++) {
                if (this->cellAt(i, j)) {
                    toReturn += "o";
                } else {
                    toReturn += ".";
                }
            }
            toReturn += "\n";
        }
        return toReturn;
    }

    // Count the neighbors around the given location. Use wraparound. A cell in row 0
    // has neighbors in the last row if a cell is in the same column, or the column to
    // the left or right. In this example, cell 0,5 has two neighbors in the last row,
    // cell 2,8  has four neighbors, cell 2,0 has four neighbors, cell 1,0 has three
    // neighbors. The cell at 3,8 has 3 neighbors. The potential location for a cell
    // at 4,8 would have three neighbors.
    //
    // .....O..O
    // O........
    // O.......O
    // O.......O
    // ....O.O..
    //
    // The return values should always be in the range of 0 through 8.
    // Return the number of neighbors around any cell using wrap around.
    int neighborCount(int row, int col) const {
        int nCount = 0;
        int maxRow = this->theSociety.size() - 1;
        int maxCol = this->theSociety[0].size() - 1;
        int rowPP = row + 1; // Row increased
        int rowMM = row - 1; // Row decreased
        int colPP = col + 1; // Col increased
        int colMM = col - 1; // Col decreased

        if (rowPP > maxRow) {
            rowPP = 0;
        }
        if (rowMM < 0) {
            rowMM = maxRow;
        }
        if (colPP > maxCol) {
            colPP = 0;
        }
        if (colMM < 0) {
            colMM = maxCol;
        }

        if (this->cellAt(rowMM, colMM)) {
            nCount += 1;
        }
        if (this->cellAt(rowMM, col)) {
            nCount += 1;
        }
        if (this->cellAt(rowMM, colPP)) {
            nCount += 1;
        }
        if (this->cellAt(rowPP, colMM)) {
            nCount += 1;
        }
        if (this->cellAt(rowPP, col)) {
            nCount += 1;
        }
        if (this->cellAt(rowPP, colPP)) {
            nCount += 1;
        }
        if (this->cellAt(row, colMM)) {
            nCount += 1;
        }
        if (this->cellAt(row, colPP)) {
            nCount += 1;
        }

        return nCount;
    }

    // Change the state to the next society of cells
    void update() {
        vector<vector<bool>> temporary;
        temporary.reserve(this->theSociety.size());
        for (int i = 0; i < this->theSociety.size(); i++) {
            temporary.push_back(std::vector<bool>(this->theSociety[i].size(), false));
        }

        for (int i = 0; i < this->theSociety.size(); i++) {
            for (int j = 0; j < this->theSociety[i].size(); j++) {
                int numNeighbors = this->neighborCount(i, j);

                if (this->cellAt(i, j)) {
                    if (numNeighbors >= 2 && numNeighbors <= 3) {
                        temporary[i][j] = true;
                    }
                    if (numNeighbors > 3) {
                        temporary[i][j] = false;
                    }
                    if (numNeighbors < 2) {
                        temporary[i][j] = false;
                    }
                } else {
                    if (numNeighbors == 3) {
                        temporary[i][j] = true;
                    }
                }
            }
        }
        this->theSociety = temporary;
    }
};
