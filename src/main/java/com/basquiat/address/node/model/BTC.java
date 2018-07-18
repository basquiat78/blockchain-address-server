package com.basquiat.address.node.model;

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
 * BTC model
 * created by basquiat
 *
 */
@Service("btc")
public class BTC implements BlockChainNodeInterface {

	public static String host;
		@Value("${bitcoin.host}")
		public void setHost(String host) { BTC.host = host; }

	public static String port;
		@Value("${bitcoin.port}")
		public void setPort(String port) { BTC.port = port; }
	
	public static String user;
		@Value("${bitcoin.user}")
		public void setUser(String user) { BTC.user = user; }
	
	public static String password;
		@Value("${bitcoin.password}")
		public void setPassword(String password) { BTC.password = password; }
		
	public static String accountName;
		@Value("${account.name}")
		public void setAccountName(String accountName) { BTC.accountName = accountName; }
	
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
		return (JsonObject)RPCCall(RPCCommandCode.BTC_GETINFO.CODE, list);
	}
	
	/**
	 * 주소 생성
	 */
	@Override
	public String getNewAddress() throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(accountName);
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.BTC_CREATEADDRESS.CODE, list);
		return jsonElement.getAsString();
	}

	/**
	 * 최신 블록 넘버 가져오기
	 */
	@Override
	public Integer getBlockCount() throws Exception {
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.BTC_GETBLOCKCOUNT.CODE, new ArrayList<>());
		return jsonElement.getAsInt();
	}

	/**
	 * 블록넘버로 블록 해쉬 가져오기
	 */
	@Override
	public String getBlockHash(Integer blockNumber) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockNumber);
		JsonElement jsonElement = (JsonElement)RPCCall(RPCCommandCode.BTC_GETBLOCKHASH.CODE, list);
		return jsonElement.getAsString();
	}

	/**
	 * 블록 해쉬로 블록 정보 가져오기
	 */
	@Override
	public JsonObject getBlock(String blockHash) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockHash);
		return (JsonObject)RPCCall(RPCCommandCode.BTC_GETBLOCK.CODE, list);
	}
	
	/**
	 * txid로 transaction정보 가져오기
	 * json map형식으로 받기 위해 파라미터에 1를 추가한다.
	 */
	@Override
	public JsonObject getRawTransaction(String txId) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(txId);
		list.add(1);
		return (JsonObject)RPCCall(RPCCommandCode.BTC_GETRAWTRANSACTION.CODE, list);
	}
	
	@Override
	public Map<String, Object> schedulingTransactionCheck(Integer lastBlock) throws Exception {
		
		// 1. 최신 블록을 가져온다.
		Integer latestBlock = this.getBlockCount();
		
		Map<String, Object> resultMap = new HashMap<>();
		
		// 2. 최신 블록이 lastBlock보다 크다면
		if(latestBlock > lastBlock) {
			List<String> list = new ArrayList<>();
			// 3. lastBlock + 1에 해당하는 블록부터 최신 블록까지 blockhash를 가져오고
			for(int i = lastBlock+1; i < latestBlock+1; i++) {
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
