package org.jacce.benchmark;


import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jacce.Cache;
import org.jacce.CacheException;
import org.jacce.CacheFactory;


public class Benchmark {
	
	private static final long SEED = 15485863;
	private static final int CACHE_CAPACITY = 1000;
	private static final int OBJECTS_NUM = 10000;
	private static final int THREADS_NUM = 10;
	private static final String FORMAT = "%1$-20s|%2$-12s|%3$-17s|\n";
	
	
	public static void main(String[] args) throws FileNotFoundException, CacheException, InterruptedException {
		
		System.out.format(FORMAT, "", " Cache Hits ", " Exec. Time (ms) ");
		System.out.format(FORMAT, "", "============", "=================");

		Integer[] objects;
		objects = new Integer[OBJECTS_NUM];
		for (int i = 0; i < OBJECTS_NUM; i++) {
			objects[i] = new Integer(i);
		}
		
		CacheFactory cf = CacheFactory.getInstance();
		
		execute(cf, "org.jacce.cache.car.ConcurrentCARCache", objects, THREADS_NUM);
		execute(cf, "org.jacce.cache.fifo.ConcurrentFIFOCache", objects, THREADS_NUM);
		execute(cf, "org.jacce.cache.lru.ConcurrentLRUCache", objects, THREADS_NUM);
	}
	
	
	
	private static void execute(CacheFactory cf, String cacheImpl, Integer[] objects, int threads) throws CacheException, InterruptedException {
		
		cf.addCache(cacheImpl, null, cacheImpl, CACHE_CAPACITY, 0);
		Cache cache = cf.getCache(cacheImpl);
		
		JacceRunner[] cars = new JacceRunner[threads];
		for (int i = 0; i < threads; i++) {
			cars[i] = new JacceRunner(objects, cache, 10000, SEED+i); 
		}
		
		ExecutorService threadExecutor = Executors.newFixedThreadPool(threads);
		
		for (int i = 0; i < threads; i++) {
			threadExecutor.execute(cars[i]); 
		}
		
		threadExecutor.shutdown();
		threadExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

		
		long totalTime = 0;
		for (int i = 0; i < threads; i++) {
			JacceRunner c = cars[i];
			totalTime += c.getTime();
		}
		
    	DecimalFormat twoDForm = new DecimalFormat("#.###");

		System.out.format(FORMAT, cacheImpl.substring(cacheImpl.lastIndexOf(".")+1), twoDForm.format(cache.hitRatio()), (double)totalTime/1000000);
	}
}
