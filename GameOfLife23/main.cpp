/*
 * RunGameMain.cpp
 *
 *  Created on: Sep 20, 2018
 *      Author: mercer
 */
#include <iostream>
#include "GameOfLife.cpp"
using namespace std;

// int mainRUN () { need this when running a unit test
int main() {
  GameOfLife society(5, 7);
  society.growCellAt(1, 2);
    society.growCellAt(1, 4);
    society.growCellAt(2, 4);
    society.growCellAt(2, 2);
    society.growCellAt(2, 3);
  string ch;
  for (int count = 1; count <= 5; count++) {
    cout << society.toString() << endl;
    cout << "---------------" << endl;

    society.update();
  }
  return 0;
}
