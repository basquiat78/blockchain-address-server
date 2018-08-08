package com.basquiat.address.redis.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * Redis Message Publisher
 * created by basquiat
 *
 */
@Service("redisMessagePublisher")
public class RedisMessagePublisher implements MessagePublisher {

	private static final Logger LOG = LoggerFactory.getLogger(RedisMessagePublisher.class);
	
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @Qualifier("pub")
    private ChannelTopic pubTopic;

    @Autowired
    @Qualifier("retry")
    private ChannelTopic retryTopic;
    
    /**
     * Constructor
     */
    public RedisMessagePublisher() {
    }

    /**
     * Constructor
     * @param redisTemplate
     * @param topic
     */
    public RedisMessagePublisher(final RedisTemplate<String, Object> redisTemplate, final ChannelTopic pubTopic, final ChannelTopic retryTopic) {
        this.redisTemplate = redisTemplate;
        this.pubTopic = pubTopic;
        this.retryTopic = retryTopic;
    }

    /**
     * redis pub message
     */
    public void publish(final String message) {
    	LOG.info(message);
        redisTemplate.convertAndSend(pubTopic.getTopic(), message);
    }
    
    /**
     * redis retry pub message
     */
    public void retry(final String message) {
    	LOG.info(message);
        redisTemplate.convertAndSend(retryTopic.getTopic(), message);
    }
    
}
