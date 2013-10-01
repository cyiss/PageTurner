package com.chengyue.util;

import java.io.File;

public class CYFile {
	public static File openFile(String filePath) {
		File file = new File(filePath);
		
		if( file.exists() ) {
			
		}
		
		return file;
	}
}