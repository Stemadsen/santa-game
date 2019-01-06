package santagame.model

import spock.lang.Specification

import static santagame.utils.TestUtils.createTestCards

class SolutionContainerSpec extends Specification {

    def "it should identify canonical representations of a deck of cards"() {
        given: 'a canonical representation in the solution set'
            SolutionContainer solutionManager = new SolutionContainer(solutions: [new CardLayout(createTestCards(false))])

        expect: 'the rotated variant is found in the solution set'
            solutionManager.contains(createTestCards(true))
    }

    def "it should add canonical representations of a deck of cards"() {
        given:
            SolutionContainer solutionManager = new SolutionContainer()

        expect:
            !solutionManager.solutions

        when: 'a non-canonical layout is added to the solution set'
            solutionManager.add(createTestCards(true))

        then: 'the solution was added as a canonical representation'
            solutionManager.solutions.size() == 1
            solutionManager.solutions[0].currentLayout*.index == (0..8) as List
            solutionManager.contains(createTestCards(false))
    }
}
