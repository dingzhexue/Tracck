package flashbox.tracck.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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
import com.gpit.android.logger.RemoteLogger;
import com.gpit.android.util.Utils;
import com.gpit.android.webapi.OnCommonAPICompleteListener;

import java.util.concurrent.TimeUnit;

import flashbox.tracck.common.api.TKSignupAPI;
import flashbox.tracck.app.TKDatabase;
import flashbox.tracck.base.TKBaseActivity;
import flashbox.tracck.connect.amazon.ui.TKAmazonLoginActivity;
import flashbox.tracck.model.TKAccount;

public abstract class TKPhoneCommonActivity extends TKBaseActivity {

    private static final String TAG = TKPhoneCommonActivity.class.getSimpleName();

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    static final int STATE_INITIALIZED = 1;
    static final int STATE_CODE_SENT = 2;
    static final int STATE_VERIFY_FAILED = 3;
    static final int STATE_VERIFY_SUCCESS = 4;
    static final int STATE_SIGNIN_FAILED = 5;
    static final int STATE_SIGNIN_SUCCESS = 6;

    static final int PIN_CODE_LENGTH = 4;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    protected boolean mVerificationInProgress = false;
    protected String mVerificationId;
    protected PhoneAuthProvider.ForceResendingToken mResendToken;
    protected PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                RemoteLogger.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                RemoteLogger.w(TAG, "onVerificationFailed: " + e.getLocalizedMessage());
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    RemoteLogger.e(TAG, "Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Utils.hideWaitingDlg();

                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                RemoteLogger.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(TKAccount.getInstance().phoneNumber);
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    protected void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;

        Utils.showWaitingDlg(this);
    }

    protected void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    protected void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    protected void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            RemoteLogger.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            RemoteLogger.w(TAG, "signInWithCredential:failure: " + task.getException().getLocalizedMessage());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                RemoteLogger.e(TAG, "Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    protected void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }

    protected void gotoSMSCodeVerification() {
        Intent intent = new Intent(this, TKPhoneVerifyActivity.class);
        startActivity(intent);
    }

    protected void gotoAmazonLogin() {
        Intent intent = new Intent(this, TKAmazonLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected String getPhoneNumber() {
        return TKAccount.getInstance().phoneNumber;
    }

    @CallSuper
    protected void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    protected void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    protected void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    protected void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    protected void updateUI(int uiState, final FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_VERIFY_SUCCESS:
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        onSMSVerified(user);
                    }
                }

                break;
            case STATE_SIGNIN_SUCCESS:
                onSMSVerified(user);
                break;
        }
    }

    protected void onSMSVerified(final FirebaseUser user) {
        TKDatabase.getInstance().setSMSVerified(true);

        signup(user);
    }

    private void signup(final FirebaseUser user) {
        // Lets signup
        final TKSignupAPI api = new TKSignupAPI(this, user.getDisplayName(), user.getPhoneNumber());
        api.exec(new OnCommonAPICompleteListener<TKSignupAPI>(this) {
            @Override
            public void onCompleted(TKSignupAPI webapi) {
                // Store account info
                TKAccount.getInstance().saveAccountInfo(api.id, getPhoneNumber(),
                        user.getDisplayName(), System.currentTimeMillis());
                gotoAmazonLogin();
            }

            public void onFailed(TKSignupAPI webapi) {
                super.onFailed(webapi);

                Toast.makeText(TKPhoneCommonActivity.this, webapi.getErrorMsg(), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected boolean validatePhoneNumber() {
        String phoneNumber = getPhoneNumber();
        if (TextUtils.isEmpty(phoneNumber)) {
            RemoteLogger.e(TAG, "Invalid phone number.");
            return false;
        }

        return true;
    }

    protected void enableViews(View... views) {
        for (View v : views) {
            if (v != null) v.setEnabled(true);
        }
    }

    protected void disableViews(View... views) {
        for (View v : views) {
            if (v != null) v.setEnabled(false);
        }
    }
}