package com.chai.findshard.impl.util;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by chaishipeng on 2017/4/7.
 */
public class IntegerUtils {

    public static Integer[] toArray(String str, String split){
        if (StringUtils.isEmpty(str)) {
            return new Integer[]{};
        }
        String[] strings = str.split(split);
        Integer[] integers = new Integer[strings.length];
        for (int i = 0; i< strings.length ; i++) {
            integers[i] = Integer.parseInt(strings[i]);
        }
        return integers;
    }

    public static List<Integer> toList(String str, String split){
        if (StringUtils.isEmpty(str)) {
            return Lists.newArrayList();
        }
        String[] strings = str.split(split);
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i< strings.length ; i++) {
            list.add(Integer.parseInt(strings[i]));
        }
        return list;
    }

    public static String convertToString(Integer[] numbers, String split){
        if (isEmpty(numbers)) {
            return StringUtils.EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0 ; i < numbers.length ; i++) {
            if (i > 0) {
                stringBuilder.append(split);
            }
            stringBuilder.append(numbers[i]);
        }
        return stringBuilder.toString();
    }

    public static boolean isEmpty(Integer[] numbers) {
        if (numbers == null) {
            return true;
        }
        if (numbers.length <= 0) {
            return true;
        }
        return false;
    }

}
