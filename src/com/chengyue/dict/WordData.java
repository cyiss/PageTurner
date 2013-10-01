package com.chengyue.dict;

public class WordData {
	private int position = 0;
	private int size = 0;
	
	public WordData(int position, int size) {
		this.position = position;
		this.size = size;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getSize() {
		return size;
	}
}