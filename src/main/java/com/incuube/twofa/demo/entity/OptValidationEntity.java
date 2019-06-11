package com.incuube.twofa.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OptValidationEntity {
    @JsonProperty("token")
    @NotEmpty(message = "token may not be null or empty")

    private String token;

    @JsonProperty("otp")
    @NotEmpty(message = "token may not be null or empty")
    private String otpCode;
}
