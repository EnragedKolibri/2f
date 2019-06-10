package com.incuube.twofa.demo.controller;

import com.incuube.twofa.demo.entity.OptValidationEntity;
import com.incuube.twofa.demo.entity.RedisMessageEntity;
import com.incuube.twofa.demo.entity.SendMsgRequest;
import com.incuube.twofa.demo.entity.SendMsgResponse;
import com.incuube.twofa.demo.otpgenerator.OtpGenerator;
import com.incuube.twofa.demo.repository.SendMsgRedisRepository;
import com.incuube.twofa.demo.service.ISmsSender;
import com.incuube.twofa.demo.utils.StringValidator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.UUID;

@Log4j
@RestController
@RequestMapping("/2fa/api")
public class Controller {

    private String[] apiKeys = {"0b9bbc-33f4c7-0dad30-5bc545-6172d28a","0b9bbc-33f4c7-0dad30-5bc545-6172d28b"};

    private final SendMsgRedisRepository redisRepository;
    private final ISmsSender smsSender;

    @Autowired
    public Controller(SendMsgRedisRepository redisRepository, ISmsSender smsSender) {
        this.redisRepository = redisRepository;
        this.smsSender = smsSender;
    }

    @PostMapping(value = "getOtp",produces = "application/json",consumes = "application/json")
    @ResponseBody
    ResponseEntity<Object> OtpRequest(@RequestHeader("ApiKey") String key, @Valid @RequestBody SendMsgRequest request)
    {
        log.info("[Request getOtp]: "+request);
        if (Arrays.asList(apiKeys).contains(key)) //TODO throw autoraization exeption
        {
            if(!StringValidator.validateTemplate(request.getTemplate()))
            {
                return new ResponseEntity<>(returnMessage("Invalid Template"),HttpStatus.BAD_REQUEST);
            }
            OtpGenerator otpGenerator = new OtpGenerator(StringValidator.getSeparatedMessage(request.getTemplate()));
            String uuid = UUID.randomUUID().toString();
            String otpCode = otpGenerator.generateOtp();
            String message = otpGenerator.generateMessage();
            RedisMessageEntity entityToSave = new RedisMessageEntity(uuid,request.getPhoneNumber(),request.getTemplate(),otpCode,message,request.getCodeTimeToLive());
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
            RedisMessageEntity redisMessageEntity = redisRepository.findById(request.getToken()).get();
            log.info("[Get from database]: "+redisMessageEntity);
            return new ResponseEntity<>(redisMessageEntity,HttpStatus.OK);
        }
        return new ResponseEntity<>(returnMessage("Wrong ApiKey"),HttpStatus.UNAUTHORIZED);
    }

    private String returnMessage(String message)
    {
        return "{\"message\":\""+message+"\"}";
    }

//    @ExceptionHandler(BadRequest.class)
//    public ResponseEntity handleBadReq(BadRequest e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//    }
//
//    private Optional<String> validateEditCard(Card card) {
//        if (card.getId() == null) {
//            return Optional.of("Invalid description");
//        }
//        return Optional.empty();
//    }

}
