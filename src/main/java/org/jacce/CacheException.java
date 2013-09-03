package org.jacce;

public class CacheException extends Exception {
	
	private static final long serialVersionUID = -6830122504067785043L;

	public CacheException() {
		super();
	}
	
	public CacheException(String error) {
		super(error);
	}
	
	public CacheException(Exception e) {
		super(e);
	}
	  
	public CacheException(String error, Exception e) {
		super(error, e);
	}
}
