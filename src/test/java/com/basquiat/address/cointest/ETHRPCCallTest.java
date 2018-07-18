package com.basquiat.address.cointest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.basquiat.address.code.Constants;
import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.util.CommonUtil;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ETHRPCCallTest {

	private String preFix;
	
	private String coinType;
	
	@Before
    public void setUp() {
		preFix = Constants.PRE_FIX;
		coinType = "ETH";
    }
	
	@Test
	public void getInfoCall() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		System.out.println(new Gson().toJson(node.getInfo()));
	}
	
//	@Test
	public void getNewAddressTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(preFix, coinType.toUpperCase());
		String result = node.getNewAddress();
		System.out.println(result);
	}
	
}
