package org.jacce;

import java.util.Set;

/**
 * Convenience base class for cache implementations.
 * 
 * @author Roman Ovseitsev. <a href="mailto:romovs@gmail.com">romovs@gmail.com</a>
 */
public abstract class Cache {

		protected String id;
		protected String description;
		protected final int capacity;
		protected int memorySize;
		protected int hits, requests;
	

		/**
		 * Creates a new cache.
		 * 
		 * Cache capacity is specified by number of items the cache can hold or a maximum memory size approximation.
		 * <i>IMPORTANT NOTE:</i> <code>memorySize</code> should be avoided in most situations since it's provides rough 
		 * <b>approximation</b> of memory limit. The actual system memory used by the cache could in practice exceed this limit.
		 * 
		 * @param id			cache id.
		 * @param description	short cache description.
		 * @param capacity		number of items the cache can hold. 0 for no limit.
		 * @param memorySize	maximum memory size. 0 for no limit.
		 */
		public Cache(String id, String description, int capacity, int memorySize) {
			this.id = id;
			this.description = description;
			this.capacity = capacity;
			this.memorySize = memorySize;
			hits = 0;
			requests = 0;
		}
		
		
		/**
		 * Converts this Cache object to a String of the form:
		 * 
		 * id: 'cache id' desc: 'description' capacity: 'cache capacity' mem-size: 'memory size'
		 */
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("id:");
			sb.append(id).append(" desc:").append(description).append(" capacity:").append(capacity).append(" mem-size:").append(memorySize);
			return sb.toString();
		}
		

		/**
		 * Add new entry to cache. 
		 * 
		 * <p>This method is supposed to be called on previous cache miss in order to add missing entry.</p>
		 * 
		 * @param id entry identificator
		 * @param entry entry to add 
		 */
		abstract public void set(Object id, Object entry);
		
		/**
		 * Return cached entry or null on cache miss.
		 * This method should update <code>hits</code> and <code>requests</code> counters.
		 * 
		 * @param id
		 * @return
		 */
		abstract public Object get(Object id);
		
		/**
		 * Invalidate cached entry.
		 * 
		 * <p> This method does nothing if entry identifiable by {@code id} is not cached.</p>
		 * 
		 * @param id
		 */
		abstract public void invalidate(Object id);
		
		/**
		 * Invalidate set of cached entries.
		 * 
		 * <p> This method does nothing if entries identifiable by {@code ids} are not cached.</p>
		 *  
		 * @param ids Set of identifiers. Should be Hash map backed.
		 */
		abstract public void invalidateGroup(Set<Object> ids);
		
		
		/**
		 * Calculate hit ratio in percentages - {@literal (cache hits)*100/(total cache queries)}
		 * @return hit ratio.
		 */
		public double hitRatio() {
			return (requests == 0)?0:((double)hits/(double)requests)*100;
		}
}
