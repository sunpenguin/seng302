package seng302.team18.util;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * Provides convenience methods to set prefixed for System.out and System.err, and preserves original streams
 */
public class NautiLogger {
    private static final PrintStream stdout = System.out;
    private static final PrintStream stderr = System.err;


    /**
     * @return time stamp for the current time
     */
    public static String getTimeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + ": ";
    }


    /**
     * Sets the default output of prefixed with timestamps
     */
    public static void setDefaultOutput() {
        setOutputPrefix(NautiLogger::getTimeStamp);
        setErrorPrefix(NautiLogger::getTimeStamp);
    }


    /**
     * Adds a prefix to System.out
     *
     * @param prefix the prefix
     */
    public static void setOutputPrefix(Supplier<String> prefix) {
        System.out.flush();
        System.setOut(new PrefixedPrintStream(stdout, prefix));
    }


    /**
     * Adds a prefix to System.err
     *
     * @param prefix the prefix
     */
    public static void setErrorPrefix(Supplier<String> prefix) {
        System.err.flush();
        System.setErr(new PrefixedPrintStream(stderr, prefix));
    }


    /**
     * @return the original System.out (stdout)
     */
    public static PrintStream getStdout() {
        return stdout;
    }


    /**
     * @return the original System.err (stderr)
     */
    public static PrintStream getStderr() {
        return stderr;
    }
}


