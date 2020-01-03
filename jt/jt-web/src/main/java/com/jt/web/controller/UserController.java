package com.jt.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private JedisCluster jedis;
	
	@Reference(check = false)
	private DubboUserService dubboUserService;
	
	/**
	 * 实现通用的页面跳转.动态获取页面名称
	 * @param moduleName
	 * @return
	 */
	@RequestMapping("/{moduleName}")
	public String toModule(@PathVariable String moduleName) {
		
		return moduleName;
		
	}
	
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult insertUser(User user) {
		
		dubboUserService.insertUser(user);
		
		return SysResult.success();
	}
	
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult doLongin(User user, HttpServletResponse response, HttpServletRequest request) {
		
		String userIP = IPUtil.getIpAddr(request);
		
		String ticket = dubboUserService.findUserByUP(user, userIP);
		
		if (StringUtils.isEmpty(ticket)) {
			return SysResult.fail();
		}
		
		Cookie cookie = new Cookie("JT_TICKET", ticket);
		cookie.setMaxAge(7*24*60*60);
		cookie.setPath("/");
		cookie.setDomain("jt.com");
		response.addCookie(cookie);
		
		Cookie cookieUserName = new Cookie("JT_USER_NAME", user.getUsername());
		cookieUserName.setMaxAge(7*24*60*60);
		cookieUserName.setPath("/");
		cookieUserName.setDomain("jt.com");
		response.addCookie(cookieUserName);
		
		return SysResult.success();
	}

	@RequestMapping("/logout")
	public String Logout(HttpServletRequest request, HttpServletResponse response) {
		
		//1.获取Cookie数据  JT_TICKET  JT_USER
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null && cookies.length > 0) {
		
			String username = null;
			String ticket = null;
			for (Cookie cookie : cookies) {
			
				if (cookie.getName().equals("JT_USER_NAME")) {
					username = cookie.getValue();
				}
			
				if (cookie.getName().equals("JT_TICKET")) {
					ticket = cookie.getValue();
				}
			
			}
				
			//2.判断Cookie是否为null
			if (!StringUtils.isEmpty(username)) {
				//删除redis
				jedis.del("JT_USER_"+username);
				//删除Cookie必须全部满足条件才行
				CookieUtil.deleteCookie("JT_USER_NAME", response, "/", "jt.com");
			}
		
			if (!StringUtils.isEmpty(ticket)) {
				//删除redis
				jedis.del(ticket);
				//删除Cookie必须全部满足条件才行
				CookieUtil.deleteCookie("JT_TICKET", response, "/", "jt.com");
			}
		
		}
		
		return "redirect:/";
		
	}
	
}
