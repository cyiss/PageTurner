package com.chengyue.koala.shanbay;

/**
 * Created with IntelliJ IDEA.
 * User: etsu.tei
 * Date: 2013/09/27
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import com.google.inject.Inject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import net.nightwhistler.pageturner.Configuration;
import roboguice.RoboGuice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Shanbay {
    private static final String TAG = "Shanbay";
    private static Shanbay mInstance = new Shanbay();
    private static SBRestClient mClient;
    private String mUsername;
    private String mPassword;
    private Context mContext;

    private Shanbay() {
        mClient = SBRestClient.getInstance();
    }

    public static Shanbay getInstance() {
        return mInstance;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void login(String username, String password) {
        String shanbayId = username;
        String shanbayPassword = password;
        Log.d(TAG, shanbayId);
        Log.d(TAG, shanbayPassword);

        if (shanbayId != null && shanbayPassword != null && !"".equals(shanbayId) && !"".equals(shanbayPassword) ) {
            mClient.setContext(mContext);
            mClient.tryLogin(shanbayId, shanbayPassword);
        }
    }

    public void addWord(final String word) {
        if( word != null ) {
            String relativeUrl = "/api/learning/add/" + word;
            mClient.get(relativeUrl, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    Log.d(TAG, "add word onStart");
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "add word onFinished");
                }

                @Override
                public void onSuccess(String content) {
                    String patternString = "(\\d+)";
                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(content);

                    if (matcher.find()) {
                        Toast.makeText(mContext, word + " added: " + matcher.group(1), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, "add word failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Throwable error, String content) {
                    if (content != null)
                        Log.d(TAG, content);
                    Log.d(TAG, error.toString());

                    AlertDialog ad = new AlertDialog.Builder(mContext)
                            .create();
                    ad.setCancelable(false);
                    ad.setTitle("Add Word Failed");
                    ad.setButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
            });
        }
    }

    public boolean isLoggedIn() {
        return mClient.isLoggedIn;
    }
}
