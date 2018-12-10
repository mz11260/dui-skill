package com.zm.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtil {
	
    private RedisTemplate<String, String> redisTemplate;
	
	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 批量删除对应的value
	 *
	 * @param keys keys
	 */
	public void remove(final List<String> keys) {
		redisTemplate.delete(keys);
	}

	/**
	 * 批量删除对应的value
	 *
	 * @param keys keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 *
	 * @param pattern pattern
	 */
	public void removePattern(final String pattern) {
		Set<String> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 *
	 * @param key key
	 * @return boolean
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 *
	 * @param key key
	 * @return string
	 */
	public String getString(final String key) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		return operations.get(key);
	}

	/**
	 * 读取缓存
	 * @param key  key
	 * @return T
	 */
	public <T> T getObject(final String key, Class<T> clazz) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		String result = operations.get(key);
		if (result != null) {
			return JSON.parseObject(result, clazz);
		}
		return null;
	}

	/**
	 * 读取缓存
	 * @param key  key
	 * @return T
	 */
	public <T> T getObject(final String key, TypeReference<T> type) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		String result = operations.get(key);
		if (result != null) {
			return JSON.parseObject(result, type);
		}
		return null;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key key
	 * @param value value
	 * @return boolean
	 */
	public boolean setObject(final String key, Object value) {
		if (value == null) {
			return false;
		}
		String json = JSON.toJSONString(value);
		return setString(key, json);
	}

	/**
	 * 写入缓存
	 * 
	 * @param key key
	 * @param value value
	 * @param expireTime expireTime
	 * @return boolean
	 */
	public boolean setObject(final String key, Object value, Long expireTime) {
		if (value == null) {
			return false;
		}
		String json = JSON.toJSONString(value);
		return setString(key, json, expireTime);
	}

	/**
	 * 写入缓存
	 *
	 * @param key key
	 * @param value value
	 * @return boolean
	 */
	public boolean setString(final String key, String value) {
		return setString(key, value, 0L);
	}

	/**
	 * 写入缓存
	 * 
	 * @param key key
	 * @param value value
	 * @param expireTime expireTime
	 * @return boolean
	 */
	public boolean setString(final String key, String value, Long expireTime) {
		try {
			ValueOperations<String, String> operations = redisTemplate.opsForValue();
			operations.set(key, value);
			if (expireTime > 0) {
				redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			log.error("set redis key error", e);
		}
		return false;
	}
	
	/**
	 * 发送ping请求
	 * @return string 服务器收到后返回pong
	 */
	public String ping() {
		return redisTemplate.execute(RedisConnectionCommands::ping);
	}

	/**
	 * 获取当前redis connection db的大小
	 * 
	 * @return long
	 */
	public long dbSize() {
		return redisTemplate.execute(RedisServerCommands::dbSize);
	}

	/**
	 * 序列化存储
	 *
	 * @param key      key
	 * @param value    value
	 * @param liveTime :0 永久有效
	 */
	public void set(final byte[] key, final byte[] value, final long liveTime) {
		this.redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	/**
	 * 清空当前redis db数据
	 *
	 * @return string
	 */
	public String flushDB() {
		return redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}
}
