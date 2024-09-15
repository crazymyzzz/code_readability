

/**
 * This class is intended to be a central place for the jdk to
 * log timing events of interest.  There is pre-defined event
 * of startTime, as well as a general
 * mechanism of setting aribtrary times in an array.
 * All unreserved times in the array can be used by callers
 * in application-defined situations.  The caller is responsible
 * for setting and getting all times and for doing whatever
 * analysis is interesting; this class is merely a central container
 * for those timing values.
 * Note that, due to the variables in this class being static,
 * use of particular time values by multiple applets will cause
 * confusing results.  For example, if plugin runs two applets
 * simultaneously, the initTime for those applets will collide
 * and the results may be undefined.
 * <P>
 * To automatically track startup performance in an app or applet,
 * use the command-line parameter sun.perflog as follows:<BR>
 *     -Dsun.perflog[=file:<filename>]
 * <BR>
 * where simply using the parameter with no value will enable output
 * to the console and a value of "file:<filename>" will cause
 * that given filename to be created and used for all output.
 * <P>
 * By default, times are measured using System.currentTimeMillis().  To use
 * System.nanoTime() instead, add the command-line parameter:<BR>
       -Dsun.perflog.nano=true
 * <BR>
 * <P>
 * <B>Warning: Use at your own risk!</B>
 * This class is intended for internal testing
 * purposes only and may be removed at any time.  More
 * permanent monitoring and profiling APIs are expected to be
 * developed for future releases and this class will cease to
 * exist once those APIs are in place.
 * @author Chet Haase
 */
public class PerformanceLogger {

    

    /**
     * Outputs all data to parameter-specified Writer object
     */
    public static void outputLog(Writer writer) {
        if (loggingEnabled()) {
            try {
                synchronized(times) {
                    for (int i = 0; i < times.size(); ++i) {
                        TimeData td = times.get(i);
                        if (td != null) {
                            writer.write(i + " " + td.getMessage() + ": " +
                                         (td.getTime() - baseTime) + "\n");

                        }
                    }
                }
                writer.flush();
            } catch (Exception e) {
                System.out.println(e + ": Writing performance log to " +
                                   writer);
            }
        }
    }

    
}
