package org.jacce;

public class CacheItem {

	public Object id;
	public Object value;
	
	public CacheItem(Object id, Object value) {
		this.id = id;
		this.value = value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
}
