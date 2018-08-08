package com.basquiat.address.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.basquiat.address.redis.queue.MessagePublisher;
import com.basquiat.address.redis.queue.RedisMessagePublisher;
import com.basquiat.address.redis.queue.RedisMessageSubscriber;
import com.basquiat.address.service.wallet.WalletService;

/**
 * 
 * RedisConfig
 * 
 * created by basquiat
 *
 */
@Configuration
public class RedisConfig {
	
	@Value("${publish.channel}")
	private String pubChannel;
	
	@Value("${retry.channel}")
	private String retryChannel;
	
	@Value("${subscribe.channel}")
	private String subChannel;
	
	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	@Value("${spring.redis.password}")
	private String redisPassword;
	
	@Autowired
	private WalletService walletService;
	
	/**
	 * redis connection factory
	 * @return JedisConnectionFactory
	 */
	@Bean
    public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(redisHost);
		jedisConnectionFactory.setPort(redisPort);
		jedisConnectionFactory.setPassword(redisPassword);
		jedisConnectionFactory.setUsePool(true);		
		return jedisConnectionFactory;
    }

	/**
	 * Redis Template Setup
	 * @return RedisTemplate<String, Object>
	 */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    /**
     * redis listner
     * @return RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        // subscribe 채널 등록
        container.addMessageListener(messageListener(), subTopics());
        return container;
    }

    /**
     * subscribe 채널 등록
     * @return ChannelTopic
     */
    @Bean
    public List<ChannelTopic> subTopics() {
    	List<ChannelTopic> list = new ArrayList<>();
    	list.add(new ChannelTopic(subChannel));
        return list;
    }
    
    /**
     * publish 채널 등록
     * @return ChannelTopic
     */
    @Bean(name="pub")
    public ChannelTopic pubTopic() {
    	return new ChannelTopic(pubChannel);
    }
    
    /**
     * retry 채널 등록
     * @return ChannelTopic
     */
    @Bean(name="retry")
    public ChannelTopic retryTopic() {
    	return new ChannelTopic(retryChannel);
    }
    
    /**
     * redis pub setup
     * @return MessageListenerAdapter
     */   
    @Bean
    public MessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), pubTopic(), retryTopic());
    }

    /**
     * redis sub setup
     * @return MessageListenerAdapter
     */
    @Bean
    public MessageListenerAdapter messageListener() {
    	return new MessageListenerAdapter(new RedisMessageSubscriber(subChannel, walletService));
    }
 
}
