package com.jt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.CacheFind;
import com.jt.config.RedisConfig;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Service
public class ItemCatServiceImpl implements ItemCatService{

	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	//类似于懒加载.什么时候用,什么时候调
	@Autowired(required = false)
	private ShardedJedis jedis;
	
	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		ItemCat selectById = itemCatMapper.selectById(itemCatId);
		return selectById;
	}

	@Override
	@CacheFind
	public List<EasyUITree> findItemCat(Long parentId) {
		
//		Jedis jedis = redisConfig.jedis();
		
//		String pId = String.valueOf(parentId);
//		Set<String> keys = jedis.keys(pId);
//		String json = ObjectMapperUtil.toJSON(jedis.keys(pId));
		
//		if(jedis.get(pId)==null) {
			List<ItemCat> selectList = selectLists(parentId);
			
			ArrayList<EasyUITree> arrayList = new ArrayList<>(selectList.size());
			
			for (ItemCat itemCat : selectList) {
				
				Long id = itemCat.getId();
				String text = itemCat.getName();
				//规定:如果是父级节点closed,否则open
				String state = itemCat.getIsParent()?"closed":"open";
				EasyUITree easyUITree = new EasyUITree(id, text, state);
				arrayList.add(easyUITree);
			}
			
//			String json2 = ObjectMapperUtil.toJSON(arrayList);
//			jedis.set(pId, json2);
//			System.out.println("******************数据库");
			return arrayList;
			
//		}else {
//			
//			String json3 = jedis.get(pId);
//			System.out.println("******************缓存");
//			return ObjectMapperUtil.toObject(json3, ArrayList.class);
//		}
		
		
	}

	private List<ItemCat> selectLists(Long parentId) {
		
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id", parentId);
		
		List<ItemCat> selectList = itemCatMapper.selectList(queryWrapper);
			
		
		return selectList;
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUITree> findItemCatCache(Long parentId) {
		Long start = System.currentTimeMillis();
		String key = "com.jt.service.ItemCatServiceImpl.findItemCatCache::"+parentId;
		String value = jedis.get(key);
		List<EasyUITree> arrayList = new ArrayList<>();
		if(StringUtils.isEmpty(value)) {
			arrayList = findItemCat(parentId);
			if(arrayList.size()>0) {
				String json = ObjectMapperUtil.toJSON(arrayList);
				jedis.set(key, json);
			}
			Long end = System.currentTimeMillis();
			System.out.println("查询数据库耗时:" + (end-start) + "毫秒");
		}else {
			arrayList = ObjectMapperUtil.toObject(value, arrayList.getClass());
			Long end = System.currentTimeMillis();
			System.out.println("查询缓存耗时:" + (end-start) + "毫秒");
		}
		return arrayList;
	}

}
