package com.saas.saasuser.util;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkUtils {
	private static OkHttpClient ok=null;
	private static final MediaType MEDIA_TYPE_PNG=MediaType.parse("image/png");
	private static File file;

	public static OkHttpClient getInstance() {
		if (ok == null) {
			synchronized (OkUtils.class) {
				if (ok == null)
					ok = new OkHttpClient();
			}
		}
		return ok;
	}

	public static void upDate(String url,Map<String, String> map,Callback callback){
		FormBody.Builder builder=new FormBody.Builder();
		//遍历map中所有的参数到builder
		for (String key : map.keySet()) {
			builder.add(key, map.get(key));
		}
		Request request = new Request.Builder()
				.url(url)
				.post(builder.build())
				.build();
		Call call = getInstance().newCall(request);
		call.enqueue(callback);
	}

	public static void upImg(String url,String strImg,String path,Map<String, String> map,Callback onClickListener){
		String imagpath = path.substring(0, path.lastIndexOf("/"));
		String imgName []=path.split("/");
		for(int i=0;i<imgName.length;i++){

			if(i==imgName.length-1){
				String name=imgName[i];
				file = new File(imagpath, name);
			}
		}
		MultipartBody.Builder builder=new MultipartBody.Builder().setType(MultipartBody.FORM);
		RequestBody fileBody=RequestBody.create(MEDIA_TYPE_PNG, file);
		//遍历map中所有的参数到builder
		for (String key : map.keySet()) {
			builder.addFormDataPart(key, map.get(key));
		}
		//将文件添加到builder中
		builder.addFormDataPart(strImg,file.getName(),fileBody);
		//创建请求体
		RequestBody requestBody=builder.build();

		Request request=new Request.Builder().url(url).post(requestBody).build();
		Call call = getInstance().newCall(request);
		call.enqueue(onClickListener);
	}

}
