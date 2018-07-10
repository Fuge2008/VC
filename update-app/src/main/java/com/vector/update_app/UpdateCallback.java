package com.vector.update_app;

import org.json.JSONObject;

/**
 * 新版本版本检测回调
 */
public class UpdateCallback {

    /**
     * 解析json,自定义协议
     *
     * @param json 服务器返回的json
     * @return UpdateAppBean
     */
    protected UpdateAppBean parseJson(String json) {
        UpdateAppBean updateAppBean = new UpdateAppBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            updateAppBean.setUpdate(jsonObject.optString("update"))
                    .setNewVersion("1.1.1")
                    .setApkFileUrl("https://api.vv-che.com/OrderV2/DownLoadNewVersion")
                    .setTargetSize(jsonObject.optString("target_size"))
                    .setUpdateLog("1，新增图片下单。\r\n 2，车辆交接。\r\n 3，新增app提示更新。\r\n 4，更改订单流程。\r\n 5，处理了一些bug。")
                    .setConstraint(jsonObject.optBoolean("constraint"))
                    .setNewMd5(jsonObject.optString("new_md5"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateAppBean;
    }

    /**
     * 有新版本
     *
     * @param updateApp        新版本信息
     * @param updateAppManager app更新管理器
     */
    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
        updateAppManager.showDialogFragment();
    }

    /**
     * 网路请求之后
     */
    protected void onAfter() {
    }


    /**
     * 没有新版本
     */
    protected void noNewApp() {
    }

    /**
     * 网络请求之前
     */
    protected void onBefore() {
    }

}
