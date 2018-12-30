package santagame.game

import santagame.model.Card

class GameResult {
    Iterable<Card[]> solutions = new ArrayList<Card[]>()
    Iterable<int[]> solutionIndices = new ArrayList<int[]>()
    int iterations
}
