package com.chengyue.util;

public class CYUtil {
	public static boolean isDevMode = false;
	
	public static boolean getDevMode() {
		return isDevMode;
	}
	
	public static void setDevMode(boolean isDevMode) {
		CYUtil.isDevMode = isDevMode;
	}
}