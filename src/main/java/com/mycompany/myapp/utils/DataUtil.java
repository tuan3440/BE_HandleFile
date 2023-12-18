package com.mycompany.myapp.utils;

import org.apache.commons.lang3.StringUtils;

public class DataUtil {

    public static String makeLikeQuery(String s) {
        if (StringUtils.isEmpty(s)) return null;
        s = s.trim().toLowerCase();
        return "%" + s + "%";
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
