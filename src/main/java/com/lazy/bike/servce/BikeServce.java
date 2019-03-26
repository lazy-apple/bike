package com.lazy.bike.servce;

import com.lazy.bike.pojo.Bike;

import java.util.List;

/**
 * @author LaZY(李志一)
 * @create 2019-03-24 21:35
 */
public interface BikeServce {
    public void save(Bike bike);

    public void save(String bike);

    List<Bike> findAll();
}

