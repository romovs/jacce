package org.jacce.benchmark;


import java.util.Random;

import org.jacce.Cache;
import org.jacce.CacheException;
import org.jacce.benchmark.Utils;

public class JacceRunner implements Runnable {

	private Integer[] objects;
	private Cache cache;
	private int iterations;
	private Random rng;
	private long time;

	
	public JacceRunner(Integer[] objects, Cache cache, int iterations, long seed) throws CacheException {
		this.iterations = iterations;
		rng = new Random(seed);
		this.cache = cache;
		this.objects = objects;
	}
	
	
	@Override
	public void run() {
		int objectsNum = objects.length;

		long startTime = Utils.getCpuTime();

		for (int i = 0; i < iterations; i++) {
			int index = rng.nextInt(objectsNum);
			
			if (cache.get(objects[index]) == null) {
				Object o = objects[index];
				cache.set(o, o);
			}
		}
		
		long endTime = Utils.getCpuTime();
		if (endTime < startTime)	// on rare occasions some HotSpot versions on Linux systems 
			endTime = startTime;	// might return incorrect results.
			
		time = endTime - startTime;
	}


	public long getTime() {
		return time;
	}
}
