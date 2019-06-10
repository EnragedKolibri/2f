package com.incuube.twofa.demo.utils;

import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {

//    private static String templateStartsFromCode = "(^[a-zA-Z\\s,.!?\\-]*)+\\[[*#]{4,8}\\]+([a-zA-Z\\s,.!?\\-]*)";
//    private static String templateStartsFromSymbols = "(^[a-zA-Z\\s,.!?\\-]*)+\\[[*#]{4,8}\\]+([a-zA-Z\\s,.!?\\-]*)";
//    private static String templatePattern = "(^[a-zA-Z\\s,.!?\\-]*\\[[*#]{4,8}\\][a-zA-Z\\s,.!?\\-]*)";
//    private static String codeMaskRegex = "(\\[[*#]{4,8}\\])";

    public static Boolean validateTemplate(String string)
    {
        if (string.length()>160)
        {
            return false;
        }

        if (StringUtils.countMatches(string,'[')>1||StringUtils.countMatches(string,']')>1)
        {
            return false;
        }
//        Pattern patternCompile = Pattern.compile(templatePattern);
//        Matcher matcher = patternCompile.matcher(string);
        getSeparatedMessage(string);
//        return matcher.find();
        return true;
    }

    public static ArrayList<String> getSeparatedMessage(String string)
    {
        String mask = string.substring(string.indexOf("["), string.indexOf("]")+1);
        ArrayList<String> parsedMessage = new ArrayList<>();
        String firstPart;
        String secondPart;
        String thirdPart;

        if (string.indexOf("[")>0)
        {
             firstPart = string.substring(0,string.indexOf("["));
             secondPart = mask;
             thirdPart = string.substring(string.indexOf("]")+1);
             parsedMessage.add(0,firstPart);
             parsedMessage.add(1,secondPart);
             parsedMessage.add(2,thirdPart);
        }

        if (string.indexOf("[") == 0)
        {
            firstPart = mask;
            secondPart = string.substring(string.indexOf("]")+1);
            parsedMessage.add(0,firstPart);
            parsedMessage.add(1,secondPart);
        }

        if (string.indexOf("[")==0 && string.length()==string.indexOf("]")+1)
        {
            firstPart = mask;
            parsedMessage.add(0,firstPart);
        }

        System.out.println("Mask is: "+mask);
        System.out.println("Parsed message is" +Arrays.toString(parsedMessage.toArray()));
        //parsedMessage.forEach(i-> System.out.println("Parsed message is" +mask));
        return parsedMessage;
    }
}
