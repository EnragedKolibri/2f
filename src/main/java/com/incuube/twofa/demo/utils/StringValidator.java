package com.incuube.twofa.demo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {

//    public static void main(String[] args) {
//        String str ="[*#*#]";
//        System.out.println(validateTemplate(str));
//        getSeparatedMessage(str);
//
//    }

    public static Boolean validateTemplate(String string, String regex)
    {
        Boolean result;
        if (string.length()>160)
        {
            return false;
        }
        Pattern patternCompile = Pattern.compile(regex);
        Matcher matcher = patternCompile.matcher(string);
        result = matcher.matches();
        System.out.println("validateTemplate "+result);
        return result;
    }




}
