package org.jacce.cache.car;

import org.jacce.CacheItemTimed;

public class CARItemTimed extends CacheItemTimed {
	
	public boolean referenceBit;     // true - 1, false - 0
	
	public CARItemTimed(Object id, Object item) {
		super(id, item);
		this.referenceBit = false;
	}
}
