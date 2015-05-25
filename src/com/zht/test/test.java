package com.zht.test;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.sun.xml.internal.stream.Entity;
import com.zht.entity.StuInfo;
import com.zht.main.Frame;

public class test {
	private Logger log = null;
	@Before
	public void initLog(){
		log = Logger.getLogger("TestMethod");
	}
	
	@Test
	public void splitTest() {
		String su = "aaaaa||test||xxxxxx||eeee|";
		String[] array = su.split("\\|\\|");

		System.out.println(array.length);
		
		String su1 = "73/0";
		System.out.println(su1.split("\\/")[0]);
	}
	
	@Test
	public void setContainsTest(){
		Set<String> set = new HashSet<String>();
		set.add("aaa");
		set.add("xxx");
		log.debug(set.contains("xxdx"));
	}
	
	@Test
	public void jsonTest() throws Exception{
		String json = "{\"stuTotal\":\"30/6/24/18/8/6\",\"trainTotal\":\"0/0/0/0/0/0\",\"ContractTotal\":\"30/6/14/0/0/0\"}";
		ObjectMapper mapper = new ObjectMapper();
		mapper.readValue(json, StuInfo.class);
	}
	
	@Test
	public void splitTest1(){
		Frame f = new Frame();
		Map<String,Set<String>> map = f.appoint();
		log.debug("Map大小--->"+map.size());
		Set<Entry<String, Set<String>>> set = map.entrySet();
		for (Entry<String, Set<String>> entry : set) {
			Set<String> time = entry.getValue();
			log.debug("Set大小"+time.size());
			for (String string : time) {
				log.debug("指定时间--->"+entry.getKey()+"<>"+string.split("-")[0]+"<>"+string.split("-")[1]);
			}
		}
	}
	
	@Test
	public void threads(){
		for (int i = 0; i <= 5; i++) {
			ThreadPoolTest tpt = new ThreadPoolTest("-"+i+"-");
			tpt.start();
		}
	}
	
}
