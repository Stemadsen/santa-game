package santagame.model

/**
 * Manages solutions using canonical representations.
 */
class SolutionContainer {
    /** Contains canonical representations of the solutions. */
    List<CardLayout> solutions = new ArrayList<CardLayout>()

    /**
     * Checks whether the given array of cards corresponds to an element of this solution set, i.e.,
     * whether its canonical representation is in the solution set.
     * @param cardSequence the cards to check.
     * @return true if the canonical representation of the array of cards exists in this solution set,
     * false otherwise.
     */
    boolean contains(Card[] cardSequence) {
        CardLayout canonicalRepresentation = new CardLayout(cardSequence).toCanonicalRepresentation()
        solutions.any {
            it.currentLayout == canonicalRepresentation.currentLayout
        }
    }

    /**
     * Adds the <i>canonical representation</i> of the given array of cards to the solutions.
     * @param cardSequence the cards to add.
     */
    void add(Card[] cardSequence) {
        solutions << new CardLayout(cardSequence).toCanonicalRepresentation()
    }
}
