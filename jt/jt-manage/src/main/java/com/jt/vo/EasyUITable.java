package com.jt.vo;

import java.util.List;

import com.jt.pojo.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//VO:后台数据与页面中的JS交互
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITable {
	
	public Integer total;		//记录总数
	
	public List<Item> rows;		//记录
	
}
