package com.zht.url;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Asserts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.sun.net.httpserver.Headers;

public class UrlFactory {

	private static Logger log = Logger.getLogger(UrlFactory.class);

	private String loginUrl = "http://wsyc.dfss.com.cn/DfssAjax.aspx";

	/**
	 * 验证码
	 */
	public HttpResponse validCode() throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet getValidpng = new HttpGet(
				"http://wsyc.dfss.com.cn/validpng.aspx");
		HttpResponse response = client.execute(getValidpng);
		return response;
	}

	/**
	 * 登录
	 */
	public String login(Map<String, String> param) throws Exception {
		Asserts.notNull(param, "请求参数不能空");

		CloseableHttpClient client = HttpClients.createDefault();
		
		log.debug("登录url--->" + loginUrl);

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
		
		log.debug("请求" + login.getURI());
		
		HttpResponse response = client.execute(login);
		int state = response.getStatusLine().getStatusCode();
		if(state != 200){
			return null;
		}
		
		return EntityUtils.toString(response.getEntity(),
				Charset.forName("utf-8"));
	}
	
	/**
	 * 获取个人信息
	 * @param cookie
	 * @param string
	 * @return
	 */
	public String userInfo(String cookie, String ajaxMethod) throws Exception{
		// TODO Auto-generated method stub
		//http://wsyc.dfss.com.cn/DfssAjax.aspx
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost userInfo = new HttpPost("http://wsyc.dfss.com.cn/DfssAjax.aspx");
		userInfo.setHeader("Cookie", cookie);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("AjaxMethod", ajaxMethod));
		userInfo.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = client.execute(userInfo);
		int state = response.getStatusLine().getStatusCode();
		if(state != 200){
			return null;
		}
		return EntityUtils.toString(response.getEntity(),Charset.forName("utf-8"));
	}
	
	/**
	 * 个人约车信息
	 */
	public String shuHdl(Map<String,String> param) throws Exception{
//		loginType=2&method=stu&stuid=04176106&sfznum=&carid=&ValidCode=
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost shu = new HttpPost("http://wsyc.dfss.com.cn/Ajax/StuHdl.ashx");
		shu.setHeader("Cookie", param.get("Cookie"));
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("loginType", param.get("loginType")));
		nvps.add(new BasicNameValuePair("method", param.get("method")));
		nvps.add(new BasicNameValuePair("stuid", param.get("stuid")));
		nvps.add(new BasicNameValuePair("sfznum", param.get("sfznum")));
		nvps.add(new BasicNameValuePair("carid", param.get("carid")));
		nvps.add(new BasicNameValuePair("ValidCode", param.get("ValidCode")));
		shu.setEntity(new UrlEncodedFormEntity(nvps));
		HttpResponse response = client.execute(shu);
		int state = response.getStatusLine().getStatusCode();
		if(state != 200){
			return null;
		}
		return EntityUtils.toString(response.getEntity(),Charset.forName("utf-8"));
	}
	
	/**
	 * 查看约车情况
	 * @return
	 */
	public String browser(Map<String,String> param) throws Exception{
		String url = "http://wsyc.dfss.com.cn/Ajax/StuHdl.ashx?" +
				"loginType=2&method=Browser&stuid="+param.get("stuid")+"&lessonid=001" +
				"&cartypeid="+param.get("cartypeid")+"&carid=&ValidCode="+param.get("ValidCode")+"&t=130766557521239846";
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet br = new HttpGet(url);
		
		br.setHeader("Cookie", param.get("Cookie"));
		
		HttpResponse response = client.execute(br);
		if(response.getStatusLine().getStatusCode()!=200){
			return null;
		}
		return EntityUtils.toString(response.getEntity(),Charset.forName("utf-8"));
	}

}
