package com.saas.saasuser.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.AvatarStudio;
import com.saas.saasuser.view.CircleImageView;
import com.saas.saasuser.view.dialog.FXAlertDialog;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.saas.saasuser.R.id.iv_avatar;

/**
 * 个人信息详情界面
 */
public class ProfileDetailActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {
    private static String TAG = ProfileDetailActivity.class.getSimpleName();
//    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
//    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
//    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int UPDATE_ADDRESS = 4;
    private static final int UPDATE_NICK = 5;
    private static final int UPDATE_SIGN = 6;
    public static final int REQUSET = 7;
    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(R.id.tv_head_more)
    TextView tvHeadMore;
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_hjid)
    TextView tvHjid;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.tv_sign)
    TextView tvSign;


    private String imageName, province, city, imagePath;
    private JSONObject json;
    private SharedPreferencesUtil util;
    private boolean isFresh = false;
    private boolean hasChange = false;    //头像，昵称，好记号是否发生变化
    private ProgressDialog progressDialog;
    int tag;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    updateUser();
                    updateView();
                    break;
                case 1:
//                    LogUtils.e("显示图片");
//                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() +"/"+ imageName);
//                    ivAvatar.setImageBitmap(bitmap);
//                    hasChange = true;
//                    LogUtils.e("显示图片宽度:"+bitmap.getWidth());




                    break;
                case 2:
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "个人信息", true);
        initUi();

    }

    private void initUi() {
        String nick = util.getNickName();
        String fxid = util.getMobilePhone();
        String sex = util.getSex();
        String sign = util.getMotto();
        String avatarUrl = util.getPicture();
        Glide.with(ProfileDetailActivity.this).load(avatarUrl).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.my_headphoto).into(ivAvatar);
        tvName.setText(nick);
        if (TextUtils.isEmpty(fxid)) {
            tvHjid.setText("未设置");

        } else {
            tvHjid.setText(fxid);
        }
        tvSex.setText(sex);
        if (TextUtils.isEmpty(sign)) {
            tvSign.setText("未填写");
        } else {
            tvSign.setText(sign);
        }

        new NetIntent().client_getPersonInfo(util.getUserId(), new NetIntentCallBackListener(ProfileDetailActivity.this));
    }


    private void updateUser() {
        try {
            Log.e("info", json.toString());
            util.setPicture("http://" + json.getString("pHeadPath"));
//            util.setProvince(json.getString("province"));
//            util.setCity(json.getString("pAddress"));
//            util.setNickName(json.getString("pNickname"));
//            util.setRealName(json.getString("StaffName"));
//            util.setMotto(json.getString("CompanyName"));
//            util.setBirthday(json.getString("pBirthday"));
//            util.setSex(StringUtils.equals(json.getString("Sex"), "男") ? 1 : 2);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateView() {
        try {
            if (util.getIsLogin()) {
                tvName.setText(StringUtils.isNotEmpty(util.getNickName(),true)?util.getNickName():"未设置");
                tvHjid.setText(util.getMobilePhone());
            } else {
                ivAvatar.setImageResource(R.drawable.my_headphoto);
                tvName.setText("请登录");
                tvHjid.setText(" ");

            }
            tvRegion.setText("广东" + " " + "深圳");
            tvAddress.setText(StringUtils.isNotEmpty(util.getAddress(),true)?util.getAddress():"未设置");
            tvSign.setText(StringUtils.isNotEmpty(util.getMotto(),true)?util.getMotto():"未设置");
            tvSex.setText(util.getSex());
            Glide.with(ProfileDetailActivity.this).load(util.getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.my_headphoto).into(ivAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void showPhotoDialog() {
//        List<String> items = new ArrayList<String>();
//        items.add("拍照");
//        items.add("相册");
//        FXAlertDialog fxAlertDialog = new FXAlertDialog(ProfileDetailActivity.this, null, items);
//        fxAlertDialog.init(new FXAlertDialog.OnItemClickListner() {
//            @Override
//            public void onClick(int position) {
//                switch (position) {
//                    case 0:
//
//                        imageName = TimeUtils.getNowTime().trim().replace(":","").replace("-","").replace(" ","")+ ".png";
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageName)));
//                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
//                        break;
//                    case 1:
//                        imageName = TimeUtils.getNowTime().trim().replace(":","").replace("-","").replace(" ","")+ ".png";
//                        Intent intent2 = new Intent(Intent.ACTION_PICK, null);
//                        intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                        startActivityForResult(intent2, PHOTO_REQUEST_GALLERY);
//                        break;
//                }
//            }
//        });
//    }

    private void showSexDialog() {
        String title = "性别";
        List<String> items = new ArrayList<String>();
        items.add("男");
        items.add("女");
        FXAlertDialog fxFXAlertDialog = new FXAlertDialog(ProfileDetailActivity.this, title, items);
        fxFXAlertDialog.init(new FXAlertDialog.OnItemClickListner() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        if (!util.getSex().equals("男")) {

                            updateInServer(Constants.JSON_KEY_SEX, "男", "");
                        }
                        break;
                    case 1:
                        if (!util.getSex().equals("女")) {

                            updateInServer(Constants.JSON_KEY_SEX, "女", "");
                        }
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
//                case PHOTO_REQUEST_TAKEPHOTO:
//
//                    startPhotoZoom(
//                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageName)),
//                            480);
//                    break;
//
//                case PHOTO_REQUEST_GALLERY:
//                    if (data != null)
//                        startPhotoZoom(data.getData(), 480);
//                    break;
//
//                case PHOTO_REQUEST_CUT:
//                    updateInServer(Constants.JSON_KEY_AVATAR, Environment.getExternalStorageDirectory() + imageName, "");
//                    File file = new File(Environment.getExternalStorageDirectory(), imageName);
//                    new NetIntent().client_editPersonImage(util.getUserId(), file, new NetIntentCallBackListener(ProfileDetailActivity.this));
//                    hasChange = true;
//
//                    break;
                case UPDATE_ADDRESS:
                    String address = data.getStringExtra("value");
                    if (address != null) {
                        util.setAddress(address);
                        tvHjid.setText(address);
                        hasChange = true;
                    }
                    break;
                case UPDATE_NICK:
                    String nick = data.getStringExtra("value");
                    if (nick != null) {
                        util.setNickName(nick);
                        tvName.setText(nick);
                        hasChange = true;
                    }
                    break;
                case UPDATE_SIGN:
                    String sign = data.getStringExtra("value");
                    util.setMotto(sign);
                    if (sign != null) {
                        tvSign.setText(sign);
                        hasChange = true;

                    }
                    break;
                case REQUSET:
                    if (requestCode == REQUSET && resultCode == RESULT_OK) {
                        province = data.getStringExtra("province");
                        city = data.getStringExtra("city");
                        String region = province + " " + city;
                        if (region != null) {
                            tvRegion.setText(region);
                            util.setProvince(province);
                            util.setCity(city);
                            hasChange = true;
                        }
                        updateInServer(Constants.JSON_KEY_CITY, city, province);
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

//    private void startPhotoZoom(Uri uri1, int size) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri1, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", size);
//        intent.putExtra("outputY", size);
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), imageName)));
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
//        intent.putExtra("noFaceDetection", true);
//        startActivityForResult(intent, PHOTO_REQUEST_CUT);
//    }


    private void updateInServer(final String key, final String value, final String value2) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在更新...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        //NetIntent netIntent = new NetIntent();
        if (StringUtils.equals(key, "sex")) {
            if (StringUtils.equals(value, "女")) {
                tag = 2;
            } else {
                tag = 1;
            }
//            netIntent.client_updateUserById(util.getUserId(), tag+"", "",  "","", "", "","",new NetIntentCallBackListener(ProfileDetailActivity.this));
            tvSex.setText(value);


        } else if (StringUtils.equals(key, "city") & value2 != null) {
//            netIntent.client_updateUserById(util.getUserId(), "",   "",value, value2,"", "","",new NetIntentCallBackListener(ProfileDetailActivity.this));
        }
        handler.sendEmptyMessageDelayed(2,1500);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            ckeckChange();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ckeckChange() {

        if (hasChange) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onError(Request request, Exception e) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResponse(String response) {
        System.out.println(response);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            json = jsonObject.getJSONObject("Data");
            //ToastUtils.showShortToastSafe(ProfileDetailActivity.this, response);
            LogUtils.e("返回数据：" + response);
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if (json.has("ImagePath") && StringUtils.isNotEmpty( json.getString("ImagePath"),true)) {
                    LogUtils.e("有头像数据：" + response);
                    imagePath =  json.getString("ImagePath");
                    util.setPicture("http://"+imagePath);
                   // handler.sendEmptyMessage(1);
                }
                if (json.has("LoginName")) {
                    LogUtils.e("有其他数据：" + response);
                    handler.sendEmptyMessage(0);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }


    @Override
    protected void onStart() {
        super.onStart();
        if (StringUtils.isNotEmpty(util.getAddress(), true)) {
            tvAddress.setText(util.getAddress());//地址保存在本地
        }

    }

    @OnClick({R.id.re_avatar, R.id.re_name, R.id.re_hjid, R.id.re_qrcode, R.id.re_address, R.id.re_sex, R.id.re_region, R.id.re_sign, iv_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.re_avatar:
                showPhotoDialog();
                break;
            case R.id.re_name:
                startActivityForResult(new Intent(ProfileDetailActivity.this,
                        ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_NICK).putExtra("default", util.getNickName()), UPDATE_NICK);
                break;
            case R.id.re_hjid:

                break;
            case R.id.re_qrcode:
                startActivity(new Intent(ProfileDetailActivity.this, QRGenerateActivity.class));
                break;
            case R.id.re_address:
                startActivityForResult(new Intent(ProfileDetailActivity.this,
                        ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_ADDRESS), UPDATE_ADDRESS);
                break;
            case R.id.re_sex:
                showSexDialog();
                break;
            case R.id.re_region:
                Intent intent3 = new Intent(ProfileDetailActivity.this, CitySelectActivity.class);
                startActivityForResult(intent3, REQUSET);
                break;
            case R.id.re_sign:
                startActivityForResult(new Intent(ProfileDetailActivity.this,
                        ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_SIGN).putExtra("default", util.getMotto()), UPDATE_SIGN);
                break;
            case iv_avatar:
                Intent intent = new Intent(ProfileDetailActivity.this, ShowPictureActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("path", util.getPicture());
                startActivity(intent);
                break;
        }
    }

    private void showPhotoDialog() {
        new AvatarStudio.Builder(ProfileDetailActivity.this)
                .needCrop(true)
                .setTextColor(Color.BLUE)
                .dimEnabled(true)
                .setAspect(1, 1)
                .setOutput(500, 500)
                .setText("打开相机", "从相册中选取", "取消")
                .show(new AvatarStudio.CallBack() {
                    @Override
                    public void callback(final String uri) {
                        // Picasso.with(MainActivity.this).load(new File(uri)).into(mImageView);
                        setAvataor(uri);
                    }
                });
    }

    private void setAvataor(final String uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(uri));
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    ivAvatar.setImageBitmap(bitmap);

                                    updateInServer(Constants.JSON_KEY_AVATAR, Environment.getExternalStorageDirectory() + imageName, "");//拉起进度条
                                    File file =new File(uri);//获取返回的文件
                                    LogUtils.e("图片路径："+uri.toString()+"  是否是文件："+file.isFile());
                                    new NetIntent().client_editPersonImage(util.getUserId(), file, new NetIntentCallBackListener(ProfileDetailActivity.this));//将图片文件提交到服务器
                                    hasChange = true;
                                }
                            });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}