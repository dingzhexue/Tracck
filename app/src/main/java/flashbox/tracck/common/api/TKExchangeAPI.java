package flashbox.tracck.common.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.api.TKBaseAPI;

/**
 * Created by android on 12/11/2017.
 */

public class TKExchangeAPI  extends TKBaseAPI<TKExchangeAPI,TKExchangeResponse>{

    private String mName;
    private String mPassword;
    private String mExchangeDate;
    private String mExchangeDescription;

    public TKExchangeAPI(Context context, String exchangeDate, String exchangeDescription) {
        super(context, "/auth/exchange_by_post");

        mExchangeDate = exchangeDate;
        mExchangeDescription = exchangeDescription;
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
