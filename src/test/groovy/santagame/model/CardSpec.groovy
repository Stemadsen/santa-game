package santagame.model

import santagame.game.SantaGame
import spock.lang.Specification
import spock.lang.Unroll

import static santagame.model.BodyPart.LEGS
import static santagame.model.BodyPart.TORSO
import static santagame.model.Color.GREEN
import static santagame.model.Color.RED
import static santagame.model.Color.BLUE
import static santagame.model.Color.YELLOW

class CardSpec extends Specification {
    static final SantaPart[] TEST_SANTA_PARTS = [
            new SantaPart(RED, TORSO),
            new SantaPart(GREEN, LEGS),
            new SantaPart(BLUE, TORSO),
            new SantaPart(YELLOW, LEGS),
    ]

    static final Card TEST_CARD = new Card(TEST_SANTA_PARTS, 4)

    Card[] cards

    void setup() {
        cards = SantaGame.createCards()
        resetTestCardRotation()
    }

    def "it should instantiate Card with index 0..8 and 4 SantaParts"() {
        when:
            Card card = new Card(TEST_SANTA_PARTS, _index)

        then:
            card.index == _index
            card.santaParts == TEST_SANTA_PARTS

        where:
            _index << (0..8)
    }

    @Unroll
    def "it should throw an error when calling constructor with index #_index and #_santaParts SantaParts"() {
        when:
            new Card((1.._santaParts).collect { null } as SantaPart[], _index)

        then:
            thrown(AssertionError)

        where:
            _index | _santaParts
            0      | 3
            0      | 5
            -1     | 4
            9      | 4
    }

    def "two cards should be equal if and only if they have the same index and SantaParts"() {
        given:
            SantaPart part = new SantaPart(RED, LEGS)
            Card card1 = new Card([part, part, part, new SantaPart(BLUE, TORSO)] as SantaPart[], 4)
            Card card2 = new Card([part, part, part, new SantaPart(_color, _bodyPart)] as SantaPart[], _index)

        expect:
            (card1 == card2) == _expected

        where:
            _color | _bodyPart | _index | _expected
            BLUE   | TORSO     | 4      | true
            BLUE   | TORSO     | 5      | false
            BLUE   | LEGS      | 4      | false
            GREEN  | TORSO     | 4      | false
    }

    def "it should rotate counter-clockwise"() {
        given:
            Card card = TEST_CARD

        when: 'the card is rotated _degree times'
            Card rotatedCard = (1.._degree).inject(card) { result, i -> result.rotateOnce() }

        then: 'the original card was rotated'
            rotatedCard.is(card)

        and: 'as expected'
            with(card) {
                rotation == _degree
                lowerPart.color == TEST_SANTA_PARTS[3 - _degree].color
            }

        where:
            _degree << (1..3)
    }

    @Unroll
    def "it should correctly rotate to rotation degree #_degree"() {
        given:
            Card card = TEST_CARD

        when:
            Card rotatedCard = card.rotateToDegrees(_degree)

        then: 'the original card was rotated'
            rotatedCard.is(card)

        and: 'as expected'
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

    def "cloning should preserve SantaParts and their order + index"() {
        given:
            Card original = TEST_CARD.rotateToDegrees(2)

        when:
            Card clone = original.clone()

        then:
            with(clone) {
                rightPart == original.rightPart
                upperPart == original.upperPart
                leftPart == original.leftPart
                lowerPart == original.lowerPart
                index == original.index
            }
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
