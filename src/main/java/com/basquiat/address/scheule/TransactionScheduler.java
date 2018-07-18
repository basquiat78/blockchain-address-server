package com.basquiat.address.scheule;

import java.util.ArrayList;
import java.util.List;

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
	
	private static List<String> BASED_ON_BTC_MODEL;
		@Value("${based.type.btc}")
		public void setBtcModel(String basedTypeBtc) { 
			List<String> list = new ArrayList<String>();
			String[] array = basedTypeBtc.split(",");
			for(String coin : array) {
				list.add(coin.trim());
			}
			BASED_ON_BTC_MODEL = list;
		}
	
	private static List<String> BASED_ON_ETH_MODEL;
	@Value("${based.type.eth}")
		public void setEthModel(String basedTypeEth) { 
			List<String> list = new ArrayList<String>();
			String[] array = basedTypeEth.split(",");
			for(String coin : array) {
				list.add(coin.trim());
			}
			BASED_ON_ETH_MODEL = list;
		}
	
	/**
	 * 1분 간격으로 btc 계열의 Node의 transaction얻어오기
	 * @throws Exception 
	 */
	@Scheduled(cron = "0 * * * * *")
	public void basedOnBtcBlockChain() throws Exception {
		for(String coinCode : BASED_ON_BTC_MODEL) {
			if(coinCode.equals("BTC")) {
				transactionHandler.processGetTransactionTypeBTC(coinCode);
			}
		}
	}

	/**
	 * 1분 간격으로 ethreum 계열의 Node의 transaction얻어오기
	 */
	@Scheduled(cron = "0 * * * * *")
	public void basedOnEthBlockChain() throws Exception {
		for(String coinCode : BASED_ON_ETH_MODEL) {
			transactionHandler.processGetTransactionTypeETH(coinCode);
		}
	}
	
}
