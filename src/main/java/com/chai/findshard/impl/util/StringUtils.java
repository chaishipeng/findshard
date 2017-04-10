package com.chai.findshard.impl.util;

/**
 * Created by chaishipeng on 2017/4/7.
 */
public class StringUtils {

    public static final String EMPTY = "";

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        if (str.trim().length() <= 0) {
            return true;
        }
        return false;
    }

    public static String[] splitStr(String str, String split){
        if (isEmpty(str)) {
            return new String[]{};
        }
        return str.split(split);
    }

}
