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

	/**
	 * publish channel로 메세지를 보낸다.
	 * @param message
	 */
	public void redisPublisher(String message) {
		messagePublisher.publish(message);
	}
	
	/**
	 * retry 메세지를 retry channel로 보낸다.
	 * @param message
	 */
	public void redisRetryPublisher(String message) {
		messagePublisher.retry(message);
	}

}
