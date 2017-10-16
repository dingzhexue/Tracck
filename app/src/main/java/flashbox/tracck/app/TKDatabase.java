package flashbox.tracck.app;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gpit.android.logger.RemoteLogger;

import flashbox.tracck.model.TKAccount;

/**
 * Created by administrator on 8/3/17.
 * <p>
 * Implemented application setting information via database and preferences.
 */

public class TKDatabase extends SQLiteOpenHelper {
    private final static String TAG = TKDatabase.class.getSimpleName();

    // Database Name and Version
    private final static String DATABASE_NAME = "tvsettings.db";
    private final static int DATABASE_VERSION = 1;

    protected final static String ROWID = "_id";
    protected final static String[] ALL_COLUMN = new String[]{ROWID, "*"};

    // Preference
    private final static String PREFS_KEY_FIRST_LAUNCHED = "first_launched";
    private final static String PREFS_KEY_SMS_VERIFIED = "sms_verified";
    private final static String PREFS_KEY_PHONE_NUMBER = "phone_number";

    private static TKDatabase instance;

    public static TKDatabase getInstance() {
        if (instance == null) {
            instance = new TKDatabase();
        }

        return instance;
    }

    private SQLiteDatabase mDatabase;

    protected SharedPreferences mPrefs;
    protected SharedPreferences.Editor mPrefsEditor;

    private TKDatabase() {
        super(TKApp.getInstance(), DATABASE_NAME, null, DATABASE_VERSION);

        RemoteLogger.d(TAG, "Init database");

        // Init database
        mDatabase = getWritableDatabase();

        // Init preference
        mPrefs = PreferenceManager.getDefaultSharedPreferences(TKApp.getInstance());
        mPrefsEditor = mPrefs.edit();
    }

    public SharedPreferences getPreference() {
        return mPrefs;
    }

    public SharedPreferences.Editor getPreferenceEditor() {
        return mPrefsEditor;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createRecordTable(database);
    }

    private void createRecordTable(SQLiteDatabase database) {
        /*
        String create_table_file = "create table if not exists " + TABLE_NAME_AUDIO_RECORD +
                "(_id integer PRIMARY KEY autoincrement, name text, filename text, duration integer, size integer, created_date integer, " +
                "uploaded_date integer, is_uploaded integer)";
        database.execSQL(create_table_file);
        */
    }

    private void dropAllTables(SQLiteDatabase database) {
        // database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_AUDIO_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // dropAllTables();
        // onCreate(database);

        RemoteLogger.d(TAG, "Upgrade database from " + oldVersion + " to " + newVersion);

        switch (oldVersion) {
            case 1:
                // Insert "server" field in email list table
                /*
                String addQuery = String.format(Locale.getDefault(),
                        "ALTER TABLE %s ADD COLUMN %s TEXT",
                        TABLE_NAME_AUDIO_RECORD, "server");
                database.execSQL(addQuery);
                */
                break;
        }
        oldVersion++;

        for (int version = oldVersion; version < newVersion; version++) {
            onUpgrade(database, version, version + 1);
        }
    }

    /********************** Preference settings ************************/
    public boolean isFirstLaunched() {
        boolean isFirstLaunched = mPrefs.getBoolean(PREFS_KEY_FIRST_LAUNCHED, true);

        RemoteLogger.d(TAG, "Is First Launched? " + isFirstLaunched);

        return isFirstLaunched;
    }

    public void setFirstLaunched(boolean firstLaunched) {
        mPrefsEditor.putBoolean(PREFS_KEY_FIRST_LAUNCHED, firstLaunched);
        mPrefsEditor.commit();

        RemoteLogger.d(TAG, "Set First Launched: " + firstLaunched);
    }

    public boolean isSMSVerified() {
        boolean isSMSVerified = mPrefs.getBoolean(PREFS_KEY_SMS_VERIFIED, false);

        RemoteLogger.d(TAG, "Is SMS Verified? " + isSMSVerified);

        return isSMSVerified;
    }

    public void setSMSVerified(boolean isSMSVerified) {
        mPrefsEditor.putBoolean(PREFS_KEY_SMS_VERIFIED, isSMSVerified);
        mPrefsEditor.commit();

        RemoteLogger.d(TAG, "Set SMS Verified: " + isSMSVerified);
    }
}
