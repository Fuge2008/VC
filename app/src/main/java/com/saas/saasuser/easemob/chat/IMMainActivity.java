/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.saas.saasuser.easemob.chat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.LoginActivity;
import com.saas.saasuser.activity.QRGenerateActivity;
import com.saas.saasuser.activity.QRScanActivity;
import com.saas.saasuser.easemob.Constant;
import com.saas.saasuser.easemob.DemoHelper;
import com.saas.saasuser.easemob.db.InviteMessgeDao;
import com.saas.saasuser.easemob.db.UserDao;
import com.saas.saasuser.entity.ActionItem;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.TitlePopup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NewApi")
public class IMMainActivity extends IMBaseActivity {

    protected static final String TAG = "TMainActivity";
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    // textview for unread message count
    private TextView unreadLabel;
    // textview for unread event message
    private TextView unreadAddressLable;

    //private Button[] mTabs;
    private ContactListFragment contactListFragment;
    private Fragment[] fragments;
    //private int index;
    private int currentTabIndex = 0;
    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    private TitlePopup titlePopup; // 定义标题栏弹窗按钮
    private SharedPreferencesUtil util;

    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//		    String packageName = getPackageName();
//		    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//		    if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//				try {
//					//some device doesn't has activity to handle this intent
//					//so add try catch
//					Intent intent = new Intent();
//					intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//					intent.setData(Uri.parse("package:" + packageName));
//					startActivity(intent);
//				} catch (Exception e) {
//				}
//			}
//		}

        //make sure activity will not in background if user is logged into another device or removed
        if (getIntent() != null &&
                (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_im_main);
        ButterKnife.bind(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "消息", true);
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);

        findViewById(R.id.head_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showPopWindow(v);
            }
        });
        // runtime permission for android 6.0, just require all permissions here for simple
//		requestPermissions();

//		initView();

        //umeng api
//		MobclickAgent.updateOnlineConfig(this);
//		UmengUpdateAgent.setUpdateOnlyWifi(false);
//		UmengUpdateAgent.update(this);

        showExceptionDialogFromIntent(getIntent());

        inviteMessgeDao = new InviteMessgeDao(this);
        UserDao userDao = new UserDao(this);
        conversationListFragment = new ConversationListFragment();
        contactListFragment = new ContactListFragment();
//		SettingsFragment settingFragment = new SettingsFragment();
        fragments = new Fragment[]{contactListFragment, conversationListFragment};
//
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
                .add(R.id.fragment_container, contactListFragment).hide(conversationListFragment).show(contactListFragment)
                .commit();

        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();


        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        EMClient.getInstance().addClientListener(clientListener);
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
        //debug purpose only
        registerInternalDebugReceiver();
    }

    EMClientListener clientListener = new EMClientListener() {
        @Override
        public void onMigrate2x(boolean success) {
            Toast.makeText(IMMainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
            if (success) {
                refreshUIWithMessage();
            }
        }
    };

//	@TargetApi(23)
//	private void requestPermissions() {
//		PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
//			@Override
//			public void onGranted() {
////				Toast.makeText(TMainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onDenied(String permission) {
//				//Toast.makeText(TMainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
//			}
//		});
//	}

    /**
     * init views
     */
//	private void initView() {
//
//		mTabs = new Button[3];
//		mTabs[0] = (Button) findViewById(R.id.btn_conversation);
//		mTabs[1] = (Button) findViewById(R.id.btn_address_list);
//		mTabs[2] = (Button) findViewById(R.id.btn_setting);
//		// select first tab
//		mTabs[0].setSelected(true);
//	}

    /**
     * on tab clicked
     *
     * @param view
     */
//	public void onTabClicked(View view) {
//		switch (view.getId()) {
//		case R.id.btn_conversation:
//			index = 0;
//			break;
//		case R.id.btn_address_list:
//			index = 1;
//			break;
//		case R.id.btn_setting:
//			index = 2;
//			break;
//		}
//		if (currentTabIndex != index) {
//			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//			trx.hide(fragments[currentTabIndex]);
//			if (!fragments[index].isAdded()) {
//				trx.add(R.id.fragment_container, fragments[index]);
//			}
//			trx.show(fragments[index]).commit();
//		}
//		mTabs[currentTabIndex].setSelected(false);
//		// set current tab selected
//		mTabs[index].setSelected(true);
//		currentTabIndex = index;
//	}

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
//			for (EMMessage message : messages) {
//				EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//				final String action = cmdMsgBody.action();//获取自定义action
//				if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//					RedPacketUtil.receiveRedPacketAckMessage(message);
//				}
//			}
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
            }
        });
    }
