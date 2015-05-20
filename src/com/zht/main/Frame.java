package com.zht.main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import com.zht.url.UrlFactory;

public class Frame extends JFrame{
	
	private static Logger log = Logger.getLogger(Frame.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Icon icon;
	
	final JLabel label = new JLabel();
	
	private String cookie;
	
	private UrlFactory url = new UrlFactory();
	
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
		user.setText("输入账号");
		
		final TextField pwd = new TextField();
		pwd.setText("输入密码");
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
					log.debug("登录结果--->"+url.login(loginParam));
				} catch (Exception e2) {
					log.debug("登录失败,出现异常");
					e2.printStackTrace();
				}
			}
		});
		this.add(submit);
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
	
}
