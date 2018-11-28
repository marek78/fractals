/**
 * Any copyright is dedicated to the Public Domain.
 * http://creativecommons.org/publicdomain/zero/1.0/
 * You can copy, modify, distribute and perform the work, 
 * even for commercial purposes, all without asking permission.
 */
package com.szajna.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    public static final int LOG_LEVEL_ERROR = 1;
    public static final int LOG_LEVEL_WARN = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_DEBUG = 4;
    public static final int LOG_LEVEL_VERBOSE = 5;
    
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static int logLevel = LOG_LEVEL_DEBUG;
    
    public static void setLogLevel(int level) {
        logLevel = level;
    }
    public static void v(String tag, String msg) {
        if (logLevel >= LOG_LEVEL_VERBOSE) {
            printLog("V: ", tag, msg);
        }
    }
    public static void d(String tag, String msg) {
        if (logLevel >= LOG_LEVEL_DEBUG) {
            printLog("D: ", tag, msg);
        }
    }
    public static void i(String tag, String msg) {
        if (logLevel >= LOG_LEVEL_INFO) {
            printLog("I: ", tag, msg);
        }
    }
    public static void w(String tag, String msg) {
        if (logLevel >= LOG_LEVEL_WARN) {
            printLog("W: ", tag, msg);
        }
    }
    public static void e(String tag, String msg) {
        if (logLevel >= LOG_LEVEL_ERROR) {
            printLog("E: ", tag, msg);
        }
    }
    
    private static void printLog(String level, String tag, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(df.format(new Date()));
        sb.append(" ");
        sb.append(Thread.currentThread().getName());
        sb.append(" [");
        sb.append(tag);
        sb.append("] " );
        sb.append(level);
        sb.append(msg);
        System.out.println(sb.toString());
    }
}
