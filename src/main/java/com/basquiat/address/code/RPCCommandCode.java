package com.basquiat.address.code;

/**
 * RPC Command Enum
 * created by basquiat
 *
 */
public enum RPCCommandCode {

	/**
	 * BTC Command
	 */
	BTC_GETINFO("getwalletinfo"),
	
	BTC_CREATEADDRESS("getnewaddress"),
	
	BTC_GETBLOCK("getblock"),
	
	BTC_GETBLOCKHASH("getblockhash"),
	
	BTC_GETBLOCKCOUNT("getblockcount"),
	
	BTC_GETRAWTRANSACTION("getrawtransaction"),
	
	/**
	 * QTUM Command
	 */
	QTUM_GETINFO("getinfo"),
	
	QTUM_CREATEADDRESS("getnewaddress"),
	
	QTUM_GETBLOCK("getblock"),
	
	QTUM_GETBLOCKHASH("getblockhash"),
	
	QTUM_GETBLOCKCOUNT("getblockcount"),
	
	QTUM_GETRAWTRANSACTION("getrawtransaction"),
	
	/**
	 * XRP Command
	 */
	XRP_GETINFO("server_info"),
	
	/**
	 * ETC Command
	 */
	ETC_GETINFO("eth_syncing"),
	
	ETC_GETBLOCKCOUNT("eth_blockNumber"),
	
	ETC_CREATEADDRESS("personal_newAccount"),
	
	ETC_GETBLOCK("eth_getBlockByNumber"),
	
	ETC_GETRAWTRANSACTION("eth_getTransactionByHash"),
	
	/**
	 * ETH Command
	 */
	ETH_GETINFO("eth_syncing"),
	
	ETH_CREATEADDRESS("personal_newAccount"),
	
	ETH_GETBLOCKCOUNT("eth_blockNumber"),
	
	ETH_GETBLOCK("eth_getBlockByNumber"),
	
	ETH_GETRAWTRANSACTION("eth_getTransactionByHash"),
	
	ETH_GETTRANSACTIONRECEIPT("eth_getTransactionReceipt");
	

	/** int type constructor */
	RPCCommandCode(String code, int Id) {
		this.CODE = code;
		this.ID = Id;
	}
	
	/** int type constructor */
	RPCCommandCode(int Id) {
		this.ID = Id;
	}

	public int ID;
		int getId() { return this.ID; }	

	/** String type constructor */
	RPCCommandCode(String code) {
		this.CODE = code;
	}
		
	public String CODE;
		String getCode() { return this.CODE; }
		
}
