package com.saas.saasuser.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tanlin on 2017/12/19.
 */

public class PreferencesManager {

    private final String tag = PreferencesManager.class.getSimpleName();

    private Context mContext;
    private SharedPreferences preferences;
    private static String shareName = "SHARE_DATA";
    private static PreferencesManager instance;

    /**
     * 构造方法
     *
     * @param context
     */
    private PreferencesManager(Context context) {
        this(context, shareName);
    }

    /**
     * 构造方法
     *
     * @param context
     * @param shareName
     */
    private PreferencesManager(Context context, String shareName) {
        mContext = context;
        preferences = context.getSharedPreferences(shareName,
                Context.MODE_PRIVATE);
    }

    /**
     * 得到单例模式的PreferencesManager对象
     *
     * @param context
     *            上下文
     * @return
     */
    public static PreferencesManager getInstance(Context context) {
        return getInstance(context, shareName);
    }

    /**
     * 得到单例模式的PreferencesManager对象
     *
     * @param context
     *            上下文
     * @param shareName
     *            文件名称
     * @return
     */
    public static PreferencesManager getInstance(Context context,
                                                 String shareName) {
        if (instance == null) {
            synchronized (PreferencesManager.class) {
                if (instance == null) {
                    instance = new PreferencesManager(context, shareName);
                }
            }
        }
        return instance;
    }

    public void put(String key, boolean value) {
        SharedPreferences.Editor edit = preferences.edit();
        if (edit != null) {
            edit.putBoolean(key, value);
            edit.commit();
        }
    }

    public void put(String key, String value) {
        SharedPreferences.Editor edit = preferences.edit();
        if (edit != null) {
            edit.putString(key, value);
            edit.commit();
        }
    }

    public void put(String key, int value) {
        SharedPreferences.Editor edit = preferences.edit();
        if (edit != null) {
            edit.putInt(key, value);
            edit.commit();
        }
    }

    public void put(String key, float value) {
        SharedPreferences.Editor edit = preferences.edit();
        if (edit != null) {
            edit.putFloat(key, value);
            edit.commit();
        }
    }

    public void put(String key, long value) {
        SharedPreferences.Editor edit = preferences.edit();
        if (edit != null) {
            edit.putLong(key, value);
            edit.commit();
        }
    }


    public String get(String key) {
        return preferences.getString(key, "");
    }

    public String get(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean get(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public int get(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public float get(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public long get(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }



}
