package com.incuube.twofa.demo.otpgenerator;

import java.util.ArrayList;

public class OtpGenerator {

    private ArrayList<String> parsedMessage;
    private String code;
    private String mask;

    public OtpGenerator(ArrayList<String> parsedMessage)
    {
        this.parsedMessage = parsedMessage;
    }

    public String generateOtp()
    {

        return "generateOtp";

    }

    private void test() {

    }

    public String generateMessage()
    {
        String result ="";



        return result;
    }
}
