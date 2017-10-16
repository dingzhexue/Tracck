package flashbox.tracck.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import flashbox.tracck.R;
import flashbox.tracck.app.TKDatabase;
import flashbox.tracck.model.TKAccount;

public class TKPhoneSubmitActivity extends TKPhoneCommonActivity implements
        CountryCodePicker.OnCountryChangeListener, TextView.OnEditorActionListener {

    private static final String TAG = TKPhoneSubmitActivity.class.getSimpleName();

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_CODE_SENT = 2;
    public static final int STATE_VERIFY_FAILED = 3;
    public static final int STATE_VERIFY_SUCCESS = 4;
    public static final int STATE_SIGNIN_FAILED = 5;
    public static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @BindView(R.id.ccp)
    CountryCodePicker mCountryCodePicker;

    @BindView(R.id.etCountryCode)
    EditText mETCountryCode;

    @BindView(R.id.etLocalNumber)
    EditText mETLocalNumber;

    @BindView(R.id.btSubmit)
    Button mBTSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_submit);
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
    }
    // [END on_start_check_user]

    @Override
    protected void initUI() {
        super.initUI();

        mCountryCodePicker.setSearchAllowed(false);
        // mCountryCodePicker.setCountryForPhoneCode(mCountryCodePicker.getDefaultCountryCodeAsInt());
        mCountryCodePicker.setOnCountryChangeListener(this);

        // mETCountryCode.addTextChangedListener(mCountryCodeWatcher);
        mETCountryCode.setOnEditorActionListener(this);

        updateUI();
    }

    @Override
    public boolean supportOffline() {
        return true;
    }

    @Override
    protected void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        super.updateUI(uiState, user, cred);

        switch (uiState) {
            case STATE_INITIALIZED:
            case STATE_VERIFY_FAILED:
            case STATE_SIGNIN_FAILED:
                enableViews(mETCountryCode, mETLocalNumber, mBTSubmit);
                break;
            case STATE_CODE_SENT:
                disableViews(mETCountryCode, mETLocalNumber, mBTSubmit);
                // Open verify activity
                gotoSMSCodeVerification();
                break;
        }
    }

    private void updateUI() {
        mETCountryCode.setText(mCountryCodePicker.getSelectedCountryCode());
        mETCountryCode.setSelection(mETCountryCode.length());
    }

    @Override
    protected String getPhoneNumber() {
        String countryNumber = mETCountryCode.getText().toString();
        String localPhoneNumber = mETLocalNumber.getText().toString();

        String phoneNumber = String.format(Locale.getDefault(), "+%s%s", countryNumber, localPhoneNumber);

        return phoneNumber;
    }

    @OnClick(R.id.btSubmit)
    void onSubmitClicked() {
        if (!validatePhoneNumber()) {
            return;
        }

        TKAccount.getInstance().saveAccountInfo(getPhoneNumber());
        startPhoneNumberVerification(getPhoneNumber());
    }

    @Override
    public void onCountrySelected() {
        updateUI();
    }

    /*
    private TextWatcher mCountryCodeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String number = charSequence.toString();

            if (!TextUtils.isEmpty(number)) {
                int countryCode = Integer.parseInt(number);
                if (countryCode != 0) {
                    if (mCountryCodePicker.getSelectedCountryCodeAsInt() != countryCode) {
                        mCountryCodePicker.setCountryForPhoneCode(Integer.parseInt(number));
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    */

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            mCountryCodePicker.setDefaultCountryUsingNameCode(mCountryCodePicker.getSelectedCountryNameCode());

            String number = textView.getText().toString();

            if (!TextUtils.isEmpty(number)) {
                int countryCode = Integer.parseInt(number);
                if (countryCode != 0) {
                    if (mCountryCodePicker.getSelectedCountryCodeAsInt() != countryCode) {
                        mCountryCodePicker.setCountryForPhoneCode(Integer.parseInt(number));
                    }
                }
            }
        }
        return false;
    }
}