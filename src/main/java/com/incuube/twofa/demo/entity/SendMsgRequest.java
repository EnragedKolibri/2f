package com.incuube.twofa.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.incuube.twofa.demo.utils.InRange;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendMsgRequest  {

    @JsonProperty(value = "phone")
    @NotEmpty(message = "Phone may not be null or empty")
    private String phoneNumber;

    @JsonProperty(value = "template")
    @NotEmpty(message = "template may not be null or empty")
    private String template;

    @JsonProperty(value = "ttl")
    @InRange(min = 10,max = 20,message = "Invalid TTL range")
    private Integer codeTimeToLive;

}
