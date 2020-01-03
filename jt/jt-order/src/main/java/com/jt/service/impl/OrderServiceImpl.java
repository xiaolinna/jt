package com.jt.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import com.jt.service.DubboOrderService;
@Service
public class OrderServiceImpl implements DubboOrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	//返回orderId
	@Override
	@Transactional	//事务控制  CURD都应该添加事务
	public String saveOrder(Order order) {
		
		String  orderId = "" + order.getUserId() + System.currentTimeMillis();
		
		Date date = new Date();
		
		//1.订单入库
		order.setOrderId(orderId)
			 .setStatus(1)	//未付款
			 .setCreated(date)
			 .setUpdated(date);
		orderMapper.insert(order);
		
		//2.入库订单物流信息
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId)
					 .setCreated(date)
					 .setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		
		//3.订单商品入库
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId)
					 .setCreated(date)
					 .setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		
		System.out.println("订单商品入库成功!!!!");
		
		return orderId;
	}

	//3张表一起查询  单表查询快于多表.
	@Override
	public Order findOrderById(String id) {
		
		//1.查询order对象
		Order order = orderMapper.selectById(id);
		
		OrderShipping shipping = orderShippingMapper.selectById(id);
		
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<OrderItem>();
		queryWrapper.eq("order_id", id);
		List<OrderItem> items = orderItemMapper.selectList(queryWrapper);
		
		order.setOrderShipping(shipping).setOrderItems(items);
		
		return order;
	}
	
	
}
