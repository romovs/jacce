package org.jacce.test;

import org.jacce.Cache;
import org.jacce.CacheException;
import org.jacce.CacheFactory;
import org.jacce.CacheTimed;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestCARCache { 
	
	/* ***********************************************************************************
	 * Simple test for set/get methods and eviction policy.  
	 * ********************************************************************************* */
	
	@Test
	public void testSimple() throws CacheException {
		test3Objects(initCache("org.jacce.cache.car.CARCache", 2, 0));
		test3Objects(initCacheTimed("org.jacce.cache.car.CARCacheTimed", 2, 0, 0, 0, 0));
		test3Objects(initCache("org.jacce.cache.car.ConcurrentCARCache", 2, 0));
		test3Objects(initCacheTimed("org.jacce.cache.car.ConcurrentCARCacheTimed", 2, 0, 0, 0, 0));
	}
	
	private void test3Objects(Cache c) {
	  	Object one = new Integer(1);
	  	Object two = new Integer(2);
	  	Object three = new Integer(3);
	  	
	  	c.set("test", one);
	  	c.set("test2", two);
	  	c.set("test3", three);
	  	
	  	assertNull(c.get("test"));
	  	assertEquals(two, c.get("test2"));
	  	assertEquals(three, c.get("test3"));
	}
	
	// TODO test for clean-up interval
	/* ***********************************************************************************
	 * Test expiration routines.
	 * ********************************************************************************* */
	
	@Test
	public void testTTL() throws InterruptedException, CacheException {
		testTTL(initCacheTimed("org.jacce.cache.car.CARCacheTimed", 2, 0, 0, 10 ,1000*60*60));
		testTTL(initCacheTimed("org.jacce.cache.car.ConcurrentCARCacheTimed", 2, 0, 0, 10 ,1000*60*60));
	}
	
	@Test
	public void testIdlePeriod() throws InterruptedException, CacheException {
		testIdle(initCacheTimed("org.jacce.cache.car.CARCacheTimed", 2, 0, 0, 0, 10));
		testIdle(initCacheTimed("org.jacce.cache.car.ConcurrentCARCacheTimed", 2, 0, 0, 0 ,10));
	}

	
	private void testTTL(Cache c) throws InterruptedException {
		c.set("test", new Integer(1));
		c.set("test2", new Integer(2));
		
		Thread.sleep(11);
		c.set("test3", new Integer(3));

	  	assertNull(c.get("test"));
	  	assertNull(c.get("test2"));
	  	assertNotNull(c.get("test3"));
	}

	
	private void testIdle(Cache c) throws InterruptedException {
		c.set("test", new Integer(1));
		c.set("test2", new Integer(2));
		c.set("test3", new Integer(3));
		
		Thread.sleep(7);
		
	  	c.get("test");
	  	c.get("test3");
		 
		Thread.sleep(3);

	  	assertNull(c.get("test"));
	  	assertNull(c.get("test2"));
	  	assertNotNull(c.get("test3"));
	}
	
	
	/* ***********************************************************************************
	 * Test invalidation
	 * ********************************************************************************* */
	
	@Test
	public void testInvalidate() throws CacheException, InterruptedException {
		testInv(initCache("org.jacce.cache.car.CARCache", 2, 0));
		testInv(initCache("org.jacce.cache.car.ConcurrentCARCache", 2, 0));
	}
	
	@Test
	public void testInvalidateGroup() throws CacheException, InterruptedException {
		testInvGroup(initCache("org.jacce.cache.car.CARCache", 3, 0));
		testInvGroup(initCache("org.jacce.cache.car.ConcurrentCARCache", 3, 0));
	}
	
	private void testInv(Cache c) throws InterruptedException {
		c.set("test", new Integer(1));
		c.set("test2", new Integer(2));
		c.set("test3", new Integer(3));
		
		c.invalidate("test2");

	  	assertNull(c.get("test"));
	  	assertNull(c.get("test2"));
	  	assertNotNull(c.get("test3"));
	}
	
	
	private void testInvGroup(Cache c) throws InterruptedException {
		c.set("test", new Integer(1));
		c.set("test2", new Integer(3));
		c.set("test3", new Integer(3));
		c.set("test4", new Integer(4));
		
		c.invalidate("test2");
		c.invalidate("test3");
		
	  	assertNull(c.get("test"));
	  	assertNull(c.get("test2"));
	  	assertNull(c.get("test3"));
	  	assertNotNull(c.get("test4"));
	}
	
	private Cache initCache(String cacheImpl, int capacity, int memorySize) throws CacheException {
		CacheFactory cf = CacheFactory.getInstance();
		return cf.addCache("test", null, cacheImpl, capacity, memorySize);
	}
	
	private CacheTimed initCacheTimed(String cacheImpl, int capacity, int memorySize, int cleanInterval, int ttl, int idelPeriod) throws CacheException {
		CacheFactory cf = CacheFactory.getInstance();
		return  cf.addCacheTimed("test", null, cacheImpl, capacity, memorySize, cleanInterval, ttl, idelPeriod);
	}
	
}