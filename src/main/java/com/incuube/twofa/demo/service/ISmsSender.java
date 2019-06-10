package com.incuube.twofa.demo.service;

public interface ISmsSender {

    public boolean send(String phone, String message);

}
