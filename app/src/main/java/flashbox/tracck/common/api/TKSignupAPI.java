package flashbox.tracck.common.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.api.TKBaseAPI;

/**
 * Created by Administrator on 12/27/2016.
 */

public class TKSignupAPI extends TKBaseAPI<TKSignupAPI, TKSignUpAPIResponse> {
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
        return super.handleResponse(response);
    }
}
