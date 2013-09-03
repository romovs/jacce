package org.jacce.cache.fifo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.jacce.Cache;

public class ConcurrentFIFOCache extends Cache {

	private final float LOAD_FACTOR = 0.75f; 
	private final Map<Object, Object> map;
    private final LinkedList<Object> fifo;

    
    public ConcurrentFIFOCache(String id, String description, int capacity, int memorySize) {
		super(id, description, capacity, memorySize);

		map = new HashMap<Object, Object>((int)Math.ceil(capacity/LOAD_FACTOR)+1);
		fifo = new LinkedList<Object>();
	}


	@Override
	public synchronized Object get(Object id) {
		requests++;
		
		Object obj = map.get(id);
		
		if (obj != null)
			hits++;
	
		return obj;
	}
	
	
	@Override
	public synchronized void invalidate(Object id) {
		if (map.remove(id) == null)
			return;
		fifo.remove(id);
	}
	
	
	@Override
	public synchronized void invalidateGroup(Set<Object> ids) {
		for (Object id : ids) {
			if (map.remove(id) == null)
				continue;
			fifo.remove(id);
		}
	}
	
	
	@Override
	public synchronized void set(Object id, Object entry) {
		if (fifo.size() >= capacity) {
			Object idToRemove = fifo.getLast();
			map.remove(idToRemove);
			fifo.removeLast();
		}
		map.put(id, entry);
		fifo.addFirst(id);
	}
}