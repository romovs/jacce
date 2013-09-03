package org.jacce;

public class CacheItemTimed extends CacheItem {

	public long lastAccessTime;
	public long createTime;
	
	
	public CacheItemTimed(Object id, Object item) {
		super(id, item);
		createTime = System.currentTimeMillis();
		lastAccessTime = createTime;
	}
	
	public void update() {
		lastAccessTime = System.currentTimeMillis();
	}
}
