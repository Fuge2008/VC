package com.saas.saasuser.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static final String TAG = "StringUtil";
	public static final String YUAN = "元";
	private static String currentString = "";
	
	public static String URL="url";
	public static String TITLE="title";

	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 判断两字符串是否相等
	 *
	 * @param a
	 *            待校验字符串a
	 * @param b
	 *            待校验字符串b
	 * @return @return {@code true}: 相等<br>
	 * 		{@code false}: 不相等
	 */
	public static boolean equals(CharSequence a, CharSequence b) {
		if (a == b)
			return true;
		int length;
		if (a != null && b != null && (length = a.length()) == b.length()) {
			if (a instanceof String && b instanceof String) {
				return a.equals(b);
			} else {
				for (int i = 0; i < length; i++) {
					if (a.charAt(i) != b.charAt(i))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断两字符串是否不相等
	 *
	 * @param a
	 *            待校验字符串a
	 * @param b
	 *            待校验字符串b
	 * @return @return {@code true}: 不相等<br>
	 * 		{@code false}: 相等
	 */
	public static boolean isNotEquals(CharSequence a, CharSequence b) {
		if (a == b)
			return false;
		int length;
		if (a != null && b != null && (length = a.length()) == b.length()) {
			if (a instanceof String && b instanceof String) {
				return a.equals(b);
			} else {
				for (int i = 0; i < length; i++) {
					if (a.charAt(i) == b.charAt(i))
						return false;
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取刚传入处理后的string
	 * 
	 * @must 上个影响currentString的方法 和 这个方法都应该在同一线程中，否则返回值可能不对
	 * @return
	 */
	public static String getCurrentString() {
		return currentString == null ? "" : currentString;
	}

	// 获取string,为null时返回"" <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 获取string,为null则返回""
	 * 
	 * @param tv
	 * @return
	 */
	public static String getString(TextView tv) {
		if (tv == null || tv.getText() == null) {
			return "";
		}
		return getString(tv.getText().toString());
	}

	/**
	 * 获取string,为null则返回""
	 * 
	 * @param object
	 * @return
	 */
	public static String getString(Object object) {
		return object == null ? "" : getString(String.valueOf(object));
	}

	/**
	 * 获取string,为null则返回""
	 * 
	 * @param cs
	 * @return
	 */
	public static String getString(CharSequence cs) {
		return cs == null ? "" : getString(cs.toString());
	}

	/**
	 * 获取string,为null则返回""
	 * 
	 * @param s
	 * @return
	 */
	public static String getString(String s) {
		return s == null ? "" : s;
	}

	// 获取string,为null时返回"" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 获取去掉前后空格后的string<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 获取去掉前后空格后的string,为null则返回""
	 * 
	 * @param tv
	 * @return
	 */
	public static String getTrimedString(TextView tv) {
		return getTrimedString(getString(tv));
	}

	/**
	 * 获取去掉前后空格后的string,为null则返回""
	 * 
	 * @param object
	 * @return
	 */
	public static String getTrimedString(Object object) {
		return getTrimedString(getString(object));
	}

	/**
	 * 获取去掉前后空格后的string,为null则返回""
	 * 
	 * @param cs
	 * @return
	 */
	public static String getTrimedString(CharSequence cs) {
		return getTrimedString(getString(cs));
	}

	/**
	 * 获取去掉前后空格后的string,为null则返回""
	 * 
	 * @param s
	 * @return
	 */
	public static String getTrimedString(String s) {
		return getString(s).trim();
	}

	// 获取去掉前后空格后的string>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 获取去掉所有空格后的string <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 获取去掉所有空格后的string,为null则返回""
	 * 
	 * @param tv
	 * @return
	 */
	public static String getNoBlankString(TextView tv) {
		return getNoBlankString(getString(tv));
	}

	/**
	 * 获取去掉所有空格后的string,为null则返回""
	 * 
	 * @param object
	 * @return
	 */
	public static String getNoBlankString(Object object) {
		return getNoBlankString(getString(object));
	}

	/**
	 * 获取去掉所有空格后的string,为null则返回""
	 * 
	 * @param cs
	 * @return
	 */
	public static String getNoBlankString(CharSequence cs) {
		return getNoBlankString(getString(cs));
	}

	/**
	 * 获取去掉所有空格后的string,为null则返回""
	 * 
	 * @param s
	 * @return
	 */
	public static String getNoBlankString(String s) {
		return getString(s).replaceAll(" ", "");
	}

	// 获取去掉所有空格后的string >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 获取string的长度<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 获取string的长度,为null则返回0
	 * 
	 * @param tv
	 * @param trim
	 * @return
	 */
	public static int getLength(TextView tv, boolean trim) {
		return getLength(getString(tv), trim);
	}

	/**
	 * 获取string的长度,为null则返回0
	 * 
	 * @param object
	 * @param trim
	 * @return
	 */
	public static int getLength(Object object, boolean trim) {
		return getLength(getString(object), trim);
	}

	/**
	 * 获取string的长度,为null则返回0
	 * 
	 * @param cs
	 * @param trim
	 * @return
	 */
	public static int getLength(CharSequence cs, boolean trim) {
		return getLength(getString(cs), trim);
	}

	/**
	 * 获取string的长度,为null则返回0
	 * 
	 * @param s
	 * @param trim
	 * @return
	 */
	public static int getLength(String s, boolean trim) {
		s = trim ? getTrimedString(s) : s;
		return getString(s).length();
	}

	// 获取string的长度>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 判断字符是否非空 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 判断字符是否非空
	 * 
	 * @param tv
	 * @param trim
	 * @return
	 */
	public static boolean isNotEmpty(TextView tv, boolean trim) {
		return isNotEmpty(getString(tv), trim);
	}

	/**
	 * 判断字符是否非空
	 * 
	 * @param object
	 * @param trim
	 * @return
	 */
	public static boolean isNotEmpty(Object object, boolean trim) {
		return isNotEmpty(getString(object), trim);
	}

	/**
	 * 判断字符是否非空
	 * 
	 * @param cs
	 * @param trim
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence cs, boolean trim) {
		return isNotEmpty(getString(cs), trim);
	}

	/**
	 * 判断字符是否非空
	 * 
	 * @param s
	 * @param trim
	 * @return
	 */
	public static boolean isNotEmpty(String s, boolean trim) {
		// Log.i(TAG, "getTrimedString s = " + s);
		if (s == null) {
			return false;
		}
		if (trim) {
			s = s.trim();
		}
		if (s.length() <= 0) {
			return false;
		}

		currentString = s;

		return true;
	}

	// 判断字符是否非空 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 判断字符类型 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 判断手机格式是否正确
	public static boolean isPhone(String phone) {
		if (isNotEmpty(phone, true) == false) {
			return false;
		}

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-3,5-9])|(17[0-9]))\\d{8}$");

		currentString = phone;

		return p.matcher(phone).matches();
	}

	// 判断email格式是否正确
	public static boolean isEmail(String email) {
		if (isNotEmpty(email, true) == false) {
			return false;
		}

		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);

		currentString = email;

		return p.matcher(email).matches();
	}

	// 判断是否全是数字
	public static boolean isNumber(String number) {
		if (isNotEmpty(number, true) == false) {
			return false;
		}

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(number);
		if (!isNum.matches()) {
			return false;
		}

		currentString = number;

		return true;
	}

	/**
	 * 判断字符类型是否是号码或字母
	 * 
	 * @param inputed
	 * @return
	 */
	public static boolean isNumberOrAlpha(String inputed) {
		if (inputed == null) {
			Log.e(TAG, "isNumberOrAlpha  inputed == null >> return false;");
			return false;
		}
		Pattern pNumber = Pattern.compile("[0-9]*");
		Matcher mNumber;
		Pattern pAlpha = Pattern.compile("[a-zA-Z]");
		Matcher mAlpha;
		for (int i = 0; i < inputed.length(); i++) {
			mNumber = pNumber.matcher(inputed.substring(i, i + 1));
			mAlpha = pAlpha.matcher(inputed.substring(i, i + 1));
			if (!mNumber.matches() && !mAlpha.matches()) {
				return false;
			}
		}

		currentString = inputed;
		return true;
	}

	/**
	 * 判断字符类型是否是身份证号
	 * 
	 * @param idCard
	 * @return
	 */
	public static boolean isIDCard(String idCard) {
		if (isNumberOrAlpha(idCard) == false) {
			return false;
		}
		idCard = getString(idCard);
		if (idCard.length() == 15) {
			Log.w(TAG, "isIDCard idCard.length() == 15 old IDCard");
			currentString = idCard;
			return true;
		}
		if (idCard.length() == 18) {
			currentString = idCard;
			return true;
		}

		return false;
	}

	public static final String HTTP = "http";
	public static final String URL_PREFIX = "http://";
	public static final String URL_PREFIXs = "https://";
	public static final String URL_STAFFIX = URL_PREFIX;
	public static final String URL_STAFFIXs = URL_PREFIXs;

	/**
	 * 判断字符类型是否是网址
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		if (isNotEmpty(url, true) == false) {
			return false;
		} else if (!url.startsWith(URL_PREFIX) && !url.startsWith(URL_PREFIXs)) {
			return false;
		}

		currentString = url;
		return true;
	}

	public static final String FILE_PATH_PREFIX = "file://";

	/**
	 * 判断文件路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFilePathExist(String path) {
		return StringUtils.isFilePath(path) && new File(path).exists();
	}

	/**
	 * 判断字符类型是否是路径
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFilePath(String path) {
		if (isNotEmpty(path, true) == false) {
			return false;
		}

		if (!path.contains(".") || path.endsWith(".")) {
			return false;
		}

		currentString = path;

		return true;
	}
	// 判断字符类型 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 提取特殊字符<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 去掉string内所有非数字类型字符
	 * 
	 * @param tv
	 * @return
	 */
	public static String getNumber(TextView tv) {
		return getNumber(getString(tv));
	}

	/**
	 * 去掉string内所有非数字类型字符
	 * 
	 * @param object
	 * @return
	 */
	public static String getNumber(Object object) {
		return getNumber(getString(object));
	}

	/**
	 * 去掉string内所有非数字类型字符
	 * 
	 * @param cs
	 * @return
	 */
	public static String getNumber(CharSequence cs) {
		return getNumber(getString(cs));
	}

	/**
	 * 去掉string内所有非数字类型字符
	 * 
	 * @param s
	 * @return
	 */
	public static String getNumber(String s) {
		if (isNotEmpty(s, true) == false) {
			return "";
		}

		String numberString = "";
		String single;
		for (int i = 0; i < s.length(); i++) {
			single = s.substring(i, i + 1);
			if (isNumber(single)) {
				numberString += single;
			}
		}

		return numberString;
	}


	/**
	 * 获取去掉所有 空格 、"-" 、"+86" 后的phone
	 *
	 * @param phone
	 * @return
	 */
	public static String getCorrectPhone(String phone) {
		if (isNotEmpty(phone, true) == false) {
			return "";
		}

		phone = getNoBlankString(phone);
		phone = phone.replaceAll("-", "");
		if (phone.startsWith("+86")) {
			phone = phone.substring(3);
		}else if(phone.startsWith("12953")){
			phone = phone.substring(5);
		}
		return phone;
	}
	/**
	 * 转化星期
	 *
	 * @param week
	 * @return
	 */
	public static String getChanceWeek(String week) {
		if (isNotEmpty(week, true) == false) {
			return "";
		}
		if(StringUtils.equals(week,"1")){
			week="星期一";

		}else if(StringUtils.equals(week,"2")){
			week="星期二";

		}else if(StringUtils.equals(week,"3")){
			week="星期三";

		}else if(StringUtils.equals(week,"4")){
			week="星期四";

		}else if(StringUtils.equals(week,"5")){
			week="星期五";

		}else if(StringUtils.equals(week,"6")){
			week="星期六";

		}else  if(StringUtils.equals(week,"7")){
			week="星期天";

		}
		return week;
	}


	/**
	 * 裁剪月日格式
	 * @param time
	 * @return
	 */
	public static String tripData(String time) {
		if (isNotEmpty(time, true) == false) {
			return "";
		}
	     String strTime=time.substring(5,10);
		return strTime;
	}
	/**
	 * 裁剪年月格式
	 * @param time
	 * @return
	 */
	public static String tripYearAndMonth(String time) {
		if (isNotEmpty(time, true) == false) {
			return "";
		}
		String strTime=time.substring(0,7);
		return strTime;
	}

	/**
	 * 裁剪时间格式
	 * @param time
	 * @return
	 */
	public static String tripTime(String time) {
		if (isNotEmpty(time, true) == false) {
			return "";
		}
	     String strTime=time.substring(11,16);
		return strTime;
	}

	public static String repalceEmptyString(String str){
		if (isNotEmpty(str, true) == false || equals("null",str)) {
			str="暂无";
		}
		return str;
	}


	//版本名
	public static String getVersionName(Context context) {
		return "VVapp-"+getPackageInfo(context).versionName.substring(0,5)+".apk";//TODO 此处获取版本名称之后，根据后端命名规范平凑出名字
	}

	//版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}
	/**
	 * 判断集合是否为null或者0个元素
	 *
	 * @param c
	 * @return
	 */
	public static boolean isNullOrEmpty(Collection c) {
		if (null == c || c.isEmpty()) {
			return true;
		}
		return false;
	}
}
