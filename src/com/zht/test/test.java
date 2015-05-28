package com.zht.test;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.zht.entity.StuInfo;
import com.zht.main.Frame;

public class test {
	private Logger log = null;

	@Before
	public void initLog() {
		log = Logger.getLogger("TestMethod");
	}

	@Test
	public void splitTest() {
		String su = "aaaaa||test||xxxxxx||eeee|";
		String[] array = su.split("\\|\\|");

		System.out.println(array.length);

		String su1 = "73/0";
		System.out.println(su1.split("\\/")[0]);

		System.out.println("xxxx(eeee)".split("\\(")[0]);

		System.out.println("--->" + "4654/0".contains("\\/"));
	}

	@Test
	public void setContainsTest() {
		Set<String> set = new HashSet<String>();
		set.add("aaa");
		set.add("xxx");
		log.debug(set.contains("xxdx"));
	}

	@Test
	public void jsonTest() throws Exception {
		String json = "{\"stuTotal\":\"30/6/24/18/8/6\",\"trainTotal\":\"0/0/0/0/0/0\",\"ContractTotal\":\"30/6/14/0/0/0\"}";
		ObjectMapper mapper = new ObjectMapper();
		mapper.readValue(json, StuInfo.class);
	}

	@Test
	public void splitTest1() {
		Frame f = new Frame();
		Map<String, Set<String>> map = f.appoint();
		log.debug("Map大小--->" + map.size());
		Set<Entry<String, Set<String>>> set = map.entrySet();
		for (Entry<String, Set<String>> entry : set) {
			Set<String> time = entry.getValue();
			log.debug("Set大小" + time.size());
			for (String string : time) {
				log.debug("指定时间--->" + entry.getKey() + "<>"
						+ string.split("-")[0] + "<>" + string.split("-")[1]);
			}
		}
	}

	@Test
	public void threads() {
		for (int i = 0; i <= 5; i++) {
			ThreadPoolTest tpt = new ThreadPoolTest("-" + i + "-");
			tpt.start();
		}
	}

	@Test
	public void testClass() {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1 = map;
		log.debug(map == map1);
	}

	@Test
	public void whileTest() {
		for (int i = 0; i < 3; i++) {
			if (i == 2) {
				log.debug("是否可以出去?" + i);
				whileTest1();
			}
			log.debug("出来了");
		}
	}

	public void whileTest1() {
		int count = 0;
		while (count < 2) {
			count++;
			log.debug("出不去" + count);
		}
	}

	@Test
	public void message() {
		for (int i = 0; i < 2; i++) {
			JOptionPane.showMessageDialog(null, "正在尝试", "test",
					JOptionPane.WARNING_MESSAGE);
			log.debug("走动");
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void yueche() throws Exception {
		for (int i = 0; i < 48; i++) {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet br = new HttpGet(
					"http://wsyc.dfss.com.cn/Ajax/StuHdl.ashx?loginType=2&method=Browser&stuid=04176106&lessonid=001&cartypeid=01&carid=&ValidCode=bgxj");
			br.setHeader("Cookie", "ASP.NET_SessionId=izw13khqy4p41rfo2ugiosew");
			HttpResponse response = client.execute(br);
			String r = EntityUtils.toString(response.getEntity(),
					Charset.forName("utf-8"));
			log.debug("结果"+r);
			String[] yuecheArray = r.split("\\|\\|");

			log.error("约车情况--->" + yuecheArray[2]);
		}
	}

}
