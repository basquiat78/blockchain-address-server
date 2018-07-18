package com.basquiat.address.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basquiat.address.redis.queue.RedisMessagePublisher;

/**
 * 
 * Redis Publish Service
 * created by basquiat
 *
 */
@Service("publisherService")
public class PublisherService {

	
	@Autowired
	private RedisMessagePublisher messagePublisher;
	
	public void redisPublisher(String message) {
		
		messagePublisher.publish(message);
		
	}
	
}
