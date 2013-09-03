package org.jacce;


import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;


public class CacheFactory {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CacheFactory.class.getName());

	public static int DEF_CAPACITY = 1000;
	

	private Map<String, Cache> caches;
	

	private static CacheFactory INSTANCE = null;
	
	
	protected CacheFactory() {
		caches = new HashMap<String, Cache>();
	}
	
	
	public void initialize(InputStream filestream) throws CacheException {
		Config.configure(filestream);
	}

	
	public static CacheFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CacheFactory(); 
		}
		
		return INSTANCE;
	}

	public Cache addCache(String id, String description, String cacheImpl, int capacity, int memorySize) throws CacheException {
		
		if (capacity < 0)
			throw new IllegalArgumentException("Invalid capacity specified for cache: " + id);
		if (memorySize < 0)
			throw new IllegalArgumentException("Invalid memory size specified for cache: " + id);
		
		
		try {
	        Class<?> cacheClass = getClass().getClassLoader().loadClass(cacheImpl);
	        Class<?>[] types = new Class[] { String.class, String.class, int.class, int.class };
	        Constructor<?> cons = cacheClass.getConstructor(types);
	        Object[] args = new Object[] { id, description, capacity, memorySize };
	        Cache cache = (Cache)cons.newInstance(args);
	        
			logger.debug("Adding cache: " + cache);
			
			caches.put(id, cache);
			return cache;
		} catch (Exception e) {
			throw new CacheException(e);
		}
		
	}
	
	public CacheTimed addCacheTimed(String id, String description, String cacheImpl, int capacity, int memorySize, int cleanInterval, int ttl, int idlePeriod) throws CacheException {
		

		if (capacity < 0)
			throw new IllegalArgumentException("Invalid capacity specified for cache: " + id);
		if (memorySize < 0)
			throw new IllegalArgumentException("Invalid memory size specified for cache: " + id);
		if (cleanInterval < 0)
			throw new IllegalArgumentException("Invalid cleaning interval specified for cache: " + id);
		
		try {
	        Class<?> cacheClass = getClass().getClassLoader().loadClass(cacheImpl);
	        Class<?>[] types = new Class[] { String.class, String.class, int.class, int.class, int.class, int.class, int.class};
	        Constructor<?> cons = cacheClass.getConstructor(types);
	        Object[] args = new Object[] { id, description, capacity, memorySize, cleanInterval, ttl, idlePeriod };
	        final CacheTimed cache = (CacheTimed)cons.newInstance(args);

			logger.debug("Adding cache: " + cache);
	        
			caches.put(id, cache);
			return cache;
		} catch (Exception e) {
			throw new CacheException(e);
		}
		
	}
    
    
	public Cache getCache(String id) {
		return caches.get(id);
	}
	
	
	public void removeCache(String id) {
		
		Cache cache = caches.get(id);
		
		if (cache != null) {
			if (cache instanceof CacheTimed)
				((CacheTimed)cache).cancel();

			caches.remove(id);
		}
		
	}
	
	
	public void removeAll() {
		

		
	}
}