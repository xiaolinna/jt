package com.jt.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ItemDescMapper itemDescMapper;
	

	
	/**
	 * 分页查询数据  20条
	 * 	select * from tb_item limit 起始位置,记录数
	 * 第一页:
	 * 	 select * from tb_item limit 0,20	0,19
	 * 第二页:
	 * 	 select * from tb_item limit 20,20  20,39
	 * 第三页:
	 * 	 select * from tb_item limit 40,20  40,59
	 * 第N页:
	 * 	 select * from tb_item limit (page-1)rows,rows
	 */
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		
		//开始毫秒数
		Long startTime = System.currentTimeMillis();


		
//		PageHelper.startPage(page, rows, true).setOrderBy("updated","desc");
//		List<Item> selectList = itemMapper.selectList(null);
//		PageInfo<Item> pageInfo = new PageInfo<>(selectList);
//		long total = pageInfo.getTotal();
//		List<Item> items = pageInfo.getList();
		
		//sql查询
		//查询记录总数 
		int total = itemMapper.selectCount(null);
		//分页查询记录
		Integer start = (page-1) * rows;
		
		
		//MP查询
		//分页查询条件
//		Page<Item> Page = new Page<>(page, rows);
//		//实体对象封装操作类
//		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
//		queryWrapper.orderByDesc("updated");
//		//查询
//		IPage<Item> selectPage = itemMapper.selectPage(Page, queryWrapper);
//		//获取查询总数的两种方法
//		int total = new Long(selectPage.getTotal()).intValue();
//		Integer t = Integer.parseInt(selectPage.getTotal()+""); 
//		//获取PM查询结果
//		List<Item> items = selectPage.getRecords();
		
		//调用dao层
		List<Item> items = itemMapper.findItemByPage(start,rows);
		
		Long endTime = System.currentTimeMillis();
		System.out.println("执行时间:"+(endTime-startTime));
		//返回结果
		
		return new EasyUITable(total,items);
	}
	
	
	/**
	 * 关于主键自增赋值问题
	 * 	说明:一般条件下,当数据入库之后,才会动态的生成主键
	 * 信息.
	 * 	如果在关联操作时,需要用到主键信息时,一般id为null.
	 * 需要动态查询!!!!
	 * 
	 * 思路1: 先入库   在查询 获取主键Id 98%正确
	 * 思路2: 先入库   动态回传主键信息 mysql配合MP实现
	 * 一般解决思路:需要开启mysql数据的主键回传配置.
	 */
	//需要添加事务控制
	@Transactional
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1)
			.setCreated(new Date())
			.setUpdated(item.getCreated());
		itemMapper.insert(item);	//动态赋值Id
		
		//实现数据封装
		itemDesc.setItemId(item.getId());
		itemDesc.setCreated(item.getCreated());
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
		
		
	}
	
	

	@Transactional
	@Override
	public void updataItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
		
		itemDesc.setItemId(item.getId())
				.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
		
	}
	
	/**
	 * sql:delete from tb_item where id in(100,101,102)
	 */
	@Transactional
	@Override
	public void deleteItems(Long[] ids) {
//		itemMapper.deleteByIds(ids);
		List<Long> idList = Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		
		itemDescMapper.deleteBatchIds(idList);
	}


	
	/**
	 * 方式1:
	 * 	Sql: update tb_item status = #{status},updated=#{date}
	 * 		 where id in (101,102)
	 * 	
	 * 方式2:MP
	 * 	entity:修改的数据
	 * 	updateWrapper:修改的条件
	 */
	//方式2:MP
//	@Transactional
//	@Override
//	public void updateStatus(Long[] ids, int status) {
//		Item item = new Item();
//		item.setStatus(status)
//			.setUpdated(new Date());
//		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
//		List<Long> coll = Arrays.asList(ids);
//		updateWrapper.in("id", coll);
//		itemMapper.update(item, updateWrapper);
//	}
	//方式1
	@Transactional
	@Override
	public void updateStatus(Long[] ids, Integer status) {
		
		itemMapper.updateStatus(ids,status);
	}

	@Override
	public ItemDesc findItemDescById(Long itemDescId) {
		ItemDesc selectById = itemDescMapper.selectById(itemDescId);
		return selectById;
	}


	@Override
	public Item findItemById(Long itemId) {
		
		return itemMapper.selectById(itemId);
	}




	
	
	
	
}
