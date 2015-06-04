package com.zht.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.zht.entity.Stu;
import com.zht.entity.StuInfo;
import com.zht.froms.Jtable;
import com.zht.url.UrlFactory;

public class Frame extends JFrame {

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

	TextField code = new TextField();

//	TextField queryCode = new TextField();
	
	ObjectMapper mapper = new ObjectMapper();
	
	List<String> sysDate = getDate();
	
	Map<String, Set<String>> userDate = new HashMap<String, Set<String>>();
	
	JPanel panel = new JPanel();
	
	JButton submit = new JButton("登录");
	
	public Frame() {
		panel.setBackground(Color.RED);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
			}
		});
		
		log.debug(sysDate.size());
		//填充表现
		final String[][] rowData = new String[6][8];
		rowData[0][0] = "日期";
		//行
		for (int i = 1; i <= sysDate.size(); i++) {
			rowData[0][i] = sysDate.get(i-1);
		}
//		rowData[0][2] = date.get(1);
//		rowData[0][3] = date.get(2);
//		rowData[0][4] = date.get(3);
//		rowData[0][5] = date.get(4);
//		rowData[0][6] = date.get(5);
//		rowData[0][7] = date.get(6);
		//列
		rowData[1][0] = "7-9";
		rowData[2][0] = "9-13";
		rowData[3][0] = "13-17";
		rowData[4][0] = "17-19";
		rowData[5][0] = "19-21";
		for(int i=1;i<rowData.length;i++){
			String[] er = rowData[i];
			for (int j = 1; j < er.length; j++) {
				rowData[i][j] = "不需要";
			}
		}
		//填充存储
		String[] columnNames = new String[8];
		columnNames[0]="";
		for (int i = 1; i <= sysDate.size(); i++) {
			columnNames[i]=sysDate.get(i-1);//.split("\\(")[0];
		}
		
		final Jtable table = new Jtable(rowData,columnNames);
		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(3).setPreferredWidth(120);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setPreferredWidth(120);
		table.getColumnModel().getColumn(6).setPreferredWidth(120);
		table.getColumnModel().getColumn(7).setPreferredWidth(120);
//		FitTableColumns(table);
		this.add(table);
		
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				TableModel tableModel = table.getModel();
				
				int[] row = table.getSelectedRows();
				int[] column = table.getSelectedColumns();
				
				String val = table.getValueAt(row[0], column[0]).toString();
				String tempVal = null;
				if(val == null || val.length() == 0 ){
					log.debug("改变表格参数失败!!!");
					return;
				}
				tempVal = val.equals("需要") ? "不需要" : "需要";
				tableModel.setValueAt(tempVal, row[0], column[0]);
//				TableCellRenderer tcr = table.getCellRenderer(row[0], column[0]);
//				tcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				log.debug("选中行-->"+table.getSelectedRow());
				int[] row = table.getSelectedRows();
				int[] column = table.getSelectedColumns();
				
				String checkColumn = table.getColumnName(column[0]);
				
				String checkRow = row[0]+"-"+table.getValueAt(row[0], 0);
				
				log.debug("列"+column[0]+"值"+checkColumn);
				log.debug("行"+row[0]+"值"+checkRow);
				
				if(checkColumn == null || checkRow == null){
					log.debug("选择日期不能为空!!!");
					return;
				}
				
				Set<String> userTime = userDate.get(checkColumn);
				if(userTime!=null){
					if(userTime.contains(checkRow)){
						userTime.remove(checkRow);
						return;
					}
				}
				if(userTime == null){
					userTime = new HashSet<String>();
				}
				userTime.add(checkRow);
				userDate.put(checkColumn, userTime);
				
			}
			
		});
		
