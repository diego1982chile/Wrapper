package cl.forevision.wrapper.helpers;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import javafx.application.Platform;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by root on 16-03-23.
 */
public class PlatformHelper {

    private static String OS = null;

    private static Logger logger;
    static LogHelper fh;

    public PlatformHelper() {
        fh = LogHelper.getInstance();
        logger = Logger.getLogger(TokenHelper.class.getName());
        logger.addHandler(fh);
    }

    public static void run(Runnable treatment) {

        if(treatment == null) {
            throw new IllegalArgumentException("The treatment to perform can not be null");
        }

        if(Platform.isFxApplicationThread()) {
            treatment.run();
        }
        else {
            Platform.runLater(treatment);
        }
    }

    private static String getOsName() {

        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    private static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    private static boolean isUnix() {
        return getOsName().startsWith("Linux");
    }

    private static Long getWindowsProcessId(final Process process) {
        /* determine the pid on windows plattforms */
        try {
            Field f = process.getClass().getDeclaredField("handle");
            f.setAccessible(true);
            long handl = f.getLong(process);
            Kernel32 kernel = Kernel32.INSTANCE;
            WinNT.HANDLE handle = new WinNT.HANDLE();
            handle.setPointer(Pointer.createConstant(handl));
            int ret = kernel.GetProcessId(handle);
            logger.log(Level.INFO, "Detected pid: {}", ret);
            return Long.valueOf(ret);
        } catch (final IllegalAccessException | NoSuchFieldException nsfe) {
            logger.log(Level.SEVERE, "Could not find PID for child process due to {}", nsfe);
        }
        return null;
    }

    private static synchronized long getUnixProcessId(Process p) {
        Long pid = null;

        try {
            if (p.getClass().getName().equals("java.lang.UNIXProcess")) {
                Field f = p.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getLong(p);
                f.setAccessible(false);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not find PID for child process due to {}", e);
        }
        return pid;
    }

    private static Long getProcessId(Process p) {

        if (isUnix()) {
            return getUnixProcessId(p);
        }
        if (isWindows()) {
            return getWindowsProcessId(p);
        }
        logger.log(Level.SEVERE, "Not supported platform");
        return null;
    }

    static void killProcess(Process p) throws IOException {
        Long pid = getProcessId(p);
        String cmd;

        if (isUnix()) {
            cmd = "kill -9 " + pid;
            Runtime.getRuntime().exec(cmd);
            return;
        }
        if (isWindows()) {
            cmd = "taskkill /F /PID " + pid;
            Runtime.getRuntime().exec(cmd);
            return;
        }
        logger.log(Level.SEVERE, "Not supported platform");
    }

    static void killBrowsers() throws IOException {

        String cmd;

        if (isUnix()) {
            cmd = "killall -9 chrome";
            Runtime.getRuntime().exec(cmd);
            return;
        }
        if (isWindows()) {
            cmd = "taskkill /f /im chrome.exe";
            Runtime.getRuntime().exec(cmd);
            return;
        }
        logger.log(Level.SEVERE, "Not supported platform");
    }

}
