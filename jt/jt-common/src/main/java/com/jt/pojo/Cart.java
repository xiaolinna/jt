package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_cart")
public class Cart extends BasePojo{

	@TableId(type = IdType.AUTO)
	private Long id;
	private Long userId;	//用户id号
	private Long itemId;	//商品id号
	private String itemTitle;
	private String itemImage;
	private Long itemPrice;
	private Integer num;
	
}