//		init();
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
		
		// Dimension dimension = new Dimension();
		// dimension.height = 20;
		// dimension.width = 100;
		// textCode.setPreferredSize(dimension);
		code.setText("输入验证码");
		this.add(code);

		final TextField user = new TextField();
		user.setText("11099677");

		final TextField pwd = new TextField();
		pwd.setText("05040");
		this.add(user);
		this.add(pwd);
		
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if(userDate == null || userDate.size() == 0){
//					JOptionPane.showMessageDialog(null, "正在尝试", "test", JOptionPane.WARNING_MESSAGE);
					JOptionPane.showMessageDialog(null,"请选择约车日期!!!","提示 ",JOptionPane.WARNING_MESSAGE);
					log.debug("请选择约车日期!!!");
					return;
				}
				
				log.debug("账号-->" + user.getText());
				log.debug("密码-->" + pwd.getText());
				log.debug("验证码-->" + code.getText());
				log.debug("Cookie-->" + cookie);
				Map<String, String> loginParam = new HashMap<String, String>();
				loginParam.put("Account", user.getText());
				loginParam.put("Pwd", pwd.getText());
				loginParam.put("ValidCode", code.getText());
				loginParam.put("AjaxMethod", "LOGIN");
				loginParam.put("Cookie", cookie);
				try {
					// 登录成功
					String loginResult = url.login(loginParam);
					log.debug("登录结果--->" + loginResult);
					
					// 读取个人信息
					String jsonStuInfo = url.userInfo(cookie, "jbxx");
					log.debug("个人信息--->" + jsonStuInfo);
					if (jsonStuInfo == null) {
						log.debug("获取信息失败");
						return;
					}
					
					submit.setText("退出");
					panel.setBackground(Color.GREEN);
					
					ObjectMapper mapper = new ObjectMapper();
					stu = mapper.readValue(jsonStuInfo, Stu[].class)[0];
					log.debug("name-->" + stu.getFchrStudentName());

					// 刷新查询验证码
					validCode();
					// 获取个人约车信息
					stu();
					// 查询可约车辆
					Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							while(true){
								try {
									browser();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									log.debug("查询可用车辆错误!!!");
								}
							}
						}
					});
					thread.start();
				} catch (Exception e1) {
					log.debug("登录失败,出现异常",e1);
					e1.printStackTrace();
				}
			}
		});
		this.add(submit);
		this.add(panel);
//		 this.add(validCode);//上面验证码只需请求即可不用显示

