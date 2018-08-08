package com.basquiat.address.pubtest;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.basquiat.address.util.CommonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDataServiceTest {

	@Resource(name="redisTemplate") 
	private ValueOperations<String, String> valueOperations;
	
	@Resource(name = "redisTemplate") 
	private ListOperations<String, String> listOperations;
	
	@Resource(name = "redisTemplate") 
	private HashOperations<String, String, String> hashOperations;
	
	@Resource(name = "redisTemplate") 
	private SetOperations<String, String> setOperations;
	
	@Resource(name="redisTemplate") 
	private ZSetOperations<String, String> zSetOperations;
	
	@Before
    public void setUp() {

		// rightPush
		// 예를 들면 [] <-- 요렇게 생긴 구조에 데이터를 오른쪽에서 밀어 넣는다고 생각하면 된다.
		// 밑에처럼 로직을 짜면 ["Java"] => ["Java", "JavaScript"] => ["Java", "JavaScript", "C++"] => ["Java", "JavaScript", "GO"]
		// leftPush
		// stack or queue
//		listOperations.rightPush("doublechain:language", "Java");
//		listOperations.rightPush("doublechain:language", "JavaScript");
//		listOperations.rightPush("doublechain:language", "C++");
//		listOperations.rightPush("doublechain:language", "GO");
		
		
		// hashMap
//		hashOperations.put("doublechain:user", "name", "basquiat");
//		hashOperations.put("doublechain:user", "age", "40");
		
		// Set
//		setOperations.add("doublechain:type", "String");
//		setOperations.add("doublechain:type", "Integer");
//		setOperations.add("doublechain:type", "BigInteger");
//		setOperations.add("doublechain:type", "BigDecimal");
		
		//zset 
//		zSetOperations.add("doublechain:server", "API-Server", 1);
//		zSetOperations.add("doublechain:server", "Address-Server", 2);
//		zSetOperations.add("doublechain:server", "Wallet-Server", 3);
//		zSetOperations.add("doublechain:server", "MultiChain-Explorer", 4);
		
    }
	
	@Test
	public void redisValueOperationsServiceTest() throws Exception {
		//String value = valueOperations.get("doublechain:test");
		//System.out.println(value);
	}
	
	//@Test
	public void redisListOperationsServiceTest() throws Exception {
		
		// without remove cache
//		List<String> list = listOperations.range("doublechain:language", 0, -1);
//		for(String key : list) {
//			System.out.println(key);
//		}

		// 왼쪽부터 데이터를 끄집어 낸다.
		// leftPop or rightPop은 redis캐쉬에서 삭제된다.
		String doublechainLanguage = listOperations.leftPop("doublechain:language");
		while(doublechainLanguage != null) {
			System.out.println(doublechainLanguage);
			doublechainLanguage = listOperations.leftPop("doublechain:language");
		}
	}
	
	//@Test
	public void redisHashOperationsServiceTest() throws Exception {
		Map<String, String> map = hashOperations.entries("doublechain:user");
		System.out.println(CommonUtil.convertJsonStringFromObject(map));
	}
	
	//@Test
	public void redisSetOperationsServiceTest() throws Exception {
		Set<String> types = setOperations.members("doublechain:type");
		for(String type : types) {
			System.out.println("type is " + type);
		}
	}
		
	//@Test
	public void redisZSetOperationsServiceTest() throws Exception {
		Set<String> servers = zSetOperations.range("doublechain:server", 0, -1);
		for(String server : servers) {
			System.out.println("server is " + server);
		}
	}	
}
