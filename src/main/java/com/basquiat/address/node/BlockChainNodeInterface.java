package com.basquiat.address.node;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

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
	
	public JsonObject getRawTransaction(String txId) throws Exception;

	public Map<String, Object> schedulingTransactionCheck(BigInteger lastBlock) throws Exception;
	
}
