package com.zht.url;


public class ThreadPool extends Thread {
	
	String code;
	public ThreadPool(String validCode){
		code = validCode;
	}
	boolean thre = true;
	public void run() {
		try {
			
			while(thre){
				boolean re = Services.browser(code);
				if(!re){
					thre = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			thre = false;
		}
	}
}
