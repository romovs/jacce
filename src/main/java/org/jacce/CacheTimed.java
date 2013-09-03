package org.jacce;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Convenience base class for timed cache implementations.
 * 
 * @author Roman Ovseitsev. <a href="mailto:romovs@gmail.com">romovs@gmail.com</a>
 */
public abstract class CacheTimed extends Cache {
	
		protected Timer timer;
		protected int cleanInterval;
		protected int ttl;
		protected int idlePeriod;
	

		public void cancel() {
			if (timer != null)
				timer.cancel();
		}
		

		public CacheTimed(String id, String description, int capacity, int memorySize,
				int cleanInterval, int ttl, int idlePeriod) {
			super(id, description, capacity, memorySize);
			
	        if (cleanInterval > 0) {
				timer = new Timer();
				
				timer.scheduleAtFixedRate(		
			        	new TimerTask() {
							@Override
							public void run() {
								clean();
							}
			    }, 0, cleanInterval);
	        }
			
			this.cleanInterval = cleanInterval;
			this.ttl = ttl;
			this.idlePeriod = idlePeriod;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(" clean-interval: ");
			sb.append(cleanInterval).append(" ttl:").append(ttl).append(" idle-period:").append(idlePeriod);
			return super.toString() + sb.toString();
		}
		
		public boolean isValid(CacheItemTimed entry) {
			long currentTime = System.currentTimeMillis();
			
			if ((ttl != 0 && currentTime >= entry.createTime + ttl) ||
					(idlePeriod != 0 && currentTime >= entry.lastAccessTime + idlePeriod)) {
				return false;	
			}
			
			return true;
		}
		
		abstract public void clean(); 
}
