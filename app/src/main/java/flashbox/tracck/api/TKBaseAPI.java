package flashbox.tracck.api;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.gpit.android.util.StringUtils;
import com.gpit.android.webapi.BaseJSONWebAPI;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import flashbox.tracck.app.TKApp;
import flashbox.tracck.model.TKBaseAPIResponse;

import static flashbox.tracck.app.TKConst.WEB_API_SERVER_URL;
/**
 * Created by Administrator on 9/11/2017.
 */

public abstract class TKBaseAPI<T, R extends TKBaseAPIResponse> extends BaseJSONWebAPI<T> {
    protected TKBaseAPIResponse mResponse;

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
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Class<R> responseType = (Class<R>) ((ParameterizedType)(getClass().getGenericSuperclass())).getActualTypeArguments()[1];
        mResponse = gson.fromJson(response.toString(), responseType);

        if (StringUtils.equalsStringIncludeNull(mResponse.status, "success")) {
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
