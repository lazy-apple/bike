package com.lazy.bike.servce;

import com.lazy.bike.pojo.Bike;
import org.springframework.data.geo.GeoResults;

import java.util.List;

/**
 * @author LaZY(李志一)
 * @create 2019-03-24 21:35
 */
public interface BikeServce {
    Bike getById(Long id);

    List<Bike> findAll();

    void save(Bike bike);

    void deleteByIds(Long[] ids);

    void update(Bike Bike);

    GeoResults<Bike> findNear(double longitude, double latitude);

//    List<Bike> findAll();
////
////    void save(Bike bike);
////
////    GeoResults<Bike> findNear(double longitude, double latitude);
}

