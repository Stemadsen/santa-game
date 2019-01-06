package santagame.model

import spock.lang.Specification

import static santagame.model.BodyPart.LEGS
import static santagame.model.BodyPart.TORSO
import static santagame.model.Color.BLUE
import static santagame.model.Color.GREEN

class SantaPartSpec extends Specification {

    def "two SantaParts should be equal if and only if they have the same color and body part"() {
        given:
            SantaPart santaPart1 = new SantaPart(BLUE, TORSO)
            SantaPart santaPart2 = new SantaPart(_color, _bodyPart)

        expect:
            (santaPart1 == santaPart2) == _expected

        where:
            _color | _bodyPart | _expected
            BLUE   | TORSO     | true
            BLUE   | LEGS      | false
            GREEN  | TORSO     | false
    }
}
