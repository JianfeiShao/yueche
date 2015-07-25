package com.zht.url;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import com.zht.froms.Jtable;

public class TableData {
	
	private static Logger log = Logger.getLogger(TableData.class);
	
	static List<String> sysDate = getDate();

	public static Map<String, Set<String>> userDate = new HashMap<String, Set<String>>();
	
	public static JTable getTable() {
		// 填充表现
		final String[][] rowData = new String[6][8];
		rowData[0][0] = "日期";
		// 行
		for (int i = 1; i <= sysDate.size(); i++) {
			rowData[0][i] = sysDate.get(i - 1);
		}
		// 列
		rowData[1][0] = "7-9";
		rowData[2][0] = "9-13";
		rowData[3][0] = "13-17";
		rowData[4][0] = "17-19";
		rowData[5][0] = "19-21";
		for (int i = 1; i < rowData.length; i++) {
			String[] er = rowData[i];
			for (int j = 1; j < er.length; j++) {
				rowData[i][j] = "不需要";
			}
		}
		// 填充存储
		String[] columnNames = new String[8];
		columnNames[0] = "";
		for (int i = 1; i <= sysDate.size(); i++) {
			columnNames[i] = sysDate.get(i - 1);
		}
		
		final Jtable table = new Jtable(rowData, columnNames);
		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(3).setPreferredWidth(120);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setPreferredWidth(120);
		table.getColumnModel().getColumn(6).setPreferredWidth(120);
		table.getColumnModel().getColumn(7).setPreferredWidth(120);

		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				TableModel tableModel = table.getModel();

				int[] row = table.getSelectedRows();
				int[] column = table.getSelectedColumns();

				String val = table.getValueAt(row[0], column[0]).toString();
				String tempVal = null;
				if (val == null || val.length() == 0) {
					log.debug("改变表格参数失败!!!");
					return;
				}
				tempVal = val.equals("需要") ? "不需要" : "需要";
				tableModel.setValueAt(tempVal, row[0], column[0]);
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				log.debug("选中行-->" + table.getSelectedRow());
				int[] row = table.getSelectedRows();
				int[] column = table.getSelectedColumns();
				String checkColumn = table.getColumnName(column[0]);
				String checkRow = row[0] + "-" + table.getValueAt(row[0], 0);
				log.debug("列" + column[0] + "值" + checkColumn);
				log.debug("行" + row[0] + "值" + checkRow);
				if (checkColumn == null || checkRow == null) {
					log.debug("选择日期不能为空!!!");
					return;
				}
				Set<String> userTime = userDate.get(checkColumn);
				if (userTime != null) {
					if (userTime.contains(checkRow)) {
						userTime.remove(checkRow);
						return;
					}
				}
				if (userTime == null) {
					userTime = new HashSet<String>();
				}
				userTime.add(checkRow);
				userDate.put(checkColumn, userTime);
			}
		});
		return table;
	}

	/**
	 * 获取当天之后的七天时间
	 */
	public static List<String> getDate() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i <= 6; i++) {
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			cal.set(Calendar.DAY_OF_MONTH, day + i);
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd(EEEE)");
			list.add(dateFm.format(cal.getTime()));
		}
		return list;
	}
}
