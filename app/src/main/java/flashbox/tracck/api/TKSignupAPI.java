package flashbox.tracck.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.app.TKApp;
import flashbox.tracck.model.TKAccount;

/**
 * Created by Administrator on 12/27/2016.
 */

public class TKSignupAPI extends TKBaseAPI<TKSignupAPI> {
    private String mName;
    private String mPhoneNumber;

    public String id;

    public TKSignupAPI(Context context, String name, String phoneNumber) {
        super(context, "/user/register");

        mName = name;
        mPhoneNumber = phoneNumber;
    }

    @Override
    protected RequestParams createReqParams(RequestParams params) {
        super.createReqParams(params);

        params.put("username", mName);
        params.put("mobile", mPhoneNumber);

        return params;
    }

    @Override
    protected boolean handleResponse(JSONObject response) throws JSONException {
        if (super.handleResponse(response)) {
            // { "userid": "2", "username": "swayam1", "email": null, "status": "success", "error": "", "mobile": "1234567890" }
            id = response.getString("userid");
            String username = response.getString("username");
            String mobile = response.getString("mobile");

            // Store account info
            TKAccount.getInstance().saveAccountInfo(id, mobile, username, System.currentTimeMillis());
            ((TKApp)TKApp.getInstance()).setLoggedIn(true);

            return true;
        }

        return false;
    }
}
