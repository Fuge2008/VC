//package com.saas.saasuser.util;
//
//import java.io.IOException;
//
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//
//
////负责网络下载
//public class HttpManager {
//    private OkHttpClient mOkHttpClient = new OkHttpClient();
//    private static HttpManager ourInstance = new HttpManager();
//
//    public static HttpManager getInstance() {
//        return ourInstance;
//    }
//
//    private HttpManager() {
//    }
//    /**
//     * 普通get请求
//     */
//    public void getRequest(String url, Callback callback){
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        mOkHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public void postRequest(String url, RequestBody requestBody, Callback callback){
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)//请求体，参数
//                .build();
//        mOkHttpClient.newCall(request).enqueue(callback);
//    }
//
//    //上传数据给服务器
//    public void uploadImage(String url, RequestBody uploadBody, Callback callback) {
//        Request request = new Request.Builder()
//                .url(url)
//                .post(uploadBody)
//                .build();
//        mOkHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public void getRangeBytes(String imageUrl, Callback callback, int start, int end) {
//
//        //断点获取服务器的数据
//        Request request = new Request.Builder()
//                .url(imageUrl)
//                .addHeader("range","bytes="+start+"-"+end)
//                .build();
//
//        //enqueue OkHttp直接开子线程，下载数据
//        mOkHttpClient.newCall(request).enqueue(callback);
//    }
//
//    public Response asyncGetRangeBytes(String imageUrl,int start,int end){
//        //断点获取服务器的数据
//        Request request = new Request.Builder()
//                .url(imageUrl)
//                .addHeader("range","bytes="+start+"-"+end)//请求头
//                .build();
//        //execute OkHttp执行联网请求，下载数据
//        try {
//            return  mOkHttpClient.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
