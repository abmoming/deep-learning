package pers.gym.io.bigfiledownload.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * <p>日志工具
 *
 * @author gym on 2023-03-31 21:14
 */
public class LogUtil {

    public static boolean debug = false;

    public static final DateTimeFormatter TIME_MATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    public static void debug(String msg, Object... args) {
        if (debug) {
            print(msg, "-DEBUG-", args);
        }
    }

    public static void info(String msg, Object... args) {
        print(msg, "-INFO-", args);
    }

    public static void error(String msg, Object... args) {
        print(msg, "-ERROR-", args);
    }

    public static void print(String msg, String level, Object... args) {
        if (args != null && args.length > 0) {
            msg = String.format(msg.replace("{}", "%s"), args);
        }
        System.out.println(LocalDateTime.now().format(TIME_MATTER)
                + " "
                + Thread.currentThread().getName()
                + level
                + msg);
    }
}