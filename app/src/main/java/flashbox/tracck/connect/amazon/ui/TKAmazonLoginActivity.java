package flashbox.tracck.connect.amazon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gpit.android.webapi.OnCommonAPICompleteListener;

import butterknife.BindView;
import butterknife.OnClick;
import flashbox.tracck.R;
import flashbox.tracck.connect.TKCommonLoginActivity;
import flashbox.tracck.connect.amazon.api.TKAmazonLoginAPI;
import flashbox.tracck.home.TKHomeActivity;

/**
 * Created by administrator on 8/11/17.
 */

public class TKAmazonLoginActivity extends TKCommonLoginActivity {
    @BindView(R.id.etUserName)
    EditText mETUserName;

    @BindView(R.id.etPassword)
    EditText mETPassword;

    @BindView(R.id.etOTP)
    EditText mETOTP;

    @BindView(R.id.btLogin)
    Button mBTLogin;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_amazon_login);
    }

    @Override
    protected void initUI() {
        super.initUI();

        updateUI();
    }

    @Override
    public boolean supportOffline() {
        return false;
    }

    private void updateUI() {
    }

    private boolean checkValidation() {
        String username = mETUserName.getText().toString();
        String password = mETPassword.getText().toString();
        String otp = mETOTP.getText().toString();

        if (TextUtils.isEmpty(username)) {
            toast(R.string.invalid_login_info);
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            toast(R.string.invalid_login_info);
            return false;
        }

        if (mETOTP.isShown() && TextUtils.isEmpty(otp)) {
            toast(R.string.invalid_login_info);
            return false;
        }

        return true;
    }

    /********************* Event Listener ******************/
    @OnClick(R.id.btLogin)
    void onLoginClicked() {
        String username = mETUserName.getText().toString();
        String password = mETPassword.getText().toString();
        String otp = mETOTP.getText().toString();

        if (!checkValidation()) return;

        TKAmazonLoginAPI api = new TKAmazonLoginAPI(this, username, password, otp);
        api.exec(new OnCommonAPICompleteListener<TKAmazonLoginAPI>(this) {
            @Override
            public void onCompleted(TKAmazonLoginAPI webapi) {
                Intent intent = new Intent(TKAmazonLoginActivity.this, TKHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityWithoutAnimation(intent);
            }

            public void onFailed(TKAmazonLoginAPI webapi) {
                super.onFailed(webapi);

                if (webapi.getErrorCode() == 1) {
                    mETOTP.setVisibility(View.VISIBLE);

                    Toast.makeText(TKAmazonLoginActivity.this, R.string.otp_confirm, Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(TKAmazonLoginActivity.this, webapi.getErrorMsg(), Toast.LENGTH_LONG);
                }
            }
        });
    }
}
