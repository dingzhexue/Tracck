package flashbox.tracck.api;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.gpit.android.util.StringUtils;
import com.gpit.android.webapi.BaseJSONWebAPI;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.app.TKApp;

import static flashbox.tracck.app.TKConst.WEB_API_SERVER_URL;
/**
 * Created by Administrator on 9/11/2017.
 */

public abstract class TKBaseAPI<T> extends BaseJSONWebAPI<T> {
    protected String mResponseStatus;

    static {
        enableCookieStore(TKApp.getInstance());
    }

    public TKBaseAPI(Context context, String apiPath) {
        super(context, apiPath);

        setRequestMethod(false);
        showProgress(true);
    }

    @Override
    protected String getAbsoluteUrl(String path) {
        path = WEB_API_SERVER_URL + path;

        return path;
    }

    @CallSuper
    @Override
    protected RequestParams createReqParams(RequestParams params) {
        return params;
    }

    protected boolean handleResponse(JSONObject response) throws JSONException {
        //  { "userid": "2", "username": "swayam1", "email": null, "status": "success", "error": "", "mobile": "1234567890" }
        mResponseStatus = response.getString("status");
        if (StringUtils.equalsStringIncludeNull(mResponseStatus, "success")) {
            mErrorCode = 0;
        } else {
            mErrorCode = 1;
            mErrorMessage = response.getString("error");
        }

        return isSuccess();
    }

    @Override
    public boolean isSuccess() {
        return mErrorCode == 0;
    }
}
