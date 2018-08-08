package com.basquiat.address.node;

import java.math.BigInteger;
import java.util.List;

import com.google.gson.JsonObject;

/**
 * 
 * Block Chain Interface
 * 
 * create by basquiat
 *
 */
public interface BlockChainNodeInterface {

	public Object RPCCall(String rpcCommand, List<Object> param) throws Exception;

	public JsonObject getInfo() throws Exception;
	
	public String getNewAddress() throws Exception;
	
	public BigInteger getBlockCount() throws Exception;
	
	public String getBlockHash(BigInteger blockNumber) throws Exception;
	
	public JsonObject getBlock(String blockHash) throws Exception;
	
	/**
	 * ethereum계열은 블럭 넘버로 block 정보를 가져올 수 있다.
	 * @param blockNumber
	 * @return JsonObject
	 * @throws Exception
	 */
	public JsonObject getBlock(BigInteger blockNumber) throws Exception;
	
	public JsonObject getRawTransaction(String txId) throws Exception;

	/**
	 * ETH 트랜잭션에 대한 수신 값 반환 
	 * @param hash
	 * @return JsonObject
	 * @throws Exception
	 */
	public JsonObject getTransactionReceipt(String hash) throws Exception;
	
	/**
	 * ETH 트랜잭션에 대한 confirmation check 
	 * @param txId
	 * @return int
	 * @throws Exception
	 */
	public int confirmationCheckETH(String txId) throws Exception;

}
