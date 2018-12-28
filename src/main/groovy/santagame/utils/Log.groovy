package santagame.utils

class Log {
    static void debug(Object message, long startTime = 0, int p = -1, int i = -1) {
        File file = logFile
        if (!file.exists()) file.createNewFile()

        String prolog = getProLog(startTime, p, i)
        String logString = "${prolog ? "$prolog " : ''}${message}"

        file.append("${logString}\n", 'UTF-8')
    }

    static void info(Object message, long startTime = 0) {
        File file = logFile
        if (!file.exists()) file.createNewFile()

        String prolog = getProLog(startTime)
        String logString = "${prolog ? "$prolog " : ''}${message}"

        file.append("${logString}\n", 'UTF-8')

        println(logString)
    }

    static String getProLog(long startTime, int p = -1, int i = -1) {
        if (!startTime) return ''
        p > -1 && i > -1 ? "${getTimestamp(startTime)} [pos. ${p}] [card ${i}]" : "${getTimestamp(startTime)}"
    }

    static String getTimestamp(long startTime) {
        String secondsString = (System.currentTimeMillis() - startTime) / 1000
        if (!secondsString.contains('.')) {
            return "[${secondsString}.000]"
        }
        String[] parts = secondsString.split(/\./)
        "[${parts[0]}.${parts[1].padRight(3, '0')}]"
    }

    static File getLogFile() {
        new File('C:\\Users\\Stemadsen\\Dropbox\\_Privat\\Programmering\\game.SantaGame\\logs\\game.SantaGame.log')
    }
}
