package com.zht.url;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ThreadPool extends Thread {
	
//	Map<String,String> param = Collections.synchronizedMap(new HashMap<String,String>());
	
	Logger log = Logger.getLogger(ThreadPool.class);
	
	Map<String,String> param = new HashMap<String, String>();
	int count;
	
	public ThreadPool(Map<String,String> yueCheParam,int threadCount){
		param = yueCheParam;
		count = threadCount;
	}
	
	UrlFactory url = new UrlFactory();
	@Override
	public void run(){
		// TODO Auto-generated method stub
//		super.run();
		try {
			while(true){
				log.debug("线程("+count+")运行");
				String yue = url.yueche(param);
				log.debug("线程("+count+")运行,约车结果--->"+yue);
			}
		} catch (Exception e) {
			log.debug("线程,约车,出现错误！！");
			e.printStackTrace();
		}
	}
}
