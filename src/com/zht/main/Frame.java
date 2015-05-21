package com.zht.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
		
		JButton button = new JButton("��һ��");
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
		code.setText("������֤��");
//		Dimension dimension = new Dimension();
//		dimension.height = 20;
//		dimension.width = 100;
//		textCode.setPreferredSize(dimension);
		this.add(code);
		
//		JButton enter = new JButton("ȷ��");
//		enter.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				System.out.println("�ж���");
//			}
//		});
//		this.add(enter);
		
		final TextField user = new TextField();
		user.setText("04176106");
		
		final TextField pwd = new TextField();
		pwd.setText("02060");
		this.add(user);
		this.add(pwd);
		
		JButton submit = new JButton("��¼");
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				log.debug("�˺�-->"+user.getText());
				log.debug("����-->"+pwd.getText());
				log.debug("��֤��-->"+code.getText());
				log.debug("Cookie-->"+cookie);
				Map<String,String> loginParam = new HashMap<String, String>();
				loginParam.put("Account", user.getText());
				loginParam.put("Pwd", pwd.getText());
				loginParam.put("ValidCode", code.getText());
				loginParam.put("AjaxMethod", "LOGIN");
				loginParam.put("Cookie", cookie);
				try {
					//��¼�ɹ�
					String loginResult = url.login(loginParam);
					log.debug("��¼���--->"+loginResult);
					
					//��ȡ������Ϣ
					String jsonShuInfo = url.userInfo(cookie,"jbxx");
					log.debug("������Ϣ--->"+jsonShuInfo);
					if(jsonShuInfo==null){
						log.debug("��ȡ��Ϣʧ��");
						return;
					}
					ObjectMapper mapper = new ObjectMapper();
					ShuInfo shuInfo = mapper.readValue(jsonShuInfo, ShuInfo[].class)[0];
					setShuInfo(shuInfo);
					log.debug("name-->"+shuInfo.getFchrStudentName());
					
					validCode();
					
					//��ȡԼ����Ϣ
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
					log.debug("��¼ʧ��,�����쳣");
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
		
		JButton query = new JButton("��ѯ");
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
					log.debug("Լ�����-->"+r);
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
			log.debug("������֤��Cookie--->"+cookie);
			icon = new ImageIcon(data);
			label.setText("��֤��");
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
//			log.debug("������֤��Cookie--->"+cookie);
			icon = new ImageIcon(data);
			validCode.setText("��ѯ��֤��");
			validCode.setIcon(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
