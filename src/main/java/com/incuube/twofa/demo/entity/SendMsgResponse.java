package com.incuube.twofa.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendMsgResponse {
    @JsonProperty("token")
    private String token;

    public SendMsgResponse(String token)
    {
        this.token=token;
    }
}
