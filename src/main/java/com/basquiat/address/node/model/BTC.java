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
 * BTC model
 * created by basquiat
 *
 */
@Service("btc")
public class BTC implements BlockChainNodeInterface {

	@Value("${bitcoin.host}")
	private String host;

	@Value("${bitcoin.port}")
	private String port;
	
	@Value("${bitcoin.user}")
	private String user;
	
	@Value("${bitcoin.password}")
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
		return (JsonObject)RPCCall(RPCCommandCode.BTC_GETINFO.CODE, list);
	}
	
	/**
	 * 주소 생성
	 */
	@Override
	public String getNewAddress() throws Exception {
		List<Object> list = new ArrayList<>();
		list.add("kdexusertmp");
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.BTC_CREATEADDRESS.CODE, list);
		return jsonElement.getAsString();
	}

	/**
	 * 최신 블록 넘버 가져오기
	 */
	@Override
	public BigInteger getBlockCount() throws Exception {
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.BTC_GETBLOCKCOUNT.CODE, new ArrayList<>());
		return jsonElement.getAsBigInteger();
	}

	/**
	 * btc계열은 사용하지 않는다.
	 */
	@Override
	public JsonObject getBlock(BigInteger blockNumber) throws Exception {
		return null;
	}
	
	/**
	 * 블록넘버로 블록 해쉬 가져오기
	 */
	@Override
	public String getBlockHash(BigInteger blockNumber) throws Exception {
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
