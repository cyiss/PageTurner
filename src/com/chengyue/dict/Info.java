package com.chengyue.dict;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class Info {
	private String filePath;
	
	// bookname=      // required
	private String bookname  = "";
	// wordcount=     // required
	private int wordcount = 0;
	// synwordcount=  // required if ".syn" file exists.
	private int synwordcount = 0;	 
	// idxfilesize=   // required
	private int idxfilesize = 0;
	// idxoffsetbits= // New in 3.0.0
	private int idxoffsetbits = 32;
	// author=
	private String author = "";
	// email=
	private String email = "";
	// website=
	private String website = "";
	// description=    // You can use <br> for new line.
	private String description = "";
	// date=
	private String date = "";
	// sametypesequence= // very important.
	private String sametypesequence = "";
	
	public Info() {
		// init
	}
	
	public Info(String filePath) {
		this.filePath = filePath;
		// processFile();
	}
	
	public void processFile(InputStream inputStream) {
		Log.i("INFO-INFO", "Process File");
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			
			while( ( line = input.readLine() ) != null ) {
				Log.i("FILE_CONTENTS: ", line);
				processLine(line);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			Log.i("INFO-INFO", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("INFO-INFO", e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void processLine(String line) {
		int equalSignIndex = line.indexOf("=") ;
		Log.i("processLine: ", "find = at " + String.valueOf( equalSignIndex ) );
		if( equalSignIndex != -1) {
			String key = line.substring(0, equalSignIndex);
			String value = line.substring(equalSignIndex + 1);
			Log.i("processLine", "key: " + key + " value: " + value);
			if ( key.equals("bookname") ) {
				setBookname( value );
			} else if ( key.equals("wordcount") ) {
				setWordcount( Integer.valueOf(value) );
			} else if ( key.equals("synwordcount") ) {
				setSynwordcount( Integer.valueOf(value) );
			} else if ( key.equals("idxfilesize") ) {
				setIdxfilesize( Integer.valueOf(value) );
			} else if ( key.equals("idxoffsetbits") ) {
				setIdxoffsetbits( Integer.valueOf(value) );
			} else if ( key.equals("author") ) {
				setAuthor( value );
			} else if ( key.equals("email") ) {
				setEmail( value );
			} else if ( key.equals("website") ) {
				setWebsite( value );
			} else if ( key.equals("description") ) {
				setDescription( value );
			} else if ( key.equals("date") ) {
				setDate( value );
			} else if ( key.equals("sametypesequence") ) {
				setSametypesequence( value );
			}
		}
	}
	
	public String getBookname() {
		return bookname;
	}
	
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	
	public int getWordcount() {
		return wordcount;
	}
	
	public void setWordcount(int wordcount) {
		this.wordcount = wordcount;
	}
	
	public int getSynwordcount() {
		return synwordcount;
	}
	
	public void setSynwordcount(int synwordcount) {
		this.synwordcount = synwordcount;
	}
	
	public int getIdxfilesize() {
		return idxfilesize;
	}
	
	public void setIdxfilesize(int idxfilesize) {
		this.idxfilesize = idxfilesize;
	}
	
	public int getIdxoffsetbits() {
		return idxoffsetbits;
	}
	
	public void setIdxoffsetbits(int idxoffsetbits) {
		this.idxoffsetbits = idxoffsetbits;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getSametypesequence() {
		return sametypesequence;
	}
	
	public void setSametypesequence(String sametypesequence) {
		this.sametypesequence = sametypesequence;
	}
	
}