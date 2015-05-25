package com.zht.test;

public class ThreadPoolTest extends Thread {
	String te;
	ThreadPoolTest(String t){
		te=t;
	}
	testThread tt = new testThread();
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		super.run();
		while(true){
			tt.test(te);
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
