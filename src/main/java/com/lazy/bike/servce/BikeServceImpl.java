package com.lazy.bike.servce;

import com.lazy.bike.mapper.BikeMapper;
import com.lazy.bike.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
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
    private BikeMapper bikeMapper;

    @Override
    public Bike getById(Long id) {
        return bikeMapper.getById(id);
    }

    @Override
    public List<Bike> findAll() {
        //调用mongo的模板查找数据，然后将数据
        return mongoTemplate.findAll(Bike.class);
    }

    @Override
    public void save(Bike Bike) {
        mongoTemplate.save(Bike);
    }

    @Override
    public void deleteByIds(Long[] ids) {
        bikeMapper.deleteByIds(ids);
    }

    @Override
    public void update(Bike Bike) {
        bikeMapper.update(Bike);
    }

    @Override
    public GeoResults<Bike> findNear(double longitude, double latitude) {
        //查找附件500米的未使用的单车，要求只显示最近的10辆
        NearQuery nearQuery = NearQuery.near(longitude, latitude, Metrics.KILOMETERS);
        nearQuery.maxDistance(0.5).query(new Query().addCriteria(Criteria.where("status").is(0)).limit(10));

        //GeoReslut不但封装了要查找的单车数据，还封装了距离
        GeoResults<Bike> bikes = mongoTemplate.geoNear(nearQuery, Bike.class);
        return bikes;
    }
//    @Override
//    public List<Bike> findAll() {
//        //调用mongo的模板查找数据，然后将数据
//        return mongoTemplate.findAll(Bike.class);
//    }
//
//    @Override
//    public void save(Bike Bike) {
//        mongoTemplate.save(Bike);
//    }
//
//    @Override
//    public GeoResults<Bike> findNear(double longitude, double latitude) {
//        //查找附件500米的未使用的单车，要求只显示最近的10辆
//        NearQuery nearQuery = NearQuery.near(longitude, latitude, Metrics.KILOMETERS);
//        nearQuery.maxDistance(0.2).query(new Query().addCriteria(Criteria.where("status").is(0)).limit(10));
//
//        GeoResults<Bike> bikes = mongoTemplate.geoNear(nearQuery, Bike.class);
//        return bikes;
//    }
}
