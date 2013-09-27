package com.chengyue.koala.shanbay;

/**
 * Created with IntelliJ IDEA.
 * User: etsu.tei
 * Date: 2013/09/27
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */

import com.google.inject.Inject;
import com.loopj.android.http.*;
import net.nightwhistler.pageturner.Configuration;

public class Shanbay {
    private static Shanbay mInstance = new Shanbay();

    @Inject
    private Configuration config;

    private Shanbay() {
        String shanbayId = config.getShanbayId();
        String shanbayPassword = config.getShanbayPassword();


    }

    public static Shanbay getInstance() {
        return mInstance;
    }

}
