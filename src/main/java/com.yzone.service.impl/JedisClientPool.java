package com.yzone.service.impl;

import java.util.List;
import java.util.Set;

import com.yzone.service.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;


public class JedisClientPool implements JedisClient {

	//如何交给spring管理
	private JedisPool jedisPool  = new JedisPool() ;

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public String set(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.set(key, value);
		jedis.close();
		return result;
	}

	@Override
	public String get(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}

	@Override
	public Boolean exists(String key) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.exists(key);
		jedis.close();
		return result;
	}

	@Override
	public Long expire(String key, int seconds) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key, seconds);
		jedis.close();
		return result;
	}

	@Override
	public Long ttl(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}

	@Override
	public Long incr(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}

	@Override
	public Long hset(String key, String field, String value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(key, field, value);
		jedis.close();
		return result;
	}

	@Override
	public String hget(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.hget(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Long hdel(String key, String... field) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(key, field);
		jedis.close();
		return result;
	}

	@Override
	public Boolean hexists(String key, String field) {
		Jedis jedis = jedisPool.getResource();
		Boolean result = jedis.hexists(key, field);
		jedis.close();
		return result;
	}

	@Override
	public List<String> hvals(String key) {
		Jedis jedis = jedisPool.getResource();
		List<String> result = jedis.hvals(key);
		jedis.close();
		return result;
	}

	@Override
	public Long del(String key) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();
		return result;
	}

	@Override
	public Long lpush(String key, String...value) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.lpush(key, value);
		jedis.close();
		return result;
	}

	@Override
	public String lpop(String key) {
		Jedis jedis = jedisPool.getResource();
		String result = jedis.lpop(key);
		jedis.close();
		return result;
	}

	@Override
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = jedisPool.getResource();
		List<String> result = jedis.lrange(key, start, end);
		jedis.close();
		return result;
	}

	@Override
	public Long zadd(String key, double score, String memeber) {
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.zadd(key,score,memeber);
		jedis.close();
		return result;
	}

	@Override
	public Set zrange(String key, Long start, Long end) {
		Jedis jedis = jedisPool.getResource();
		Set result= jedis.zrange(key,start,end);
		jedis.close();
		return result;
	}

	@Override
	public double zincrby(String key, double score, String member) {
		Jedis jedis = jedisPool.getResource();
		double result= jedis.zincrby(key,score,member);
		jedis.close();
		return result;
	}
}
