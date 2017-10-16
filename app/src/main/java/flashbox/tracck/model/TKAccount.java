package flashbox.tracck.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

import flashbox.tracck.app.TKDatabase;

/**
 * Created by administrator on 8/3/17.
 */

public final class TKAccount implements Serializable {
    private static TKAccount instance;

    private final static String PREFS_KEY_ACCOUNT = "account";

    public static TKAccount getInstance() {
        if (instance == null) {
            instance = new TKAccount(true);
        }

        return instance;
    }

    public String id;
    public String name;
    public String phoneNumber;
    public long lastLoggedInTime;

    private TKAccount() {
        this(false);
    }

    private TKAccount(boolean load) {
        if (load) {
            // Load account info from preference
            String accountInfo = TKDatabase.getInstance().getPreference().getString(PREFS_KEY_ACCOUNT, null);
            if (accountInfo != null) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                TKAccount amazonAccount = gson.fromJson(accountInfo, TKAccount.class);
                set(amazonAccount);
            }
        }
    }

    public void saveAccountInfo() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String accountInfo = gson.toJson(this);
        TKDatabase.getInstance().getPreferenceEditor().putString(PREFS_KEY_ACCOUNT, accountInfo);
        TKDatabase.getInstance().getPreferenceEditor().commit();
    }

    public void saveAccountInfo(String phoneNumber) {
        saveAccountInfo(this.id, phoneNumber, this.name, this.lastLoggedInTime);
    }

    public void saveAccountInfo(String id, String phoneNumber, String name) {
        saveAccountInfo(id, phoneNumber, name, this.lastLoggedInTime);
    }

    public void saveAccountInfo(String id, String phoneNumber, String name, long lastLoggedInTime) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.lastLoggedInTime = lastLoggedInTime;

        saveAccountInfo();
    }

    public void set(TKAccount account) {
        id = account.id;
        phoneNumber = account.phoneNumber;
        name = account.name;
        lastLoggedInTime = account.lastLoggedInTime;
    }
}
