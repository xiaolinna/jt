package com.jt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JedisCluster jedis;

	@Override
	public List<User> findAll() {
		
		return userMapper.selectList(null);
	}
	
	/**
	 * 参数说明:
	 * 	1.param  需要校验的数据
	 *  2.type   1 username、2 phone、3 email
	 * 
	 * 业务说明:根据参数查询数据库
	 * 	sql: select * from tb_user  字段 = #{param}
	 * 
	 * 结果返回: 如果存在返回true  不存在返回 false
	 */
	@Override
	public boolean findUserByType(String param, Integer type) {
		
		//1.类型转化为字段
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "username");
		map.put(2, "phone");
		map.put(3, "email");
		String column = map.get(type);
		
		//2.校验用户是否存在
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		User user = userMapper.selectOne(queryWrapper);
		
		return user == null?false:true;
	}

	@Override
	public SysResult findUserName(String ticket, String username, HttpServletRequest request, HttpServletResponse response) {
		
		//验证username对应的ticket是否是用户传来的ticket
		//从redis中获取ticket
		String redisTicket = jedis.get("JT_USER_" + username);
		
		if (StringUtils.isEmpty(redisTicket)) {
			
			CookieUtil.deleteCookie("JT_TICKET", response, "/", "jt.com");
			CookieUtil.deleteCookie("JT_USER_NAME", response, "/", "jt.com");
			return SysResult.fail();
		}
		
		//校验redisTicket是否和ticket相同
		if (!redisTicket.equals(ticket)) {
			
			CookieUtil.deleteCookie("JT_TICKET", response, "/", "jt.com");
			CookieUtil.deleteCookie("JT_USER_NAME", response, "/", "jt.com");
			return SysResult.fail();
		}
		
		//校验用户ip
		//获取当前登录用户的ip
		String ipAddr = IPUtil.getIpAddr(request);
		//根据ticket获取redis中的数据
		Map<String, String> map = jedis.hgetAll(ticket);
		String userIP = map.get("JT_USER_IP");
		//校验ip是否正确
		if (!ipAddr.equals(userIP)) {
			
			CookieUtil.deleteCookie("JT_TICKET", response, "/", "jt.com");
			CookieUtil.deleteCookie("JT_USER_NAME", response, "/", "jt.com");
			return SysResult.fail();
		} 
		//校验数据是否存在
		String json = map.get("JT_USER");
		if (StringUtils.isEmpty(json)) {
			CookieUtil.deleteCookie("JT_TICKET", response, "/", "jt.com");
			CookieUtil.deleteCookie("JT_USER_NAME", response, "/", "jt.com");
			return SysResult.fail();
		}
		
		return SysResult.success(json);
		
	}

	
}
