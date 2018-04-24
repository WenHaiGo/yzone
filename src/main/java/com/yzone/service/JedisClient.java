package com.yzone.service;

import java.util.List;
import java.util.Set;

public interface JedisClient {

	String set(String key, String value);
	String get(String key);
	Boolean exists(String key);
	Long expire(String key, int seconds);
	Long ttl(String key);
	Long incr(String key);
	Long hset(String key, String field, String value);
	String hget(String key, String field);
	Long hdel(String key, String... field);
	Boolean hexists(String key, String field);
	List<String> hvals(String key);
	Long del(String key);
	Long lpush(String key,String...value);
	String lpop(String key);
	List<String> lrange(String key,long start,long end);
	Long zadd(String key,double score,String memeber);
	Set zrange(String key, Long start, Long end);
	double zincrby(String key,double score,String member);

}
