package com.jt.service;

import java.util.List;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

public interface ItemService {

	EasyUITable findItemByPage(Integer page, Integer rows);

	void saveItem(Item item, ItemDesc itemDesc);

	void updataItem(Item item, ItemDesc itemDesc);

	void deleteItems(Long[] ids);

	void updateStatus(Long[] ids, Integer status);

	ItemDesc findItemDescById(Long itemDescId);

	Item findItemById(Long itemId);



	
}
