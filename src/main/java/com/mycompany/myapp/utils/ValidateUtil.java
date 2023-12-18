package com.mycompany.myapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class ValidateUtil {

    public static boolean checkResetPass(String oldPass, String newPass) {
        return oldPass.equals(newPass);
    }
}
