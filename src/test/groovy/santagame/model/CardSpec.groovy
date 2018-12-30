package santagame.model

import santagame.game.SantaGame
import spock.lang.Specification
import spock.lang.Unroll

import static santagame.model.Color.GREEN
import static santagame.model.Color.RED
import static santagame.model.Color.BLUE
import static santagame.model.Color.YELLOW

class CardSpec extends Specification {
    static final SantaPart[] TEST_SANTA_PARTS = [
            new SantaPart(RED, BodyPart.TORSO),
            new SantaPart(GREEN, BodyPart.LEGS),
            new SantaPart(BLUE, BodyPart.TORSO),
            new SantaPart(YELLOW, BodyPart.LEGS),
    ]

    static final Card TEST_CARD = new Card(TEST_SANTA_PARTS)

    Card[] cards

    void setup() {
        cards = SantaGame.createCards()
        resetTestCardRotation()
    }

    def "it should correctly rotate to a specific color on the left"() {
        given:
        Color color = _color as Color
            Card card = cards[5]
            List originalBodyParts = card.santaParts*.bodyPart
            List originalColors = card.santaParts*.color

        when: 'we rotate to obtain a left color'
            card.rotateToLeftColor(color)

        then: 'the left color is as expected'
            card.leftPart.color == color

        and: 'the original colors and body parts have not been replaced'
            card.santaParts*.color.sort() == originalColors.sort()
            card.santaParts*.bodyPart.sort() == originalBodyParts.sort()

        where:
            _color << Color.values()
    }

    def "it should correctly rotate to a specific color on the top"() {
        given:
            Color color = _color as Color
            Card card = cards[5]
            List originalBodyParts = card.santaParts*.bodyPart
            List originalColors = card.santaParts*.color

        when: 'we rotate to obtain a top color'
            card.rotateToUpperColor(color)

        then: 'the top color is as expected'
            card.upperPart.color == color

        and: 'the original colors and body parts have not been replaced'
            card.santaParts*.color.sort() == originalColors.sort()
            card.santaParts*.bodyPart.sort() == originalBodyParts.sort()

        where:
            _color << Color.values()
    }

    @Unroll
    def "it should correctly rotate to rotation degree #_degree"() {
        given:
            Card card = TEST_CARD

        when:
            Card rotatedCard = card.rotateToDegrees(_degree)

        then:
            rotatedCard == card
            with(card) {
                rotation == _degree
                lowerPart.color == TEST_SANTA_PARTS[3 - _degree].color
                lowerPart.bodyPart == TEST_SANTA_PARTS[3 - _degree].bodyPart
            }

        where:
            _degree << (0..3)
    }

    def "it should not rotate to a negative rotation degree"() {
        given:
            Card card = TEST_CARD

        when:
            card.rotateToDegrees(-1)

        then:
            thrown(AssertionError)
    }

    def "test getSantaParts"() {
        expect:
            TEST_CARD.santaParts == TEST_SANTA_PARTS
    }

    def "test getRightPart"() {
        expect:
            TEST_CARD.rightPart == TEST_SANTA_PARTS[0]

        and: 'it also works after rotation'
            TEST_CARD.rotateToUpperColor(TEST_SANTA_PARTS[0].color).rightPart == TEST_SANTA_PARTS[3]
    }

    def "test getUpperPart"() {
        expect:
            TEST_CARD.upperPart == TEST_SANTA_PARTS[1]

        and: 'it also works after rotation'
            TEST_CARD.rotateToLeftColor(TEST_SANTA_PARTS[0].color).upperPart == TEST_SANTA_PARTS[3]
    }

    def "test getLeftPart"() {
        expect:
            TEST_CARD.leftPart == TEST_SANTA_PARTS[2]

        and: 'it also works after rotation'
            TEST_CARD.rotateToUpperColor(TEST_SANTA_PARTS[0].color).leftPart == TEST_SANTA_PARTS[1]
    }

    def "test getLowerPart"() {
        expect:
            TEST_CARD.lowerPart == TEST_SANTA_PARTS[3]

        and: 'it also works after rotation'
            TEST_CARD.rotateToUpperColor(TEST_SANTA_PARTS[0].color).lowerPart == TEST_SANTA_PARTS[2]
    }

    private static void resetTestCardRotation() {
        TEST_CARD.rotateToUpperColor(TEST_SANTA_PARTS[1].color)
    }
}
