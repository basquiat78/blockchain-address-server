package com.basquiat.address.node.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.basquiat.address.code.RPCCommandCode;
import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.util.CommonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	public Integer getBlockCount() throws Exception {
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.QTUM_GETBLOCKCOUNT.CODE, new ArrayList<>());
		return jsonElement.getAsInt();
	}

	@Override
	public String getBlockHash(Integer blockNumber) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockNumber);
		JsonElement jsonElement = (JsonElement)RPCCall(RPCCommandCode.QTUM_GETBLOCKHASH.CODE, list);
		return jsonElement.getAsString();
	}

	@Override
	public JsonObject getBlock(String blockHash) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(blockHash);
		return (JsonObject)RPCCall(RPCCommandCode.QTUM_GETBLOCK.CODE, list);
	}

	@Override
	public JsonObject getRawTransaction(String txId) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(txId);
		list.add(1);
		return (JsonObject)RPCCall(RPCCommandCode.QTUM_GETRAWTRANSACTION.CODE, list);
	}
	
	@Override
	public Map<String, Object> schedulingTransactionCheck(Integer lastBlock) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
