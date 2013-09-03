package org.jacce.benchmark;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Utils {
	
	/**
	 * Get CPU time.
	 * On Linux timer accuracy is 10ms.
	 * @return CPU time in nanoseconds or -1 if not supported by VM.
	 */
	public static long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean();
	    return bean.isCurrentThreadCpuTimeSupported()?bean.getCurrentThreadCpuTime():-1L;
	}

}
