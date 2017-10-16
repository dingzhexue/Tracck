package com.gpit.android.webapi;

import android.content.Context;

import com.gpit.android.logger.RemoteLogger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import junit.framework.Assert;

public class CoreRestClient {
	public final static String TAG = CoreRestClient.class.getSimpleName();

	private final static int REST_CLIENT_MTIMEOUT = 60 * 1000;

	private static PersistentCookieStore mCookieStore;
	public static void enableCookieStore(Context context) {
		mCookieStore = new PersistentCookieStore(context);
	}

	private Context mContext;
	private AsyncHttpClient mAsyncClient;
	private SyncHttpClient mSyncClient;

	public CoreRestClient(Context context, boolean sslMode) {
		mContext = context;

		if (sslMode) {
			mAsyncClient = new AsyncHttpClient(true, 80, 443);
			mSyncClient = new SyncHttpClient(true, 80, 443);
		} else {
			mAsyncClient = new AsyncHttpClient();
			mSyncClient = new SyncHttpClient();
		}
		// Initialize async client
		mAsyncClient.setTimeout(REST_CLIENT_MTIMEOUT);
		
		// Initialize sync client
		mSyncClient.setTimeout(REST_CLIENT_MTIMEOUT);

		if (mCookieStore != null) {
			mAsyncClient.setCookieStore(mCookieStore);
			mSyncClient.setCookieStore(mCookieStore);
		}
	}

	public void get(String url, RequestParams params, boolean asyncCall,
			AsyncHttpResponseHandler responseHandler) {
		RemoteLogger.d(CoreRestClient.class.getSimpleName(), "Http Get: "
				+ AsyncHttpClient.getUrlWithQueryString(false, url, params));

		Assert.assertTrue(responseHandler != null);
		
		if (asyncCall) {
			mAsyncClient.get(url, params, responseHandler);
		} else {
			mSyncClient.get(url, params, responseHandler);
		}
	}

	public void post(String url, RequestParams params, boolean asyncCall,
			AsyncHttpResponseHandler responseHandler) {
		RemoteLogger.d(CoreRestClient.TAG, "Http Post: "
				+ url + ", " + params.toString());

		Assert.assertTrue(responseHandler != null);
		
		if (asyncCall) {
			mAsyncClient.post(url, params, responseHandler);
		} else {
			mSyncClient.post(url, params, responseHandler);
		}
	}

	public void setBasicAuth(String username, String password) {
		mAsyncClient.setBasicAuth(username, password);
		mSyncClient.setBasicAuth(username, password);
	}
	
	public void addHeader(String header, String value) {
		mAsyncClient.addHeader(header, value);
		mSyncClient.addHeader(header, value);
	}

	public void cancel() {
		try {
			mAsyncClient.cancelRequests(mContext, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			mSyncClient.cancelRequests(mContext, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
