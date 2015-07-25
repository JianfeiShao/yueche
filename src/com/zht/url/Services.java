package com.zht.url;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.zht.entity.Stu;
import com.zht.entity.StuInfo;

public class Services {
	private static Logger log = Logger.getLogger(Services.class);
	
	static String cookie ;
	
	static UrlFactory url = new UrlFactory();
	
	/**
	 * 获取图片验证码显示
	 */
	public static byte[] getImgCode(){
		try {
			HttpResponse response = url.validCode();
			InputStream input = response.getEntity().getContent();
			byte[] data = new byte[(int) response.getEntity()
					.getContentLength()];
			input.read(data);
			input.close();
			cookie = response.getLastHeader("Set-Cookie").getValue();
			log.debug("请求验证码Cookie--->" + cookie);
			return data;
		} catch (Exception e) {
			log.debug("初始化验证码",e);
			e.printStackTrace();
		}
		return null;
	}
	
	static Stu stu;
	static ObjectMapper mapper = new ObjectMapper();
	/**
	 * 登录
	 */
	public static String login(final Map<String, String> loginParam)throws Exception{
		String loginResult;
		try {
			loginParam.put("Cookie", cookie);
			// 登录成功
			loginResult = url.login(loginParam);
			if(loginResult!=null && loginResult.equals("true")){
				url.add(loginParam.get("Account"), loginParam.get("Pwd"));
				return loginResult;
			}
		} catch (Exception e) {
			log.debug(e.getMessage(),e);
			throw new Exception("登录失败");
		}
		return loginResult;
		
	}
	
	/**
	 * 获取个人信息
	 */
	public static boolean getInfo() throws Exception{
		// 读取个人信息
		String jsonStuInfo = url.userInfo(cookie, "jbxx");
		try {
			log.debug("个人信息-->"+jsonStuInfo);
			stu = mapper.readValue(jsonStuInfo, Stu[].class)[0];
			if(stu != null){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.getMessage(),e);
			throw new Exception("获取个人信息失败");
		}
		return false;
	}
	static StuInfo stuInfo;
	
	/**
	 * 用户约车信息(里面有约车令牌)
	 */
	public static boolean stu(String validCode) throws Exception {
		Map<String, String> shuParam = new HashMap<String, String>();
		shuParam.put("loginType", "2");
		shuParam.put("method", "stu");
		shuParam.put("stuid", stu.getFchrStudentID());
		shuParam.put("sfznum", "");
		shuParam.put("carid", "");
		shuParam.put("ValidCode", validCode);
		shuParam.put("Cookie", cookie);
		String stuArray = url.stuHdl(shuParam);
		log.debug("个人约车信息--->" + stuArray);
		try {
			String[] stuAr = stuArray.split("\\|\\|\\|\\|");
			if(stuAr[0].equals("nodata")){
				throw new Exception("没有此学员信息或学员进度表信息不正确！");
			}
			stuInfo = mapper.readValue(stuAr[0], StuInfo[].class)[0];
			if(stuInfo != null){
				return true;
			}
		} catch (Exception e) {
			log.debug(e.getMessage(),e);
			throw new Exception("获取约车信息失败");
		}
		return false;
	}
	
	/**
	 * 约车信息
	 */
	public static boolean browser(String validCode) throws Exception {
		Map<String, String> param = new HashMap<String, String>();
		param.put("stuid", stu.getFchrStudentID());
		param.put("cartypeid", "01");
		param.put("carid", "");
		param.put("ValidCode", validCode);
		param.put("Cookie", cookie);
		try {
			String yuecheCase = url.browser(param);
			
			// 0:成功标志，1：表头数据(用于HTML显示)，2：表体数据(分析)
			String[] yuecheArray = yuecheCase.split("\\|\\|");
			
			log.debug("约车查询结果-->" + yuecheArray[2]);
			
			if (!yuecheArray[0].equalsIgnoreCase("success")) {
				log.debug("查询约车情况失败！！");
				return false;
			}
			
			Map<String, String>[] carResult = mapper.readValue(yuecheArray[2],Map[].class);
			
			Set<String> userDateSet = TableData.userDate.keySet();// 获取用户所有日期

			for (Map<String, String> map : carResult) {
				//获取返回的约车日期
				String queryDate = map.get("fchrdate");
				//比较用户选定的日期
				if (!userDateSet.contains(queryDate)) {
					continue;
				}
				// 用户指定时间
				Set<String> userTimeSet = TableData.userDate.get(queryDate);
				for (String time : userTimeSet) {
					//断定车是否可约
					String yueCheVal = map.get(time.split("-")[0]);//1，2，3  断定时间
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
					yueche(queryDate,time,validCode);
				}
			}
		} catch (Exception e) {
			log.debug("查询可约车辆有误",e);
			e.printStackTrace();
			throw new Exception("查询可约车辆有误");
		}
		return true;
	}
	
	public static void yueche(String date, String time,String validCode) {
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
		yueCheParam.put("ValidCode", validCode);
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
				log.debug("尝试约车!!");
				count++;
				String yuecheResult = url.yueche(yueCheParam);
				if(yuecheResult.equals("success")){
					JOptionPane.showMessageDialog(null, "预约成功:"+date.split("\\(")[0]+"->"+array[1]+"至"+array[2], "恭喜", JOptionPane.PLAIN_MESSAGE);
				}
				JOptionPane.showMessageDialog(null, yuecheResult, "错误", JOptionPane.PLAIN_MESSAGE);
			}
		} catch (Exception e) {
			log.error("约车错误!!!",e);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		JOptionPane.showMessageDialog(null, "预约成功:", "恭喜", JOptionPane.PLAIN_MESSAGE);
	}
}
