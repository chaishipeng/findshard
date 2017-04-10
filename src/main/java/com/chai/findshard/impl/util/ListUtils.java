package com.chai.findshard.impl.util;

import java.util.List;

/**
 * Created by chaishipeng on 2017/4/7.
 */
public class ListUtils {

    public static boolean isEmpty(List list) {
        if (list == null) {
            return true;
        }
        if (list.isEmpty()){
            return true;
        }
        return false;
    }

}
