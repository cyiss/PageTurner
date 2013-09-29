package com.chengyue.koala.shanbay;

/**
 * Created with IntelliJ IDEA.
 * User: etsu.tei
 * Date: 2013/09/27
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */

import android.content.Context;
import android.util.Log;
import com.google.inject.Inject;
import net.nightwhistler.pageturner.Configuration;
import roboguice.RoboGuice;

public class Shanbay {
    private static final String TAG = "Shanbay";
    private static Shanbay mInstance = new Shanbay();
    private static SBRestClient mClient;
    private String mUsername;
    private String mPassword;
    private Context mContext;

    private Shanbay() {

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
            mClient = SBRestClient.getInstance();
            mClient.setContext(mContext);
            mClient.tryLogin(shanbayId, shanbayPassword);
        }
    }

    public boolean isLoggedIn() {
        return mClient.isLoggedIn;
    }
}
