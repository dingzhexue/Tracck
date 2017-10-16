package flashbox.tracck.connect.amazon.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import flashbox.tracck.api.TKBaseAPI;
import flashbox.tracck.app.TKApp;
import flashbox.tracck.connect.amazon.model.TKAmazonAccount;
import flashbox.tracck.model.TKAccount;

/**
 * Created by Administrator on 12/27/2016.
 */

public class TKAmazonLoginAPI extends TKBaseAPI<TKAmazonLoginAPI> {
    private String mUserID;
    private String mPassword;
    private String mOtp;

    public TKAmazonLoginAPI(Context context, String userID, String password, String otp) {
        super(context, "/amazon/login");

        mUserID = userID;
        mPassword = password;
        mOtp = otp;
    }

    @Override
    protected RequestParams createReqParams(RequestParams params) {
        super.createReqParams(params);

        addHeader("userid", mUserID);
        addHeader("password", mPassword);
        addHeader("otp", mOtp);
        addHeader("locale", Locale.getDefault().getCountry());

        return params;
    }

    @Override
    protected boolean handleResponse(JSONObject response) throws JSONException {
        if (super.handleResponse(response)) {
            // { "userid": "userid", "username": "username", "email": "sample@sample.com", "status": "success", "error": "" }
            String id = response.getString("userid");
            String username = response.getString("username");
            String email = response.getString("email");

            // Store account info
            TKAmazonAccount.getInstance().saveAccountInfo(id, username, mPassword, email);
            ((TKApp)TKApp.getInstance()).setLoggedIn(true);

            return true;
        }

        return false;
    }
}
