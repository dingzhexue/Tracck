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

public class TKLoginAPI extends TKBaseAPI<TKLoginAPI> {
    private String mName;
    private String mPassword;

    public TKLoginAPI(Context context, String name, String password) {
        super(context, "/auth/login_by_post");

        mName = name;
        mPassword = password;
    }

    @Override
    protected RequestParams createReqParams(RequestParams params) {
        super.createReqParams(params);

        params.put("login", mName);
        params.put("password", mPassword);

        return params;
    }

    @Override
    protected boolean handleResponse(JSONObject response) throws JSONException {
        if (super.handleResponse(response)) {
            //  { "userid": "2", "username": "swayam1", "email": null, "status": "success", "error": "", "mobile": "1234567890" }
            String id = response.getString("userid");
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
