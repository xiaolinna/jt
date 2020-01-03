package com.jt.service.impl;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedis;
	
	@Override
	public void insertUser(User user) {
		
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass)
			.setCreated(new Date())
			.setUpdated(user.getUpdated());
		
		userMapper.insert(user);
	}

	@Override
	public String findUserByUP(User user, String userIP) {
		
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);	//密码加密
		
		//根据对象中不为null的属性充当where条件 关系符=号
		QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
		
		//根据条件查询数据库记录
		User userDB = userMapper.selectOne(queryWrapper);
		
		//判断userDB是否为null
		if (userDB == null) {
			//用户名和密码不正确
			return null;
		}
		
		/**
		 * 为了保证redis资源不浪费,则需要校验数据.
		 * 如果检查发现当前用户已经登陆过,则删除之前的数据.
		 */
		if(jedis.exists("JT_USER_"+user.getUsername())) {
			//之前已经登录过.删除之前的ticket
			String oldTicket = jedis.get("JT_USER_"+user.getUsername());
			jedis.del(oldTicket);
		}
		
		//程序执行到这里说明用户输入正确
		//3.1获取uuid
		String ticket = UUID.randomUUID().toString();
		//3.2准备userJSON数据 数据必须进行脱敏处理
		userDB.setPassword("(*´▽｀)ノノ");
		
		String userJSON = ObjectMapperUtil.toJSON(userDB);
		
		jedis.hset(ticket, "JT_USER", userJSON);
		jedis.hset(ticket, "JT_USER_IP", userIP);
		jedis.expire(ticket, 7*24*60*60);
		//将用户名和ticket信息绑定
		jedis.setex("JT_USER_"+user.getUsername(), 7*24*60*60, ticket);
		//用户名和ticket绑定即可!!!!!!
		return ticket;
	}



}
