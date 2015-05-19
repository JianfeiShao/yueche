package com.zht.event;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ButtonEvent implements ActionListener {

	final JLabel label = new JLabel();
	
	final static String LOOKANDFEEL = "System";

	public Component createComponents() {
		this.actionPerformed(null);
		JButton button = new JButton("换一张");
		button.setMnemonic(KeyEvent.VK_I);
		button.addActionListener(this);
		button.setVisible(false);
		label.setLabelFor(button);
		
		/*
		 * An easy way to put space between a top-level container and its
		 * contents is to put the contents in a JPanel that has an "empty"
		 * border.
		 */
		JPanel pane = new JPanel(new GridLayout(0, 1));
		pane.add(button);
		pane.add(label);
		pane.setBorder(BorderFactory.createEmptyBorder(30, // top
				200, // left
				10, // bottom
				30) // right
		);
		pane.setSize(50, 50);

		return pane;
	}
	
	private Icon icon;
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			icon = new ImageIcon(getPng());
			label.setText("验证码");
			label.setIcon(icon);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	private static void initLookAndFeel() {
		String lookAndFeel = null;

		if (LOOKANDFEEL != null) {
			if (LOOKANDFEEL.equals("Metal")) {
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("System")) {
				lookAndFeel = UIManager.getSystemLookAndFeelClassName();
			} else if (LOOKANDFEEL.equals("Motif")) {
				lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			} else if (LOOKANDFEEL.equals("GTK+")) { // new in 1.4.2
				lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
			} else {
				System.err
						.println("Unexpected value of LOOKANDFEEL specified: "
								+ LOOKANDFEEL);
				lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			}

			try {
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ClassNotFoundException e) {
				System.err
						.println("Couldn't find class for specified look and feel:"
								+ lookAndFeel);
				System.err
						.println("Did you include the L&F library in the class path?");
				System.err.println("Using the default look and feel.");
			} catch (UnsupportedLookAndFeelException e) {
				System.err.println("Can't use the specified look and feel ("
						+ lookAndFeel + ") on this platform.");
				System.err.println("Using the default look and feel.");
			} catch (Exception e) {
				System.err.println("Couldn't get specified look and feel ("
						+ lookAndFeel + "), for some reason.");
				System.err.println("Using the default look and feel.");
				e.printStackTrace();
			}
		}
	}

	private static void createAndShowGUI() {
		// Set the look and feel.---设置外观，可以忽略
		// initLookAndFeel();

		// Make sure we have nice window decorations.
		// 设置为false的话，即为不改变外观
		// JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("SwingApplication");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ButtonEvent app = new ButtonEvent();
		Component contents = app.createComponents();
		frame.getContentPane().add(contents, BorderLayout.CENTER);
		
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public byte[] getPng() throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet getValidpng = new HttpGet(
				"http://wsyc.dfss.com.cn/validpng.aspx");
		HttpEntity entity = client.execute(getValidpng).getEntity();
		InputStream input = entity.getContent();
		byte[] data = new byte[(int) entity.getContentLength()];
		input.read(data);
		input.close();
		return data;
	}

}
