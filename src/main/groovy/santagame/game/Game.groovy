package santagame.game

import santagame.model.Card
import santagame.utils.Log

class Game {
    Card[] cards // the current deck
    int[] nexts = new int[9] // index of the next card to try for each position
    int startingCardRotation = 0 // rotation of the card being used at position 0
    Card[] board = new Card[9]

    Game(Card[] cards) {
        assert cards.size() == 9
        this.cards = cards*.clone()
    }

    GameResult playGame(final long startTime, final boolean debug = false, final int maxIterations = 0) {
        int p = 0 // position to check
        int toTry // index of the card to try next
        Card card // properly rotated card to try
        GameResult result = new GameResult()

        while (nexts.any { it < 9 }) {
            result.iterations++
            if (result.iterations == maxIterations) {
                Log.info("Stopping after ${result.iterations} iterations", startTime)
                return result
            }

            // First do backtracking
            while (nexts[p] == 9) {
                // Out of options for this p
                if (p == 0) {
                    // Completely out of options
                    Log.info("All possibilities exhausted, exiting now", startTime)
                    return result
                }
                // Backtrack
                if (debug) Log.debug("Backtracking", startTime, p, 9)
                nexts[p] = 0
                p--
                removeFromBoard(p)
            }

            toTry = nexts[p]

            if (isOnBoard(toTry)) {
                if (debug) Log.debug("Card is already on board", startTime, p, toTry)
                nexts[p]++
                continue
            }

            card = getRotatedCard(p)
            if (p == 0) {
                // We should try rotating the card before replacing it
                if (rotateStartingCard()) {
                    if (debug) Log.debug("Changed next starting card rotation to ${startingCardRotation}", startTime, p, toTry)
                } else {
                    changeStartingCard()
                    if (debug) Log.debug("Changed next starting card index to ${nexts[0]}", startTime, p, toTry)
                }
            } else {
                nexts[p]++
            }

            if (isValid(card, p)) {
                if (debug) Log.debug("Placing card", startTime, p, toTry)
                placeOnBoard(card, p)
                if (p == 8) {
                    // We just placed the last card!
                    assert !result.solutions.contains(board): "Solution already exists!"
                    result.solutions.add(board*.clone() as Card[])
                    Log.info("New solution found: ${board*.index}", startTime)
                    removeFromBoard(8) // this is necessary since we can't do normal backtracking
                    continue
                }
                // Advance to next position
                p++
            } else {
                // Can't place card, try the next one
                if (debug) Log.debug("Can't place card", startTime, p, toTry)
            }
        }

        String warning = "[WARNING] Uncontrolled exit happened! p: $p, board: $board, nexts: $nexts"
        Log.info(warning, startTime)
        throw new RuntimeException(warning)
    }

    /**
     * Checks whether the i'th card is on the board.
     * @return true iff the i'th card is on the board.
     */
    boolean isOnBoard(int i) {
        board*.index.contains(i)
    }

    /**
     * Places card on the board at position p.
     */
    void placeOnBoard(Card card, int p) {
        board[p] = card
    }

    /**
     * Removes the card currently at position p from the board.
     */
    void removeFromBoard(int p) {
        board[p] = null
    }

    /**
     * Increases the starting card rotation by 1, unless it has already been rotated 3 times.
     * @return whether the rotation was increased.
     */
    boolean rotateStartingCard() {
        // Note: No need to reset nexts array, since it will have been done during backtracking
        if (startingCardRotation == 3) return false
        startingCardRotation++
        return true
    }

    /**
     * Increases the starting card index by 1 and resets the starting card rotation.
     */
    void changeStartingCard() {
        // Note: No need to reset nexts array, since it will have been done during backtracking
        nexts[0]++
        startingCardRotation = 0
    }

    /**
     * Finds the only possible rotation of the next card to try for position p, namely the one that matches the color
     * of the first neighbor. The first neighbor is the left one if it exists, otherwise the upper one if it exists.
     * If no neighbors exist, the card is not rotated.
     * @param p the position
     * @return the rotated card
     */
    Card getRotatedCard(int p) {
        Card card = cards[nexts[p]]
        if (p == 0) {
            return card.rotateToDegrees(startingCardRotation)
        }
        Card leftNeighbor = getLeftNeighbor(p)
        if (leftNeighbor) {
            return card.rotateToLeftColor(leftNeighbor.rightPart.color)
        }
        Card upperNeighbor = getUpperNeighbor(p)
        // If p > 0 and there is no left neighbor, then there must be an upper neighbor
        return card.rotateToUpperColor(upperNeighbor.lowerPart.color)
    }

    /**
     * @return whether the card is valid and can be placed on the board in position p.
     */
    boolean isValid(Card card, int p) {
        isValidToTheLeft(card, p) && isValidAbove(card, p)
    }

    boolean isValidToTheLeft(Card card, int p) {
        Card neighbor = getLeftNeighbor(p)
        if (!neighbor) return true
        return neighbor.rightPart.color == card.leftPart.color &&
                neighbor.rightPart.bodyPart != card.leftPart.bodyPart
    }

    boolean isValidAbove(Card card, int p) {
        Card neighbor = getUpperNeighbor(p)
        if (!neighbor) return true
        return neighbor.lowerPart.color == card.upperPart.color &&
                neighbor.lowerPart.bodyPart != card.upperPart.bodyPart
    }

    Card getUpperNeighbor(int p) {
        p in (3..8) ? board[p - 3] : null
    }

    Card getLeftNeighbor(int p) {
        p in (1..8) && p % 3 != 0 ? board[p - 1] : null
    }
}
