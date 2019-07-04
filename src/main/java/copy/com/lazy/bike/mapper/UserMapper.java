package copy.com.lazy.bike.mapper;

import copy.com.lazy.bike.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {

	public User getById(Long id);
	
	public List<User> findAll();

	public void save(User user);

	public void deleteByIds(Long[] ids);

	public void update(User user);

	public User login(User user);
}
