package com.zht.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import com.zht.entity.Stu;
import com.zht.url.Services;
import com.zht.url.TableData;
import com.zht.url.ThreadPool;
import com.zht.url.UrlFactory;

public class Home extends JFrame {

	private static Logger log = Logger.getLogger(Home.class);
	
	JTabbedPane tabbedPane;
	JLabel reserveLabel, logLabel, label3;
	JPanel reservePanel, LogPanel, loginPanel;
	
	JLabel codeShow;
	Stu stu;
	
	TextField code;
	UrlFactory url = new UrlFactory();
	JPanel runStatus;
	
	JTextArea logContent = new JTextArea();
	JScrollPane scroll = new JScrollPane(logContent); 
	
	public Home() {
		setSize(980,250);
		setVisible(true);
		setResizable(false);
		Container c = getContentPane();
		// 创建选项卡面板对象
		tabbedPane = new JTabbedPane();
		// 创建标签
		reserveLabel = new JLabel("选择时间", SwingConstants.CENTER);
//		logLabel = new JLabel("日志", SwingConstants.CENTER);
		label3 = new JLabel("第三个标签的面板", SwingConstants.CENTER);
		// 创建面板
		reservePanel = new JPanel();
//		reservePanel.setBackground(Color.BLACK);
		LogPanel = new JPanel();
		//登录容器
		loginPanel = new JPanel();
//		loginPanel.setBackground(Color.RED);
		Dimension d = new Dimension(980, 150);
		loginPanel.setPreferredSize(d);
		
		reservePanel.add(reserveLabel);
		
		//添加一个表格
		reservePanel.add(TableData.getTable());
		
		//验证码呈现
		codeShow = new JLabel();
		//获取验证码
		JButton getCode = new JButton("换一张");
		getCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte[] bytesData = Services.getImgCode();
				if(bytesData == null || bytesData.length < 0){
					JOptionPane.showMessageDialog(null,"验证码获取失败再次点击","提示 ",JOptionPane.WARNING_MESSAGE);
				};
				ImageIcon icon = new ImageIcon(bytesData);
				codeShow.setIcon(icon);
			}
		});
		
		//验证码接收
		code = new TextField();
//		code.setText("验证码");
//		Font font = new Font(Font.SERIF, Font.PLAIN, 15);
//		code.setFont(font);
		//登录
		final JButton submit = new JButton("登录");
		//运行状态
		runStatus = new JPanel();
		runStatus.setBackground(Color.RED);
		
		JLabel userl = new JLabel("账号");
		final TextField user = new TextField();
		Dimension userd = new Dimension(110,20);
		user.setPreferredSize(userd);
		loginPanel.add(userl);
		loginPanel.add(user);
		
		JLabel pwdl = new JLabel("密码");
		final TextField pwd = new TextField();
		Dimension pwdd = new Dimension(110,20);
		pwd.setPreferredSize(pwdd);
		loginPanel.add(pwdl);
		loginPanel.add(pwd);
		
		loginPanel.add(getCode);
		loginPanel.add(codeShow);
		JLabel codel = new JLabel("验证码");
		loginPanel.add(codel);
		Dimension coded = new Dimension(80,20);
		code.setPreferredSize(coded);
		loginPanel.add(code);
		loginPanel.add(submit);
		loginPanel.add(runStatus);
		
//		LogPanel.add(logLabel);
		
		//把定义的JTextArea放到JScrollPane里面去 
		//分别设置水平和垂直滚动条自动出现 
		scroll.setHorizontalScrollBarPolicy( 
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		scroll.setVerticalScrollBarPolicy( 
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
		
//		panel3.add(label3);
		reservePanel.add(loginPanel);
		// 将标签面板加入到选项卡的对象上
		tabbedPane.addTab("约车", null, reservePanel, "选择约车");
		tabbedPane.addTab("日志", null, scroll, "查看日志");
//		tabbedPane.addTab("标签3", null, panel3, "未定义");
		
		c.add(tabbedPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if(TableData.userDate == null || TableData.userDate.size() == 0){
					JOptionPane.showMessageDialog(null,"请选择约车日期!!!","提示 ",JOptionPane.WARNING_MESSAGE);
					log.debug("请选择约车日期!!!");
					return;
				}
				
				Map<String, String> loginParam = new HashMap<String, String>();
				loginParam.put("Account", user.getText());
				loginParam.put("Pwd", pwd.getText());
				loginParam.put("ValidCode", code.getText());
				loginParam.put("AjaxMethod", "LOGIN");
				loginParam.put("ValidCode", code.getText());
				try {
					String re = Services.login(loginParam);
					if(re == null || !re.equals("true")){
						JOptionPane.showMessageDialog(null,re==null?"输入验证码，账号，密码":re,"提示 ",JOptionPane.WARNING_MESSAGE);
						return;
					};
					logContent.setText(logContent.getText()+"\n登录完成");
					Services.getInfo();
					Services.stu(code.getText());
					ThreadPool tp = new ThreadPool(code.getText());
					tp.start();
					if(!tp.isAlive()){
						logContent.setText(logContent.getText()+"\n刷票停止，请重新启动");
					}
				} catch (Exception e2) {
					e2.printStackTrace();
					logContent.setText(logContent.getText()+"\n"+e2.getMessage());
				}
				submit.setText("退出");
				runStatus.setBackground(Color.GREEN);
			}
		});
	}
	
	public static void main(String[] args) {
		Home home = new Home();
	}
}
