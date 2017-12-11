package flashbox.tracck.common.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.api.TKBaseAPI;

/**
 * Created by android on 12/11/2017.
 */

public class TKItemReturnAPI  extends TKBaseAPI<TKItemReturnAPI, TKItemReturnResponse> {

    private String mName;
    private String mPassword;
    private String mReturnDate;
    private String mReturnDescription;

    public TKItemReturnAPI(Context context, String returnDate, String returnDescription) {
        super(context, "/auth/return_by_post");

        mReturnDate = returnDate;
        mReturnDescription = returnDescription;

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
