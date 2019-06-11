package com.incuube.twofa.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.incuube.twofa.demo.utils.InRange;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Code")
@AllArgsConstructor
@Data
public class RedisMessageEntity implements Serializable {

        @Id
        private String token;
        private String phoneNumber;
        private String template;
        private String otpCode;
        private String message;
        private Integer codeTimeToLive;
        private Long timeStamp;
        private Boolean used;
}