//		Dimension di = new Dimension();
//		di.height = 25;
//		di.width = 70;
//		queryCode.setPreferredSize(di);
//		this.add(queryCode);

	}

	public void init() {
		try {
			HttpResponse response = url.validCode();
			InputStream input = response.getEntity().getContent();
			byte[] data = new byte[(int) response.getEntity()
					.getContentLength()];
			input.read(data);
			input.close();
			cookie = response.getLastHeader("Set-Cookie").getValue();
			log.debug("请求验证码Cookie--->" + cookie);
			icon = new ImageIcon(data);
			label.setText("验证码");
			label.setIcon(icon);
		} catch (Exception e) {
			log.debug("初始化验证码",e);
			e.printStackTrace();
		}
	}
	
	public void validCode() {
		try {
			HttpResponse response = url.validCode();
			InputStream input = response.getEntity().getContent();
			byte[] data = new byte[(int) response.getEntity()
					.getContentLength()];
			input.read(data);
			input.close();
			// cookie = response.getLastHeader("Set-Cookie").getValue();
			// log.debug("请求验证码Cookie--->"+cookie);
			icon = new ImageIcon(data);
			validCode.setText("查询验证码");
			validCode.setIcon(icon);
		} catch (Exception e) {
			log.debug("查询初始化验证码",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 约车信息
	 */
	public void browser() throws Exception {
		String vc = code.getText();
		Map<String, String> param = new HashMap<String, String>();
		param.put("stuid", stu.getFchrStudentID());
		param.put("cartypeid", "01");
		param.put("carid", "");
		param.put("ValidCode", vc);
		param.put("Cookie", cookie);
		try {
			
			String yuecheCase = url.browser(param);

			log.debug("约车情况-->" + yuecheCase);

			// 0:成功标志，1：表头数据(用于HTML显示)，2：表体数据(分析)
			String[] yuecheArray = yuecheCase.split("\\|\\|");
			
			log.debug("约车情况--->" + yuecheArray[2]);
			
			log.error("约车情况--->" + yuecheArray[2]);
			
			if (!yuecheArray[0].equalsIgnoreCase("success")) {
				log.debug("查询约车情况失败！！");
				return;
			}
			
			Map<String, String>[] carResult = mapper.readValue(yuecheArray[2],Map[].class);

			Set<String> userDateSet = userDate.keySet();// 获取用户所有日期

			for (Map<String, String> map : carResult) {
				String queryDate = map.get("fchrdate");
				if (!userDateSet.contains(queryDate)) {
					continue;
				}
				// 用户指定时间
				Set<String> userTimeSet = userDate.get(queryDate);
				for (String time : userTimeSet) {

					String yueCheVal = map.get(time.split("-")[0]);
					log.debug("传入yueche参数"+time);
					log.debug("约车---->" + yueCheVal);
					// 根据显示值判断是否已经越过车（越过车显示车号，没有“/”符号）
					if (!yueCheVal.contains("/")) {
						continue;
					}
					String[] valArray = yueCheVal.split("\\/");
					if (valArray[1].equalsIgnoreCase("0")) {
						log.debug("不可约车-->" + queryDate + "-->" + valArray[0]);
						continue;
					};
					yueche(queryDate,time);
				}
			}
		} catch (Exception e) {
			log.debug("查询可约车辆有误！！！",e);
			e.printStackTrace();
			panel.setBackground(Color.RED);
		}
	}
	
	/**
	 * 用户约车信息(里面有约车令牌)
	 */
	public void stu() {
		try {
			Map<String, String> shuParam = new HashMap<String, String>();
			shuParam.put("loginType", "2");
			shuParam.put("method", "stu");
			shuParam.put("stuid", stu.getFchrStudentID());
			shuParam.put("sfznum", "");
			shuParam.put("carid", "");
			shuParam.put("ValidCode", code.getText());
			shuParam.put("Cookie", cookie);
			String stuArray = url.stuHdl(shuParam);
			log.debug("个人约车信息--->" + stuArray);
			String[] stuAr = stuArray.split("\\|\\|\\|\\|");
			stuInfo = mapper.readValue(stuAr[0], StuInfo[].class)[0];
			// 个人约车结果(Map方式)这里需要grid UI
			// Map<String, String>[] stuInfoMap = mapper.readValue(stuAr[1],Map[].class);
		} catch (Exception e) {
			// TODO: handle exception
			log.debug("获取用户约车信息,失败!!!",e);
			e.printStackTrace();
		}
	}
	/**
	 * 约车
	 * @param str2 
	 * @param sDate 
	 * @throws Exception
	 */
	int threadCount = 0;
	public void yueche(String date, String time) {
		Map<String, String> yueCheParam = new HashMap<String, String>();
		yueCheParam.put("loginType", "2");
		yueCheParam.put("method", "yueche");
		yueCheParam.put("stuid", stuInfo.getFchrStudentID());
		yueCheParam.put("bmnum", stuInfo.getFchrRegistrationID());
		// yueCheParam.put("start", "13");//开始时间
		// yueCheParam.put("end", "17");//结束时间
		yueCheParam.put("lessionid", stuInfo.getFchrLessonID());
		yueCheParam.put("trainpriceid", stuInfo.getFchrTrainPriceID());
		yueCheParam.put("lesstypeid", stuInfo.getFchrLessonTypeID());// 约车类型？
		// yueCheParam.put("date", "2015-05-26");//约车日期
		yueCheParam.put("id", "1");// 固定值 ,约车id=0:自选车号；id=1：随机分配
		yueCheParam.put("carid", "");
		yueCheParam.put("ycmethod", "03");// 约车方法？
		yueCheParam.put("cartypeid", stuInfo.getFchrCarTypeID());
		// yueCheParam.put("trainsessionid", "03"); //课程时段ID 和上面 时间 对比成立
		yueCheParam.put("ReleaseCarID", "");
		yueCheParam.put("ValidCode", code.getText());
		yueCheParam.put("Cookie", cookie);
		yueCheParam.put("date", date.split("\\(")[0]);
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("1", "7-9");
//		map.put("2", "9-13");
//		map.put("3", "13-17");
//		map.put("4", "17-19");
//		map.put("5", "19-21");
		String[] array = time.split("-");
		yueCheParam.put("start", array[1]);// 开始时间
		yueCheParam.put("end", array[2]);// 结束时间
		
		yueCheParam.put("trainsessionid", "0"+array[0]);
		try {
			int count = 0;
			while(count < 2){
				log.error("尝试约车!!");
				count++;
				String yuecheResult = url.yueche(yueCheParam);
				log.error("尝试结果--->"+yuecheResult);
//				JOptionPane.showMessageDialog(null, "正在尝试", "test", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			log.error("约车错误!!!",e);
			e.printStackTrace();
		}
		
//		ThreadPool tp = new ThreadPool(yueCheParam, threadCount++);
//		tp.start();
	}
	
	/**
	 * 获取当天之后的七天时间
	 * @return List<String>
	 */
	public List<String> getDate(){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i <= 6; i++) {
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
//			cal.add(Calendar.DATE, i);
			cal.set(Calendar.DAY_OF_MONTH,day+i );
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd(EEEE)");
			list.add(dateFm.format(cal.getTime()));
		}
		return list;
	}
	
	/**
	 * 自动适应 table 表格
	 * @param myTable
	 */
	public void FitTableColumns(JTable myTable){
		  JTableHeader header = myTable.getTableHeader();
		     int rowCount = myTable.getRowCount();
		     
		     Enumeration<TableColumn> columns = myTable.getColumnModel().getColumns();
		     while(columns.hasMoreElements()){
		         TableColumn column = (TableColumn)columns.nextElement();
		         int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
		         int width = (int)myTable.getTableHeader().getDefaultRenderer()
		                 .getTableCellRendererComponent(myTable, column.getIdentifier()
		                         , false, false, -1, col).getPreferredSize().getWidth();
		         for(int row = 0; row<rowCount; row++){
		             int preferedWidth = (int)myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable,
		               myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
		             width = Math.max(width, preferedWidth);
		         }
		         header.setResizingColumn(column); // 此行很重要
		         column.setWidth(width+myTable.getIntercellSpacing().width);
		     }
	}
}
