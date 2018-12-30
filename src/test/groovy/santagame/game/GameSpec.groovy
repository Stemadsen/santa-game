package santagame.game

import santagame.model.BodyPart
import santagame.model.Card
import santagame.model.Color
import santagame.model.SantaPart
import spock.lang.Specification

import static santagame.model.BodyPart.LEGS
import static santagame.model.BodyPart.TORSO
import static santagame.model.Color.BLUE
import static santagame.model.Color.YELLOW

class GameSpec extends Specification {
    Card[] cards
    Game game

    void setup() {
        cards = SantaGame.createCards()
        game = new Game(cards)
    }

    void "test isOnBoard"() {
        expect:
            !game.isOnBoard(i)

        when:
            game.boardIndices[3] = i

        then:
            game.isOnBoard(i)

        where:
            i << (0..8)
    }

    void "it should place card"() {
        given:
            int i = _i as int
            int p = _p as int
        Card card = cards[i]

        expect:
            !game.isOnBoard(i)

        when:
            game.placeOnBoard(card, i, p)

        then:
            game.boardIndices[p] == i
            game.board[p] == card

        where:
            [_i, _p] << [(0..8), (0..8)].combinations()
    }

    void "it should remove card from board"() {
        given:
            int i = _i as int
            int p = _p as int
            Card card = cards[i]
            game.placeOnBoard(card, i, p)

        when:
            game.removeFromBoard(p)

        then:
            assertBoardPositionEmpty(p)

        where:
            [_i, _p] << [(0..8), (0..8)].combinations()
    }

    void "it should correctly rotate first card up to 3 times, but not 4"() {
        given:
            game.startingCardRotation = 0

        when: 'we try rotating the starting card'
            boolean rotated = game.rotateStartingCard()

        then: 'the card was rotated'
            rotated
            game.startingCardRotation == 1

        when: 'we try rotating the starting card a second time'
            rotated = game.rotateStartingCard()

        then: 'the card was rotated'
            rotated
            game.startingCardRotation == 2

        when: 'we try rotating the starting card a third time'
            rotated = game.rotateStartingCard()

        then: 'the card was rotated'
            rotated
            game.startingCardRotation == 3

        when: 'we try rotating the starting card a fourth time'
            rotated = game.rotateStartingCard()

        then: 'the card was not rotated'
            !rotated
            game.startingCardRotation == 3
    }

    void "it should correctly change first card up to 8 times"() {
        given:
            game.nexts[0] = 0

        and: 'the starting card has been rotated'
            game.rotateStartingCard()

        when: 'we change the starting card'
            game.changeStartingCard()

        then: 'the starting card index has been increased by 1'
            game.nexts[0] == 1

        and: 'the starting card rotation has been reset'
            game.startingCardRotation == 0

        when: 'we change the starting card 7 more times'
            7.times { game.changeStartingCard() }

        then: 'the starting card index has been increased by 7'
            game.nexts[0] == 8
    }

    void "it should correctly return rotated card for left neighbor"() {
        given:
        Color color = _color as Color
            game.placeOnBoard(cardWithRightPart(color), -1, 0)

        when:
            Card result = game.getRotatedCard(1)

        then:
            result.leftPart.color == color

        where:
            _color << Color.values()
    }

    void "it should correctly return rotated card for upper neighbor"() {
        given:
            Color color = _color as Color
            game.placeOnBoard(cardWithLowerPart(color), -1, 0)
            game.placeOnBoard(cards[1], 1, 1)
            game.placeOnBoard(cards[2], 2, 2)

        when:
            Card result = game.getRotatedCard(3)

        then:
            result.upperPart.color == color

        where:
            _color << Color.values()
    }

    void "test isValid for left neighbor"() {
        given: 'a first card whose right part is blue legs'
        BodyPart bodyPart = _bodyPart as BodyPart
            Color color = _color as Color
            game.placeOnBoard(cardWithRightPart(BLUE, LEGS), -1, 0)

        when: 'we check if a card is valid to the right of the first card'
            boolean valid = game.isValid(cardWithLeftPart(color, bodyPart), 1)

        then: 'the card is valid iff its left part is a blue torso'
            valid == (color == BLUE && bodyPart == TORSO)

        where:
            [_color, _bodyPart] << [Color.values(), BodyPart.values()].combinations()
    }

    void "test isValid for upper neighbor"() {
        given: 'a first card whose lower part is yellow torso'
            BodyPart bodyPart = _bodyPart as BodyPart
            Color color = _color as Color
            game.placeOnBoard(cardWithLowerPart(YELLOW, TORSO), -1, 0)
            game.placeOnBoard(cards[1], 1, 1)
            game.placeOnBoard(cards[2], 2, 2)

        when: 'we check if a card is valid below the first card'
            boolean valid = game.isValid(cardWithUpperPart(color, bodyPart), 3)

        then: 'the card is valid iff its upper part is yellow legs'
            valid == (color == YELLOW && bodyPart == LEGS)

        where:
            [_color, _bodyPart] << [Color.values(), BodyPart.values()].combinations()
    }


    Card cardWithRightPart(Color color, BodyPart bodyPart = LEGS) {
        def otherColors = Color.values() - color
        def otherBodyPart = (BodyPart.values() - bodyPart)[0]
        new Card([
                new SantaPart(color, bodyPart),
                new SantaPart(otherColors[0], otherBodyPart),
                new SantaPart(otherColors[1], otherBodyPart),
                new SantaPart(otherColors[2], otherBodyPart),
        ] as SantaPart[])
    }

    Card cardWithUpperPart(Color color, BodyPart bodyPart = LEGS) {
        def otherColors = Color.values() - color
        def otherBodyPart = (BodyPart.values() - bodyPart)[0]
        new Card([
                new SantaPart(otherColors[0], otherBodyPart),
                new SantaPart(color, bodyPart),
                new SantaPart(otherColors[1], otherBodyPart),
                new SantaPart(otherColors[2], otherBodyPart),
        ] as SantaPart[])
    }

    Card cardWithLeftPart(Color color, BodyPart bodyPart = LEGS) {
        def otherColors = Color.values() - color
        def otherBodyPart = (BodyPart.values() - bodyPart)[0]
        new Card([
                new SantaPart(otherColors[0], otherBodyPart),
                new SantaPart(otherColors[1], otherBodyPart),
                new SantaPart(color, bodyPart),
                new SantaPart(otherColors[2], otherBodyPart),
        ] as SantaPart[])
    }

    Card cardWithLowerPart(Color color, BodyPart bodyPart = LEGS) {
        def otherColors = Color.values() - color
        def otherBodyPart = (BodyPart.values() - bodyPart)[0]
        new Card([
                new SantaPart(otherColors[0], otherBodyPart),
                new SantaPart(otherColors[1], otherBodyPart),
                new SantaPart(otherColors[2], otherBodyPart),
                new SantaPart(color, bodyPart),
        ] as SantaPart[])
    }

    void assertBoardPositionEmpty(int p) {
        assert !game.board[p]
        assert game.boardIndices[p] == -1
    }
}