//
//	@Override
//	public void back(View view) {
//		super.back(view);
//	}

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
//		intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                if (currentTabIndex == 0) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                } else if (currentTabIndex == 1) {
                    if (contactListFragment != null) {
                        contactListFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(IMMainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
                //red packet code : 处理红包回执透传消息
//				if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//					if (conversationListFragment != null){
//						conversationListFragment.refresh();
//					}
//				}
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        Toast.makeText(IMMainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
            updateUnreadAddressLable();
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onFriendRequestAccepted(String username) {
        }

        @Override
        public void onFriendRequestDeclined(String username) {
        }
    }

    public class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {

        }

        @Override
        public void onGroupEvent(int event, String target, final List<String> username) {
            switch (event) {
                case EMMultiDeviceListener.GROUP_LEAVE:
                    ChatActivity.activityInstance.finish();
                    break;
                default:
                    break;
            }
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }

    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            //unreadLabel.setVisibility(View.VISIBLE);
        } else {
            //unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                    //unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    //unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMsgsCount();
    }

    private InviteMessgeDao inviteMessgeDao;

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    private AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;
    private BroadcastReceiver internalDebugReceiver;
    private ConversationListFragment conversationListFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!IMMainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new AlertDialog.Builder(IMMainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(IMMainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(IMMainActivity.this, LoginActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

//	@Override
//	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//			@NonNull int[] grantResults) {
//		PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
//	}


    private void showPopWindow(View v) {
        // 实例化标题栏弹窗
        titlePopup = new TitlePopup(this, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        // 给标题栏弹窗添加子类

        titlePopup.addAction(new ActionItem(this, "加好友",
                R.drawable.popwindow_add_icon));
        titlePopup.addAction(new ActionItem(this, "二维码",
                R.drawable.mm_title_btn_qrcode_normal));
        titlePopup.addAction(new ActionItem(this, "扫一扫",
                R.drawable.popwindow_sc_icon));
        titlePopup.addAction(new ActionItem(this, "通讯录",
                R.drawable.popwindow_more_icon));


        titlePopup.show(v);
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {

            @Override
            public void onItemClick(ActionItem item, int position) {
                if (util.getIsLogin()) {
                    if (item.mTitle.equals("加好友")) {
                        startActivity(new Intent(IMMainActivity.this, AddContactActivity.class));
                    } else if (item.mTitle.equals("二维码")) {

                        startActivity(new Intent(IMMainActivity.this, QRGenerateActivity.class));

                    } else if (item.mTitle.equals("扫一扫")) {

                        startActivity(new Intent(IMMainActivity.this, QRScanActivity.class));

                    } else if (item.mTitle.equals("通讯录")) {

                        //startActivity(new Intent(IMMainActivity.this, AddEnergyActivity.class));
                        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                        trx.hide(fragments[currentTabIndex]);
                        if (!fragments[1].isAdded()) {
                            trx.add(R.id.fragment_container, fragments[1]);
                        }
                        trx.show(fragments[1]).commit();
                    }
                } else {
                    ToastUtils.showShortToast(IMMainActivity.this, "抱歉，请您先登录！！");
                }
            }

        });
    }
}
