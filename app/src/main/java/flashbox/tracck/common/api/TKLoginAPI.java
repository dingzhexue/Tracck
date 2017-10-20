package flashbox.tracck.common.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.api.TKBaseAPI;

/**
 * Created by Administrator on 12/27/2016.
 */

public class TKLoginAPI extends TKBaseAPI<TKLoginAPI, TKLoginAPIResponse> {
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
        return super.handleResponse(response);
    }
}
