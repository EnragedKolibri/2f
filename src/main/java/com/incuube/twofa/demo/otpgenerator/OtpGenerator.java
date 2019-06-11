package com.incuube.twofa.demo.otpgenerator;

import com.incuube.twofa.demo.utils.CommonConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpGenerator {

    private ArrayList<String> parsedMessage;
    private String otp;
    private String message;
    private String codeMask;


    public OtpGenerator(String string)
    {
        parsedMessage = getSeparatedMessage(string);
        //System.out.println("parsedMessage "+Arrays.deepToString(parsedMessage.toArray()));
        codeMask = getCodeMaskFromArrayList(parsedMessage);
        otp = generateOtpByCodeMask(codeMask);
        message = generateMessage(parsedMessage,otp);
        //System.out.println(message);
    }

    private ArrayList<String> getSeparatedMessage(String string)
    {
        Pattern patternCompile = Pattern.compile(CommonConstants.templatePattern);
        Matcher matcher = patternCompile.matcher(string);
        boolean match = matcher.matches();
        ArrayList<String> result = new ArrayList<>();
        for(int i = 1; i!=matcher.groupCount()+1;i++) {
            if (!matcher.group(i).isEmpty()){
                result.add(matcher.group(i));
            }
        }
        //System.out.println("getSeparatedMessage "+Arrays.deepToString(result.toArray()));

        return result;

    }

    public String getOtp() {
        return otp;
    }

    private static String generateOtpByCodeMask(String codeMask)
    {
        Random random = new Random();
        String code = codeMask.replaceAll("\\[", "").replaceAll("\\]","");
        char[] parsedCodeMask = code.toCharArray();
        String result;
        for (int i = 0; i!=parsedCodeMask.length; i++) {
            if (parsedCodeMask[i]=='*') {
                parsedCodeMask[i]=CommonConstants.alphabet.charAt(random.nextInt(CommonConstants.alphabet.length()));
            }

            if (parsedCodeMask[i]=='#'){
                parsedCodeMask[i]=(char)(random.nextInt(10)+'0');
            }
        }
        result = new String(parsedCodeMask);
        return result;

    }

    private String getCodeMaskFromArrayList(ArrayList<String> arrayList)
    {
        if (arrayList.size()>1)
        {
            if (arrayList.size()==2)
            {
                if (arrayList.get(0).contains("["))
                {
                    return arrayList.get(0);
                }else if (arrayList.get(1).contains("["))
                {
                    return arrayList.get(1);
                }else {
                    System.out.println("getCodeMaskFromArrayList ERROR there is no code mask in array list");
                }

            }

            if (arrayList.size()==3)
            {
                return arrayList.get(1);
            }
        }
        return arrayList.get(0);
    }

    private String generateMessage(ArrayList<String> parsedMessage,String otp)
    {
        String result;
        if (parsedMessage.size()>1)
        {
            if (parsedMessage.size()==2)
            {
                if (parsedMessage.get(0).contains("["))
                {
                    result = otp+parsedMessage.get(1);
                    return result;

                }else if (parsedMessage.get(1).contains("["))
                {
                    result = parsedMessage.get(0)+otp;
                    return result;
                }else {
                    System.out.println("generateMessage ERROR there is no code mask in array list");
                }

            }

            if (parsedMessage.size()==3)
            {
                result = parsedMessage.get(0)+otp+parsedMessage.get(2);
                return result;
            }
        }
        result = otp;
        return result;
    }

    public String getMessage() {
        return message;
    }
}
