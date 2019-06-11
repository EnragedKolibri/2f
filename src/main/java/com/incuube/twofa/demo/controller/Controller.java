package com.incuube.twofa.demo.controller;

import com.incuube.twofa.demo.entity.OptValidationEntity;
import com.incuube.twofa.demo.entity.RedisMessageEntity;
import com.incuube.twofa.demo.entity.SendMsgRequest;
import com.incuube.twofa.demo.entity.SendMsgResponse;
import com.incuube.twofa.demo.otpgenerator.OtpGenerator;
import com.incuube.twofa.demo.repository.SendMsgRedisRepository;
import com.incuube.twofa.demo.service.ISender;
import com.incuube.twofa.demo.utils.CommonConstants;
import com.incuube.twofa.demo.utils.StringValidator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.UUID;

@Log4j
@RestController
@RequestMapping("/2fa/api")
public class Controller {

    private String[] apiKeys = {"0b9bbc-33f4c7-0dad30-5bc545-6172d28a","0b9bbc-33f4c7-0dad30-5bc545-6172d28b"};

    private final SendMsgRedisRepository redisRepository;
    private final ISender smsSender;

    @Autowired
    public Controller(SendMsgRedisRepository redisRepository, ISender smsSender) {
        this.redisRepository = redisRepository;
        this.smsSender = smsSender;
    }

    @PostMapping(value = "getOtp",produces = "application/json",consumes = "application/json")
    @ResponseBody
    ResponseEntity<Object> OtpRequest(@RequestHeader("ApiKey") String key, @Valid @RequestBody SendMsgRequest request) throws IOException {
        log.info("[Request getOtp]: "+request);
        if (Arrays.asList(apiKeys).contains(key)) //TODO throw autoraization exeption
        {
            if(!StringValidator.validateTemplate(request.getTemplate(), CommonConstants.templatePattern))
            {
                return new ResponseEntity<>(returnMessage("Invalid Template"),HttpStatus.BAD_REQUEST);
            }

            OtpGenerator otpGenerator = new OtpGenerator(request.getTemplate());
            String uuid = UUID.randomUUID().toString();
            String otpCode = otpGenerator.getOtp();
            String message = otpGenerator.getMessage();
            Instant instant = Instant.now();
            Long timeStampSeconds = instant.getEpochSecond();
            RedisMessageEntity entityToSave = new RedisMessageEntity(uuid,request.getPhoneNumber(),request.getTemplate(),otpCode,message,request.getCodeTimeToLive(),timeStampSeconds,false);
            log.info("[Write in database]: "+entityToSave);
            RedisMessageEntity savedEntity = redisRepository.save(entityToSave);
            smsSender.send(savedEntity.getPhoneNumber(), savedEntity.getMessage());
            return new ResponseEntity<>(new SendMsgResponse(savedEntity.getToken()), HttpStatus.OK);
        }
        return new ResponseEntity<>(returnMessage("Wrong ApiKey"),HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "validateOtp",produces = "application/json",consumes = "application/json")
    @ResponseBody
    ResponseEntity<Object> validateOtp(@RequestHeader("ApiKey") String key, @RequestBody OptValidationEntity request){
        log.info("[Request validateOtp]: "+request);
        if (Arrays.asList(apiKeys).contains(key))
        {
            //TODO logic for matching code by token
            RedisMessageEntity redisMessageEntity;
            try {
                redisMessageEntity = redisRepository.findById(request.getToken()).get();
                log.info("[Get from database]: "+redisMessageEntity);
                Instant instant = Instant.now();
                long timeStampSeconds = instant.getEpochSecond();
                if (!request.getOtpCode().equals(redisMessageEntity.getOtpCode()))
                {
                    System.out.println("Request code: "+request.getOtpCode());
                    System.out.println("Request code: "+redisMessageEntity.getOtpCode());
                    return new ResponseEntity<>(returnMessage("OTP pass doesn't match"),HttpStatus.NOT_FOUND);
                }

                if (timeStampSeconds>redisMessageEntity.getTimeStamp()+redisMessageEntity.getCodeTimeToLive())
                {
                    System.out.println("Validation timestamp: "+timeStampSeconds);
                    System.out.println("Redis timestamp: "+redisMessageEntity.getTimeStamp());
                    System.out.println("Redis timestamp + ttl: "+redisMessageEntity.getTimeStamp()+redisMessageEntity.getCodeTimeToLive());
                    return new ResponseEntity<>(returnMessage("OTP expiered"),HttpStatus.GONE);
                }

                //TODO logick for field used OTP or not
            }catch (NoSuchElementException e)
            {
                return new ResponseEntity<>(returnMessage("Token Not Found"),HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(redisMessageEntity,HttpStatus.OK);
        }
        return new ResponseEntity<>(returnMessage("Wrong ApiKey"),HttpStatus.UNAUTHORIZED);
    }


    private String returnMessage(String message)
    {
        return "{\"message\":\""+message+"\"}";
    }

}
