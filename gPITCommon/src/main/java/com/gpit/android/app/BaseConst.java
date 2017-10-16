/**
 * Define application constant variable
 */

package com.gpit.android.app;


import android.content.Context;

public class BaseConst {
    private final static String TAG = BaseConst.class.getSimpleName();

    public static final int LOG_LEVEL_VERBOSE = 0;
    public static final int LOG_LEVEL_DEBUG = 1;
    public static final int LOG_LEVEL_INFO = 2;
    public static final int LOG_LEVEL_WARNING = 3;
    public static final int LOG_LEVEL_ERROR = 4;
    public enum LogLevel {
        LOG_LEVEL_VERBOSE,
        LOG_LEVEL_DEBUG,
        LOG_LEVEL_INFO,
        LOG_LEVEL_WARNING,
        LOG_LEVEL_ERROR,
        LOG_LEVEL_NONE,
    }

    // Mandatory required field should be set before use this class
    public static boolean DEV_MODE_ON = true;

    public static boolean SSL_SUPPORT = true;

	// Remote Logger for Logentires
	public static String REMOTE_LOGGER_TOKEN = null;
    public static String MINT_TOKEN = null;

	public static LogLevel REMOTE_LOGGER_LOGGING_LEVEL = LogLevel.LOG_LEVEL_VERBOSE;

    /********************* Product Configuration ***********************/
    public static String PRODUCTION_REMOTE_LOGGER_TOKEN = "36471fb1-0dc0-4ee9-844d-da424ec92391";
    public static String PRODUCTION_MINT_TOKEN = null;
    public static LogLevel PRODUCTION_REMOTE_LOGGER_LOGGING_LEVEL = LogLevel.LOG_LEVEL_DEBUG;

    /********************* Dev Configuration ***********************/
    public static String DEV_REMOTE_LOGGER_TOKEN = "8cb9b78d-48ec-4c93-9ad2-d7e1f461034f";
    public static String DEV_MINT_TOKEN = null;
    public static LogLevel DEV_REMOTE_LOGGER_LOGGING_LEVEL = LogLevel.LOG_LEVEL_VERBOSE;

    static {
        // Unfortunately, we can't set the debug mode at here due to bug in library
        // https://code.google.com/p/android/issues/detail?id=52962
        // DEV_MODE_ON = BuildConfig.DEBUG;
        // RemoteLogger.i(TAG, "debug mode: " + DEV_MODE_ON);
    }

    public static void init(Context context) {
        if (BaseConst.DEV_MODE_ON) {
            // Log
            BaseConst.REMOTE_LOGGER_TOKEN = BaseConst.DEV_REMOTE_LOGGER_TOKEN;
            BaseConst.REMOTE_LOGGER_LOGGING_LEVEL = BaseConst.DEV_REMOTE_LOGGER_LOGGING_LEVEL;

            BaseConst.MINT_TOKEN = BaseConst.DEV_MINT_TOKEN;
        } else {
            // Log
            BaseConst.REMOTE_LOGGER_TOKEN = BaseConst.PRODUCTION_REMOTE_LOGGER_TOKEN;
            BaseConst.REMOTE_LOGGER_LOGGING_LEVEL = BaseConst.PRODUCTION_REMOTE_LOGGER_LOGGING_LEVEL;

            BaseConst.MINT_TOKEN = BaseConst.PRODUCTION_MINT_TOKEN;
        }
    }

}
