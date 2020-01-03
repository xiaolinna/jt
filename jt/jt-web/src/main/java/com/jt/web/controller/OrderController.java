package com.jt.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Reference
	private DubboCartService cartService;
	
	@Reference
	private DubboOrderService dubboOrderService;
	
	@RequestMapping("/create")
	public String create(Model model) {
		
		//根据用户Id信息.获取全部购物车记录
		Long userId = UserThreadLocal.get().getId();
		List<Cart> userList = cartService.findCartListByUserId(userId);
		
		model.addAttribute("carts", userList);
		
		return "order-cart"; 
	}
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult Submit(Order order) {
		
		//获取userId
		Long userId = UserThreadLocal.get().getId();
		order.setUserId(userId);
		
		//需要返回订单号orderId.
		String orderId = dubboOrderService.saveOrder(order);
		return SysResult.success(orderId);
	}
	
	@RequestMapping("/success")
	public String findOrderById(String id, Model model) {
		
		Order order = dubboOrderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
}
