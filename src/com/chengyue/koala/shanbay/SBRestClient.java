package com.chengyue.koala.shanbay;

/**
 * Created with IntelliJ IDEA.
 * User: chengyue
 * Date: 9/28/13
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */

import android.util.Log;
import android.content.Context;
import com.google.inject.Inject;
import com.loopj.android.http.*;
import net.nightwhistler.pageturner.Configuration;

public class SBRestClient {
    private static final String TAG = "SBRectClient";
    private static final String BASE_URL = "http://www.shanbay.com";

    private static AsyncHttpClient client = new AsyncHttpClient();

    private static SBRestClient mInstance = new SBRestClient();

    private Context mContext;

    public boolean isLoggedIn = false;

    @Inject
    private Configuration config;

    private SBRestClient() {

    }

    public static SBRestClient getInstance() {
        return mInstance;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void tryLogin(final String username, final String password) {
        String loginUrl = "/accounts/login/";

        get(loginUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d(TAG, "request onStart");
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "request onFinished");
            }

            @Override
            public void onSuccess(String content) {
                login(username, password);

                Log.d(TAG, content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "Failure: " + content);
            }
        });
    }

    public void login(String username, String password) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        client.setCookieStore(myCookieStore);

        String loginUrl = "/accounts/login/";

        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);

        post(loginUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.d(TAG, "request onStart");
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "request onFinished");
            }

            @Override
            public void onSuccess(String content) {
                Log.d(TAG, "request onSuccess");
                isLoggedIn = true;

                Log.d(TAG, content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "Failure: " + content);
            }
        });
    }

    public static void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
