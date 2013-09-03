package org.jacce.cache.car;

import org.jacce.CacheItem;


public class CARItem extends CacheItem {

	public boolean referenceBit;     // true - 1, false - 0
	
	public CARItem(Object id, Object obj) {
		super(id, obj);
		referenceBit = false;
	}
	
	@Override
	public void setValue(Object obj) {
		super.setValue(obj);
		referenceBit = false;
	}
}
