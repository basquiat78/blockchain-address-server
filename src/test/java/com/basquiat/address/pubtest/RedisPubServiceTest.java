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
	
	private String message;
	
	@Before
    public void setUp() {
		message = "{'userId':10, 'coinType': 'BTC'}";
    }
	
	@Test
	public void redisPublisherTest() throws Exception {
		publisherService.redisPublisher(message);
	}
	
}
