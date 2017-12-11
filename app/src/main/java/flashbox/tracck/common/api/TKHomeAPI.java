package flashbox.tracck.common.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.api.TKBaseAPI;

/**
 * Created by android on 12/11/2017.
 */

public class TKHomeAPI extends TKBaseAPI<TKHomeAPI,TKHomeAPIResponse> {

    private String mLoginId;

    public TKHomeAPI(Context context, String loginId) {
        super(context, "/auth/home_by_post");

        mLoginId = loginId;

    }

    @Override
    protected RequestParams createReqParams(RequestParams params) {
        super.createReqParams(params);

        params.put("loginId", mLoginId);
        return params;
    }

    @Override
    protected boolean handleResponse(JSONObject response) throws JSONException {
        return super.handleResponse(response);
    }
}
