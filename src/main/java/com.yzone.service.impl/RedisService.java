/*
 * File name:          IndexService.java
 * Copyright@Handkoo (China)
 * Editor:           JDK1.6.32
 */
package com.yzone.service.impl;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;





@Service
public class RedisService  {
	
	@Resource(name="redisTemplate")
	private RedisTemplate redisTemplate;

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void CacheRemove(final String... keys) {
		for (String key : keys) {
			CacheRemove(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	public void CacheRemovePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
		
		
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void CacheRemove(final String key) {
		if (CacheExists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的key
	 * 
	 * @param key
	 * @return
	 */
	public boolean CacheExists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	public Object CacheGet(final String key) {
		
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean CacheSet(final String key, Object value) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean CacheZSet(final String key, Object value,double score) {
		boolean result = false;
		try {
			ZSetOperations operations = redisTemplate.opsForZSet() ;
			operations.add(key, value, score);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean CacheSet(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
			operations.set(key,value, expireTime, TimeUnit.SECONDS);
			
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
