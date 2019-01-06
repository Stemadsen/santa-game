package santagame.game

import santagame.model.Card
import santagame.model.SantaPart
import santagame.utils.Log

import static santagame.model.BodyPart.LEGS
import static santagame.model.BodyPart.TORSO
import static santagame.model.Color.RED
import static santagame.model.Color.GREEN
import static santagame.model.Color.BLUE
import static santagame.model.Color.YELLOW

class SantaGame {
    static void main(String[] args) {
        boolean DEBUG = false

        runGame(DEBUG)
    }

    static void runGame(boolean debug, int maxIterations = 0) {
        Game game = new Game(createCards())

        Log.info("Playing game ...\n")
        long start = System.currentTimeMillis()

        GameResult result = game.playGame(start, debug, maxIterations)

        long end = System.currentTimeMillis()
        long ms = end - start
        Log.info("\nPlayed game in ${ms / 1000} seconds")
        Log.info("\nRan ${result.iterations} iterations (${(result.iterations / ms).toDouble().round(3)} iterations/ms)")
        Log.info("\nFound ${result.solutionContainer.solutions.size()} unique solutions:\n")
        result.solutionContainer.solutions.each {
            Log.info(it)
        }
    }

    static Card[] createCards() {
        return [
                new Card([new SantaPart(BLUE, TORSO),
                          new SantaPart(YELLOW, TORSO),
                          new SantaPart(RED, TORSO),
                          new SantaPart(GREEN, TORSO)] as SantaPart[],
                        0),
                new Card([new SantaPart(BLUE, TORSO),
                          new SantaPart(RED, TORSO),
                          new SantaPart(YELLOW, TORSO),
                          new SantaPart(GREEN, TORSO)] as SantaPart[],
                        1),
                new Card([new SantaPart(BLUE, LEGS),
                          new SantaPart(YELLOW, TORSO),
                          new SantaPart(GREEN, TORSO),
                          new SantaPart(RED, LEGS)] as SantaPart[],
                        2),
                new Card([new SantaPart(BLUE, TORSO),
                          new SantaPart(GREEN, TORSO),
                          new SantaPart(YELLOW, TORSO),
                          new SantaPart(RED, LEGS)] as SantaPart[],
                        3),
                new Card([new SantaPart(BLUE, LEGS),
                          new SantaPart(RED, TORSO),
                          new SantaPart(GREEN, LEGS),
                          new SantaPart(YELLOW, LEGS)] as SantaPart[],
                        4),
                new Card([new SantaPart(BLUE, TORSO),
                          new SantaPart(RED, LEGS),
                          new SantaPart(YELLOW, LEGS),
                          new SantaPart(GREEN, TORSO)] as SantaPart[],
                        5),
                new Card([new SantaPart(BLUE, LEGS),
                          new SantaPart(YELLOW, LEGS),
                          new SantaPart(RED, TORSO),
                          new SantaPart(GREEN, LEGS)] as SantaPart[],
                        6),
                new Card([new SantaPart(BLUE, LEGS),
                          new SantaPart(GREEN, LEGS),
                          new SantaPart(RED, TORSO),
                          new SantaPart(YELLOW, LEGS)] as SantaPart[],
                        7),
                new Card([new SantaPart(BLUE, TORSO),
                          new SantaPart(GREEN, LEGS),
                          new SantaPart(YELLOW, LEGS),
                          new SantaPart(RED, LEGS)] as SantaPart[],
                        8)
        ]
    }
}
