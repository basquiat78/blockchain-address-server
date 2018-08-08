package com.basquiat.address.cointest;

import java.math.BigInteger;
import java.util.List;

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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BTCRPCCallTest {

	@Autowired
	private ApplicationContext context;
	
	private String coinCode;
	
	@Before
    public void setUp() {
		coinCode = "BTC";
    }
	
	//@Test
	public void getInfoCall() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		System.out.println(new Gson().toJson(node.getInfo()));
	}
	
	//@Test
	public void getNewAddressTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		System.out.println(node.getNewAddress());
	}
	
	@Test
	public void getBlockCountTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		System.out.println(node.getBlockCount());
	}
	
	//@Test
	public void getBlockHashTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		System.out.println(node.getBlockHash(BigInteger.valueOf(1353056)));
	}
	
	//@Test
	public void getBlockTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		JsonObject jo = node.getBlock("00000000000000095ba9340a24b9937ef4f3eb5784def453c93d1a9418fc7084");
		System.out.println(CommonUtil.convertJsonStringFromGson(jo));
		TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
		List<String> txList = CommonUtil.convertTypeTokenFromGson(jo.get("tx"), typeToken);
		System.out.println(txList.size());
		for(String txId : txList) {
			System.out.println(">>>>>>>"+node.getRawTransaction(txId));
		}
	}
	
	//@Test
	public void getRawTransactionTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		JsonObject jsonObject = node.getRawTransaction("5afbe85f7e02d07ffd02fea62cfcfb80fadcb7ae73d25627810f0ef3184506cb");
		System.out.println(CommonUtil.convertJsonStringFromGson(jsonObject));
		System.out.println(jsonObject.get("blockhash").getAsString());
		System.out.println(jsonObject.get("confirmations").getAsInt());
	}
}
