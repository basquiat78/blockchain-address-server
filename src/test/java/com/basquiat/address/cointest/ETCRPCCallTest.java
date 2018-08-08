package com.basquiat.address.cointest;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ETCRPCCallTest {
	
	@Autowired
	private ApplicationContext context;
	
	private String coinCode;
	
	@Before
    public void setUp() {
		coinCode = "ETC";
    } 
	
	//@Test
	public void getInfoCall() throws Exception {
		BlockChainNodeInterface node =	CommonUtil.createInstance(coinCode.toLowerCase(), context);
		System.out.println(new Gson().toJson(node.getInfo()));
	}
	
	@Test
	public void getNewAddressTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		String result = node.getNewAddress();
//		LOG.info("before replace teset address : "+result);
//		result.replaceAll("\"", "");
		//String address2 = walletVO.getAddress();
		//LOG.info("after replace teset address : "+result);
		System.out.println("test get net address etc!!!!!!!!!!!!!!!!!!!!!!!: "+result);
	}
	
	//@Test
	public void getBlockCountTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		BigInteger result = node.getBlockCount();
		System.out.println(result); 
	}
	
	//@Test
	public void getBlockTest1() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		BigInteger blockNumber = BigInteger.valueOf(2465748);
		JsonObject result = node.getBlock(blockNumber);
		System.out.println(result); 
	}
	
	//@Test
	public void testHexConvert() throws Exception {
		String hexString = new BigDecimal("2652938").toBigInteger().toString(16);
		String blockNumber = "0x"+hexString;
		System.out.println("test testHexConvert : "+blockNumber);
	}
	
	//@Test
	public void getBlockTest2() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		BigInteger blockNumber = BigInteger.valueOf(2465748); //transaction 값 존
		JsonObject jo = node.getBlock(blockNumber);	
		JsonElement jsonElement = (JsonElement) jo.get("transactions"); 
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		for(JsonElement txId : jsonArray) {
			System.out.println(">>>>>>>"+node.getRawTransaction(txId.getAsString()));
		}
	}
	
	//@Test
	public void testReplace() throws Exception{
		
		String address = "0xfca576ac65deaca9fb8744c706538f7377b3fd5d";
		//String a1 = address.replace("\"", "");
		address.replaceAll("\"", "");
		System.out.println("a1 test : "+address);
	}
	
}
