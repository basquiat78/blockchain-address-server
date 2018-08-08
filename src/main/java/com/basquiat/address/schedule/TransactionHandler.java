package com.basquiat.address.schedule;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.basquiat.address.code.UnitCode;
import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.service.block.BlockHistoryService;
import com.basquiat.address.service.block.vo.BlockHistoryVO;
import com.basquiat.address.service.transaction.TransactionHistoryService;
import com.basquiat.address.service.transaction.vo.TransactionVO;
import com.basquiat.address.service.wallet.WalletService;
import com.basquiat.address.service.wallet.vo.WalletVO;
import com.basquiat.address.util.CommonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 
 * 스케쥴링할 때 for문을 돌면서 실행하는 프로세스를 비동기로 받아서 처리한다.
 * 
 * created by basquiat
 *
 */
@Component
public class TransactionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionHandler.class);
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private BlockHistoryService blockHistoryService;
	
	@Autowired
	private TransactionHistoryService transactionHistoryService;
	
	@Autowired
	private ApplicationContext context;
	
	/**
	 * BTC계열의 트랜잭션 체크
	 * @param coinCode
	 * @throws Exception
	 */
	@Transactional
	public void processGetTransactionTypeBTC(String coinCode) {
		
		try {
			BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
			
			// 1. db로부터 lastBlock을 가져온다.
			BlockHistoryVO blockHistoryVO = blockHistoryService.getBlockHistory(coinCode.toUpperCase());
			
			// 2. 최신 블록 넘버를 가져온다.
			BigInteger latestBlock = node.getBlockCount();
			
			BigInteger lastBlock = blockHistoryVO.getLastBlockNumber();
			
			// 3. 최신 블록이 lastBlock보다 크다면 transaction정보들을 조회한다.
			if(latestBlock.compareTo(lastBlock) >  0 ) {
				for(BigInteger i = lastBlock.add(BigInteger.ONE); i.compareTo(latestBlock.add(BigInteger.ONE)) < 0; i = i.add(BigInteger.ONE)) {
					String blockHash = node.getBlockHash(i);
					JsonObject blockInfo = node.getBlock(blockHash);
					List<String> resultList = CommonUtil.convertTypeTokenFromGson(blockInfo.get("tx"), new TypeToken<List<String>>(){});
					for(String txId : resultList) {
						// 4. txId별로 vout의 정보를 얻어온다.
						JsonObject txJsonObject = node.getRawTransaction(txId);
						if(txJsonObject != null) {
							// 5. vout List를 가져온다.
							LOG.info(CommonUtil.convertJsonStringFromGson(txJsonObject));
							List<JsonObject> vOutList = CommonUtil.getVOutFromTransaction(txJsonObject);
							for(JsonObject vout : vOutList) {
								String addressFromVOut = CommonUtil.getAddressFromVOut(vout);
								// 6. address정보가 있는 경우만 체크한다.
								if(addressFromVOut != null) {
									//LOG.info(CommonUtil.convertJsonStringFromGson(vout));
									BigDecimal value = CommonUtil.getValueFromVOut(vout);
									//LOG.info("value is " + value.toString() + ", toAddress [" + addressFromVOut + "]");
									// 7. 해당 주소로 db에서 셀렉트해서 존재하는 주소인지 확인한다.
									WalletVO selectedWalletVO = walletService.getWalletByAddress(addressFromVOut);
									// 8. 존재하는 주소라면 해당 주소의 balance를 주소로 들어온 value와 합해서 업데이트를 한다.
									if( selectedWalletVO != null ) {
										//LOG.info("this address [" + addressFromVOut + "] exit our Node");
										BigDecimal updateValue = value.add(selectedWalletVO.getBalance());
										selectedWalletVO.setBalance(updateValue);
										walletService.updateWalletByAddress(selectedWalletVO);
										LOG.info("find txId : [" + txId + "]");
										LOG.info("find blockhash : [" +  txJsonObject.get("blockhash").getAsString() + "]");
										//LOG.info("find confirmations : [" + txJsonObject.get("confirmations").getAsInt() + "]");
										// 9. 해당 txId에 대한 트랜잭션 정보는 DB에 인서트 한다. transaction로그를 남긴다. TODO로 남겨두자
										TransactionVO transactionVO = new TransactionVO();
										transactionVO.setTxId(txId);
										transactionVO.setBlockHash(txJsonObject.get("blockhash").getAsString());
										transactionVO.setCoinType(coinCode.toUpperCase());
										transactionVO.setAddress(addressFromVOut);
										transactionVO.setBalance(value);
										transactionVO.setConfirmations(txJsonObject.get("confirmations").getAsInt());
										transactionHistoryService.createTransactionHistory(transactionVO);
									}
								}
							}
						}
						
					}
				
				}
			}
				
			// 10. 최종적으로 모든 txId별로 조회 및 엡데이트가 완료가 된다면 마지막 블록 정보를 BLOCK_HISTORY 테이블에 업데이트를 한다.
			blockHistoryVO.setLastBlockNumber(latestBlock);
			blockHistoryService.updateBlockHistory(blockHistoryVO);
			
		} catch (Exception e) {
			LOG.error("", e);
		}
		
	}
	
	/**
	 * ETH계열의 트랜잭션 체크
	 * @param coinCode
	 * @throws Exception
	 */
	@Transactional
	public void processGetTransactionTypeETH(String coinCode) {
		
		try {
			BlockChainNodeInterface node = CommonUtil.createInstance(coinCode.toLowerCase(), context);
			
			// 1. lastBlock을 가져온다.
			BlockHistoryVO blockHistoryVO = blockHistoryService.getBlockHistory(coinCode.toUpperCase());
			
			// 2. 최신 블록 넘버를 가져온다.
			BigInteger latestBlock = node.getBlockCount();
						
			BigInteger lastBlock = blockHistoryVO.getLastBlockNumber();
			
			// 3. 최신 블록이 lastBlock보다 크다면 transaction정보들을 조회한다.
			if(latestBlock.compareTo(lastBlock) >  0 ) {
				for(BigInteger i = lastBlock.add(BigInteger.ONE); i.compareTo(latestBlock.add(BigInteger.ONE)) < 0; i = i.add(BigInteger.ONE)) {
					JsonObject blockInfo = node.getBlock(i);
					JsonElement jsonElement = (JsonElement)blockInfo.get("transactions");
					List<String> resultList = CommonUtil.convertTypeTokenFromGson(jsonElement, new TypeToken<List<String>>(){});
					for(String txId : resultList) {
						// 4. txId별로 block정보를 가져온다.
						JsonObject jsonObject = node.getRawTransaction(txId);
						LOG.info(CommonUtil.convertJsonStringFromGson(jsonObject));
						if(jsonObject != null) {
							//5. address 를 가져오고, null처리 
							String address = CommonUtil.getAddressFromTxId(jsonObject);
							//6. address 정보가 있는 경우만 체크
							if(address != null) {
								LOG.info(CommonUtil.convertJsonStringFromGson(jsonObject));
								BigDecimal value = CommonUtil.getValueFromJsonObjectETH(jsonObject);
								LOG.info("value is " + value.toString() + ", toAddress [" + address + "]");
								// 7. 해당 주소로 db에서 셀렉트해서 존재하는 주소인지 확인한다.(wallet 테이블에서 조회한다!)
								WalletVO selectedWalletVO = walletService.getWalletByAddress(address);
								// 8. 존재하는 주소라면 해당 주소의 balance를 주소로 들어온 value와 합해서 업데이트를 한다.
								if( selectedWalletVO != null ) {
									//9. ETH의 경우, wei라는 통화단위때문에 아래와 같이 단위를 컨버트를 시킨다.
									BigDecimal balanceValue = CommonUtil.convertWeiToEther(value, UnitCode.ETHER);  
									BigDecimal updateValue = balanceValue.add(selectedWalletVO.getBalance());
									selectedWalletVO.setBalance(updateValue);
									walletService.updateWalletByAddress(selectedWalletVO);
									int confirmationCheck = node.confirmationCheckETH(txId); //1161
									LOG.info("find txId : [" + txId + "]");
									LOG.info("find blockhash : [" +  jsonObject.get("blockHash").getAsString() + "]");
									// 10. 해당 txId에 대한 트랜잭션 정보는 DB에 인서트 한다. transaction로그를 남긴다.
									TransactionVO transactionVO = new TransactionVO();
									transactionVO.setTxId(txId);
									transactionVO.setBlockHash(jsonObject.get("blockHash").getAsString());
									transactionVO.setCoinType(coinCode.toUpperCase());
									transactionVO.setAddress(address);
									transactionVO.setBalance(balanceValue);
									transactionVO.setConfirmations(confirmationCheck);
									transactionHistoryService.createTransactionHistory(transactionVO);
								}
							}
						}
						
					}
				}
				
			}
				
			// 11. 최종적으로 모든 txId별로 조회 및 업데이트가 완료가 된다면 마지막 블록 정보를 BLOCK_HISTORY 테이블에 업데이트를 한다.
			blockHistoryVO.setLastBlockNumber(latestBlock);
			blockHistoryService.updateBlockHistory(blockHistoryVO);
			
		} catch (Exception e) {
			LOG.error("", e);
		}
		
	}

	/**
	 * confirmations 체크 및 업데이트
	 * @throws Exception
	 */
	public void processCheckConfirmations() {
		try {
			// 1. db에서 트랜잭션 리스트를 가져온다.
			List<TransactionVO> txList = transactionHistoryService.getTransactionHistorys();
			for(TransactionVO txVO : txList) {
				String txId = txVO.getTxId();
				String coinType =  txVO.getCoinType();
				//2. transaction history정보에서 코인 코드를 통해서 node instance를 생성하고 txId로 rawTransaction정보를 불러온다.
				BlockChainNodeInterface node = CommonUtil.createInstance(coinType.toLowerCase(), context);
				Integer confirmationsFromTx = 0;
				if("ETH".equals(coinType) || "ETC".equals(coinType)) {
					confirmationsFromTx = node.confirmationCheckETH(txId);
				} else {
					JsonObject txInfo = node.getRawTransaction(txId);
					confirmationsFromTx = txInfo.get("confirmations").getAsInt();
				}
				txVO.setConfirmations(confirmationsFromTx);
				transactionHistoryService.updateTransactionHistory(txVO);
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
		
	}

}
