package com.basquiat.address.node.model;

import java.math.BigDecimal;
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
 * ETC model
 * created by wkimdev
 *
 */
@Service("etc")
public class ETC implements BlockChainNodeInterface {
	
	@Value("${etc.host}")
	private String host;
		
	@Value("${etc.port}")
	private String port;
		
	@Value("${etc.user}")
	private String user;
	
	@Value("${etc.password}")
	private String password;
	
	/**
	 * RPC Call
	 */	
	@Override
	public Object RPCCall(String rpcCommand, List<Object> param) throws Exception {
		return CommonUtil.RemoteCallTypeOfETH(user, password, host, port, rpcCommand, param);
	}
	
	/**
	 * ETC info 정보 가져오기
	 * false값을 리턴시키고 있기 때문에 pass한다.
	 */
	@Override
	public JsonObject getInfo() throws Exception {
		List<Object> list = new ArrayList<>();
		JsonElement jsonElement = (JsonElement)RPCCall(RPCCommandCode.ETC_GETINFO.CODE, list); //jsonPrimitive
		String jsonObj = jsonElement.getAsString();	
		JsonObject newGO = new JsonObject();
		newGO.addProperty("testKey", jsonObj);
		return newGO;
	}
	
	/**
	 * ETC address 생성
	 */
	@Override 
	public String getNewAddress() throws Exception {
		List<Object> list = new ArrayList<>();
		list.add("kdexusertmp");	//password
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.ETC_CREATEADDRESS.CODE, list);
		String address = jsonElement.getAsString();
		return address;
	}
	
	/**
	 * ETC 최신 블록 넘버 가져오기 
	 */
	@Override
	public BigInteger getBlockCount() throws Exception {
		List<Object> list = new ArrayList<>();
		list.add("latest");
		JsonElement jsonElement = (JsonElement) RPCCall(RPCCommandCode.ETC_GETBLOCKCOUNT.CODE, list);
		return CommonUtil.decodeQuantityTypeOfETH(jsonElement.getAsString());
	}
	
	/**
	 * eth계열은 btc랑 다르게 블록 넘버로 block 정보를 가져올 수 있다.
	 * eth계열에서는 사용하지 않는다.
	 */
	@Override
	public String getBlockHash(BigInteger blockNumber) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * BTC계열에서만 사용한다.
	 */
	@Override
	public JsonObject getBlock(String blockHash) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * etc를 위한 block 정보가져오기
	 * @param blockNumber
	 * @return JsonObject
	 * @throws Exception
	 */
	@Override
	public JsonObject getBlock(BigInteger blockNumber) throws Exception {
		List<Object> list = new ArrayList<>();
		String hexString = new BigDecimal(blockNumber).toBigInteger().toString(16);
		String hexBlockNumber = "0x"+hexString;
		list.add(hexBlockNumber);
		list.add(false);
		return (JsonObject)RPCCall(RPCCommandCode.ETC_GETBLOCK.CODE, list);
	}
	
	/**
	 * ETC txid로 transaction정보 가져오기
	 */
	@Override
	public JsonObject getRawTransaction(String txId) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(txId);
		return (JsonObject)RPCCall(RPCCommandCode.ETC_GETRAWTRANSACTION.CODE, list);
	}
	
	/**
	 * ETH 트랜잭션에 대한 수신 값 반환
	 */
	@Override
	public JsonObject getTransactionReceipt(String txId) throws Exception {
		List<Object> list = new ArrayList<>();
		list.add(txId);
		return (JsonObject)RPCCall(RPCCommandCode.ETH_GETTRANSACTIONRECEIPT.CODE, list);
	}
	
	/**
	 * ETH 계열에 대한 블럭 confirmation 체크
	 */
	@Override
	public int confirmationCheckETH(String txId) throws Exception {
		JsonObject jo = getTransactionReceipt(txId);
		int confirm = 0;
		JsonElement jsonElement = (JsonElement)jo.get("blockNumber");
		String blockNumber = jsonElement.getAsString();
		
		if(blockNumber != null) {
			BigInteger latestBlock = getBlockCount();
			BigInteger currentBlock = CommonUtil.decodeQuantityTypeOfETH(blockNumber);
			// confirm = latestBlock - currentBlock; (최신 블럭 - 히스토리 블럭)
			BigInteger confirmNumber = latestBlock.subtract(currentBlock);
			confirm = confirmNumber.intValue();
		}
		return confirm;
	}

}
