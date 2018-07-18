package com.basquiat.address.redis.queue;

/**
 * 
 * Redis Message Publisher Interface
 * 
 * created by basquiat
 *
 */
public interface MessagePublisher {

	void publish(final String message);
	
}
