package santagame.model

class Card {
    private SantaPart[] santaParts // starting from right, moving counter-clockwise
    private int rotation
    int index

    /**
     * @param santaParts an array whose elements correspond to the right, upper, left, and lower part, respectively,
     * of the card (in that order).
     * @param index the card's index in the deck, between 0 and 8.
     */
    Card(SantaPart[] santaParts, int index = 0) {
        assert index in (0..8)
        assert santaParts.size() == 4
        this.index = index
        this.santaParts = santaParts
    }

    /**
     * Rotates the card so that the given color faces left.
     * @param leftColor the color to face left.
     * @return this card.
     */
    Card rotateToLeftColor(Color leftColor) {
        if (leftPart.color != leftColor) {
            int degree
            if (upperPart.color == leftColor) {
                degree = 1
            } else if (rightPart.color == leftColor) {
                degree = 2
            } else {
                degree = -1
            }
            rotation = (rotation + degree) % 4
        }
        return this
    }

    /**
     * Rotates the card so that the given color faces up.
     * @param upperColor the color to face up.
     * @return this card.
     */
    Card rotateToUpperColor(Color upperColor) {
        if (upperPart.color != upperColor) {
            int degree
            if (rightPart.color == upperColor) {
                degree = 1
            } else if (lowerPart.color == upperColor) {
                degree = 2
            } else {
                degree = -1
            }
            rotation = (rotation + degree) % 4
        }
        return this
    }

    /**
     * Rotates the card a number of degrees counter-clockwise.
     * @param degrees the number of degrees to rotate the card, between 0 and 3.
     * @return this card.
     */
    Card rotateToDegrees(int degrees) {
        assert degrees in (0..3): "Rotation degree must be between 0 and 3"
        rotation = degrees
        return this
    }

    /**
     * Rotates the card once counter-clockwise.
     * @return this card.
     */
    Card rotateOnce() {
        rotation = (rotation + 1) % 4
        return this
    }

    SantaPart[] getSantaParts() {
        santaParts
    }

    SantaPart getUpperPart() {
        santaParts[(1 - rotation) % 4]
    }

    SantaPart getLowerPart() {
        santaParts[(3 - rotation) % 4]
    }

    SantaPart getLeftPart() {
        santaParts[(2 - rotation) % 4]
    }

    SantaPart getRightPart() {
        santaParts[-rotation % 4]
    }

    @Override
    Card clone() {
        return new Card([rightPart, upperPart, leftPart, lowerPart] as SantaPart[], index)
    }

    /**
     * Two Cards are equal if and only if they have the same index and the same SantaParts, including position,
     * but not necessarily order.
     */
    @Override
    boolean equals(Object other) {
        if (!(other instanceof Card)) {
            return false
        }
        return other.index == index && other.rightPart == rightPart && other.upperPart == upperPart &&
                other.leftPart == leftPart && other.lowerPart == lowerPart
    }

    @Override
    int hashCode() {
        return index + santaParts[0].hashCode() - santaParts[1].hashCode() +
                santaParts[2].hashCode() - santaParts[3].hashCode()
    }

    @Override
    String toString() {
        "Card(${index}, Right: ${rightPart}, Upper: ${upperPart}, Left: ${leftPart}, Lower: ${lowerPart})"
    }
}
