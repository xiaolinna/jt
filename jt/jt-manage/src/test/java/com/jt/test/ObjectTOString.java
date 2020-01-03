package com.jt.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

@SpringBootTest
public class ObjectTOString {

	//private final static ObjectMapper objectMapper = new ObjectMapper();
	
	private ObjectMapperUtil MapperUtil;
	
	@SuppressWarnings("static-access")
	@Test
	public void testObj() throws JsonProcessingException {
		ItemDesc desc = new ItemDesc();
		desc.setItemId(1000L)
			.setItemDesc("啦啦啦啦啦啦啦啦啦啦")
			.setCreated(new Date())
			.setUpdated(desc.getCreated());
//		String json = objectMapper.writeValueAsString(desc);
//		System.out.println(json);
//		ItemDesc readValue = objectMapper.readValue(json, ItemDesc.class);
//		System.out.println(readValue);
		
		String json = MapperUtil.toJSON(desc);
		System.out.println(json);
		ItemDesc obj = MapperUtil.toObject(json, ItemDesc.class);
		System.out.println(obj);
	}
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	@Test
	public void testObject() throws Throwable {
		ItemDesc desc1 = new ItemDesc();
		desc1.setItemId(1000L).setItemDesc("啦啦啦啦啦啦啦啦啦啦").setCreated(new Date()).setUpdated(desc1.getCreated());
		ItemDesc desc2= new ItemDesc();
		desc2.setItemId(1000L).setItemDesc("啦啦啦啦啦啦啦啦啦啦").setCreated(new Date()).setUpdated(desc2.getCreated());
		List<ItemDesc> list = new ArrayList<>();
		list.add(desc1);
		list.add(desc2);
		
//		String json = objectMapper.writeValueAsString(list);
//		System.out.println(json);
//		
//		List readValue = objectMapper.readValue(json, list.getClass());
//		System.out.println(readValue);
		
		String json = MapperUtil.toJSON(list);
		System.out.println(json);
		
		List obj = MapperUtil.toObject(json, list.getClass());
		System.out.println(obj);
		
	}
	
	
}
