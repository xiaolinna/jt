package com.jt.stream;

import java.io.FileInputStream;
import java.io.InputStream;

public class LuaStream {
	
	@SuppressWarnings("resource")
	public static String getLuaFileString() {
		String script = null;
		try {
			InputStream fileInputStream = new FileInputStream("src/main/resources/lua/redis-lock.lua");
			//获取需要读取的字符串长度
			byte[] bytes = new byte[fileInputStream.available()];
			fileInputStream.read(bytes);
			script = new String(bytes);
			fileInputStream.close(); //关闭流
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return script;
	}
}
