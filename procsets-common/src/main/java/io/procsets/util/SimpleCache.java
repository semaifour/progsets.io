package io.procsets.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Object Cache Component
 * 
 * @author mjs
 *
 */

public class SimpleCache<T> {

	Logger LOG = LoggerFactory.getLogger(this.getClass());

	
	private String name = null;
    private long defaultTTL = 0;
	private Map<String, T> cache = null;
    
    public SimpleCache(String name, long defaultTTL) {
    	this.name = name;
    	this.defaultTTL = defaultTTL;
    	this.cache = Collections.synchronizedMap(new HashMap<String, T>());
    }

    public void putForGood(String cacheKey, T value) {
        cache.put(cacheKey, value);
    }
    
    /**
     * 
     * Put a cache object with default timer
     * 
     * @param cacheKey
     * @param value
     */
    public void put(String cacheKey, T value) {
    	this.put(cacheKey, value, this.defaultTTL);
    }
    
    public void put(String cacheKey, T value, long ttlms) {
        cache.put(cacheKey, value);
        setTimer(cacheKey, ttlms);
    }

    public T get(String cacheKey) {
        return cache.get(cacheKey);
    }
    
    public Set<String> keys() {
    	return cache.keySet();
    }

    public Collection<T> values() {
    	return cache.values();
    }

    public T clear(String cacheKey) {
        return cache.put(cacheKey, null);
    }

    public void clear() {
        cache.clear();
    }
    
	private void setTimer(String cacheKey, long ttlms) {
		if (ttlms > 0) {
			Timer t = new Timer("["+ this.getName() + "] Cache Invalidator :"  + cacheKey, true);
			t.schedule(new CacheInvalidationTask<T>(cacheKey, this), ttlms);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Default Time To Live in milliseconds for cached objects 
	 * @return
	 */
	public long getDefaultTTL() {
		return defaultTTL;
	}

	public void setDefaultTTL(long defaultTTL) {
		this.defaultTTL = defaultTTL;
	}

}

class CacheInvalidationTask<T> extends TimerTask {
	
	Logger LOG = LoggerFactory.getLogger(CacheInvalidationTask.class.getName());

	SimpleCache<T> simpleCache;
	String cacheKey;
	
	CacheInvalidationTask(String cacheKey, SimpleCache<T> simpleCache) {
		this.cacheKey = cacheKey;
		this.simpleCache = simpleCache;
	}
	
	@Override
	public void run() {
		this.simpleCache.clear(this.cacheKey);
	}
	
}