package com.chengyue.koala.shanbay;

/**
 * Created with IntelliJ IDEA.
 * User: chengyue
 * Date: 9/28/13
 * Time: 11:42 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import com.google.inject.Inject;
import com.loopj.android.http.*;
import net.nightwhistler.pageturner.Configuration;
import org.apache.http.client.params.ClientPNames;

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
        // Set the Cookie store
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        client.setCookieStore(myCookieStore);
        // set client allow Circular redirects
        client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

        String loginUrl = "/";

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
                // get csrfmiddlewaretoken for login verification
                String patternString = "<input type=.hidden. name=.csrfmiddlewaretoken. value=['|\"](.*?)[\'|\"]";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    String csrfmiddlewaretoken = matcher.group(1);
                    Log.d(TAG, csrfmiddlewaretoken);
                    login(username, password, csrfmiddlewaretoken);
                } else {
                    AlertDialog ad = new AlertDialog.Builder(mContext)
                            .create();
                    ad.setCancelable(false);
                    ad.setTitle("Shanbay Login Failed");
                    ad.setMessage("Unable to fetch the verification code.");
                    ad.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
                // Log.d(TAG, content);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                Log.d(TAG, "Failure: " + content);
            }
        });
    }

    public void login(String username, String password, String csrfmiddlewaretoken) {
        String loginUrl = "/accounts/login/";

        RequestParams params = new RequestParams();
        params.put("csrfmiddlewaretoken", csrfmiddlewaretoken);
        params.put("username", username);
        params.put("password", password);
        Log.d(TAG, "param: " + csrfmiddlewaretoken);
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
                if (content != null)
                    Log.d(TAG, content);
                Log.d(TAG, error.toString());

                AlertDialog ad = new AlertDialog.Builder(mContext)
                        .create();
                ad.setCancelable(false);
                ad.setTitle("Shanbay Login Failed");
                ad.setMessage("Login Failed. Please check you account info in the Settings.");
                ad.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });
    }

    public void get(String url, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
