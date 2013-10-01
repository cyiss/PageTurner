package com.chengyue.dict;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.chengyue.util.CYLog;
import com.imagero.uio.UIOStreamBuilder;
import com.imagero.uio.RandomAccessInput;

import android.content.res.AssetManager;

public class Dict {
	private static String TAG = "com.chengyue.dict";
	
	private String dictPath;
	
	private static Info info;
	
	private static Idx idx;
	
	private static HashMap<String, WordData> indexMap;

	private static RandomAccessInput dictRandomStream;
	
	private static AssetManager assetManager;
	
	private static boolean isReady = false;
	
	public static void init(AssetManager pAssetManager) {
		setAssetManager(pAssetManager);
		
		Dict.setInfo("dict/stardict-collins5-2.4.2/Collins5.ifo");
        Dict.setInx("dict/stardict-collins5-2.4.2/Collins5.idx");
        Dict.setDict("dict/stardict-collins5-2.4.2/Collins5.dict");
		/*
		new Thread(new Runnable(){
			@Override
			public void run() {
		        
			}
        }).start();
        */
	}
	
	public static void setAssetManager( AssetManager pAssetManager) {
		assetManager = pAssetManager;
	}
	
	public static void setInfo(String infoFilePath) {
		InputStream infoStream;
		try {
			infoStream = assetManager.open(infoFilePath);
		    info = new Info();
		    info.processFile(infoStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setInx(String idxFilePath) {
	    InputStream idxStream;
		try {
			idxStream = assetManager.open(idxFilePath);
		    idx = new Idx();
		    if( info != null ) {
		    	idx.setIdxoffsetbits( info.getIdxoffsetbits() );
		    }
		    indexMap = idx.processFile(idxStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String find(String word) {
		String content = "";
		
		if( !getIsReady() ) {
			return content;
		}
		
		try {
			WordData wd = (WordData)indexMap.get(word);
			if( wd != null ) {
				// Log.i(TAG, word + " offset:" + String.valueOf(wd.getPosition()) + " size:" + String.valueOf(wd.getSize()));
				content = searchDict(wd);
                if ("".equals(content)) {
                    /* Try to fix all kinds of irregular form of words to the standard */
                }
			}
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		
		return content;
	}

	public static void setDict(String dictFilePath) {
		try {
			UIOStreamBuilder dictInputStream = new UIOStreamBuilder(assetManager.open(dictFilePath, 1));
			dictInputStream.setMode(UIOStreamBuilder.READ_ONLY);
			dictRandomStream = dictInputStream.create();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setIsReady(true);
		
		CYLog.i(TAG, "The dictionay has been loaded.");
	}
	
	public static boolean getIsReady() {
		return isReady;
	}
	
	public static void setIsReady(boolean pIsReady) {
		isReady = pIsReady;
	}
	
	public static String searchDict(WordData wordData) {
		String get_dict_string = "";
		
		int offset = wordData.getPosition();
		int size = wordData.getSize();
		
		byte[] contentBytes = new byte[size];
		
		try {
			dictRandomStream.seek(offset);
			dictRandomStream.read(contentBytes);
			
			get_dict_string = new String(contentBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return get_dict_string;
	}
	
}