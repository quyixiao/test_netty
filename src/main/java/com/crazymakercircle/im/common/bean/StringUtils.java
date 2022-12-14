package com.crazymakercircle.im.common.bean;

public class StringUtils {


    public static boolean isNotEmpty(String from) {
        return from == null || from.length() == 0 ? false : true;
    }
}
