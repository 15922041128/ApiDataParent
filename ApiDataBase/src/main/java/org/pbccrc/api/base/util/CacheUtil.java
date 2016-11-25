package org.pbccrc.api.base.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class CacheUtil {
	// 缓存到期时间
	private static ConcurrentHashMap<String,Long> cacheTime =new ConcurrentHashMap<String,Long>();
	// 缓存
	private static ConcurrentHashMap<String,Object> objectCache =new ConcurrentHashMap<String,Object>();

	/**
	 * 删除无用key
	 * @param key
	 */
	private void removeKey(String key){
		cacheTime.remove(key);
		objectCache.remove(key);
		if(cacheTime.size()>10000){
			Set<String> timeKeys = cacheTime.keySet();
			for(String timeKey:timeKeys){
				if(cacheTime.get(timeKey) < System.currentTimeMillis()){
					cacheTime.remove(timeKey);
					objectCache.remove(timeKey);
				}
			}
		}
	}
	
	/**
	 * 添加缓存
	 * @param key
	 * @return
	 */
	public Object getObj(String key){
		// 缓存过期
		if( cacheTime.get(key) != null && cacheTime.get(key) < System.currentTimeMillis()){
			removeKey(key);
			return null;
		}
		return  objectCache.get(key);
	}
	
	/** 
	 * 添加缓存
	 * @param key
	 * @param value
	 * @param expireTime 过期时间 (秒)
	 * @return
	 */
	public boolean setObj(String key, Object value, int expireTime){
		// 如果为-1，存储一年
		if(expireTime == -1) expireTime = 360 * 24 * 60 *60;
		objectCache.put(key, value);
		cacheTime.put(key, System.currentTimeMillis() + expireTime * 1000);
		return true;
	}
	
	/**
	 * 删除缓存
	 * @param key
	 * @return
	 */
	public boolean delObj(String key) {
		objectCache.remove(key);
		cacheTime.remove(key);
		return false;
	} 
}
