package com.zht.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.zht.entity.ShuInfo;
import com.zht.url.UrlFactory;

public class Frame extends JFrame{
	
	private static Logger log = Logger.getLogger(Frame.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Icon icon;
	
	final JLabel label = new JLabel();
	
	final JLabel validCode = new JLabel();
	
	private String cookie;
	
	private UrlFactory url = new UrlFactory();
	
	public ShuInfo shuInfo; 
	
	void setShuInfo(ShuInfo shu){
		this.shuInfo = shu;
	}
	public Frame(){
		init();
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 300);
		
		JButton button = new JButton("换一张");
		this.add(button, BorderLayout.WEST);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				init();
			}
		});
		this.add(label);
		
		final TextField code = new TextField();
		code.setText("输入验证码");
//		Dimension dimension = new Dimension();
//		dimension.height = 20;
//		dimension.width = 100;
//		textCode.setPreferredSize(dimension);
		this.add(code);
		
//		JButton enter = new JButton("确认");
//		enter.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("有动作");
//			}
//		});
//		this.add(enter);
		
		final TextField user = new TextField();
		user.setText("04176106");
		
		final TextField pwd = new TextField();
		pwd.setText("02060");
		this.add(user);
		this.add(pwd);
		
		JButton submit = new JButton("登录");
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				log.debug("账号-->"+user.getText());
				log.debug("密码-->"+pwd.getText());
				log.debug("验证码-->"+code.getText());
				log.debug("Cookie-->"+cookie);
				Map<String,String> loginParam = new HashMap<String, String>();
				loginParam.put("Account", user.getText());
				loginParam.put("Pwd", pwd.getText());
				loginParam.put("ValidCode", code.getText());
				loginParam.put("AjaxMethod", "LOGIN");
				loginParam.put("Cookie", cookie);
				try {
					//登录成功
					String loginResult = url.login(loginParam);
					log.debug("登录结果--->"+loginResult);
					
					//读取个人信息
					String jsonShuInfo = url.userInfo(cookie,"jbxx");
					log.debug("个人信息--->"+jsonShuInfo);
					if(jsonShuInfo==null){
						log.debug("获取信息失败");
						return;
					}
					ObjectMapper mapper = new ObjectMapper();
					ShuInfo shuInfo = mapper.readValue(jsonShuInfo, ShuInfo[].class)[0];
					setShuInfo(shuInfo);
					log.debug("name-->"+shuInfo.getFchrStudentName());
					
					validCode();
					
					//获取约车信息
//					if(loginResult.equalsIgnoreCase("true")){
//						Map<String, String> shuParam = new HashMap<String,String>();
//						shuParam.put("loginType", "2");
//						shuParam.put("method", "shu");
//						shuParam.put("stuid", shuInfo.getFchrStudentID());
//						shuParam.put("sfznum", "");
//						shuParam.put("carid", "");
//						shuParam.put("ValidCode", code.getText());
//						shuParam.put("Cookie",cookie);
//						log.debug("--->"+url.shuHdl(shuParam));
//						
//					}
				} catch (Exception e2) {
					log.debug("登录失败,出现异常");
					e2.printStackTrace();
				}
			}
		});
		this.add(submit);
		this.add(validCode);
		
		final TextField queryCode = new TextField();
		Dimension di = new Dimension();
		di.height = 25;
		di.width = 70;
		queryCode.setPreferredSize(di);
		this.add(queryCode);
		
		JButton query = new JButton("查询");
		query.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String vc = queryCode.getText();
				ShuInfo sh = shuInfo;
				Map<String,String> param = new HashMap<String, String>();
//				param.put("loginType", "2");
				param.put("stuid", sh.getFchrStudentID());
				param.put("cartypeid", "01");
				param.put("carid", "");
				param.put("ValidCode", vc);
				param.put("Cookie", cookie);
				try {
					String r = url.browser(param);
					log.debug("约车情况-->"+r);
					String[] yuecheInfo = r.split("\\|\\|");
					log.debug("约车情况--->"+yuecheInfo[2]);
					if(yuecheInfo.length!=3){
						log.debug("结果有误");
						return ;
					}
					
					Map<String,Set<String>> date = appoint();
					Set<String> userDateSet = date.keySet();
					
					//约车信息
					ObjectMapper mapper = new ObjectMapper();
					Map<String,String>[] carResult = mapper.readValue(yuecheInfo[2], Map[].class);
					for (Map<String, String> map : carResult) {
//						Set<Entry<String, String>> set = map.entrySet();
						String sDate = map.get("fchrdate");
						if(userDateSet.contains(sDate)){
							//用户指定时间
							Set<String> userTimeSet = date.get(sDate);
							for (String str : userTimeSet) {
								String yueCheId = map.get(str);
								log.debug("约车---->"+yueCheId);
								if(!yueCheId.split("\\/")[1].equalsIgnoreCase("0")){
									log.debug("没车"+sDate+"--->"+str);
								};
							}
						}
//						for (Entry<String, String> entry : set) {
//							log.debug(entry.getKey()+":"+entry.getValue());
							//是否存在用户指定日期
//							if(userDateSet.contains(entry.getValue())){
								//取出用户时间比较
//								Set<Integer> userTimeSet =  date.get(entry.getValue());
//								continue;
//							}
//							break;
//						}
					}
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			}
		});
		this.add(query);
	}
	public void init(){
		try {
			
			HttpResponse response = url.validCode();
			InputStream input = response.getEntity().getContent();
			byte[] data = new byte[(int) response.getEntity().getContentLength()];
			input.read(data);
			input.close();
			cookie = response.getLastHeader("Set-Cookie").getValue();
			log.debug("请求验证码Cookie--->"+cookie);
			icon = new ImageIcon(data);
			label.setText("验证码");
			label.setIcon(icon);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	
	public void validCode(){
		try {
			HttpResponse response = url.validCode();
			InputStream input = response.getEntity().getContent();
			byte[] data = new byte[(int) response.getEntity().getContentLength()];
			input.read(data);
			input.close();
//			cookie = response.getLastHeader("Set-Cookie").getValue();
//			log.debug("请求验证码Cookie--->"+cookie);
			icon = new ImageIcon(data);
			validCode.setText("查询验证码");
			validCode.setIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String,Set<String>> appoint(){
		//获取用户指定时间
		Map<String,Set<String>> date = new HashMap<String, Set<String>>();
		Set<String> time = new HashSet<String>();
		time.add("1");//7-9
		time.add("2");//9-13
		date.put("2015-05-28(星期四)", time);
		return date;
	}
}
