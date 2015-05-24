package com.zht.test;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
}
