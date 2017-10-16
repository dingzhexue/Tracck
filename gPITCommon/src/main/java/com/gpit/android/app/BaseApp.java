package com.gpit.android.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.CallSuper;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.gpit.android.logger.FileLog;
import com.gpit.android.logger.RemoteLogger;
import com.gpit.android.util.PrivateAccessor;
import com.gpit.android.util.SystemUtils;
import com.splunk.mint.Mint;

import java.util.Locale;

public abstract class BaseApp extends MultiDexApplication {
    public abstract Class<? extends Activity> getMainActivityClass();
    public abstract Class<? extends BaseConst> getConstClass();

    public final static String TAG = BaseApp.class.getSimpleName();

    public static BaseApp APP;

    // UI
    private Activity mVisibleActivity;

    public static SharedPreferences getPreference(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs;

    }

    public static SharedPreferences.Editor getPreferenceEditor(Context context) {
        return getPreference(context).edit();
    }

    public static BaseApp getInstance() {
        return APP;
    }

    private boolean mIsProductMode = false;

    @Override
    public void onCreate() {
        preCreate();

        super.onCreate();

        APP = this;

        initApp();
    }

    private void preCreate() {
        // java.lang.ClassNotFoundException: android.os.AsyncTask caused by AdMob / Google Play Services
        // https://code.google.com/p/android/issues/detail?id=81083
        try {
            Class.forName("android.os.AsyncTask");
        } catch (Throwable ignore) {
        }
    }

    protected void registerReceiver() {
        IntentFilter filter = new IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mPhoneStateReceiver, filter);
    }

    protected void unregisterReceiver() {
        try {
            unregisterReceiver(mPhoneStateReceiver);
        } catch (Exception e) {}
    }

    public void initApp() {
        RemoteLogger.i(TAG, "debug mode: " + BaseConst.DEV_MODE_ON);

        registerReceiver();

        Class<? extends BaseConst> constClz = getConstClass();
        PrivateAccessor.invokePrivateStaticMethod(constClz, "init", new Object[] {this});

        // Init Remote Logger
//        initRemoteLogger();
        initMint();
    }

    public void initEnv() {
        if (mIsProductMode) {
        } else {
        }
    }

    public void setPackageEnv(boolean isProduct) {
        mIsProductMode = isProduct;

        initEnv();
    }

    @CallSuper
    public void initRemoteLogger() {
        if (BaseConst.REMOTE_LOGGER_TOKEN != null) {
            RemoteLogger.init(getApplicationContext(), BaseConst.REMOTE_LOGGER_TOKEN, BaseConst.REMOTE_LOGGER_LOGGING_LEVEL);

            String deviceId = null;
            if (BaseConst.DEV_MODE_ON) {
                deviceId = SystemUtils.getAccount(getApplicationContext());
            }

            // We can't use email address due to google's policy. So lets use device id at this case
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = SystemUtils.getDeviceID(this);
            }
            RemoteLogger.setUserName(deviceId);

            RemoteLogger.i(TAG, "Init Remote Logger: " + BaseConst.REMOTE_LOGGER_TOKEN);
        }

        RemoteLogger.i(TAG, "*************************** Application Initialized! ***************************");
        FileLog.logMessage(getApplicationContext(), "*************************** Application Initialized! ***************************");
    }

    private void initMint() {
        if (BaseConst.MINT_TOKEN != null) {
            // Attention: There is critical issue when Mint is initialized in boot time. Please check below codes.
            // batteryIntent is not initialized at that time, but there is no validation checking.
            // Lets get the delay for the initialization.
            // Utils.getBatteryLevel
            // Intent batteryIntent = context.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            // int level = batteryIntent.getIntExtra("level", -1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 10);
                        Mint.initAndStartSession(BaseApp.this, BaseConst.MINT_TOKEN);
                        RemoteLogger.i(TAG, "Init Mint: " + BaseConst.MINT_TOKEN);
                    } catch (Exception e) {
                        e.printStackTrace();
                        RemoteLogger.e(TAG, e.getLocalizedMessage());;
                    }
                }
            }).start();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        FileLog.logMessage(getApplicationContext(), "BaseApp onConfigurationChanged called");

        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onLowMemory() {
        FileLog.logMessage(getApplicationContext(), "BaseApp onLowMemory called");

        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        FileLog.logMessage(getApplicationContext(),
                String.format(Locale.getDefault(), "BaseApp onTrimMemory called : level = %d", level));

        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        FileLog.logMessage(getApplicationContext(), "BaseApp onTerminate called");

        unregisterReceiver();

        super.onTerminate();
    }

    /******************************
     * UI
     ******************************/
    public String getCurrentVisibleActivityName() {
        if (mVisibleActivity != null) {
            return mVisibleActivity.getClass().getName();
        } else {
            return null;
        }
    }

    public Activity getCurrentVisibleActivity() {
        return mVisibleActivity;
    }

    public void setCurrentVisibleActivity(Activity activity) {
        if (activity != null) {
            RemoteLogger.d(TAG, "Current page (" + activity + ")");
        } else if (mVisibleActivity != null) {
            RemoteLogger.d(TAG, "Hidden page (" + mVisibleActivity + ")");
        }

        mVisibleActivity = activity;
        if (mVisibleActivity == null) {
            RemoteLogger.d(TAG, "Nothing page is shown");
        }
    }

    public boolean isScreenVisible(Class<?> clz) {
        if (clz == null) return false;

        return isScreenVisible(clz.getName());
    }

    public boolean isScreenVisible(String activityName) {
        if (mVisibleActivity == null) {
            return false;
        }

        if (mVisibleActivity.getClass().getName().equals(activityName)) {
            return true;
        }

        return false;
    }

    public void onUpgrade() {
        // TODO Upgrade the database
    }

    private BroadcastReceiver mPhoneStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nWifiInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (nWifiInfo != null && nWifiInfo.isConnected()) {
                    initRemoteLogger();
                    return;
                }
                NetworkInfo n3gInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (n3gInfo != null && n3gInfo.isConnected()) {
                    initRemoteLogger();
                    return;
                }
            }
        }
    };
}