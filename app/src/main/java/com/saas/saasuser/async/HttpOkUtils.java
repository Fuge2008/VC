package com.saas.saasuser.async;


import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;


/**
 * Created by tanlin on 2017/6/29.
 */

public class HttpOkUtils {
    private static String SIGN_ENCRY;
    //提供方法供外界调用
    public static String post(String url, Map<String, String> hashMap) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).post(buildRequsetBody(hashMap)).build();

        Response response = client.newCall(request).execute();

        String result = response.body().string();

        return result;
    }

    //通过MMap的键值对构建请求对象body
    private static RequestBody buildRequsetBody(Map<String, String> params) {

        // FormBody.Builder builder = new FormBody.Builder();

        FormEncodingBuilder builder=new FormEncodingBuilder();

        if (params != null) {
            for (Map.Entry<String, String> entity : params.entrySet()) {

                builder.add(entity.getKey(), entity.getValue());
            }

        }
        return builder.build();

    }
//    public static  Map<String,String> getSignStr(Map<String,String> map){
//        if(map!=null){
//            StringBuffer sb = new StringBuffer();
//            TreeMap<String, String> treeMap = new TreeMap<String, String>();
//
//            Set map_set = map.keySet();
//            Iterator iterator = map_set.iterator();
//            while (iterator.hasNext()) {
//                String keyString = (String) iterator.next();
//                String valueString = (String) map.get(keyString);
//                treeMap.put(keyString, valueString);
//            }
//            Set tree_set = treeMap.entrySet();
//            Iterator it = tree_set.iterator();
//            while (it.hasNext()) {
//                Map.Entry entry = (Map.Entry) it.next();
//                String k = (String) entry.getKey();
//                String v = (String) entry.getValue();
//                sb.append(k + "=" + v + "&");
//            }
//            sb.append("secret="+SIGN_ENCRY);
//
//            try {
//                String signString= Encry_MD5.getMD5(sb.toString(), "utf-8");
//                map.put("sign", signString);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return map;
//        }else {
//            return null;
//        }
//
//    }

}
