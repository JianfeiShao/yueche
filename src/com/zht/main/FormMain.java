package com.zht.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.zht.event.MyListener;

public class FormMain {
	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame("Welcome To JAVA") ;
		Icon icon = new ImageIcon(data);
		JLabel lab = new JLabel("JAVA", icon, JLabel.CENTER); // 实例化标签对象
		Font fnt = new Font("Serief", Font.ITALIC + Font.BOLD, 28);
		lab.setFont(fnt);
		lab.setForeground(Color.RED);
//		lab.setBackground(Color.YELLOW);// 设置窗体的背景颜色
		frame.add(lab); // 将组件件入到面板之中
		Dimension dim = new Dimension();
//		frame.setBackground(Color.WHITE);// 设置窗体的背景颜色
		dim.setSize(300, 160);
		frame.setSize(dim);
		Point point = new Point(0, 0); // 设置坐标
		frame.setLocation(point);
		frame.setVisible(true);
		JButton button = new JButton("换一张");
		ActionListener listener = new MyListener();
		button.addActionListener(listener);
		frame.setl
		
	}
	
	public static byte[] getPng() throws Exception{
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet getValidpng = new HttpGet("http://wsyc.dfss.com.cn/validpng.aspx");
		HttpEntity entity = client.execute(getValidpng).getEntity();
		InputStream input = entity.getContent();
		
		byte[] data = new byte[(int) entity.getContentLength()];
		input.read(data);
		input.close();
		return data;
	}
}