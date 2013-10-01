package com.chengyue.util;

import android.util.Log;

public class CYLog {
	
	public static void error(String tag, String msg) {
		if ( CYUtil.getDevMode() ) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		CYLog.error(tag, msg);
	}
	
	public static void info(String tag, String msg) {
		if ( CYUtil.getDevMode() ) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		CYLog.info(tag, msg);
	}
}