# bike
共享单车项目
# 架构图
![Image](https://github.com/lazy-apple/bike/blob/master/images/1.png)

# 小程序首页

![Image](https://github.com/lazy-apple/bike/blob/master/images/111.png)
![Image](https://github.com/lazy-apple/bike/blob/master/images/222.png)

# 用户注册
![Image](https://github.com/lazy-apple/bike/blob/master/images/注册1.png)
![Image](https://github.com/lazy-apple/bike/blob/master/images/注册2.png)
![Image](https://github.com/lazy-apple/bike/blob/master/images/押金.png)

# 广告和充值
![Image](https://github.com/lazy-apple/bike/blob/master/images/add.png)
![Image](https://github.com/lazy-apple/bike/blob/master/images/充值.png)



# 详细流程：
1.	加载页面时埋点记录log，发送（用户appid，log，lat，时间，省市区，终端类型）
2.	存在两台nginx反向代理服务器：一台作为日志采集服务器，一台作为业务系统负载均衡服务器（三台tomcat处理业务）。
3.	首次打开页面会根据MongoDB的geohash算法获取用户五百米范围内的单车。
4.	用户状态分为：0未注册、1绑定手机号、2已交押金、3绑定身份证。
5.	首次点击扫码会跳转到注册页面。
6.	用户填写手机号,请求后台发送验证码,后台生成4位随机验证码保存到redis中（key:手机号，value:验证码），有效时长120秒，发送给用户。
7.	用户验证码匹配正确后，将用户数据保存到MongoDB中。
8.	用户交押金、绑定身份证后更新用户状态、更新用户信息。
9.	用户点击充值按钮，充值成功后，通过埋点给日志采集服务器发送日志（所在地区，经纬度，手机号，appid，金额）。
10.	用户点击保修，埋点记录log(所在地区，经纬度)
11.	用户点击广告，埋点记录log（所在地区，appid，手机号，活动编号，时间，经纬度）
12.	nginx日志采集服务器集成kafka，将日志发送到kafka的topic中。
13.	sparkStreaming从kafka中拉取数据实时计算进行数据清洗（json转cvs）保存到hive的分区表中
14.	使用linux的调度机制，定时添加分区。
15.	配置两个flume，flum使用kafka channel 和 hdfs sink 保存日志到hdfs。由于同一个消费者组，不会重复记录日志。
16.	使用sparkSQL集成hive、MongoDB进行数据分析。
17.	数据分析结果保存到MySQL集群。

