package santagame.model

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class SantaPart {
    Color color
    BodyPart bodyPart

    SantaPart(Color color, BodyPart bodyPart) {
        this.color = color
        this.bodyPart = bodyPart
    }

    @Override
    String toString() {
        "$color $bodyPart"
    }
}
