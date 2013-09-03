package org.jacce.cache.fifo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.jacce.CacheItemTimed;
import org.jacce.CacheTimed;

public class FIFOCacheTimed extends CacheTimed {
	
	private final float LOAD_FACTOR = 0.75f; 
	private final Map<Object, CacheItemTimed> map;
    private final LinkedList<Object> fifo;

    
    public FIFOCacheTimed(String id, String description, int capacity, int memorySize, 
    		int cleanInterval, int ttl, int idlePeriod) {
		super(id, description, capacity, memorySize, cleanInterval, ttl, idlePeriod);

		map = new HashMap<Object, CacheItemTimed>((int)Math.ceil(capacity/LOAD_FACTOR)+1);
		fifo = new LinkedList<Object>();
	}


	@Override
	public Object get(Object id) {
		requests++;
		
		CacheItemTimed entry = map.get(id);
		
		if (entry != null) {
			if (isValid(entry)) {
				hits++;
				entry.update();
				return entry.value;
			} else {
				map.remove(id);
				fifo.remove(id);
			}
		}

		return null;
	}
	
	
	@Override
	public void invalidate(Object id) {
		if (map.remove(id) == null)
			return;
		fifo.remove(id);
	}
	
	
	@Override
	public void invalidateGroup(Set<Object> ids) {
		for (Object id : ids) {
			if (map.remove(id) == null)
				continue;
			fifo.remove(id);
		}
	}
	
	
	@Override
	public void set(Object id, Object entry) {
		if (fifo.size() >= capacity) {
			Object idToRemove = fifo.getLast();
			map.remove(idToRemove);
			fifo.removeLast();
		}
		map.put(id, new CacheItemTimed(id, entry));
		fifo.addFirst(id);
	}


	@Override
	public void clean() {
	    for (Iterator<?> it = fifo.iterator(); it.hasNext(); ) {
	    	Object id = it.next(); 
			CacheItemTimed entry = map.get(id);
	        if (!isValid(entry)) {
	            it.remove();
	            map.remove(id);
	        }
	    }
	}
}