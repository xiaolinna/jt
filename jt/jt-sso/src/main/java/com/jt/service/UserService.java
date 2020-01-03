package com.jt.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jt.pojo.User;
import com.jt.vo.SysResult;

public interface UserService {

	List<User> findAll();

	boolean findUserByType(String param, Integer type);

	SysResult findUserName(String ticket, String username, HttpServletRequest request, HttpServletResponse response);


}
