package testing;

import model.MinesweeperModel;

import static org.junit.jupiter.api.Assertions.*;

class MinesweeperModelTest {


    @org.junit.jupiter.api.Test
    void placeBomb() {
        MinesweeperModel mod = new MinesweeperModel(10,10,10);
        mod.move(5,5);
        assert(mod.getBoard().getLoc(5,5).getHasBomb() == false);
        mod.placeBomb(5,5);
        assert(!mod.getBoard().getLoc(5,5).getHasBomb());
        assert(mod.getBombLeft() == 10);
        assert(!mod.isGameOver());
    }


    @org.junit.jupiter.api.Test
    void mark() {
        MinesweeperModel mod = new MinesweeperModel(10,10,10);
        mod.move(5,5);
        mod.mark(5,5);
        assert(mod.getBombLeft() == 9);
        assert(mod.getBoard().getLoc(5,5).getIsVisited());

    }

    @org.junit.jupiter.api.Test
    void move() {
        MinesweeperModel mod = new MinesweeperModel(10,10,10);
        mod.move(5,5);
        assert (mod.isGameOver() == false);
        assert (mod.getBoard().getLoc(5,5).getIsVisited() == true);
    }

    @org.junit.jupiter.api.Test
    void getStartingVars() {
        MinesweeperModel mod = new MinesweeperModel(10,10,10);
        assert(mod.getStartingVars()[0] == 10);
        assert(mod.getStartingVars()[1] == 10);
        assert(mod.getStartingVars()[2] == 10);
    }
}