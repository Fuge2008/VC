//package com.saas.saasuser.util;
//
//import java.net.URL;
//import java.net.URLConnection;
//import java.sql.Timestamp;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.Locale;
//import java.util.Random;
//
//import android.annotation.SuppressLint;
//
//import java.util.*;
//import java.text.*;
//
///**
// * 获取时间工具包
// */
//@SuppressLint("SimpleDateFormat")
//public class DateUtil {
//
//	/**
//	 * 获取系统时间,将Date实例转化为有格式的字符串
//	 *
//	 * @return
//	 */
//	public static String getSystemTime() {
//		String time = null;
//		try {
//			SimpleDateFormat formatter = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss");
//			Date curDate = new Date(System.currentTimeMillis());
//			// 获取时间
//			time = formatter.format(curDate);
//			// Date()为获取当前系统时间
//			System.out.println("本机时间:" + time);
//		} catch (Exception e) {
//			System.err.println("发生异常=" + e.getMessage());
//		}
//		return time;
//	}
//
//	/**
//	 * 获取北京时间
//	 *
//	 * @return
//	 */
//	public static String getBeijingTime() {
//		String time = null;
//		try {
//			// 取得资源对象
//			URL url = new URL("http://open.baidu.com/special/time/");
//			// 生成连接对象
//			URLConnection uc = url.openConnection();
//			// 发出连接
//			uc.connect();
//			// 取得网站日期时间
//			long ld = uc.getDate();
//			// 设置日期格式
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			// Date()为获取当前系统时间
//			time = df.format(new Date(ld)).toString();
//			System.out.println("北京时间:" + time);
//		} catch (Exception e) {
//			System.err.println("发生异常=" + e.getMessage());
//		}
//		return time;
//	}
//
//	  // 获取当前日期
//    @SuppressLint("SimpleDateFormat")
//    public static String getCurrentDate() {
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        return sdf.format(c.getTime());
//    }
//
//    public static int[] getYMDArray(String datetime, String splite) {
//        int[] date = { 0, 0, 0, 0, 0 };
//        if (datetime != null && datetime.length() > 0) {
//            String[] dates = datetime.split(splite);
//            int position = 0;
//            for (String temp : dates) {
//                date[position] = Integer.valueOf(temp);
//                position++;
//            }
//        }
//        return date;
//    }
//
//    /**
//     * 将当前时间戳转化为标准时间函数
//     *
//     * @param timestamp
//     * @return
//     */
//    @SuppressLint("SimpleDateFormat")
//    public static String getTime(String time1) {
//
//        int timestamp = Integer.parseInt(time1);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = null;
//        try {
//            String str = sdf.format(new Timestamp(intToLong(timestamp)));
//            time = str.substring(11, 16);
//            String month = str.substring(5, 7);
//            String day = str.substring(8, 10);
//            time = getDate(month, day) + time;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return time;
//    }
//
//    public static String getTime(int timestamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = null;
//        try {
//            String str = sdf.format(new Timestamp(intToLong(timestamp)));
//            time = str.substring(11, 16);
//
//            String month = str.substring(5, 7);
//            String day = str.substring(8, 10);
//            time = getDate(month, day) + time;
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return time;
//    }
//
//    public static String getHMS(long timestamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        String time = null;
//        try {
//            return sdf.format(new Date(timestamp));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return time;
//    }
//
//    /**
//     * 将当前时间戳转化为标准时间函数
//     *
//     * @param timestamp
//     * @return
//     */
//    @SuppressLint("SimpleDateFormat")
//    public static String getHMS(String time) {
//
//        long timestamp = Long.parseLong(time);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        try {
//            String str = sdf.format(new Timestamp(timestamp));
//            return str;
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return time;
//    }
//
//    // java Timestamp构造函数需传入Long型
//    public static long intToLong(int i) {
//        long result = (long) i;
//        result *= 1000;
//        return result;
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    public static String getDate(String month, String day) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 24小时制
//        Date d = new Date();
//        ;
//        String str = sdf.format(d);
//        @SuppressWarnings("unused")
//        String nowmonth = str.substring(5, 7);
//        String nowday = str.substring(8, 10);
//        String result = null;
//
//        int temp = Integer.parseInt(nowday) - Integer.parseInt(day);
//        switch (temp) {
//            case 0:
//                result = "今天";
//                break;
//            case 1:
//                result = "昨天";
//                break;
//            case 2:
//                result = "前天";
//                break;
//            default:
//                StringBuilder sb = new StringBuilder();
//                sb.append(Integer.parseInt(month) + "月");
//                sb.append(Integer.parseInt(day) + "日");
//                result = sb.toString();
//                break;
//        }
//        return result;
//    }
//
//    /* 将字符串转为时间戳 */
//    public static String getTimeToStamp(String time) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
//                Locale.CHINA);
//        Date date = new Date();
//        try {
//            date = sdf.parse(time);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        String tmptime = String.valueOf(date.getTime()).substring(0, 10);
//
//        return tmptime;
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    public static String getYMD(long timestamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        return sdf.format(new Date(timestamp));
//    }
//
//    public static String getDate(long timestamp) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//        return sdf.format(new Date(timestamp * 1000));
//    }
//
//    public static String getTimestamp() {
//        long time = System.currentTimeMillis() / 1000;
//        return String.valueOf(time);
//    }
//
//
//}
