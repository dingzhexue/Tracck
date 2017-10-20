package flashbox.tracck.connect.amazon.api;

import android.content.Context;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import flashbox.tracck.api.TKBaseAPI;
import flashbox.tracck.common.api.TKLoginAPIResponse;
import flashbox.tracck.model.TKBaseAPIResponse;

/**
 * Created by Administrator on 12/27/2016.
 */

public class TKAmazonLoginAPIResponse extends TKBaseAPIResponse {
    class User {
        public String id;
        public String userName;
        public String name;
        public String password;
        public String mobile;
        public boolean otprequired;
    }
    public User user;
}
