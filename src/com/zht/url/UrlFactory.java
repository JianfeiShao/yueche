package com.zht.url;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Asserts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class UrlFactory {

	private static Logger log = Logger.getLogger(UrlFactory.class);

	private String loginUrl = "http://wsyc.dfss.com.cn/DfssAjax.aspx";

	/**
	 * ��֤��
	 */
	public HttpResponse validCode() throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet getValidpng = new HttpGet(
				"http://wsyc.dfss.com.cn/validpng.aspx");
		HttpResponse response = client.execute(getValidpng);
		return response;
	}

	/**
	 * ��¼
	 */
	public String login(Map<String, String> param) throws Exception {
		Asserts.notNull(param, "����������ܿ�");

		CloseableHttpClient client = HttpClients.createDefault();

		log.debug("��¼url--->" + loginUrl);

		HttpPost login = new HttpPost(loginUrl);

		login.setHeader("Cookie", param.get("Cookie"));
		// login.setHeader("Accept","*/*");
		// login.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("AjaxMethod", param.get("AjaxMethod")));
		nvps.add(new BasicNameValuePair("Account", param.get("Account")));
		nvps.add(new BasicNameValuePair("Pwd", param.get("Pwd")));
		nvps.add(new BasicNameValuePair("ValidCode", param.get("ValidCode")));
		login.setEntity(new UrlEncodedFormEntity(nvps));
		
		log.debug("����" + login.getURI());

		HttpResponse response = client.execute(login);
		return EntityUtils.toString(response.getEntity(),
				Charset.forName("utf-8"));
	}
	
	/**
	 * Լ���б�
	 */
	public String shuHdl(){
//		http://wsyc.dfss.com.cn/Ajax/StuHdl.ashx?loginType=2&method=stu&stuid=04176106&sfznum=&carid=&ValidCode=
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post 
	}
}
