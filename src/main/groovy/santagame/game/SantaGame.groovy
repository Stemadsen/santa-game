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
        int MAX_ITERATIONS = 100000

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
        Log.info("\nFound ${result.solutions.size()} solutions:\n")
        result.solutions.each {
            Log.info("${it.toString()}")
        }
    }

    static Card[] createCards() {
        return [
                new Card(
                        new SantaPart(BLUE, TORSO),
                        new SantaPart(YELLOW, TORSO),
                        new SantaPart(RED, TORSO),
                        new SantaPart(GREEN, TORSO)
                ),
                new Card(
                        new SantaPart(BLUE, TORSO),
                        new SantaPart(RED, TORSO),
                        new SantaPart(YELLOW, TORSO),
                        new SantaPart(GREEN, TORSO)
                ),
                new Card(
                        new SantaPart(BLUE, LEGS),
                        new SantaPart(YELLOW, TORSO),
                        new SantaPart(GREEN, TORSO),
                        new SantaPart(RED, LEGS)
                ),
                new Card(
                        new SantaPart(BLUE, TORSO),
                        new SantaPart(GREEN, TORSO),
                        new SantaPart(YELLOW, TORSO),
                        new SantaPart(RED, LEGS)
                ),
                new Card(
                        new SantaPart(BLUE, LEGS),
                        new SantaPart(RED, TORSO),
                        new SantaPart(GREEN, LEGS),
                        new SantaPart(YELLOW, LEGS)
                ),
                new Card(
                        new SantaPart(BLUE, TORSO),
                        new SantaPart(RED, LEGS),
                        new SantaPart(YELLOW, LEGS),
                        new SantaPart(GREEN, TORSO)
                ),
                new Card(
                        new SantaPart(BLUE, LEGS),
                        new SantaPart(YELLOW, LEGS),
                        new SantaPart(RED, TORSO),
                        new SantaPart(GREEN, LEGS)
                ),
                new Card(
                        new SantaPart(BLUE, LEGS),
                        new SantaPart(GREEN, LEGS),
                        new SantaPart(RED, TORSO),
                        new SantaPart(YELLOW, LEGS)
                ),
                new Card(
                        new SantaPart(BLUE, TORSO),
                        new SantaPart(GREEN, LEGS),
                        new SantaPart(YELLOW, LEGS),
                        new SantaPart(RED, LEGS)
                )
        ]
    }
}
