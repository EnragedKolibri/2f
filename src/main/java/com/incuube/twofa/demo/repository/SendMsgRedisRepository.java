package com.incuube.twofa.demo.repository;

import com.incuube.twofa.demo.entity.RedisMessageEntity;
import com.incuube.twofa.demo.entity.SendMsgRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendMsgRedisRepository extends CrudRepository<RedisMessageEntity, String> {}
