package com.basquiat.address.node.model;

import java.math.BigInteger;
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
 * ETC model
 * created by wkimdev
 *
 */
@Service("etc")
public class ETC implements BlockChainNodeInterface {
	
	public static String host;
		@Value("${etc.host}")
		public void setHost(String host) { ETC.host = host; }
		
	public static String port;
		@Value("${etc.port}")
		public void setPort(String port) { ETC.port = port; }
		
	public static String user;
		@Value("${etc.user}")
		public void setUser(String user) { ETC.user = user; }
	
	public static String password;
		@Value("${etc.password}")
		public void setPassword(String password) { ETC.password = password; }
	
	public static String accountName;
		@Value("${account.name}")
		public void setAccountName(String accountName) { ETC.accountName = accountName; }	
		
	/**
	 * RPC Call
	 */	
	@Override
	public Object RPCCall(String rpcCommand, List<Object> param) throws Exception {
		return CommonUtil.RemoteCallTypeOfETH(user, password, host, port, rpcCommand, param);
	}
	
	/**
	 * info 정보 가져오기
	 */
	@Override
	public JsonObject getInfo() throws Exception {
		List<Object> list = new ArrayList<>();
		list.add("latest");
		list.add(true);
		return (JsonObject)RPCCall(RPCCommandCode.ETC_GETINFO.CODE, list);
	}

	@Override 
	public String getNewAddress() throws Exception {
		List<Object> list = new ArrayList<>();
		// ETH 계열의 블록체인의 경우에는 계정 생성시 비번을 입력한다.
		list.add(accountName);
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.ETC_CREATEADDRESS.CODE, list);
		return jsonElement.getAsString();
	}

	@Override
	public BigInteger getBlockCount() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBlockHash(BigInteger blockNumber) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getBlock(String blockHash) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getRawTransaction(String txId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> schedulingTransactionCheck(BigInteger lastBlock) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
