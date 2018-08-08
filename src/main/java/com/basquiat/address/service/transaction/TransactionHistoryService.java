package com.basquiat.address.service.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.basquiat.address.service.transaction.mapper.TransactionHistoryMapper;
import com.basquiat.address.service.transaction.vo.TransactionVO;

/**
 * 
 * 트랜잭션
 * 
 * created by basquiat
 *
 */
@Service("transactionService")
public class TransactionHistoryService {

	@Value("${block.confirmations}")
	private Integer COMFIRMATIONS;
	
	@Autowired
	private TransactionHistoryMapper transactionHistoryMapper;
	
	/**
	 * 발생한 트랜잭션의 정보를 생성한다.
	 * @param transactionVO
	 * @throws Exception
	 */
	@Transactional
	public void createTransactionHistory(TransactionVO transactionVO) throws Exception {
		transactionHistoryMapper.insertTransactionHistory(transactionVO);
	}
	
	/**
	 * confirmations 업데이트
	 * @param transactionVO
	 * @throws Exception
	 */
	@Transactional
	public void updateTransactionHistory(TransactionVO transactionVO) throws Exception {
		transactionHistoryMapper.updateTransactionHistory(transactionVO);
	}
	
	/**
	 * txid로 조회
	 * @param txId
	 * @return TransactionVO
	 * @throws Exception
	 */
	public TransactionVO getTransactionHistoryById(String txId) throws Exception {
		return transactionHistoryMapper.selectTransactionHistoryById(txId);
	}
	
	/**
	 * 모든 트랙잭션 정보를 조회한다.
	 * 검색 조건
	 * 특정 이상의 트랜잭션은 조회하지 않는다.
	 * @param blockHash
	 * @return
	 * @throws Exception
	 */
	public List<TransactionVO> getTransactionHistorys() throws Exception {
		return transactionHistoryMapper.selectTransactionHistoryByConfirmations(COMFIRMATIONS);
	}
	
	/**
	 * blockHash로 조회
	 * @param blockHash
	 * @return List<TransactionVO>
	 * @throws Exception
	 */
	public List<TransactionVO> getTransactionHistoryByBlockHash(String blockHash) throws Exception {
		return transactionHistoryMapper.selectTransactionHistoryByBlockHash(blockHash);
	}
	
}
