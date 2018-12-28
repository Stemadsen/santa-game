package santagame.game

import santagame.model.Card
import santagame.utils.Log

class Game {
    Card[] originalCards // never changes
    Card[] cards // the current deck, including order and rotation
    int firstCard = 0 // index of the card original card acting as the first card
    int firstCardRotation = 0 // rotateFirstCard() didn't update the card instance ...
    Card[] board = new Card[9]
    int[] boardIndices = (0..8).collect { -1 }
    int[] nexts = new int[9]
    Iterable<Card[]> solutions = new ArrayList<Card[]>()

    Game(Card[] cards) {
        assert cards.size() == 9
        originalCards = cards.clone()
        this.cards = cards.clone()
    }

    Tuple2<Iterable<Card[]>, Integer> runAlgorithm(final long startTime, final boolean debug = false, final int maxIterations = 0) {
        int p = 0 // position being checked
        int toTry // index of the card to try next
        Card card // properly rotated card to try
        int iterationCount = 0

        Log.info("Using card ${firstCard} - ${cards[0]} as first card, rotated ${firstCardRotation} times", startTime)

        while (nexts.any { it < 9 }) {
            iterationCount++
            if (iterationCount == maxIterations) {
                Log.info("Exiting after ${iterationCount} iterations", startTime)
                return [solutions, iterationCount]
            }

            // First do backtracking
            while (nexts[p] == 9) {
                // Out of options for this p
                if (p == 0) {
                    // Try something else at the first position
                    if (rotateFirstCard()) {
                        Log.info("Starting over with first card rotated ${firstCardRotation} time(s)", startTime)
                        continue
                    } else {
                        if (changeFirstCard()) {
                            Log.info("Starting over with card ${firstCard} - ${cards[0]} as first card", startTime)
                            continue
                        } else {
                            // Completely out of options
                            Log.info("All possibilities exhausted, exiting now", startTime)
                            return [solutions, iterationCount]
                        }
                    }
                }
                // Backtrack
                if (debug) Log.debug("Backtracking", startTime, p, 8)
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
            nexts[p]++

            if (isValid(card, p)) {
                if (debug) Log.debug("Placing card", startTime, p, toTry)
                placeOnBoard(card, toTry, p)
                if (p == 8) {
                    // We just placed the last card!
                    solutions << board.clone()
                    Log.info("New solution found:\n${stringify(board)}", startTime)
                } else {
                    // Continue to next position
                    p++
                }
            } else {
                // Can't place card
                if (debug) Log.debug("Can't place card", startTime, p, toTry)
            }
        }

        Log.info("[WARNING] Uncontrolled exit happened! p: $p, board: $board, nexts: $nexts", startTime)
        return [solutions, iterationCount]
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
     * Increases the first card rotation by 1 if it hasn't already been increased 3 times, and in this case resets
     * the nexts array.
     * @return whether any action was taken.
     */
    boolean rotateFirstCard() {
        if (firstCardRotation == 3) return false
        firstCardRotation++
        nexts = new int[9]
        return true
    }

    /**
     * Increases the first card index by 1 if it hasn't already been increased 8 times, and in this case resets
     * the cards and nexts arrays along with the first card rotation.
     * @return whether any action was taken.
     */
    boolean changeFirstCard() {
        if (firstCard == 8) return false
        firstCard++
        cards = (firstCard..firstCard + 8).collect { originalCards[it % 9] }
        nexts = new int[9]
        firstCardRotation = 0
        return true
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
        if (p == 0 && nexts[p] == 0) {
            // Manual rotation (hack)
            card.rotation = firstCardRotation
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
        // No neighbors
        return card
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

    static String stringify(Card[] cards) {
        cards*.toString().join(",\n")
    }
}
