package com.jt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/findAll")
	public List<User> findAll(){
		return userService.findAll();
	}
	
	/**
	 * url地址:http://sso.jt.com/user/check/{param}/{type}
	 * @return
	 */
	@RequestMapping("check/{param}/{type}")
	public JSONPObject checkUser(
			@PathVariable String param,
			@PathVariable Integer type,
			String callback) {
		
		//定义boolean 判断用户是否存在
		
		boolean flag = userService.findUserByType(param,type);
		SysResult sysResult = SysResult.success(flag);
		return new JSONPObject(callback, sysResult);
	}
	
	
	
	@RequestMapping("/query/{ticket}/{username}")
	public JSONPObject doQuery(@PathVariable String ticket,
							   @PathVariable String username,
								String callback, 
								HttpServletRequest request, 
								HttpServletResponse response) {
		
		SysResult sysResult = userService.findUserName(ticket, username,request, response);
		return new JSONPObject(callback, sysResult);
		
	}
	

}
