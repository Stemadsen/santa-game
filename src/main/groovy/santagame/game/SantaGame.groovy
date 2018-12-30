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

        Log.info("Running algorithm ...\n")
        long start = System.currentTimeMillis()

        AlgorithmOutput output = game.runAlgorithm(start, debug, maxIterations)

        long end = System.currentTimeMillis()
        long ms = end - start
        Log.info("\nRan algorithm in ${ms / 1000} seconds")
        Log.info("\nRan ${output.iterationsRun} iterations total (${(output.iterationsRun / ms).toDouble().round(3)} iterations/ms)")
        Log.info("\nFound ${output.solutions.size()} solutions:\n")
        output.solutions.each {
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
