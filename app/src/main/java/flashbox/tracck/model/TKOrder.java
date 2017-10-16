package flashbox.tracck.model;

import com.gpit.android.util.StringUtils;

import java.io.Serializable;

/**
 * Created by administrator on 8/3/17.
 */

public final class TKOrder implements Serializable {
    private static TKOrder instance;

    private final static long ACCOUNT_EXPIRED_TIMEOUT = 24 * 60 * 60 * 1000;

    public static TKOrder getInstance() {
        if (instance == null) {
            instance = new TKOrder();
        }

        return instance;
    }

    private String mId;
    private String mName;
    private String mPassword;
    private String mEmail;
    private long mLastLoggedInTime;

    private TKOrder() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public long getLastLoggedInTime() {
        return mLastLoggedInTime;
    }

    public void setLastLoggedInTime(long time) {
        mLastLoggedInTime = time;
    }

    public void saveAccountInfo(String name, String password, long lastLoggedInTime) {
        mName = name;
        mPassword = password;
        mLastLoggedInTime = lastLoggedInTime;
    }

    public boolean isExpired() {
        return isExpired(ACCOUNT_EXPIRED_TIMEOUT);
    }

    public boolean isExpired(long duration) {
        if (StringUtils.isNullOrEmpty(mName)) return true;
        if (StringUtils.isNullOrEmpty(mPassword)) return true;

        long diff = System.currentTimeMillis() - mLastLoggedInTime;
        if (diff > duration) {
            return true;
        }

        return false;
    }
}
