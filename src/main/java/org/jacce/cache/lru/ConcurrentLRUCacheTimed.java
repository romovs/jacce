package org.jacce.cache.lru;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jacce.CacheItemTimed;
import org.jacce.CacheTimed;


public class ConcurrentLRUCacheTimed extends CacheTimed {
	
	private final float LOAD_FACTOR = 0.75f; 
	private LinkedHashMap<Object,CacheItemTimed> map;
	
	public ConcurrentLRUCacheTimed(String id, String description, int capacity, int memorySize,
			int cleanInterval, int ttl, int idlePeriod) {
		super(id, description, capacity, memorySize, cleanInterval, ttl, idlePeriod);

		map = new LinkedHashMap<Object,CacheItemTimed>((int)Math.ceil(capacity/LOAD_FACTOR) + 1, LOAD_FACTOR, true) {

			private static final long serialVersionUID = 1;
			@Override protected boolean removeEldestEntry (Map.Entry<Object,CacheItemTimed> eldest) {
				return size() > ConcurrentLRUCacheTimed.this.capacity;
			}
		}; 
	}

	
	@Override
	public synchronized Object get(Object id) {
		requests++;
		
		CacheItemTimed ti = map.get(id);
		
		if (ti != null) {
			if (isValid(ti)) {
				hits++;
				ti.update();
				return ti.value;
			} else {
				map.remove(id);
			}
		}
		
	   return null;
	}
	
	@Override
	public synchronized void set(Object id, Object value) {
	   map.put(id, new CacheItemTimed(id, value));
	}
	
	
	@Override
	public synchronized void clean() {
		for (Object id : map.keySet()) {
			CacheItemTimed ti = map.get(id);
			
			if (ti != null && !isValid(ti))
				map.remove(id);
		}
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
