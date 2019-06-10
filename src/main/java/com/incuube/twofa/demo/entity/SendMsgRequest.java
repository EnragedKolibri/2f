package com.incuube.twofa.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendMsgRequest  {
    @JsonProperty("phone")
    private String phoneNumber;
    @JsonProperty("template")
    private String template;
    @JsonProperty("ttl")
    private Integer codeTimeToLive;
}
