package com.lazy.bike.mapper;

import com.lazy.bike.pojo.Bike;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author LaZY(李志一)
 * @create 2019-03-24 21:46
 */
@Mapper
public interface BikeMapper {
    public Bike getById(Long id);

    public List<Bike> findAll();

    public void save(Bike Bike);

    public void deleteByIds(Long[] ids);

    public void update(Bike Bike);

    public Bike login(Bike Bike);
//    public void save(Bike bike);
}
