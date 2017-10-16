package com.gpit.android.logger;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.gpit.android.app.BaseConst;
import com.gpit.android.util.SystemUtils;
import com.jaredrummler.android.device.DeviceName;
import com.logentries.logger.AndroidLogger;

import java.io.IOException;

public class RemoteLogger {
	private static AndroidLogger logger;
	private static Context context;
	private static String userName = "Anonymous";
    private static String deviceName = "";
	private static String additional = "";
    private static BaseConst.LogLevel logLevel;

	private static boolean mEnableRemoteLogs = false;

	public static void init(Context ctx, String token, BaseConst.LogLevel logLevel) {
		context = ctx;
		try {
			logger = AndroidLogger.createInstance(context, false, true, false, null, 0, token, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		RemoteLogger.logLevel = logLevel;

		DeviceName.with(context).request(new DeviceName.Callback() {
			@Override
            public void onFinished(DeviceName.DeviceInfo info, Exception error) {
				String manufacturer = info.manufacturer;  // "Samsung"
                String marketDeviceName = info.marketName;            // "Galaxy S7 Edge"
				String model = info.model;                // "SAMSUNG-SM-G935A"
				String codename = info.codename;          // "hero2lte"
				deviceName = info.getName();       // "Galaxy S7 Edge"

                if (!TextUtils.isEmpty(deviceName)) {
                    deviceName = ", " + deviceName;
                } else {
					deviceName = "";
				}
			}
		});
	}

	public static void setUserName(String name) {
		userName = name;
		if (!TextUtils.isEmpty(userName)) {
			userName = ", " + userName;
		} else {
			userName = "";
		}
	}

	public static String getUserName() {
		return userName;
	}

	public static void setAdditional(String additional) {
		if (TextUtils.isEmpty(additional)) {
			additional = "";
		} else {
			additional = ", " + additional;
		}

		RemoteLogger.additional = additional;
	}

	public static String getAdditional() {
		return additional;
	}

	public static void enableRemoteLogs(boolean enable) {
		mEnableRemoteLogs = enable;
	}

	public static void v(String tag, String msg) {
		msg = preprocessMsg(tag, msg);
		Log.v(tag, msg);

		if (!mEnableRemoteLogs || logger == null) {
			return;
		}

		if (logger != null && (logLevel.ordinal() <= BaseConst.LogLevel.LOG_LEVEL_VERBOSE.ordinal())) {
			logger.log(msg);
		}
	}

	public static void d(String tag, String msg) {
		msg = preprocessMsg(tag, msg);
		Log.d(tag, msg);

		if (!mEnableRemoteLogs || logger == null) {
			return;
		}

		if (logger != null && (logLevel.ordinal() <= BaseConst.LogLevel.LOG_LEVEL_DEBUG.ordinal())) {
			logger.log(msg);
		}
	}

	public static void i(String tag, String msg) {
		msg = preprocessMsg(tag, msg);
		Log.i(tag, msg);

		if (!mEnableRemoteLogs || logger == null) {
			return;
		}

		if (logger != null && (logLevel.ordinal() <= BaseConst.LogLevel.LOG_LEVEL_INFO.ordinal())) {
			logger.log(msg);
		}
	}

	public static void w(String tag, String msg) {
		msg = preprocessMsg(tag, msg);
		Log.w(tag, msg);

		if (!mEnableRemoteLogs || logger == null) {
			return;
		}

		if (logger != null && (logLevel.ordinal() <= BaseConst.LogLevel.LOG_LEVEL_WARNING.ordinal())) {
			logger.log(msg);
		}
	}

	public static void e(String tag, String msg) {
		msg = preprocessMsg(tag, msg);
		Log.e(tag, msg);

		if (!mEnableRemoteLogs || logger == null) {
			return;
		}

		if (logger != null && (logLevel.ordinal() <= BaseConst.LogLevel.LOG_LEVEL_ERROR.ordinal())) {
			logger.log(msg);
		}
	}

	private static String preprocessMsg(String tag, String msg) {
		if (msg == null) {
			msg = "null";
		}

		String version = "N/A";
		if (context != null) {
			version = SystemUtils.getAppVersionName(context);
		}
        msg = msg.replaceAll("\r", "\\\\r");
        msg = msg.replaceAll("\n", "\\\\n");
		msg = String.format("[%s%s%s%s] %s: %s", version, deviceName, userName, additional, tag, msg);

		return msg;
	}
}
