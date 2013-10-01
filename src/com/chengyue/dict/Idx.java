package com.chengyue.dict;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import android.util.Log;

public class Idx {
	private String fileName;
	
	private String TAG = "com.chengyue.dic.Idx";
	
	private int idxoffsetbits = 32;
	
	public void setIdxoffsetbits(int idxoffsetbits) {
		this.idxoffsetbits = idxoffsetbits;
	}

	public Idx() {
		Log.i(TAG, "initialize Info");
		// processFile();
	}
	
	public Idx(String pFileName) {
		this.fileName = pFileName;
	}
	
	public HashMap<String, WordData> processFile(InputStream inputStream) {
		HashMap<String, WordData> indexMap = new HashMap<String, WordData>();
		
		Log.i("INFO-INFO", "Process File");
		try {
			int data = inputStream.read();

			StringBuffer word = new StringBuffer(256); 
			
			while( data != -1 ) {
				char theChar = (char)data;
				// String s = new String(buf, "UTF-8");
				if( theChar != '\0' ) {
					word.append(theChar);
				} else {
					byte[] bufOffset;
					if ( this.idxoffsetbits == 32 ) {
						bufOffset = new byte[4];
					} else {
						bufOffset = new byte[8];
					}
					inputStream.read(bufOffset);
					// this might need to be fixed later, because we can't use a 8 bytes data as INT
					int word_data_offset = ByteBuffer.wrap(bufOffset).getInt();
					byte[] bufSize = new byte[4];
					inputStream.read(bufSize);
					int word_data_size = ByteBuffer.wrap(bufSize).getInt();
					// Log.i(TAG, word.toString() + " offset:" + String.valueOf(word_data_offset) + " size:" + String.valueOf(word_data_size));
					indexMap.put(word.toString(), new WordData( word_data_offset, word_data_size) );
					 word = new StringBuffer(256);
				}
				// processLine(line);
				data = inputStream.read();
			}
			
			inputStream.close();
		} catch (FileNotFoundException e) {
			Log.i("INFO-INFO-FILE", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("INFO-INFO-IO", e.getMessage());
			e.printStackTrace();
		}
		
		return indexMap;
	}
}