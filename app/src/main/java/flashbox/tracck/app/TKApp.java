package flashbox.tracck.app;

import android.app.Activity;

import com.gpit.android.app.BaseApp;
import com.gpit.android.app.BaseConst;
import com.gpit.android.logger.RemoteLogger;

import net.ralphpina.permissionsmanager.PermissionsManager;

import flashbox.tracck.base.TKBaseActivity;

/**
 * Created by administrator on 8/3/17.
 */

public class TKApp extends BaseApp {
    private boolean mIsLoggedIn = false;

    @Override
    public void onCreate() {
        // Init const first
        TKConst.init(this);

        // Enable remote logging system
        RemoteLogger.enableRemoteLogs(true);

        super.onCreate();

        PermissionsManager.init(this);
    }

    @Override
    public Class<? extends Activity> getMainActivityClass() {
        return TKSplashActivity.class;
    }

    @Override
    public Class<? extends BaseConst> getConstClass() {
        return TKConst.class;
    }

    public void showLockScreen(TKBaseActivity activity, int requestCode) {
    }

    public void setLoggedIn(boolean isLoggedIn) {
        mIsLoggedIn = isLoggedIn;
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }
}
