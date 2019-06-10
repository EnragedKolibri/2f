package com.incuube.twofa.demo.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SmsSenderImpl implements ISmsSender {
    @Override
    public boolean send(String phone, String message) {
        return false;
    }
}
