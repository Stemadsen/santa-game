package santagame.model

import spock.lang.Specification

import static santagame.utils.TestUtils.createTestCards

class CardLayoutSpec extends Specification {

    CardLayout cardLayout = new CardLayout(createTestCards(true))

    def "it should create the canonical representation"() {
        when:
            CardLayout canonicalRepresentation = cardLayout.toCanonicalRepresentation()

        then:
            canonicalRepresentation.currentLayout*.index == (0..8).toList()
    }
}
