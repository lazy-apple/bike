package com.lazy.bike.servce;

import com.lazy.bike.mapper.BikeMapper;
import com.lazy.bike.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author LaZY(李志一)
 * @create 2019-03-24 21:36
 */

//事务管理
@Transactional
@Service
public class BikeServceImpl implements BikeServce{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BikeMapper bikeMapper;//此处报错不需要管

    @Override
    public void save(Bike bike) {
        bikeMapper.save(bike);
        bikeMapper.save(bike);
    }

    @Override
    public void save(String bike) {
        mongoTemplate.save(bike, "bikes");
    }

    @Override
    public List<Bike> findAll() {
        return mongoTemplate.findAll(Bike.class,"bikes");
    }
}
