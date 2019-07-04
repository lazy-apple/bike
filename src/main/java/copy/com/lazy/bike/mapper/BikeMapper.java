package copy.com.lazy.bike.mapper;

import copy.com.lazy.bike.pojo.Bike;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author LaZY(李志一)
 * @create 2019-03-24 21:46
 */
@Mapper
public interface BikeMapper {
    public void save(Bike bike);
}
