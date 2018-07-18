package com.basquiat.address.scheule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.basquiat.address.code.Constants;
import com.basquiat.address.node.BlockChainNodeInterface;
import com.basquiat.address.service.block.BlockHistoryService;
import com.basquiat.address.service.block.vo.BlockHistoryVO;
import com.basquiat.address.service.wallet.WalletService;
import com.basquiat.address.service.wallet.vo.WalletVO;
import com.basquiat.address.util.CommonUtil;
import com.google.gson.JsonObject;

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
	
	@Async
	@SuppressWarnings("unchecked")
	public void processGetTransactionTypeBTC(String coinCode) throws Exception {
		
		LOG.info("do something!!!!!!!!!!!!!!!!!! with "+coinCode);
		BlockChainNodeInterface node = CommonUtil.createInstance(Constants.PRE_FIX, coinCode.toUpperCase());
		
		// 1. lastBlock을 가져온다.
		BlockHistoryVO blockHistoryVO = blockHistoryService.getBlockHistory(coinCode.toUpperCase());
		
		// 2. txIdsMap가 null이 아닐때 
		Map<String, Object> txIdsMap = node.schedulingTransactionCheck(blockHistoryVO.getLastBlockNumber());
		if(txIdsMap != null) {
			List<String> txList = (List<String>) txIdsMap.get("txList");
			for(String txId : txList) {
				// 3. txId별로 vout의 정보를 얻어온다.
				JsonObject jsonObject = node.getRawTransaction(txId);
				LOG.info(CommonUtil.convertJsonStringFromGson(jsonObject));
				if(jsonObject != null) {
					// 4. vout List를 가져온다.
					List<JsonObject> vOutList = CommonUtil.getVOutFromTransaction(jsonObject);
					for(JsonObject vout : vOutList) {
						String addressFromVOut = CommonUtil.getAddressFromVOut(vout);
						// 5. address정보가 있는 경우만 체크한다.
						if(addressFromVOut != null) {
							LOG.info(CommonUtil.convertJsonStringFromGson(vout));
							BigDecimal value = CommonUtil.getValueFromVOut(vout);
							LOG.info("value is " + value.toString() + ", toAddress [" + addressFromVOut + "]");
							// 6. 해당 주소로 db에서 셀렉트해서 존재하는 주소인지 확인한다.
							WalletVO selectedWalletVO = walletService.getWalletByAddress(addressFromVOut);
							// 7. 존재하는 주소라면 해당 주소의 balance를 주소로 들어온 value와 합해서 업데이트를 한다.
							if( selectedWalletVO != null ) {
								LOG.info("this address [" + addressFromVOut + "] exit our Node");
								BigDecimal updateValue = value.add(selectedWalletVO.getBalance());
								selectedWalletVO.setBalance(updateValue);
								walletService.updateWalletByAddress(selectedWalletVO);

								// 8. 해당 txId에 대한 트랜잭션 정보는 DB에 인서트 한다. transaction로그를 남긴다. TODO로 남겨두자
								
							} else {
								LOG.info("this address [" + addressFromVOut + "] do not exit our Node");
							}
						}
					}
				}
			}
			
			// 9. 최종적으로 모든 txId별로 조회 및 엡데이트가 완료가 된다면 마지막 블록 정보를 BLOCK_HISTORY 테이블에 업데이트를 한다.
			Integer lastBlockFromBlockChain = (Integer) txIdsMap.get("lastBlock");
			LOG.info("lastBlock : "+ txIdsMap.get("lastBlock"));
			blockHistoryVO.setLastBlockNumber(lastBlockFromBlockChain);
			blockHistoryService.updateBlockHistory(blockHistoryVO);
		}
		
	}
	
	@Async
	public void processGetTransactionTypeETH(String coinCode) throws Exception {
		//LOG.info("do something!!!!!!!!!!!!!!!!!! with "+coinCode);
	}
	
}
