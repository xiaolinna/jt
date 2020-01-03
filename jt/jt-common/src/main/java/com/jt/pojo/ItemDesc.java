package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_item_desc")
@Data
@Accessors(chain = true)
public class ItemDesc extends BasePojo{
	
	@TableId
	private Long itemId;		//id必须与商品Id号一致
	
	private String itemDesc;	//商品详情信息 html
	
}
