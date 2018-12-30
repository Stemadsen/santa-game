package santagame.utils

class Log {

    static final String UTF8 = 'UTF-8'
    private static final String LOG_DIR_NAME = 'C:\\Users\\Stemadsen\\Dropbox\\_Privat\\Programmering\\SantaGame\\logs'
    private static final String LOG_FILE_NAME = 'SantaGame.log'
    private static File logFile

    static void debug(Object message, long startTime = 0, int p = -1, int i = -1) {
        String prolog = getProLog(startTime, p, i)
        String logString = "${prolog ? "$prolog " : ''}${message}"

        getLogFile().append("${logString}\n", UTF8)
    }

    static void info(Object message, long startTime = 0) {
        String prolog = getProLog(startTime)
        String logString = "${prolog ? "$prolog " : ''}${message}"

        getLogFile().append("${logString}\n", UTF8)

        println(logString)
    }

    static File getLogFile() {
        if (!logFile) {
            initLogFile()
        }
        return logFile
    }

    private static String getProLog(long startTime, int p = -1, int i = -1) {
        if (!startTime) return ''
        p > -1 && i > -1 ? "${getTimestamp(startTime)} [pos. ${p}] [card ${i}]" : "${getTimestamp(startTime)}"
    }

    private static String getTimestamp(long startTime) {
        String secondsString = (System.currentTimeMillis() - startTime) / 1000
        if (!secondsString.contains('.')) {
            return "[${secondsString}.000]"
        }
        String[] parts = secondsString.split(/\./)
        "[${parts[0]}.${parts[1].padRight(3, '0')}]"
    }

    private static void initLogFile() {
        File logDir = new File(LOG_DIR_NAME)
        if (!logDir.exists()) {
            logDir.mkdir()
        }
        logFile = new File("${LOG_DIR_NAME}\\${LOG_FILE_NAME}")
        logFile.delete()
    }
}
