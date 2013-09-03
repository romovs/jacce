package org.jacce.cache.dummy;

import java.util.Set;

import org.jacce.CacheTimed;

public class DummyCache extends CacheTimed {

    public DummyCache(String id, String description, int capacity, int memorySize) {
		super(id, description, capacity, memorySize, 0, 0, 0);
	}

    public DummyCache(String id, String description, int capacity, int memorySize,
    		int cleanInterval, int ttl, int idlePeriod) {
		super(id, description, capacity, memorySize, cleanInterval, ttl, idlePeriod);
	}
    
	@Override
	public Object get(Object id) {
		return null;
	}
	
	@Override
	public void invalidate(Object id) {
	}
	
	@Override
	public void invalidateGroup(Set<Object> ids) {
	}
	
	@Override
	public void set(Object id, Object entry) {
	}

	@Override
	public void clean() {
	}
}