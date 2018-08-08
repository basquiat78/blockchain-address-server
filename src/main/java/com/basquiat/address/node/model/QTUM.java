package com.basquiat.address.node.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

	@Value("${qtum.host}")
	private String host;
	
	@Value("${qtum.port}")
	private String port;
	
	@Value("${qtum.user}")
	private String user;
	
	@Value("${qtum.password}")
	private String password;
	
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
		list.add("kdexusertmp");
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

	@Override
	public String getBlockHash(BigInteger blockNumber) throws Exception {
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

	/**
	 * btc계열은 사용하지 않는다.
	 */
	@Override
	public JsonObject getBlock(BigInteger blockNumber) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public JsonObject getRawTransaction(String txId) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(txId);
		list.add(1);
		return (JsonObject)RPCCall(RPCCommandCode.QTUM_GETRAWTRANSACTION.CODE, list);
	}
	
	/**
	 * ETH 계열에서 사용 
	 */
	@Override
	public JsonObject getTransactionReceipt(String hash) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * ETH 계열에서 사용 
	 */
	@Override
	public int confirmationCheckETH(String txId) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
