package flashbox.tracck.connect.amazon.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

import flashbox.tracck.app.TKDatabase;

/**
 * Created by administrator on 8/3/17.
 */

public final class TKAmazonAccount implements Serializable {
    private static TKAmazonAccount instance;

    private final static String PREFS_KEY_AMAZON_ACCOUNT = "amazon_account";

    public static TKAmazonAccount getInstance() {
        if (instance == null) {
            instance = new TKAmazonAccount(true);
        }

        return instance;
    }

    public String userID;
    public String name;
    public String password;
    public String email;
    public long lastLoggedInTime;

    private TKAmazonAccount() {
        this(false);
    }

    private TKAmazonAccount(boolean load) {
        if (load) {
            // Load account info from preference
            String accountInfo = TKDatabase.getInstance().getPreference().getString(PREFS_KEY_AMAZON_ACCOUNT, null);
            if (accountInfo != null) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                TKAmazonAccount amazonAccount = gson.fromJson(accountInfo, TKAmazonAccount.class);
                set(amazonAccount);
            }
        }
    }

    public boolean isLoggedIn() {
        if (lastLoggedInTime == 0) return false;

        return true;
    }

    public void saveAccountInfo() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String accountInfo = gson.toJson(this);
        TKDatabase.getInstance().getPreferenceEditor().putString(PREFS_KEY_AMAZON_ACCOUNT, accountInfo);
        TKDatabase.getInstance().getPreferenceEditor().commit();
    }

    public void saveAccountInfo(String userID, String name, String password, String email) {
        saveAccountInfo(userID, name, password, email, lastLoggedInTime);
    }

    public void saveAccountInfo(String userID, String name, String password, String email, long lastLoggedInTime) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.lastLoggedInTime = lastLoggedInTime;

        saveAccountInfo();
    }

    public void set(TKAmazonAccount account) {
        userID = account.userID;
        name = account.name;
        email = account.email;
        password = account.password;
        lastLoggedInTime = account.lastLoggedInTime;
    }
}
