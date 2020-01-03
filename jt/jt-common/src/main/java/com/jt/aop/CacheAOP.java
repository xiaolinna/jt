package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.CacheFind;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

//定义缓存切面
@Component	//万能注解 交给容器管理
@Aspect	//自定义切面
public class CacheAOP {

	@Autowired(required = false)
	private JedisCluster jedis;
//	private Jedis jedis; 
//	private ShardedJedis jedis;
	/**
	 * 	环绕通知的语法
	 * 	返回值类型: 任意类型用
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		
		
		String key = getkey(joinPoint,cacheFind);
		String value = jedis.get(key);
		Object result  = null;
		if(StringUtils.isEmpty(value)) {
			try {
			result = joinPoint.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			String json = ObjectMapperUtil.toJSON(result);
			
			if(cacheFind.seconds()>0) {
				jedis.setex(key, cacheFind.seconds(), json);
			}else {
				jedis.set(key, json);
			}
			
			System.out.println("AOP实现数据库查询!!!!!");
			 return result;
		}else {
			
			Class ReturnType = getReturnType(joinPoint);
			result = ObjectMapperUtil.toObject(value, ReturnType);
			System.out.println("AOP实现缓存查询!!!!!");
			return result;
		}
	}

	private Class getReturnType(ProceedingJoinPoint joinPoint) {
		MethodSignature sinature = (MethodSignature) joinPoint.getSignature();
		
		return sinature.getReturnType();
	}

	private String getkey(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
		
		String key = cacheFind.key();
		if(StringUtils.isEmpty(key)) {
			String className = joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object firstArgs = joinPoint.getArgs()[0];
			return className+"."+methodName+"::"+firstArgs;
		}else {
			return key;
		}
		
	}
	
}
