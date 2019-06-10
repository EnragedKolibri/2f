package com.incuube.twofa.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OptValidationEntity {
    @JsonProperty("token")
    private String token;
    @JsonProperty("otp")
    private String otpCode;
}
