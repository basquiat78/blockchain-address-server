package com.basquiat.address.cointest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.basquiat.address.code.UnitCode;
import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ETHRPCCallTest {

	@Autowired
	private ApplicationContext context;
	
	private String coinCode;
	
	@Before
    public void setUp() {
		coinCode = "ETH";
    }
	
	//@Test
	public void getInfoCall() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		System.out.println(new Gson().toJson(node.getInfo()));
	}
	
	//@Test
	public void getNewAddressTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		String result = node.getNewAddress();
		System.out.println(result);
	}
	
	@Test
	public void getBlockCountTest() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		BigInteger result = node.getBlockCount();
		System.out.println(result); 
	}
	
	//@Test
	public void getBlockTest1() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		BigInteger blockNumber = BigInteger.valueOf(2664518);
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
		BigInteger blockNumber = BigInteger.valueOf(2664518);
		JsonObject jo = node.getBlock(blockNumber);
		JsonElement jsonElement = (JsonElement) jo.get("transactions"); 
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		for(JsonElement txId : jsonArray) {
			System.out.println(">>>>>>>"+node.getRawTransaction(txId.getAsString()));
		}
	}
	
	//@Test
	public void convertWeiToEth() throws Exception {
		//BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		//8. ETH의 경우, wei라는 통화단위때문에 아래와 같이 단위를 컨버트를 시킨다.
		BigDecimal bd = new BigDecimal(1000000000000000L);
		//BigDecimal result = CommonUtil.convertWeiToEther(bd, UnitCode.); 
		System.out.println("test BigDecimal :: "+bd.divide(UnitCode.ETHER.getWeiFactor()));
		
	}
	
	//트랜잭션 hash에 의한, 트랜잭션 수신을 반환.
	//confirmation check test
	//@Test
	public void getTransactionReceipt() throws Exception {
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
		//txId를 파라미터로 받는다.
		JsonObject jo = node.getTransactionReceipt("0xc36b940645ff8040713d7b30dee3aa9e77212f4f00a703a7b5037306b2bf169b");
		System.out.println("test gettransactionreceipt >>>>>>>>>>>>>"+jo);
		int confirm = 0;
		JsonElement jsonElement = (JsonElement)jo.get("blockNumber");
		String blockNumber = jsonElement.getAsString();
		System.out.println("checkblockNumber>>>>>>>>>>>>> "+jo.get("blockNumber")); //"0x28a846"
		
		if(blockNumber != null) {
			BigInteger latestBlock = node.getBlockCount();
			BigInteger currentBlock = CommonUtil.decodeQuantityTypeOfETH(blockNumber);
			
			//confirm = latestBlock - currentBlock;
			BigInteger confirmNumber = latestBlock.subtract(currentBlock);
			System.out.println(">>>> latestBlock value : "+latestBlock+"  >>>> currentBlock value : "+currentBlock+"  >>>>  confirmNumber value : "+confirmNumber); //58339 ???
			confirm = confirmNumber.intValue();
		}
		System.out.println("check confirm >>>>>>>>>>>>>>>>>."+confirm);	//.58322 ???
	}
	
	//@Test
	public void getTxListTest() throws Exception {
		System.out.println("test start getTxListTest!!!!!!!!!!!>>>>>>>>>>>");
		BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
//		for(int i = 0; i <= 200000 ; i++) {
//			System.out.println("i : "+i);
//		}
		//2540255, 2730040
		Map<String, Object> resultMap = new HashMap<>();
		
		// 2. 최신 블록이 lastBlock보다 크다면
		List<String> list = new ArrayList<>();
		// 3. lastBlock + 1에 해당하는 블록부터 최신 블록까지 blockhash를 가져오고
		// 최신블럭 - 현재블럭 > 20000 
		// 
		for(int i = 2540255 ; i < 2730041; i++) {
			BigInteger number = BigInteger.valueOf(i);
			JsonObject blockInfo = node.getBlock(number); //jsonobject로 데이터를 가져와서
			JsonElement jsonElement = (JsonElement)blockInfo.get("transactions");	//
			List<String> resultList = CommonUtil.convertTypeTokenFromGson(jsonElement, new TypeToken<List<String>>(){});
			list = ListUtils.union(list, resultList);
		}
		
		resultMap.put("txList", list);
		System.out.println("result test : "+list);
		System.out.println("test after getTxListTest!!!!!!!!!!!>>>>>>>>>>>");
	}
}
