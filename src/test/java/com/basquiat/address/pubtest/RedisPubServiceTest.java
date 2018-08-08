package com.basquiat.address.pubtest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.basquiat.address.redis.service.PublisherService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisPubServiceTest {

	@Autowired
	private PublisherService publisherService;
	
	private String pubMessage;
	
	private String retryMessage;
	
	@Before
    public void setUp() {
		pubMessage = "redisPublisherTest";
		retryMessage = "redisRetryTest";
    }
	
	//@Test
	public void redisPublisherTest() throws Exception {
		publisherService.redisPublisher(pubMessage);
	}
	
	@Test
	public void redisRetryTest() throws Exception {
		publisherService.redisRetryPublisher(retryMessage);
	}
	
}
