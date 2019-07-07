package com.game.utils.log;

public class LogUtil {
    public static void log(String message) {
        System.out.println(message);
    }

    public static void logError(String message, Exception ex) {
        System.out.println(message);
    }

    public static void logError(String message) {
        System.out.println("ERROR:" + message);
    }
}
