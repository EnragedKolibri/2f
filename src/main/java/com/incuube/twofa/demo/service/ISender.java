package com.incuube.twofa.demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface ISender {

    public void send(String phone, String message) throws IOException;

}
