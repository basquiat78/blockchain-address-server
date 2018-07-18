package com.basquiat.address.cointest;

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
public class QTUMRPCCallTest {

	private String preFix;
	
	private String coinType;
	
	@Before
    public void setUp() {
		preFix = Constants.PRE_FIX;
		coinType = "QTUM";
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
	
	//@Test
	public void getBlockCountTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(node.getBlockCount());
	}
	
	//@Test
	public void getBlockHashTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(node.getBlockHash(177844));
	}
	
	//@Test
	public void getBlockTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		JsonObject jo = node.getBlock("8be4f1e4e974e5673ceddb10fbae37683069b5fbe4c7cf0ba66c61b83349e465");
		TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
		List<String> txList = CommonUtil.convertTypeTokenFromGson(jo.get("tx"), typeToken);
		System.out.println(txList.size());
		for(String txId : txList) {
			System.out.println(">>>>>>>"+node.getRawTransaction(txId));
		}
	}
	
	@Test
	public void getRawTransactionTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(node.getRawTransaction("c94c195036d362471f9696b050a0e6160096e7ad7fccd461874dd1bb06123f72"));
	}
}
