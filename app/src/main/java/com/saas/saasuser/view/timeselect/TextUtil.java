package com.saas.saasuser.view.timeselect;

import java.text.DecimalFormat;

public class TextUtil {

    private TextUtil(){}

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || "null".equalsIgnoreCase(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    public static String filterNull(String str, String _default, String tail){

        if (str == null || "".equals(str) || "null".equalsIgnoreCase(str)) {
            return _default;
        }else {
            if(tail != null) {
                str += tail;
            }
            return str;
        }
    }

    //重载
    public static String filterNull(String str, String _default){
        return filterNull(str, _default, null);
    }

    //重载
    public static String filterNull(String str){
        return filterNull(str, "", null);
    }

    //重载
    public static String filterNull(Float f, String _default, String tail){
        String str = f+"";
        if (str == null || "".equals(str) || "null".equalsIgnoreCase(str)) {
            return _default;
        }else {
            if(tail != null) {
                str += tail;
            }
            return str;
        }
    }

    /**
     * 保留2位小数
     * @return
     */
    public String fix(float f) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(f);
    }

}