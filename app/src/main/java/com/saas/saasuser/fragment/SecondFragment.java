package com.saas.saasuser.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.activity.AddTripApplyActivity;
import com.saas.saasuser.activity.OrderCompeleteListActivity;
import com.saas.saasuser.activity.ProfileActivity;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.ActionSheetDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class SecondFragment extends BaseFragment  {

    private Button btn_rent_apply;
    private Button btn_order_complete;
    private SharedPreferencesUtil util;
    private RelativeLayout rlRoot;
    private ImageView ivBackground;
    private LinearLayout llhead;

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private String imageName, imagePath;
    private Bitmap mBitmap;
    private File mFile;
    public String filePath;
    //private List<AdInfo> advList = null;




    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_second, null);
        btn_rent_apply = (Button) view.findViewById(R.id.btn_rent_apply);
        btn_order_complete = (Button) view.findViewById(R.id.btn_order_complete);
        rlRoot = (RelativeLayout) view.findViewById(R.id.root_layout);
        ivBackground = (ImageView) view.findViewById(R.id.iv_background);
        llhead = (LinearLayout) view.findViewById(R.id.head_more);
        util = new SharedPreferencesUtil(mActivity);
        TextView tvTitle=(TextView)view.findViewById(R.id.head_title);
        tvTitle.setText("VV租行");
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/vv.png";
        Bitmap bitmap1 = readImg();
        if (bitmap1 != null) {
            ivBackground.setImageBitmap(bitmap1);
        }

        btn_order_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, OrderCompeleteListActivity.class));
            }
        });
        btn_rent_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mActivity, AddTripApplyActivity.class).putExtra("reBuiltOrder", "0").putExtra("orderId", ""));

            }
        });
        llhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, ProfileActivity.class));
            }
        });
        rlRoot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog();
                return false;

            }
        });


        return view;
    }


    private void showDialog() {
        new ActionSheetDialog(mActivity)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                imageName = "vvpic" + ".png";
                                Intent intent2 = new Intent(Intent.ACTION_PICK, null);
                                intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent2, PHOTO_REQUEST_GALLERY);

                            }
                        }).addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        ToastUtils.showShortToastSafe(mActivity, "暂不支持拍照设置！");
//                        imageName = "vvpic" + ".png";
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(new File(DIR_AVATAR, imageName)));
//                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                }).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PHOTO_REQUEST_TAKEPHOTO:
                    try {
                        if (data != null) {
                            Uri uri = data.getData();
                            getImg(uri);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case PHOTO_REQUEST_GALLERY:
                    if (data != null) {
                        try {
                            mBitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                            saveImg(mBitmap);
                            ivBackground.setImageBitmap(mBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void getImg(Uri uri) {
        try {
            InputStream inputStream = mActivity.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            saveImg(bitmap);
            ivBackground.setImageBitmap(bitmap);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    //裁剪图片
//    private void cutImg(Uri uri) {
//        if (uri != null) {
//            Intent intent = new Intent("com.android.camera.action.CROP");
//            intent.setDataAndType(uri, "image/*");
//            //true:出现裁剪的框
//            intent.putExtra("crop", "true");
//            //裁剪宽高时的比例
//            intent.putExtra("aspectX", 1);
//            intent.putExtra("aspectY", 1);
//            //裁剪后的图片的大小
//            intent.putExtra("outputX", 300);
//            intent.putExtra("outputY", 300);
//            intent.putExtra("return-data", true);  // 返回数据
//            intent.putExtra("output", uri);
//            intent.putExtra("scale", true);
//            startActivityForResult(intent, 0x2);
//        } else {
//            return;
//        }
//    }


    private Bitmap readImg() {
        File mfile = new File(filePath);
        Bitmap bm = null;
        if (mfile.exists()) {
            bm = BitmapFactory.decodeFile(filePath);
        }
        return bm;
    }

    private void saveImg(Bitmap mBitmap) {
        File f = new File(filePath);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





//    private void saveBitmap(Bitmap bitmap,String bitName) throws IOException
//    {
//        File file = new File(Constants.DIR_AVATAR+bitName);
//        if(file.exists()){
//            file.delete();
//        }
//        FileOutputStream out;
//        try{
//            out = new FileOutputStream(file);
//            if(bitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
//            {
//                out.flush();
//                out.close();
//            }
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//       // initData();
//    }
//    	private void initData() {
//		advList = new ArrayList<>();
//		AdInfo adInfo = new AdInfo();
//		adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png");
//		adInfo.setAdId("1");
//
//		advList.add(adInfo);
//		adInfo = new AdInfo();
//		adInfo.setActivityImg("https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage2.png");
//		adInfo.setAdId("2");
//
//		advList.add(adInfo);
//		AdManager adManager = new AdManager(getActivity(), advList);
//		adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
//			@Override
//			public void onImageClick(View view, AdInfo advInfo) {
//				Toast.makeText(getActivity(), "您点击了ViewPagerItem..."+advInfo.getAdId(), Toast.LENGTH_SHORT).show();
//			}
//		});
//		adManager.setOverScreen(true)
//				.setPageTransformer(new RotateDownPageTransformer());
//
//		adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP);
//
//
//
//
//	}


}
