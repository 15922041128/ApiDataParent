package org.pbccrc.api.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
	
	public static JedisPool jedisPool;

	static {
		// 读取相关的配置
		Properties prop = new Properties();   
        InputStream in = RedisClient.class.getResourceAsStream("redis.properties");
        int maxActive = 0;
		int maxIdle = 0;
		int maxWait = 0;
		String ip = Constants.BLANK;
		int port = 0;
		String auth = Constants.BLANK;
        try {
			prop.load(in);
			maxActive = Integer.parseInt(prop.getProperty("redis.pool.maxActive"));
			maxIdle = Integer.parseInt(prop.getProperty("redis.pool.maxIdle"));
			maxWait = Integer.parseInt(prop.getProperty("redis.pool.maxWait"));
			ip = prop.getProperty("redis.ip");
			port = Integer.parseInt(prop.getProperty("redis.port"));
			auth = prop.getProperty("redis.auth");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxTotal(maxActive);
		// 设置最大空闲数
		config.setMaxIdle(maxIdle);
		// 设置超时时间
		config.setMaxWaitMillis(maxWait);

		// 初始化连接池
		jedisPool = new JedisPool(config, ip, port, 0, auth);
	}

	/**
	 * 向缓存中设置字符串内容
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return
	 * @throws Exception
	 */
	public static boolean set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 查看key是否已经在缓存中
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @return
	 * @throws Exception
	 */
	public static boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 设置缓存字符串过期时间
	 * 
	 * @param key
	 *            key
	 * @param times
	 *            times
	 * @return
	 * @throws Exception
	 */
	public static boolean setExpireTime(String key, int times) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.expire(key, times);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 向缓存中设置对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(String key, Object value) {
		Jedis jedis = null;
		try {
			String objectJson = JSON.toJSONString(value);
			jedis = jedisPool.getResource();
			jedis.set(key, objectJson);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 向缓存中设置对象
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setObject(String key, Object value) {
		Jedis jedis = null;
		try {
			
			Map<String, String> map = BeanUtils.describe(value);
			map.remove("class");
			
			String objectJson = JSON.toJSONString(map);
			jedis = jedisPool.getResource();
			jedis.set(key, objectJson);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 删除缓存中得对象，根据key
	 * 
	 * @param key
	 * @return
	 */
	public static boolean del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 根据key 获取内容
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Object value = jedis.get(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 根据key 获取对象
	 * 
	 * @param key
	 * @return
	 */
	public static <T> T get(String key, Class<T> clazz) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String value = jedis.get(key);
			return JSON.parseObject(value, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 清空所有数据
	 */
	public static void flushAll() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.flushAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 模糊查询
	 * @param fuzzyKey
	 * @return
	 */
	public static List<Map<String, Object>> fuzzyQuery(String fuzzyKey){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Set<String> keySet = jedis.keys(fuzzyKey + "*");
			Iterator<String> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				Object value = jedis.get(key);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(key, value);
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return list;
	}
	
}
