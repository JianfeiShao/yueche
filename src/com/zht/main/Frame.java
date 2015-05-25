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
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.zht.entity.Stu;
import com.zht.entity.StuInfo;
import com.zht.url.ThreadPool;
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
	
	public Stu stu; 
	
	public StuInfo stuInfo;
	
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
					String jsonStuInfo = url.userInfo(cookie,"jbxx");
					log.debug("个人信息--->"+jsonStuInfo);
					if(jsonStuInfo==null){
						log.debug("获取信息失败");
						return;
					}
					ObjectMapper mapper = new ObjectMapper();
					stu = mapper.readValue(jsonStuInfo, Stu[].class)[0];
					log.debug("name-->"+stu.getFchrStudentName());
					
					//刷新查询验证码
					validCode();
					
					//获取个人约车信息
					if(loginResult.equalsIgnoreCase("true")){
						Map<String, String> shuParam = new HashMap<String,String>();
						shuParam.put("loginType", "2");
						shuParam.put("method", "stu");
						shuParam.put("stuid", stu.getFchrStudentID());
						shuParam.put("sfznum", "");
						shuParam.put("carid", "");
						shuParam.put("ValidCode", code.getText());
						shuParam.put("Cookie",cookie);
						String stuArray = url.stuHdl(shuParam);
						log.debug("个人约车信息--->"+stuArray);
						String[] stuAr= stuArray.split("\\|\\|\\|\\|");
						log.debug(stuAr.length);
						//这里是个人约车信息(SOAP)
//						mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						stuInfo = mapper.readValue(stuAr[0], StuInfo[].class)[0];
						//个人约车结果(Map方式)这里需要grid UI
						Map<String,String>[] stuInfoMap = mapper.readValue(stuAr[1], Map[].class);
						
						log.debug("Map-->"+stuInfoMap.length);
						
						Map<String,String> yueCheParam = new HashMap<String, String>();
						yueCheParam.put("loginType", "2");
						yueCheParam.put("method", "yueche");
						yueCheParam.put("stuid", stuInfo.getFchrStudentID());
						yueCheParam.put("bmnum", stuInfo.getFchrRegistrationID());
						yueCheParam.put("start", "13");//开始时间
						yueCheParam.put("end", "17");//结束时间
						yueCheParam.put("lessionid", stuInfo.getFchrLessonID());
						yueCheParam.put("trainpriceid", stuInfo.getFchrTrainPriceID());
						yueCheParam.put("lesstypeid", stuInfo.getFchrLessonTypeID());//约车类型？
						yueCheParam.put("date", "2015-05-26");//约车日期
						yueCheParam.put("id", "1");//固定值
						yueCheParam.put("carid", "");
						yueCheParam.put("ycmethod", "03");//约车方法？
						yueCheParam.put("cartypeid", stuInfo.getFchrCarTypeID());
						yueCheParam.put("trainsessionid", "03"); //课程时段ID     和上面 时间 对比成立
						yueCheParam.put("ReleaseCarID", "");
						yueCheParam.put("ValidCode", code.getText());
						yueCheParam.put("Cookie", cookie);
//						Map<String,String> app = appoint();
//						for (int i = 0; i < app.size(); i++) {
						Set<Entry<String, Set<String>>> userAppoint = appoint().entrySet();
						int threadCount = 0;
						for (Entry<String, Set<String>> entry : userAppoint) {
							log.debug(entry.getKey()+":"+entry.getValue());
							Set<String> t = entry.getValue();
							for (String str : t) {
								String[] s = str.split(",");
								yueCheParam.put("date", entry.getKey());
								yueCheParam.put("start", s[1].split("-")[0]);//开始时间
								yueCheParam.put("end", s[1].split("-")[1]);//结束时间
								yueCheParam.put("trainsessionid", s[0]);
								log.debug("是否有循环");
								ThreadPool tp = new ThreadPool(yueCheParam, threadCount++);
								tp.start();
							}
						}
//						while(true){
//							System.out.println("约车结果-------->"+url.yueche(yueCheParam));
//						}
					}
					
				} catch (Exception e2) {
					log.debug("登录失败,出现异常");
					e2.printStackTrace();
				}
			}
		});
		this.add(submit);
//		this.add(validCode);//上面验证码只需请求即可不用显示
		
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
				Map<String,String> param = new HashMap<String, String>();
//				param.put("loginType", "2");
				param.put("stuid", stu.getFchrStudentID());
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
					
					Map<String,Set<String>> date = null;//appoint();
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
		time.add("02,9-13");
		time.add("03,13-17");
		time.add("04,17-19");
		time.add("05,19-21");
		date.put("2015-05-31", time);
		return date;
	}
}
