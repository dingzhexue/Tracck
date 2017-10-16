package flashbox.tracck.signin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import flashbox.tracck.R;
import flashbox.tracck.app.TKDatabase;

public class TKPhoneVerifyActivity extends TKPhoneCommonActivity {

    private static final String TAG = TKPhoneVerifyActivity.class.getSimpleName();

    @BindView(R.id.txtPinEntry)
    PinEntryEditText mPinEntry;

    @BindView(R.id.tvEnterCorrectCode)
    TextView mTVEnterCorrectCode;

    @BindView(R.id.btVerify)
    Button mBTVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean supportOffline() {
        return false;
    }

    private String getPinCode() {
        String pinCode = mPinEntry.getText().toString();
        return pinCode;
    }

    @Override
    protected void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        super.updateUI(uiState, user, cred);

        switch (uiState) {
            case STATE_VERIFY_SUCCESS:
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mTVEnterCorrectCode.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case STATE_SIGNIN_SUCCESS:
                mTVEnterCorrectCode.setVisibility(View.INVISIBLE);
                break;
            case STATE_VERIFY_FAILED:
            case STATE_SIGNIN_FAILED:
                mTVEnterCorrectCode.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.btVerify)
    void onSubmitClicked() {
        String pinCode = getPinCode();
        if (!TextUtils.isEmpty(pinCode) && pinCode.length() == PIN_CODE_LENGTH) {
            verifyPhoneNumberWithCode(mVerificationId, getPinCode());
        }
    }
}