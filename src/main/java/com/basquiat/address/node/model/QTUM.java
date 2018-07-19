package com.basquiat.address.node.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.basquiat.address.code.RPCCommandCode;
import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.util.CommonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * QTUM model
 * created by basquiat
 *
 */
@Service("qtum")
public class QTUM implements BlockChainNodeInterface {

	public static String host;
		@Value("${qtum.host}")
		public void setHost(String host) { QTUM.host = host; }
	
	public static String port;
		@Value("${qtum.port}")
		public void setPort(String port) { QTUM.port = port; }
	
	public static String user;
		@Value("${qtum.user}")
		public void setUser(String user) { QTUM.user = user; }
	
	public static String password;
		@Value("${qtum.password}")
		public void setPassword(String password) { QTUM.password = password; }
	
	/**
	 * RPC Call
	 */
	@Override
	public Object RPCCall(String rpcCommand, List<Object> param) throws Exception {
		return CommonUtil.RemoteCallTypeOfBTC(user, password, host, port, rpcCommand, param);
	}
	
	/**
	 * info 정보 가져오기
	 */
	@Override
	public JsonObject getInfo() throws Exception {
		List<Object> list = new ArrayList<>();
		return (JsonObject) RPCCall(RPCCommandCode.QTUM_GETINFO.CODE, list);
	}

	/**
	 * 주소 생성
	 */
	@Override
	public String getNewAddress() throws Exception {
		List<Object> list = new ArrayList<>();
		list.add("basquiat");
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.QTUM_CREATEADDRESS.CODE, list);
		return jsonElement.getAsString();
	}

	/**
	 * 최신 블록 넘버 가져오기
	 */
	@Override
	public BigInteger getBlockCount() throws Exception {
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.QTUM_GETBLOCKCOUNT.CODE, new ArrayList<>());
		return jsonElement.getAsBigInteger();
	}

	/**
	 * 블록 넘버로 블록 해쉬값을 구한다.
	 */
	@Override
	public String getBlockHash(BigInteger blockNumber) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockNumber);
		JsonElement jsonElement = (JsonElement)RPCCall(RPCCommandCode.QTUM_GETBLOCKHASH.CODE, list);
		return jsonElement.getAsString();
	}

	/**
	 * 블록 해쉬로 블록 정보를 얻는다.
	 */
	@Override
	public JsonObject getBlock(String blockHash) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockHash);
		return (JsonObject)RPCCall(RPCCommandCode.QTUM_GETBLOCK.CODE, list);
	}

	/**
	 * 트랜잭션 아이디로 트랜잭션 정보를 얻는다.
	 */
	@Override
	public JsonObject getRawTransaction(String txId) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(txId);
		list.add(1);
		return (JsonObject)RPCCall(RPCCommandCode.QTUM_GETRAWTRANSACTION.CODE, list);
	}
	
	/**
	 * schedule check
	 * txId의 리스트와 최신 블록 넘버를 맵객체로 넘긴다.
	 */
	@Override
	public Map<String, Object> schedulingTransactionCheck(BigInteger lastBlock) throws Exception {
		
		// 1. 최신 블록을 가져온다.
		BigInteger latestBlock = this.getBlockCount();
		
		Map<String, Object> resultMap = new HashMap<>();
		
		// 2. 최신 블록이 lastBlock보다 크다면
		if(latestBlock.compareTo(lastBlock) >  0 ) {
			List<String> list = new ArrayList<>();
			// 3. lastBlock + 1에 해당하는 블록부터 최신 블록까지 blockhash를 가져오고
			for(BigInteger i = lastBlock.add(BigInteger.ONE); i.compareTo(latestBlock.add(BigInteger.ONE)) < 0; i = i.add(BigInteger.ONE)) {
				// 4. blockNumber로 blockHash로 해당 블록 정보를 가져와서
				String blockHash = this.getBlockHash(i);
				JsonObject blockInfo = this.getBlock(blockHash);
				List<String> resultList = CommonUtil.convertTypeTokenFromGson(blockInfo.get("tx"), new TypeToken<List<String>>(){});
				list = ListUtils.union(list, resultList);
			}
			
			resultMap.put("txList", list);
			resultMap.put("lastBlock", latestBlock);
		} else {
			resultMap = null;
		}
		
		return resultMap;
	}
	
}
