package flashbox.tracck.connect;

import android.os.Bundle;

import flashbox.tracck.base.TKBaseActivity;

/**
 * Created by administrator on 8/11/17.
 */

public abstract class TKCommonLoginActivity extends TKBaseActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @Override
    public boolean supportOffline() {
        return false;
    }

    private boolean checkValidation() {
        return true;
    }

    private void updateUI() {
    }
}
