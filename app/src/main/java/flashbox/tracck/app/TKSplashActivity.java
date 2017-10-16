package flashbox.tracck.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import flashbox.tracck.R;
import flashbox.tracck.base.TKBaseActivity;
import flashbox.tracck.connect.amazon.model.TKAmazonAccount;
import flashbox.tracck.connect.amazon.ui.TKAmazonLoginActivity;
import flashbox.tracck.home.TKHomeActivity;
import flashbox.tracck.signin.TKPhoneSubmitActivity;

public class TKSplashActivity extends TKBaseActivity {

    protected MyCount mCounter;

    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    public int getSplashTimeSeconds() {
        return 0;
    }

    public Class<? extends Activity> getNextActivity() {
        return TKPhoneSubmitActivity.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
    }

    @Override
    protected void initUI() {
        super.initUI();

        mCounter = new MyCount(getSplashTimeSeconds() * 1000, getSplashTimeSeconds() * 1000);
        mCounter.start();
    }

    @Override
    public boolean supportOffline() {
        return true;
    }

    public final class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (isDestroyed()) return;
            }

            Intent intent = null;
            if (TKDatabase.getInstance().isSMSVerified()) {
                if (TKAmazonAccount.getInstance().isLoggedIn()) {
                    // Show home screen
                    intent = new Intent(TKSplashActivity.this, TKHomeActivity.class);
                } else {
                    // Show amazon login screen
                    // intent = new Intent(TKSplashActivity.this, TKAmazonLoginActivity.class);
                    intent = new Intent(TKSplashActivity.this, TKHomeActivity.class);
                }
            } else {
                intent = new Intent(TKSplashActivity.this, getNextActivity());
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityWithoutAnimation(intent);

            finishWithoutAnimation();
        }
    }
}
