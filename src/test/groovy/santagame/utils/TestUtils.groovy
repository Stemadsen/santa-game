package santagame.utils

import santagame.game.SantaGame
import santagame.model.Card

class TestUtils {

    static final ROTATED_INDICES = [2, 5, 8, 1, 4, 7, 0, 3, 6]

    static Card[] createTestCards(boolean rotated) {
        Card[] cards = SantaGame.createCards()
        if (!rotated) {
            return cards
        }
        return ROTATED_INDICES.collect { int i ->
            cards[i].rotateOnce()
        }
    }
}
