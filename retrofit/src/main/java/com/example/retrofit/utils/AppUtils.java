package com.example.retrofit.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    /**
     * 判断字符串是否手机号码
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        boolean isValid = false;
        CharSequence inputStr = phoneNumber;
        //正则表达式
        String phone="^1[34578]\\d{9}$" ;

        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
