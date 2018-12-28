package santagame.model

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
