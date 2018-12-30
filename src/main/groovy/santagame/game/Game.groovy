package santagame.game

import santagame.model.Card
import santagame.utils.Log

class Game {
    Card[] originalCards // never changes
    Card[] cards // the current deck, including order and rotation
    int[] nexts = new int[9]
    int startingCardRotation = 0 // rotation of the card being used at position 0
    Card[] board = new Card[9]
    int[] boardIndices = (0..8).collect { -1 }
    AlgorithmOutput output = new AlgorithmOutput()

    Game(Card[] cards) {
        assert cards.size() == 9
        originalCards = cards.clone()
        this.cards = cards.clone()
    }

    AlgorithmOutput runAlgorithm(final long startTime, final boolean debug = false, final int maxIterations = 0) {
        int p = 0 // position to check
        int toTry // index of the card to try next
        Card card // properly rotated card to try

        while (nexts.any { it < 9 }) {
            output.iterationsRun++
            if (output.iterationsRun == maxIterations) {
                Log.info("Stopping after ${output.iterationsRun} iterations", startTime)
                return output
            }

            // First do backtracking
            while (nexts[p] == 9) {
                // Out of options for this p
                if (p == 0) {
                    // Completely out of options
                    Log.info("All possibilities exhausted, exiting now", startTime)
                    return output
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
                // If this is the first position, we want to try rotating the card before replacing it
                if (rotateStartingCard()) {
                    Log.info("Changed next starting card rotation to ${startingCardRotation}", startTime)
                } else {
                    changeStartingCard()
                    Log.info("Changed next starting card index to ${nexts[0]}", startTime)
                }
            } else {
                nexts[p]++
            }

            if (isValid(card, p)) {
                if (debug) Log.debug("Placing card", startTime, p, toTry)
                placeOnBoard(card, toTry, p)
                if (p == 8) {
                    // We just placed the last card!
                    assert !output.solutions.contains(board.clone()): "Solution already exists!"
                    output.solutions << board.clone()
                    output.solutionIndices << boardIndices.clone()
                    Log.info("New solution found: ${boardIndices}\n${stringify(board)}", startTime)
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

        Log.info("[WARNING] Uncontrolled exit happened! p: $p, board: $board, nexts: $nexts", startTime)
        return output
    }

    /**
     * Checks whether the i'th card is on the board.
     * @return true iff the i'th card is on the board.
     */
    boolean isOnBoard(int i) {
        boardIndices.contains(i)
    }

    /**
     * Places card, with index i, on the board at position p.
     */
    void placeOnBoard(Card card, int i, int p) {
        boardIndices[p] = i
        board[p] = card
    }

    /**
     * Removes the card currently in position p from the board.
     */
    void removeFromBoard(int p) {
        boardIndices[p] = -1
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
            // Manual rotation
            card.rotation = startingCardRotation
            return card
        }
        Card leftNeighbor = getLeftNeighbor(p)
        if (leftNeighbor) {
            return card.rotateToLeftColor(leftNeighbor.rightPart.color)
        }
        Card upperNeighbor = getUpperNeighbor(p)
        if (upperNeighbor) {
            return card.rotateToUpperColor(upperNeighbor.lowerPart.color)
        }
        // This should never happen
        String warning = "[WARNING] No neighbors for position $p! Board: $board, nexts: $nexts"
        Log.info(warning)
        throw new RuntimeException(warning)
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

    // TODO Move
    static String stringify(Card[] cards) {
        cards*.toString().join(",\n")
    }
}
