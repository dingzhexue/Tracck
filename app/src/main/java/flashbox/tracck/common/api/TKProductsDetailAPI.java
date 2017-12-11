package flashbox.tracck.common.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.api.TKBaseAPI;

/**
 * Created by android on 12/12/2017.
 */

public class TKProductsDetailAPI extends TKBaseAPI<TKProductsDetailAPI, TKProductsDetailResponse>{

    private String mName;
    private String mPassword;
    private String mPurchasedItemId;
    public TKProductsDetailAPI(Context context, String name, String password, String purchasedItemId) {
        super(context, "/auth/productsDetail_by_post");

        mName = name;
        mPassword = password;
        mPurchasedItemId = purchasedItemId;
    }

    @Override
    protected RequestParams createReqParams(RequestParams params) {
        super.createReqParams(params);

        params.put("login", mName);
        params.put("password", mPassword);
        params.put("purchasedItemId", mPurchasedItemId);

        return params;
    }

    @Override
    protected boolean handleResponse(JSONObject response) throws JSONException {
        return super.handleResponse(response);
    }
}
