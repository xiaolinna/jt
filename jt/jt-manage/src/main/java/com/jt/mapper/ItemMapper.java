package com.jt.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;

public interface ItemMapper extends BaseMapper<Item>{

	List<Item> findItemByPage(Integer start, Integer rows);
	
	
	/**
	 * 关于Mybatis数据封装问题
	 * 	1.低版本/默认条件下.mybatis不允多值传参
	 * 	
	 * 	实现思路: 将多值封装为单值
	 * 
	 * 	1.POJO对象
	 * 	2.Map集合@Param  万能的
	 * 	3.封装为数组  array
	 * 	4.封装为List
	 * 
	 * @param ids
	 */
	//封装了一个Map集合     ids=101,102,103
	void deleteByIds(@Param("ids")Long ... ids);

	void updateStatus(
			@Param("ids")Long[] ids,
			@Param("status")Integer status);
	
}
