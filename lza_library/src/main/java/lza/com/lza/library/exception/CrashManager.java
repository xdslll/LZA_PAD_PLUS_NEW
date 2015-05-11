package lza.com.lza.library.exception;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/7/15.
 */
public class CrashManager {

    public static void registerHandler() {
        Thread.UncaughtExceptionHandler currentHandler = Thread
                .getDefaultUncaughtExceptionHandler();

        // Register if not already registered
        if (!(currentHandler instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(currentHandler));
        }
    }

}
