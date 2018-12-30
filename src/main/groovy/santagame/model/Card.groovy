package santagame.model

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Card {
    private SantaPart[] santaParts // starting from right, moving counter-clockwise
    private int rotation

    Card(SantaPart[] santaParts) {
        assert santaParts.size() == 4
        this.santaParts = santaParts
    }

    /**
     * Rotates the card so that the given color faces left.
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

    Card rotateToDegrees(int degrees) {
        assert degrees in (0..3): "Rotation degree must be between 0 and 3"
        rotation = degrees
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
    String toString() {
        "Card(Right: ${rightPart}, Upper: ${upperPart}, Left: ${leftPart}, Lower: ${lowerPart})"
    }
}
