package com.zht.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.io.InputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.zht.event.ButtonEvent;

public class FormMain {
	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame("Welcome To JAVA");
//		Container pane = new Container();
//		pane.setLayout(new FlowLayout());
		
//		Icon icon = new ImageIcon(data);
//		JLabel lab = new JLabel("这是验证码",icon, JLabel.CENTER); // 实例化标签对象
//		Font fnt = new Font("Serief", Font.ITALIC + Font.BOLD, 28);
//		lab.setFont(fnt);
//		lab.setForeground(Color.RED);
//		lab.setBounds(0, 0, 0, 0);
//		frame.add(lab); // 将组件件入到面板之中
//		pane.add(lab);
//		frame.getContentPane().add(pane, BorderLayout.CENTER);
		
//		Dimension dim = new Dimension();
//		dim.setSize(200, 100);
//		frame.setSize(dim);
//		Point point = new Point(0, 0); // 设置坐标
//		frame.setLocation(point);
//		frame.setVisible(true);
		
		JTextField text = new JTextField();
		text.setBounds(0, 50, 100, 20);
		frame.add(text);
		
		ButtonEvent button = new ButtonEvent();
		Component contents = button.createComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}