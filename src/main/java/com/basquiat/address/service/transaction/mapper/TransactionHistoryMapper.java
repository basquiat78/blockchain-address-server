package com.basquiat.address.service.transaction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.basquiat.address.service.transaction.vo.TransactionVO;

/**
 * TransactionMapper
 * 
 * created by basquiat
 *
 */
@Mapper
public interface TransactionHistoryMapper {

	/**
	 * 트랜잭션 정보를 생성한다.
	 * @param transactioVO
	 */
	public void insertTransactionHistory(TransactionVO transactionVO);
	
	/**
	 * 트랜잭션의 confirmation, updateDttm을 업데이트한다.
	 * 업데이트 조건
	 * 1. blockHash --> 일관 업데이트
	 * 2. txId --> tx건당 업데이트
	 * @param blockHistoryVO
	 */
	public void updateTransactionHistory(TransactionVO transactionVO);
	
	/**
	 * 트랜잭션 건별 조회
	 * @param txId
	 * @return TransactionVO
	 */
	public TransactionVO selectTransactionHistoryById(String txId);
	
	/**
	 * 특정 confirmations 미만의 confiramations의 트랜잭션 정보만 가져온다.
	 * @param confirmations
	 * @return
	 */
	public List<TransactionVO> selectTransactionHistoryByConfirmations(Integer confirmations);
	
	/**
	 * blockHash의 조회할 경우에는 List로 반환될수 있다.
	 * @param blockHash
	 * @return List<TransactionVO>
	 */
	public List<TransactionVO> selectTransactionHistoryByBlockHash(String blockHash);
	
}
