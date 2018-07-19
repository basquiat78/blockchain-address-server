package com.basquiat.address.cointest;

import java.math.BigInteger;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.basquiat.address.code.Constants;
import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BTCRPCCallTest {

	private String preFix;
	
	private String coinType;
	
	@Before
    public void setUp() {
		preFix = Constants.PRE_FIX;
		coinType = "BTC";
    }
	
	//@Test
	public void getInfoCall() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(new Gson().toJson(node.getInfo()));
	}
	
	//@Test
	public void getNewAddressTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(node.getNewAddress());
	}
	
	@Test
	public void getBlockCountTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(node.getBlockCount());
	}
	
	//@Test
	public void getBlockHashTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(node.getBlockHash(BigInteger.valueOf(1353056)));
	}
	
	//@Test
	public void getBlockTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		JsonObject jo = node.getBlock("000000000000005e20511478c761d90da5297457233b7fbd032578f604379525");
		TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
		List<String> txList = CommonUtil.convertTypeTokenFromGson(jo.get("tx"), typeToken);
		System.out.println(txList.size());
		for(String txId : txList) {
			System.out.println(">>>>>>>"+node.getRawTransaction(txId));
		}
	}
	
	//@Test
	public void getRawTransactionTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(node.getRawTransaction("44b784eb2f310f303a987ad33fe7339033c115d1b66ab779a502a1125b0276ac"));
	}
}
