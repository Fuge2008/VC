package com.saas.saasuser.util;

import android.content.Context;

import com.alibaba.fastjson.util.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 处理网络缓存
 *
 */
public class CacheUtil {

	/**
	 * 设置 Pref缓存
	 * @param key   缓存表示: 可以使用url来标示一段json数据
	 * @param value    缓存内容是son数据
	 */
	public static void setCache(Context ctx, String key, String value) {
		 SPUtils.put(ctx, key, value);
	}

	/**
	 * 获取Pref缓存
	 * @param key
	 * @return
	 */
	public static String getCache(Context ctx, String key) {
		return (String) SPUtils.get(ctx, key, null);
	}

	/**
	 * 从本地缓存中读取数据
	 * @param cxt  contex对象
	 * @param url  
	 * @return
	 * @throws Exception
	 */
	public static String getFileCache(Context cxt, String url) throws Exception {
		// 获取系统缓存目录
		File cacheDir = cxt.getCacheDir();
		// 以网络链接作为文件名称,保证特定接口对应特定数据
		File cacheFile = new File(cacheDir, MD5Encoder.encode(url));

		if (cacheFile.exists()) {// 缓存文件存在
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(cacheFile));
				String validTime = reader.readLine();// 读取第一行内容,缓存截止时间
				if (System.currentTimeMillis() < Long.parseLong(validTime)) {// 当前时间小于缓存截止时间,说明缓存还在有效期范围内

					String line = null;
					StringBuffer sb = new StringBuffer();
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}

					return sb.toString();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.close(reader);
			}
		}

		return null;
	}

	/**
	 * 向本地缓存写数据(缓存只保留30分钟)
	 * @param context   Context对象
	 * @param url		资源的url
	 * @param result	取出的缓存结果
	 * @throws Exception
	 */
	public static void setFileCache(Context context, String url, String result) throws Exception {
		// 获取系统缓存目录
		File cacheDir = context.getCacheDir();
		// 以网络链接作为文件名称,保证特定接口对应特定数据
		File cacheFile = new File(cacheDir, MD5Encoder.encode(url));

		FileWriter writer = null;
		try {
			writer = new FileWriter(cacheFile);

			// 缓存有效期限, 截止时间设定为半小时之后
			long validTime = System.currentTimeMillis() + 30 * 60 * 1000;
			writer.write(validTime + "\n");// 将缓存截止时间写入文件第一行
			writer.write(result.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(writer);
		}
	}

}
