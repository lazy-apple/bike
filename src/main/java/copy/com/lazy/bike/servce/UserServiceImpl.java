package copy.com.lazy.bike.servce;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.sms.SmsSingleSender;
import copy.com.lazy.bike.mapper.UserMapper;
import copy.com.lazy.bike.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Transactional
@Service
public class UserServiceImpl implements UserService {

	//http://www.cnblogs.com/waterystone/p/6214212.html
	@Autowired
	private UserMapper userMapper;

	//操作redis中的v是对象类型的数据
	@Autowired
	private RedisTemplate redisTemplate;

	//操作redis中的字符串类型数据
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public User getById(Long id) {
		return userMapper.getById(id);
	}

	@Override
	public List<User> findAll() {
		return userMapper.findAll();
	}

	@Override
	public void save(User user) {
		userMapper.save(user);
	}

	@Override
	public void deleteByIds(Long[] ids) {
		userMapper.deleteByIds(ids);
	}

	@Override
	public void update(User user) {
		userMapper.update(user);
	}

	@Override
	public User login(User user) {
		return userMapper.login(user);
	}


	@Override
	public User getUserByOpenid(String openid) {
		return mongoTemplate.findById(openid, User.class);
	}

	@Override
	public void register(User user) {
		mongoTemplate.save(user);
		//mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(user.getPhoneNum())), new Update().set("name", user.getName()).set("idNum", user.getIdNum()),"user");
	}

	/***
	 * 校验用户输入的验证码
	 * @param user
	 * @return
	 */
	@Override
	public boolean verify(User user) {
//		boolean flag = false;
		boolean flag = true;//测试代码（验证码一定生效）
		String phoneNum = user.getPhoneNum();
		String verifyCode = user.getVerifyCode();
		String code = stringRedisTemplate.opsForValue().get(phoneNum);
		if(verifyCode != null && verifyCode.equals(code)) {
			mongoTemplate.save(user);
			flag = true;
		}
		return flag;
	}

	/***
	 * 发送验证码
	 * @param nationCode
	 * @param phoneNum
	 * @throws Exception
	 */
	@Override
	public void genVerifyCode(String nationCode, String phoneNum) throws Exception {
		String appkey = stringRedisTemplate.opsForValue().get("883e8930380d3c6cd3e039e17067ca16");
		//redisTemplate.
		//调用腾讯云的短信接口（短信的appid， 短信的appkey）
		SmsSingleSender singleSender = new SmsSingleSender(1400198174, appkey);
		//普通单发
		//String code = "8888";
		String code =  (int)((Math.random() * 9 + 1) * 1000) + "";
		//调用发送短信功能
		singleSender.send(0, nationCode, phoneNum, "您的登录验证码为" + code, "", "");
		//将数据保存到redis中，redis的key手机号，value是验证码，有效时长120秒
		stringRedisTemplate.opsForValue().set(phoneNum, code, 120, TimeUnit.SECONDS);
	}

	@Override
	public void deposit(User user) {
		mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(user.getPhoneNum())), new Update().set("status", user.getStatus()).set("deposit", 299),  User.class);
	}

	@Override
	public void identify(User user) {
		mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(user.getPhoneNum())), new Update().set("status", user.getStatus()).set("name", user.getName()).set("idNum", user.getIdNum()),  User.class);
	}

	/***
	 * 更新用户余额
	 * @param params
	 * @return
	 */
	@Override
	public boolean recharge(String params) {
		boolean flag = true;
		User user = JSONObject.parseObject(params, User.class);
		String phoneNum = user.getPhoneNum();
		double balance = user.getBalance();
		//跟新用户的余额
		try {
			mongoTemplate.updateFirst(new Query(Criteria.where("phoneNum").is(phoneNum)), new Update().inc("balance", balance),  User.class);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
}
