package org.jacce.cache.lru;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jacce.Cache;
import org.jacce.CacheItem;


public class ConcurrentLRUCache extends Cache {

	private final float LOAD_FACTOR = 0.75f; 
	private LinkedHashMap<Object,CacheItem> map;
	
	public ConcurrentLRUCache(String id, String description, int capacity, int memorySize) {
		super(id, description, capacity, memorySize);

		map = new LinkedHashMap<Object,CacheItem>((int)Math.ceil(capacity/LOAD_FACTOR)+1, LOAD_FACTOR, true) {

			private static final long serialVersionUID = 1;
			@Override protected boolean removeEldestEntry (Map.Entry<Object,CacheItem> eldest) {
				return size() > ConcurrentLRUCache.this.capacity;
			}
		}; 
	}

	
	@Override
	public synchronized Object get(Object id) {
		requests++;
		
		CacheItem ti = map.get(id);
		
		if (ti != null) {
			hits++;
			return ti.value;
		}
		
	   return null;
	}
	
	@Override
	public synchronized void set(Object id, Object value) {
	   map.put(id, new CacheItem(id, value));
	}
	
	
	@Override
	public synchronized void invalidate(Object id) {
		map.remove(id);
	}
	
	@Override
	public synchronized void invalidateGroup(Set<Object> ids) {
		for (Object id : ids) {
			map.remove(id);
		}
	}
}
