package com.incuube.twofa.demo;

import com.incuube.twofa.demo.entity.RedisMessageEntity;
import com.incuube.twofa.demo.entity.SendMsgResponse;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

@SpringBootApplication
public class DemoApplication {

    @Value("${redis.host}")
    String hostName;
    @Value("${redis.port}")
    Integer port;
    @Value("${redis.password}")
    String password;

    @Bean
    RedisStandaloneConfiguration jedisClientConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(hostName);
        configuration.setPort(port);
        configuration.setPassword(password);
        return configuration;
    }

    @Bean
    RedisConnectionFactory jedisConnectionFactory(){
        return new JedisConnectionFactory(jedisClientConfiguration());
    }

    @Bean
    RedisTemplate<String, RedisMessageEntity> redisTemplate()
    {
        RedisTemplate<String,RedisMessageEntity> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;

    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
