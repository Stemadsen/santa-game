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

        init()
        runGame(DEBUG)
    }

    static void init() {
        File logFile = Log.logFile
        if (logFile.exists()) logFile.delete()
    }

    static void runGame(boolean debug, int maxIterations = 0) {
        Game game = new Game(createCards())

        Log.debug("Running algorithm ...\n")
        long start = System.currentTimeMillis()

        def (solutions, iterations) = game.runAlgorithm(start, debug, maxIterations)

        long end = System.currentTimeMillis()
        def ms = end - start
        if (iterations) {
            double iterationsPerMs = (iterations / ms).toDouble().round(3)
            Log.debug("\nRan ${iterations} iterations in ${ms / 1000} seconds (${iterationsPerMs} iterations/ms)")
        } else {
            Log.debug("\nRan algorithm in ${ms / 1000} seconds")
        }
        Log.debug("Found ${solutions.size()} solutions${solutions ? ':\n' : ''}")
        solutions.each {
            Log.debug(it.toString())
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
