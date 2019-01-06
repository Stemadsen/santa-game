package santagame.model

class CardLayout {
    Card[] originalLayout
    Card[] currentLayout

    private static final CORNER_INDICES = [0, 2, 6, 8]
    private static final UPPER_LEFT_CORNER = 0

    CardLayout(Card[] cards) {
        assert cards.size() == 9
        originalLayout = cards.clone()
        currentLayout = originalLayout.clone()
    }

    /**
     * Returns a canonical representation of this card layout. The representation is rotation invariant.
     * @return the representation.
     */
    CardLayout toCanonicalRepresentation() {
        while (minCornerCard != currentLayout[UPPER_LEFT_CORNER]) {
            rotateOnce()
        }
        return this
    }

    private void rotateOnce() {
        currentLayout = [2, 5, 8, 1, 4, 7, 0, 3, 6].collect { currentLayout[it].clone().rotateOnce() }
    }

    private Card getMinCornerCard() {
        CORNER_INDICES.collect { currentLayout[it] }.min { it.index }
    }

    @Override
    String toString() {
        currentLayout.toString()
    }
}
