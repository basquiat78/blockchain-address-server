package com.basquiat.address.scheule;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 * TransactionScheduler
 * 
 * created by basquiat
 *
 */
@Component
public class TransactionScheduler {

	@Autowired
	private TransactionHandler transactionHandler;
	
	private static Map<String, String> BASED_ON_BTC_MODEL;
		@Value("${based.type.btc}")
		public void setBtcModel(String basedTypeBtc) { 
			Map<String, String> map = new HashMap<>();
			String[] array = basedTypeBtc.split(",");
			for(String coin : array) {
				map.put(coin.trim(), coin.trim());
			}
			BASED_ON_BTC_MODEL = map;
		}
	
	private static Map<String, String> BASED_ON_ETH_MODEL;
	@Value("${based.type.eth}")
		public void setEthModel(String basedTypeEth) { 
			Map<String, String> map = new HashMap<>();
			String[] array = basedTypeEth.split(",");
			for(String coin : array) {
				map.put(coin.trim(), coin.trim());
			}
			BASED_ON_ETH_MODEL = map;
		}
	
	/**
	 * 10분 간격으로 btc transaction얻어오기
	 * @throws Exception 
	 */
	@Scheduled(cron = "0 0/10 * * * *")
	public void checkBTCBlockChain() throws Exception {
		transactionHandler.processGetTransactionTypeBTC(BASED_ON_BTC_MODEL.get("BTC"));
	}

	/**
	 * 10분 간격으로 qtum transaction얻어오기
	 * @throws Exception 
	 */
	@Scheduled(cron = "0 0/10 * * * *")
	public void checkQTUMBlockChain() throws Exception {
		transactionHandler.processGetTransactionTypeBTC(BASED_ON_BTC_MODEL.get("QTUM"));
	}
	
	/**
	 * 10분 간격으로 eth transaction얻어오기
	 */
	@Scheduled(cron = "0 0/10 * * * *")
	public void checkETHBlockChain() throws Exception {
		transactionHandler.processGetTransactionTypeETH(BASED_ON_ETH_MODEL.get("ETH"));
	}
	
	/**
	 * 10분 간격으로 ethreum 계열의 Node의 transaction얻어오기
	 */
	@Scheduled(cron = "0 0/10 * * * *")
	public void checkETCBlockChain() throws Exception {
		transactionHandler.processGetTransactionTypeETH(BASED_ON_ETH_MODEL.get("ETC"));
	}
	
	/**
	 * 2분 간격으로 컴펌 체크
	 */
	@Scheduled(cron = "0 0/2 * * * *")
	public void checkConfirmations() throws Exception {
		transactionHandler.processCheckConfirmations();
	}
	
}
