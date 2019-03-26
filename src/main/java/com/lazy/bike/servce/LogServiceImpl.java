package com.lazy.bike.servce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * @author LaZY(李志一)
 * @create 2019-03-26 17:12
 */
@Service
public class LogServiceImpl implements LogService{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void save(String log) {
        mongoTemplate.save(log, "logs");
    }
}
